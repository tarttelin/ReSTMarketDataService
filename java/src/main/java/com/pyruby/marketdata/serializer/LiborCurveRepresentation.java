package com.pyruby.marketdata.serializer;

import com.pyruby.marketdata.model.LiborCurve;
import com.pyruby.marketdata.model.Tenor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "LiborCurve")
@XmlAccessorType(XmlAccessType.FIELD)
public class LiborCurveRepresentation {
    @XmlElement(name="Name")
    private String name;
    @XmlElement(name="Currency")
    private String currency;
    @XmlElementWrapper(name="Tenors")
    @XmlElement(name="Tenor")
    private List<TenorRepresentation> tenors = new ArrayList<TenorRepresentation>();

    public LiborCurveRepresentation(LiborCurve curve) {
        name = curve.getName();
        currency = curve.getCurrency();
        for (Tenor tenor : curve.getTenors()) {
            TenorRepresentation repr = new TenorRepresentation();
            repr.setPeriod(tenor.getInterval());
            repr.setBasisPoints(tenor.getBps());
            addTenor(repr);
        }
    }

    public LiborCurveRepresentation() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<TenorRepresentation> getTenors() {
        return tenors;
    }

    public void addTenor(TenorRepresentation tenor) {
        this.tenors.add(tenor);
    }

    public LiborCurve toLiborCurve() {
        LiborCurve curve = new LiborCurve();
        curve.setName(name);
        curve.setCurrency(currency);
        List<Tenor> domainTenors = new ArrayList<Tenor>(tenors.size());
        for(TenorRepresentation tenor : tenors) {
            domainTenors.add(new Tenor(tenor.getPeriod(), tenor.getBasisPoints()));
        }
        curve.setTenors(domainTenors);
        return curve;
    }

}
