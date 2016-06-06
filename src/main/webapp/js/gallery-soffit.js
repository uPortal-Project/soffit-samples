"use strict";
var up = up || {};

(function () {

    // Sub-structure for widgets of this type
    up.blueimpGallery = {};

    var defaults = {
        selectors: {
            spinner: '.bg-spinner',
            iconsContainer: '.bg-icons',
            icons: '.bg-icons a'
            /*itemsContainer: '.hc-items-container',
            templateAlive: '.hc-template-alive',
            templateDead: '.hc-template-dead',
            link: '.hc-link'*/
        }
    };

    up.blueimpGallery.Gallery = function($, container, options) {
        var configs = $.extend({}, defaults, options);
        var gallery = $(container);
        var spinner = gallery.find(configs.selectors.spinner);

        $.ajax({
            // Flickr API is SSL only:
            // https://code.flickr.net/2014/04/30/flickr-api-going-ssl-only-on-june-27th-2014/
            url: 'https://api.flickr.com/services/rest/',
            data: {
                format: 'json',
                method: 'flickr.interestingness.getList',
                api_key: '7617adae70159d09ba78cfec73c13be3' // jshint ignore:line
            },
            dataType: 'jsonp',
            jsonp: 'jsoncallback'
        }).done(function (result) {
            var linksContainer = gallery.find(configs.selectors.iconsContainer);
            // Add the demo images as links with thumbnails to the page:
            $.each(result.photos.photo, function (index, photo) {
                var baseUrl = 'https://farm' + photo.farm + '.static.flickr.com/'
                        + photo.server + '/' + photo.id + '_' + photo.secret;
                $('<a/>').append($('<img>').prop('src', baseUrl + '_s.jpg'))
                    .prop('href', baseUrl + '_b.jpg')
                    .prop('title', photo.title)
                    .attr('data-gallery', '')
                    .appendTo(linksContainer);
            })
        });

        gallery.find(configs.selectors.iconsContainer).click(function(event) {
            var target = event.target || event.srcElement,
                    link = target.src ? target.parentNode : target,
                    options = {index: link, event: event},
                    links = this.getElementsByTagName('a');
            blueimp.Gallery(links, options);
        });

        spinner.slideUp();

    };

})();
