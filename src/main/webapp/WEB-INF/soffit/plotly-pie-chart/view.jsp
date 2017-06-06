<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/soffit/include.jsp"/>

<c:set var="n" value="${portalRequest.attributes['namespace'][0]}" />

<style>
#${n} .pie-spinner {
    padding: 16px;
    font-size: 400%;
    text-align: center;
}
#${n} h2 {
    font-size: 200%;
}
#${n} ${n}_chart {
    width: 480px;
    height: 380px;
    display: none
}
</style>

<!-- plotly.js version 1.27.1) -->
<script type="text/javascript" src="https://cdn.plot.ly/plotly-1.27.1.min.js"></script>

<c:set var="req" value="${pageContext.request}" />
<c:set var="urlBase" value="${req.scheme}://${req.serverName}:${req.localPort}${req.contextPath}" />
<script src="${urlBase}/js/plotly-pie-chart.js" type="text/javascript"></script>

<div id="${n}" class="jumbotron">
  <h2 class="text-info"><c:out value="${definition.title}" /></h2>
  <p><c:out value="${definition.description}" /></p>
  <div class="pie-spinner">
    <i class="fa fa-spinner fa-spin" aria-hidden="true"></i>
  </div>
  <div id="${n}_chart"><!-- Plotly chart will be drawn inside this DIV --></div>
</div>

<script type="text/javascript">
(function($) {
    var options = {
        apiUrl: "${urlBase}/api/pie-chart/v1-0/slices",
        preferencesToken: '${preferences.encryptedToken}'
    };
    up.PlotlyPieChart.PieChart($, '#${n}_chart', options, Plotly)
})(up.jQuery);
</script>
