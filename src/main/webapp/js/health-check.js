"use strict";
var up = up || {};

(function() {

    // Sub-structure for health-check widgets
    up.HealthCheck = {};

    var defaults = {
        selectors: {
            spinner: '.hc-spinner',
            itemsContainer: '.hc-items-container',
            templateAlive: '.hc-template-alive',
            templateDead: '.hc-template-dead',
            link: '.hc-link'
        }
    };

    up.HealthCheck.Dashboard = function($, container, options) {
        var configs = $.extend({}, defaults, options);
        var dashboard = $(container);
        var spinner = dashboard.find(configs.selectors.spinner);
        var itemsContainer = dashboard.find(configs.selectors.itemsContainer);
        var aliveTemplate = itemsContainer.find(configs.selectors.templateAlive);
        var deadTemplate = itemsContainer.find(configs.selectors.templateDead);

        $.ajax(configs.apiUrl, {
            dataType: 'jsonp',
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Failed to fetch system status information:  ' + textStatus);
            },
            success: function(data, textStatus, jqXHR) {
                $.each(data, function(index, item) {
                    var itemElement = item.alive ? aliveTemplate.clone() : deadTemplate.clone();
                    itemElement.removeClass('hc-template-alive hc-template-dead');
                    itemElement.find(configs.selectors.link).attr('href', item.url);
                    itemElement.find(configs.selectors.link).html(item.title);
                    itemsContainer.append(itemElement);
                    itemElement.show();
                });
                itemsContainer.slideDown();
                spinner.slideUp();
            }
        });
    }

})();
