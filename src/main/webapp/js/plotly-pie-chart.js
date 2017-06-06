"use strict";
var up = up || {};

(function() {

    // Sub-structure for health-check widgets
    up.PlotlyPieChart = {};

    var defaults = {
        selectors: {
            spinner: '.pie-spinner'
        }
    };

    up.PlotlyPieChart.PieChart = function($, container, options, Plotly) {
        var configs = $.extend({}, defaults, options);
        var chartContainer = $(container);
        var spinner = chartContainer.parent().find(configs.selectors.spinner);

        var displayPieChart = function(json) {
            var layout = {
              height: 400,
              width: 500
            };

            var data = [{
              labels: [],
              values: [],
              type: 'pie'
            }];

            // And again for label/value
            $.each(json, function(index, item) {
                data[0].labels.push(item.label);
                data[0].values.push(item.value);
            });

            Plotly.newPlot($(container).attr('id'), data, layout);
        };

        $.ajax(configs.apiUrl, {
            dataType: 'jsonp',
            data: {preferencesToken: encodeURIComponent(options.preferencesToken)},
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Failed to fetch pie chart data:  ' + textStatus);
            },
            success: function(json, textStatus, jqXHR) {
                displayPieChart(json);
                spinner.slideUp();
                chartContainer.slideDown();
            }
        });
    }

})();
