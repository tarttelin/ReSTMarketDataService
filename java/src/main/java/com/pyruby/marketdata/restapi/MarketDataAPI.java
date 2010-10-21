package com.pyruby.marketdata.restapi;

import com.pyruby.marketdata.MarketDataService;
import com.pyruby.marketdata.MarketDataServiceException;
import com.pyruby.marketdata.serializer.BondRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Path("/marketdata/v1")
public class MarketDataAPI {

    @Context
    private UriInfo uriInfo;

    public MarketDataAPI() {
        
    }

    @PUT
    @Consumes({"application/xml"})
    public Response saveBond(@Context MarketDataService svc, JAXBElement<BondRepresentation> bond) {
        try {
            svc.storeBond(bond.getValue());
            return Response.created(uriInfo.getAbsolutePath()).build();
        } catch (MarketDataServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Mandatory bond information missing").build();
        }
    }

    @GET
    @Produces({"application/xml"})
    @Path("/name/{name}/maturity/{maturity}")
    public BondRepresentation findBond(@Context MarketDataService svc, @PathParam("name") String name, @PathParam("maturity") String maturity) throws UnsupportedEncodingException {
        BondRepresentation bond = svc.findBondByNameAndMaturity(URLDecoder.decode(name, "UTF-8"), maturity);
        if (bond == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("No bond with name " + name + " and maturity " + maturity + " exists.").build());
        } else {
            return bond;
        }
    }
}
