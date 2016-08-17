
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
<c:set var="req" value="${pageContext.request}" />
<c:set var="urlBase" value="${req.scheme}://${req.serverName}:${req.localPort}${req.contextPath}" />

<link rel="stylesheet" href="${urlBase}/css/blueimp-gallery.min.css">

<style>
#${n} blockquote {
    padding: 0 0 0 16px;
}
#${n} .bg-logo {
    display: inline-block;
    height: 80px;
}
#${n} .bg-spinner {
    padding: 16px;
    font-size: 72px;
    text-align: center;
}
#${n} #blueimp-gallery i.fa {
    font-size: 66%;
}
</style>

<div id="${n}">
    <blockquote>
        <h2 class="h1 text-primary">
            <img class="bg-logo img-responsive" src="${urlBase}/img/instagram.jpg" />
            @tikkido on Instagram!
        </h2>
    </blockquote>

    <div class="panel panel-default">
        <div class="panel-body text-center">
            <div class="bg-spinner text-primary">
                <i class="fa fa-spinner fa-spin" aria-hidden="true"></i>
            </div>
            <div class="bg-icons"></div>
        </div>
    </div>

    <!-- The Gallery as lightbox dialog, should be a child element of the document body -->
    <div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls">
        <div class="slides"></div>
        <h3 class="title"></h3>
        <a class="prev"><i class="fa fa-chevron-left" aria-hidden="true"></i></a>
        <a class="next"><i class="fa fa-chevron-right" aria-hidden="true"></i></a>
        <a class="close">x</a>
        <a class="play-pause"></a>
        <ol class="indicator"></ol>
    </div>
</div>

<script type="text/javascript">
    // De-alias the up.jQuery to load the gallery plugin properly
    var jQuery = up.jQuery;
</script>
<script src="${urlBase}/js/blueimp-gallery.min.js"></script>
<script src="${urlBase}/js/gallery-soffit.js"></script>
<script type="text/javascript">
    jQuery(function() {
        var options = {
            apiUrl: "${urlBase}/api/gallery/v1-0/images"
        };
        up.blueimpGallery.Gallery(up.jQuery, '#${n}', options);
    });
    // Clear jQuery (again) from the global namespace
    var jQuery = undefined;
</script>
