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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apereo.portal.soffits.service.HealthCheckService;
import org.apereo.portal.soffits.service.HealthStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private static final String SERVICES_JSON_LOCATION = "classpath:/health-check-services.json";
    private static final String FIELD_NAME_STRATEGY = "strategy";

    private JsonNode servicesJson;

    @Value(SERVICES_JSON_LOCATION)
    private Resource servicesJsonResource;

    @PostConstruct
    public void init() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            servicesJson = mapper.readTree(servicesJsonResource.getInputStream());
            if (!servicesJson.isArray()) {
                throw new RuntimeException("services.json does not contain a JsonArray");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse services.json into JsonNode;  servicesJsonResource=" + servicesJsonResource, e);
        }
    }

    @Cacheable("healthCheckCache")
    @Override
    public List<HealthStatus> checkHealth() {
        final List<HealthStatus> rslt = new ArrayList<>();
        for (JsonNode node : servicesJson) {
            final String strategyName = node.get(FIELD_NAME_STRATEGY).asText();
            final HealthCheckStrategy strategy = HealthCheckStrategy.valueOf(strategyName);
            final HealthStatus status = strategy.checkHealth(node);
            if (status != null) {
                rslt.add(status);
            }
        }
        return rslt;
    }

}
