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
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<jsp:directive.include file="/WEB-INF/soffit/include.jsp"/>

<c:set var="n" value="${portalRequest.attributes['namespace'][0]}" />

<div id="${n}">

    <h2 class="text-primary">Snooper</h2>
    <p>Here is an example of data elements available to a remote soffit endpoint.</p>

    <h3>Request Headers</h3>
    <ul>
        <c:forEach items="${header}" var="entry">
            <li><code><strong><c:out value="${entry.key}" />:</strong> <c:out value="${entry.value}" /></code></li>
        </c:forEach>
    </ul>

    <h3>Bearer:</h3>
    <pre>${bearerJson}</pre>

    <h3>Portal Request:</h3>
    <pre>${portalRequestJson}</pre>

    <h3>Preferences:</h3>
    <pre>${preferencesJson}</pre>

    <h3>Definition:</h3>
    <pre>${definitionJson}</pre>

</div>
