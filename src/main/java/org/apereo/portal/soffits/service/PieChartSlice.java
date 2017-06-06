package org.apereo.portal.soffits.service;

import java.math.BigDecimal;

public class PieChartSlice {

    private final String label;
    private final BigDecimal value;

    public PieChartSlice(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getValue() {
        return value;
    }

}
