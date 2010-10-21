package com.pyruby.marketdata.restapi.resource;

import com.pyruby.marketdata.restapi.resource.SpringContext;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.*;
import java.io.IOException;

public class TransactionalServletFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        PlatformTransactionManager txnMgr = (PlatformTransactionManager) SpringContext.getInstance().get("transactionManager");
        TransactionTemplate template = new TransactionTemplate(txnMgr);
        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    filterChain.doFilter(servletRequest, servletResponse);
                } catch (Exception e) {
                    throw new RuntimeException("Messed up", e);
                }
            }
        });

    }

    public void destroy() {
        System.out.println("Destroyed");
    }
}
