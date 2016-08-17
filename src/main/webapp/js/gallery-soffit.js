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

        $.ajax(configs.apiUrl, {
            dataType: 'jsonp',
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Failed to fetch gallery images:  ' + textStatus);
            },
            success: function(data, textStatus, jqXHR) {
                var linksContainer = gallery.find(configs.selectors.iconsContainer);

                // Add the demo images as links with thumbnails to the page
                $.each(data, function (index, photo) {
                    $('<a/>').append($('<img>').prop('src', photo.thumbnail))
                        .prop('href', photo.url)
                        .prop('title', photo.title)
                        .attr('data-gallery', '')
                        .appendTo(linksContainer);
                })

                // Initialize the gallery
                gallery.find(configs.selectors.iconsContainer).click(function(event) {
                    var target = event.target || event.srcElement,
                            link = target.src ? target.parentNode : target,
                            options = {index: link, event: event},
                            links = this.getElementsByTagName('a');
                    blueimp.Gallery(links, options);
                });

                // Remove the spinner
                spinner.slideUp();
            }
        });

    };

})();
