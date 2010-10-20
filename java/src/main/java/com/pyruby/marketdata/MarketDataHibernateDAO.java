package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class MarketDataHibernateDAO implements MarketDataRepository {

    private SessionFactory sf;

    public MarketDataHibernateDAO(SessionFactory sf) {
        this.sf = sf;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Bond save(Bond bond) {
        sf.getCurrentSession().save(bond);
        return bond;
    }

    public Bond findByNameAndMaturity(String name, String maturity) {
        return (Bond) sf.getCurrentSession()
                .createQuery("from Bond where name=:name and maturity=:maturity")
                .setParameter("name", name)
                .setParameter("maturity", maturity)
                .uniqueResult();
    }
    
}
