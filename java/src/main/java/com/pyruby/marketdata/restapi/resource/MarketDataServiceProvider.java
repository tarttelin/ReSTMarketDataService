package com.pyruby.marketdata.restapi.resource;

import com.pyruby.marketdata.MarketDataService;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class MarketDataServiceProvider extends AbstractHttpContextInjectable<MarketDataService>
      implements InjectableProvider<Context, Type> {

    public Injectable<MarketDataService> getInjectable(ComponentContext ic, Context a, Type c) {
        if (c.equals(MarketDataService.class)) {
            return this;
        }

        return null;
    }

    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public MarketDataService getValue(HttpContext c) {
        return (MarketDataService) SpringContext.getInstance().get("marketDataService");
    }
}


