package com.pyruby.marketdata.serializer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Tenor")
@XmlAccessorType(XmlAccessType.FIELD)
public class TenorRepresentation {
    @XmlAttribute(name="period")
    private String period;
    @XmlAttribute(name="bps")
    private double basisPoints;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getBasisPoints() {
        return basisPoints;
    }

    public void setBasisPoints(double basisPoints) {
        this.basisPoints = basisPoints;
    }
}
