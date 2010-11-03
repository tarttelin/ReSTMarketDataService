package com.pyruby.marketdata;

import com.pyruby.marketdata.model.AbstractCurve;
import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class MarketDataHibernateDAO implements MarketDataRepository {

    private SessionFactory sf;

    public MarketDataHibernateDAO(SessionFactory sf) {
        this.sf = sf;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public AbstractCurve save(AbstractCurve curve) {
        sf.getCurrentSession().save(curve);
        return curve;
    }

    /*
     * This shouldn't be transactional, and should not have to eagerly load it's tenors.  Having trouble with
     * the Grizzly tests applying the TransactionServletFilter, but I suspect it will work once deployed to a proper
     * container =0)
     */
    @Transactional
    public Bond findBondByNameAndMaturity(String name, String maturity) {
        return (Bond) sf.getCurrentSession()
                .createQuery("from Bond b  join fetch b.tenors t where b.name=:name and b.maturity=:maturity")
                .setParameter("name", name)
                .setParameter("maturity", maturity)
                .uniqueResult();
    }

    @Transactional
    public LiborCurve findLiborCurveByName(String name) {
        return (LiborCurve) sf.getCurrentSession()
                .createQuery("from LiborCurve l join fetch l.tenors t where l.name = :name")
                .setParameter("name", name)
                .uniqueResult();
    }

}
