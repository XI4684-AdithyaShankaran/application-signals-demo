/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.samples.petclinic.customers.web;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.annotation.Timed;
import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.customers.Util.WellKnownAttributes;
import org.springframework.samples.petclinic.customers.aws.*;
import org.springframework.samples.petclinic.customers.model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 * @author Ramazan Sakin
 */
@RestController
@Timed("petclinic.pet")
@RequiredArgsConstructor
class PetResource {

    private static final Logger log = LoggerFactory.getLogger(PetResource.class);

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final SqsService sqsService;
    private final KinesisService kinesisService;
    private final BedrockAgentV1Service bedrockAgentV1Service;
    private final BedrockAgentV2Service bedrockAgentV2Service;
    private final BedrockRuntimeV1Service bedrockRuntimeV1Service;
    private final BedrockRuntimeV2Service bedrockRuntimeV2Service;
    private final BedrockV1Service bedrockV1Service;
    private final BedrockV2Service bedrockV2Service;

    private final RestTemplate restTemplate;

    @GetMapping("/petTypes")
    public List<PetType> getPetTypes() {
        return petRepository.findPetTypes();
    }

    @PostMapping("/owners/{ownerId}/pets")
    @ResponseStatus(HttpStatus.CREATED)
    public Pet processCreationForm(
        @RequestBody PetRequest petRequest,
        @PathVariable("ownerId") @Min(1) int ownerId) {

        Span.current().setAttribute(WellKnownAttributes.PET_ID, petRequest.getId());
        Span.current().setAttribute(WellKnownAttributes.OWNER_ID, ownerId);
        Span.current().setAttribute(WellKnownAttributes.ORDER_ID, petRequest.getId());

        final Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        Owner owner = optionalOwner.orElseThrow(() -> new ResourceNotFoundException(String.format("Owner %d not found", ownerId)));
        
        final Pet pet = new Pet();
        try {
            sqsService.sendMsg();
            owner.addPet(pet);
        } catch (Exception e) {
            log.error("Failed to add pet for owner ID: {}", ownerId, e);
            throw e;
        }
        return save(pet, petRequest);
    }

    @GetMapping("/diagnose/owners/{ownerId}/pets/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processDiagnose(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
        Span.current().setAttribute(WellKnownAttributes.PET_ID, petId);
        Span.current().setAttribute(WellKnownAttributes.OWNER_ID, ownerId);
        Span.current().setAttribute(WellKnownAttributes.ORDER_ID, petId);

        log.debug("bedrockAgentV1Service getting knowledge base");
        bedrockAgentV1Service.getKnowledgeBase();
        log.debug("bedrockAgentV1Service finished getting knowledge base");
        log.debug("bedrockV1Service getting guardrail");
        bedrockV1Service.getGuardrail();
        log.debug("bedrockV1Service finished getting guardrail");
        log.debug("Calling Bedrock petId={}", petId);
        log.debug("bedrockRuntimeV1Service invoking Titan model");
        String petType = "pets";
        try {
            Pet pet = findPetById(petId);
            if (pet.getType() != null) {
                petType = pet.getType().getName();
            }
        } catch (Exception e) {
            log.error("Failed to find pet with ID: {} for owner: {}", petId, ownerId, e);
        }

        bedrockRuntimeV1Service.invokeTitanModel(petType);
        log.debug("bedrockRuntimeV1Service finished invoking Titan model");
        log.debug("bedrockAgentV2Service getting knowledge base");
        bedrockAgentV2Service.bedrockAgentGetKnowledgeBaseV2();
        log.debug("bedrockAgentV2Service finished getting knowledge base");
        log.debug("bedrockV2Service getting guardrail");
        bedrockV2Service.getGuardrail();
        log.debug("bedrockV2Service finished getting guardrail");
        log.debug("bedrockRuntimeV2Service invoking Anthropic Claude");
        bedrockRuntimeV2Service.invokeAnthropicClaude(petType);
        log.debug("bedrockRuntimeV2Service finished invoking Anthropic Claude");
    }

    @PutMapping("/owners/{ownerId}/pets/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processUpdateForm(@PathVariable("ownerId") int ownerId, @RequestBody PetRequest petRequest) {
        int petId = petRequest.getId();
        Span.current().setAttribute(WellKnownAttributes.PET_ID, petId);
        Span.current().setAttribute(WellKnownAttributes.OWNER_ID, ownerId);
        Span.current().setAttribute(WellKnownAttributes.ORDER_ID, petId);

        Pet pet = findPetById(petId);
        kinesisService.getStreamRecords();
        save(pet, petRequest);
    }

    private Pet save(final Pet pet, final PetRequest petRequest) {

        pet.setName(petRequest.getName());
        pet.setBirthDate(petRequest.getBirthDate());
        petRepository.findPetTypeById(petRequest.getTypeId())
            .ifPresent(pet::setType);

        log.info("Saving pet with ID: {}", pet.getId());
        return petRepository.save(pet);
    }

    @GetMapping("owners/{ownerId}/pets/{petId}")
    @CircuitBreaker(name = "pet-service", fallbackMethod = "findPetFallback")
    public PetDetails findPet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId) {
        Span.current().setAttribute(WellKnownAttributes.PET_ID, petId);
        Span.current().setAttribute(WellKnownAttributes.OWNER_ID, ownerId);
        Span.current().setAttribute(WellKnownAttributes.ORDER_ID, petId);

        PetDetails detail = new PetDetails(findPetById(petId));

        // enrich with insurance
        try {
            String url = String.format("http://insurance-service/pet-insurances/%d", detail.getId());
            ResponseEntity<PetInsurance> response = restTemplate.getForEntity(url, PetInsurance.class);
            PetInsurance petInsurance = response.getBody();
            if (petInsurance != null) {
                detail.setInsurance_id(petInsurance.getInsurance_id());
                detail.setInsurance_name(petInsurance.getInsurance_name());
                detail.setPrice(petInsurance.getPrice());
            } else {
                log.warn("No insurance found for pet {}", petId);
            }
        } catch (RestClientException ex) {
            log.error("Failed to fetch insurance for pet with ID: {} for owner: {}", petId, ownerId, ex);
        }

        // enrich with nutrition
        if (detail.getType() != null && detail.getType().getName() != null) {
            try {
                String nutritionUrl = String.format("http://nutrition-service/nutrition/%s", detail.getType().getName());
                ResponseEntity<PetNutrition> response = restTemplate.getForEntity(nutritionUrl, PetNutrition.class);
                PetNutrition petNutrition = response.getBody();
                if (petNutrition != null) {
                    detail.setNutritionFacts(petNutrition.getFacts());
                } else {
                    log.warn("No nutrition facts found for pet type {}", detail.getType().getName());
                }
            } catch (RestClientException ex) {
                log.error("Failed to fetch nutrition for pet type: {} for owner: {}", 
                    detail.getType().getName(), ownerId, ex);
            }
        } else {
            log.warn("Pet {} has no type information, skipping nutrition lookup", petId);
        }

        return detail;
    }

    public PetDetails findPetFallback(int ownerId, int petId, Exception ex) {
        log.warn("Circuit breaker fallback for pet {}", petId, ex);
        PetDetails detail = new PetDetails(findPetById(petId));
        return detail;
    }

    private Pet findPetById(int petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pet %d not found", petId)));
    }

}