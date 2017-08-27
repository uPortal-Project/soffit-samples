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

<style>
#${n} .carousel {
    width:100%;
    margin:0 auto; /* center your carousel if other than 100% */
}
#${n} .item-image {
    height: 400px;
}
#${n} .item-title {
    font-size: 200%;
    font-weight: bold;
}
#${n} .item-link {
    color: #FDB913;
    font-weight: bold;
}
/* Cover a Bootstrap issue */
#${n} .carousel-inner > .item:not(.active) {
    position: absolute;
}
</style>

<!-- Carousel container -->
<div id="${n}" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
        <li data-target="#${n}" data-slide-to="0" class="active"></li>
        <li data-target="#${n}" data-slide-to="1"></li>
        <li data-target="#${n}" data-slide-to="2"></li>
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner" role="listbox">
        <div class="item template" style="display: none;">
            <div class="item-image" style=""></div>
            <div class="carousel-caption">
                <h3 class="item-title"></h3>
                <a href="" class="item-link pull-right" title="">learn more</a>
            </div>
        </div>
    </div>

    <!-- Controls -->
    <a class="left carousel-control" href="#${n}" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#${n}" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>

<script type="text/javascript">
(function($) {

    var template = $('#${n} div.template');
    var defaultBackground = 'background:url(${urlBase}/img/dark-background.jpg) center center; background-size:cover;';

    var rssFeedUrl = "${preferences.preferencesMap['carousel.rssFeedUrl'][0]}";
    if (rssFeedUrl) {

        $.get(rssFeedUrl, function(data) {
            var hasActive = false;

            var xmlDoc = $.parseXML(data);
            var rssTree = $(xmlDoc);

            rssTree.find('item').each(function () {
                var item = $(this);
                var element = template.clone();
                var background = defaultBackground;
                var enclosures = item.find('enclosure');
                if (enclosures.size() > 0) {
                    background = 'background:url('
                            + enclosures.first().attr('url')
                            + ') center center; background-size:cover;';
                }
                element.find('.item-image').attr('style', background);
                element.find('.item-title').html(item.find('title').text());
                if (item.find('link').size() > 0) {
                    element.find('.item-link').attr('title', item.find('title').text());
                    element.find('.item-link').attr('href', item.find('link').text());
                }
                element.removeClass('template');
                element.insertAfter(template);
                element.show();

                if (!hasActive) {
                    element.addClass('active');
                    hasActive = true;
                }
            });

            template.remove();
        });

    } else {
        // Instead of data, use the template to instruct the adopter
        template.find('.item-image').attr('style', defaultBackground);
        template.find('.item-title')
                .html('Feed not defined;  please specify a <code>carousel.rssFeedUrl</code> portlet preference.');
        template.removeClass('template').addClass('active').show();
    }

    $('#${n}').carousel({ interval: false });

})(up.jQuery);
</script>
