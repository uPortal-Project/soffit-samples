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

package org.apereo.portal.soffits.rest;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apereo.portal.soffit.model.v1_0.Preferences;
import org.apereo.portal.soffit.service.PreferencesService;
import org.apereo.portal.soffits.service.PieChartSlice;
import org.apereo.portal.soffits.service.PieChartSlicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@Controller
@RequestMapping("/api/pie-chart")
public class PieChartSlicesController {

    private static final String DATA_STRATEGY_PREFEERENCE = PieChartSlicesController.class.getSimpleName() + ".dataStrategy";
    private static final String DATA_ELEMENTS_PREFEERENCE = PieChartSlicesController.class.getSimpleName() + ".dataElements";

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private Map<String,PieChartSlicesService> pieChartSlicesServices;

    @RequestMapping(method=RequestMethod.GET, value="/v1-0/slices")
    public @ResponseBody List<PieChartSlice> getSlices(@RequestParam("preferencesToken") String preferencesToken) {
        final List<PieChartSlice> rslt = new ArrayList<>();

        final Preferences preferences = preferencesService.parsePreferences(URLDecoder.decode(preferencesToken));

        final String dataServiceName = preferences.getValues(DATA_STRATEGY_PREFEERENCE).get(0);
        final PieChartSlicesService dataService = pieChartSlicesServices.get(dataServiceName);

        for (String s : preferences.getValues(DATA_ELEMENTS_PREFEERENCE)) {
            rslt.add(dataService.getSlice(s));
        }

        return rslt;
    }

    @ControllerAdvice
    static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        public JsonpAdvice() {
            super("callback");
        }
    }

}
