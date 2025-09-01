# Infra (CDK/TypeScript)
- S3: encryption=S3_MANAGED, blockPublicAccess=BLOCK_ALL, enforceSSL, versioned.
- RDS/ECR/EKS: encryption and deletionProtection where applicable.
- IAM: least privilege; avoid wildcards. Justify any `nag` suppressions.
