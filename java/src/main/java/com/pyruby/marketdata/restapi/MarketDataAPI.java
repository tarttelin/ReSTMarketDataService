package com.pyruby.marketdata.restapi;

import com.pyruby.marketdata.MarketDataService;
import com.pyruby.marketdata.MarketDataServiceException;
import com.pyruby.marketdata.serializer.BondRepresentation;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

@Path("/marketdata/v1")
public class MarketDataAPI {

    @Context
    private UriInfo uriInfo;

    public MarketDataAPI() {
        
    }

    @PUT
    @Consumes({"application/xml"})
    public Response saveBond(@Context MarketDataService svc, JAXBElement<BondRepresentation> bond) {
        svc.storeBond(bond.getValue());
        return Response.created(uriInfo.getAbsolutePath()).build();
    }
    
}
