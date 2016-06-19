/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apereo.portal.soffits.service.hc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apereo.portal.soffits.service.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public enum HealthCheckStrategy {

    HTTP_HEAD {

        private static final int TIMEOUT_SECONDS = 10;

        private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();

        private final RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_SECONDS * 1000)
                .setConnectTimeout(TIMEOUT_SECONDS * 1000)
                .build();

        private final CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public HealthStatus checkHealth(JsonNode json) {

            logger.debug("Checking health of the service defined by the following JSON:  {}", json.toString());

            final String url = json.get(FIELD_NAME_URL).asText();

            final HttpHead headMethod = new HttpHead(url);
            HttpResponse httpResponse;
            try {
                logger.debug("Performing a HEAD request on url={}", url);

                httpResponse = httpClient.execute(headMethod);
                final int statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.debug("HTTP response code for url '{}' was '{}'", url, statusCode);

                final boolean alive = (statusCode == HttpStatus.SC_OK);

                return new HealthStatus(
                    url,
                    json.get(FIELD_NAME_TITLE).asText(),
                    alive
                );
            } catch (IOException e) {
                logger.error("Failed to check the status of URL '{}'", url);
            }

            return null;

        }
    };

    private static final String FIELD_NAME_URL = "url";
    private static final String FIELD_NAME_TITLE = "title";

    public abstract HealthStatus checkHealth(JsonNode json);

}
