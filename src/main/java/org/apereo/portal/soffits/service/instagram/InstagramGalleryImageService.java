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


package org.apereo.portal.soffits.service.instagram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apereo.portal.soffits.service.GalleryImage;
import org.apereo.portal.soffits.service.GalleryImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstagramGalleryImageService implements GalleryImageService {

    private static final int TIMEOUT_SECONDS = 10;

    private static final String IMAGES_FIELDNAME = "data";

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @Value("${instagram.api.url:https://api.instagram.com/v1/users/self/media/recent/?access_token=}")
    private String apiUrl;

    @Value("${instagram.api.access_token:''}")
    private String apiAccessToken;

    @Cacheable("galleryImageCache")
    public List<GalleryImage> getImages() {

        final List<GalleryImage> rslt = new ArrayList<>();

        final String url = apiUrl + apiAccessToken;

        logger.debug("Updating the configured Instagram feed using URL '{}'", url);

        final HttpGet getMethod = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(getMethod);

            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("HTTP response code for url '{}' was '{}'", url, statusCode);

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Failed to update the Instagram feed at URL '{}';  HttpStatus={}", url, statusCode);
                return null;
            }

            final HttpEntity entity = httpResponse.getEntity();
            final JsonNode node = objectMapper.readValue(entity.getContent(), JsonNode.class);

            logger.debug("JSON returned from Instagram API.../n{}", node.toString());

            for(JsonNode image : node.get(IMAGES_FIELDNAME)) {
                rslt.add(
                        new GalleryImage(
                                image.get("caption").get("text").asText(),
                                image.get("images").get("standard_resolution").get("url").asText(),
                                image.get("images").get("thumbnail").get("url").asText())
                );
            }

        } catch (IOException e) {
            logger.error("IOException for URL '{}'", url);
        }

        return rslt;

    }

}
