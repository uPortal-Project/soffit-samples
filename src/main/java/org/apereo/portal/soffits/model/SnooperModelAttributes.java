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

package org.apereo.portal.soffits.model;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.portal.soffit.model.v1_0.Bearer;
import org.apereo.portal.soffit.model.v1_0.Definition;
import org.apereo.portal.soffit.model.v1_0.PortalRequest;
import org.apereo.portal.soffit.model.v1_0.Preferences;
import org.apereo.portal.soffit.renderer.SoffitModelAttribute;
import org.springframework.stereotype.Component;

/**
 * This class illustrates the use of the <code>SoffitModelAttribute</code>
 * annotation.  It's methods produce objects that will be bound to the
 * evaluation context of the JSP.
 *
 * @author drewwills
 */
@Component
public class SnooperModelAttributes {

    private final ObjectMapper mapper = new ObjectMapper();

    @SoffitModelAttribute(value="bearerJson",viewRegex=".*/snooper/.*")
    public String getBearerJson(Bearer bearer) {
        String rslt = null;
        try {
            rslt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bearer);
        } catch (JsonProcessingException e) {
            final String msg = "Unable to write the Bearer object to JSON";
            throw new RuntimeException(msg, e);
        }
        return rslt;
    }

    @SoffitModelAttribute(value="portalRequestJson",viewRegex=".*/snooper/.*")
    public String getPortalRequestJson(PortalRequest portalRequest) {
        String rslt = null;
        try {
            rslt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(portalRequest);
        } catch (JsonProcessingException e) {
            final String msg = "Unable to write the PortalRequest object to JSON";
            throw new RuntimeException(msg, e);
        }
        return rslt;
    }

    @SoffitModelAttribute(value="preferencesJson",viewRegex=".*/snooper/.*")
    public String getPreferencesJson(Preferences preferences) {
        String rslt = null;
        try {
            rslt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(preferences);
        } catch (JsonProcessingException e) {
            final String msg = "Unable to write the Preferences object to JSON";
            throw new RuntimeException(msg, e);
        }
        return rslt;
    }

    @SoffitModelAttribute(value="definitionJson",viewRegex=".*/snooper/.*")
    public String getDefinitionJson(Definition definition) {
        String rslt = null;
        try {
            rslt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(definition);
        } catch (JsonProcessingException e) {
            final String msg = "Unable to write the Definition object to JSON";
            throw new RuntimeException(msg, e);
        }
        return rslt;
    }
}
