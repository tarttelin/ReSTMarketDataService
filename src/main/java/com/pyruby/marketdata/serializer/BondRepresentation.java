package com.pyruby.marketdata.serializer;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.Tenor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Bond")
@XmlAccessorType(XmlAccessType.FIELD)
public class BondRepresentation {

    @XmlElement(name="Name")
    private String name;
    @XmlElement(name="Ticker")
    private String ticker;
    @XmlElement(name="Maturity")
    private String maturity;
    @XmlElement(name="Issuer")
    private String issuer;
    @XmlElementWrapper(name="Tenors")
    @XmlElement(name="Tenor")
    private List<TenorRepresentation> tenors = new ArrayList<TenorRepresentation>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public List<TenorRepresentation> getTenors() {
        return tenors;
    }

    public void addTenor(TenorRepresentation tenor) {
        this.tenors.add(tenor);
    }

    public Bond toBond() {
        Bond bond = new Bond();
        bond.setName(name);
        bond.setTicker(ticker);
        bond.setMaturity(maturity);
        bond.setIssuer(issuer);
        List<Tenor> domainTenors = new ArrayList<Tenor>(tenors.size());
        for(TenorRepresentation tenor : tenors) {
            domainTenors.add(new Tenor(tenor.getPeriod(), tenor.getBasisPoints()));
        }
        bond.setTenors(domainTenors);
        return bond;
    }
}
