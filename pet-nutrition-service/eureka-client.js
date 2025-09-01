const logger = require('pino')();
const axios = require('axios');
const axiosRetry = require('axios-retry');
const ip = require('ip');

const URL = process.env.EUREKA_SERVER_URL || `http://localhost:8761/eureka`;

/**
 * Retry configuration.
 */

axiosRetry.default(axios, { 
  retries: Infinity, 
  retryDelay: axiosRetry.exponentialDelay,
  onRetry: (n, error) => {
    logger.info(`eureka retry attempt: ${n}`);
  }
});



/**
 * Register with Eureka server.
 */

module.exports = function (app, port) {
  const sanitizedApp = String(app).replace(/[\r\n\t]/g, '');
  logger.info('attempting to register with eureka', {
    url: `${URL}/apps/${sanitizedApp}/`,
    instanceId: `${sanitizedApp}-${port}`,
    app: sanitizedApp.toUpperCase(),
    hostName: ip.address(),
    port: port
  });

  axios.post(`${URL}/apps/${app}/`, {
    instance: {
      hostName: ip.address(),
      instanceId: `${app}-${port}`,
      vipAddress: `${app}`,
      app: `${app.toUpperCase()}`,
      ipAddr: ip.address(),
      status: `UP`,
      port: {
          $: port,
          "@enabled": true
      },
      dataCenterInfo: {
          "@class": `com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo`,
          name: `MyOwn`
      }
    }
  })
  .then(function (res) {
    logger.info('successfully registered with eureka', {
      statusCode: res.status,
      statusText: String(res.statusText || '').replace(/[\r\n\t]/g, ''),
      instanceId: `${sanitizedApp}-${port}`
    });
    setInterval(() => {
      axios.put(`${URL}/apps/${app}/${app}-${port}`)
        .then(function (res) {
          logger.info('eureka hearbeat', {
            statusCode: res.status,
            instanceId: `${sanitizedApp}-${port}`
          });
        })
        .catch(function (err) {
          logger.error('failed to add heartbeat', {
            error: String(err.message || '').replace(/[\r\n\t]/g, ''),
            statusCode: err.response?.status,
            statusText: String(err.response?.statusText || '').replace(/[\r\n\t]/g, ''),
            responseData: String(err.response?.data || '').replace(/[\r\n\t]/g, ''),
            url: `${URL}/apps/${sanitizedApp}/${sanitizedApp}-${port}`
          });
        })
    }, 50 * 1000);
  })
  .catch(function (err) {
    logger.error('failed to register with eureka', {
      error: String(err.message || '').replace(/[\r\n\t]/g, ''),
      statusCode: err.response?.status,
      statusText: String(err.response?.statusText || '').replace(/[\r\n\t]/g, ''),
      responseData: String(err.response?.data || '').replace(/[\r\n\t]/g, ''),
      url: `${URL}/apps/${sanitizedApp}/`,
      instanceId: `${sanitizedApp}-${port}`
    });
  });
};