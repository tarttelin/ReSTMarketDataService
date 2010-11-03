package com.pyruby.marketdata;

import com.pyruby.marketdata.restapi.resource.TransactionalServletFilter;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.LiborCurveRepresentation;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.pyruby.marketdata.CurveBuilder.newBond;
import static com.pyruby.marketdata.CurveBuilder.newLiborCurve;
import static org.junit.Assert.*;

public class MarketDataEndToEndTest {
    private GrizzlyWebServer ws;

    @Before
    public void setUp() throws IOException {
        ws = new GrizzlyWebServer(9998);
        ServletAdapter jersey = new ServletAdapter();
        jersey.setServletInstance(new ServletContainer());
        jersey.addInitParameter(
            "com.sun.jersey.config.property.packages", "com.pyruby.marketdata.restapi");
        jersey.setServletPath("/ms");

        jersey.addFilter(new TransactionalServletFilter(), "HibernateSessionFilter", null);

        ws.addGrizzlyAdapter(jersey);
        ws.start();

    }

    @After
    public void tearDown() {
        ws.stop();
    }

    @Test
    public void putBond_shouldSaveABondAndReplyWithA201() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/bond");
        String request = "<Bond>" +
        "  <Name>British Petrolium</Name>" +
        "  <Ticker>LON:BP</Ticker>" +
        "  <Maturity>5Y</Maturity>" +
        "  <Issuer>British Petrolium</Issuer>" +
        "  <Tenors>" +
        "    <Tenor period=\"3m\" bps=\"40\"/>" +
        "    <Tenor period=\"6m\" bps=\"40\"/>" +
        "    <Tenor period=\"9m\" bps=\"40\"/>" +
        "    <Tenor period=\"1Y\" bps=\"40\"/>" +
        "    <Tenor period=\"2Y\" bps=\"160\"/>" +
        "    <Tenor period=\"3Y\" bps=\"160\"/>" +
        "    <Tenor period=\"4Y\" bps=\"160\"/>" +
        "    <Tenor period=\"5Y\" bps=\"160\"/>" +
        "  </Tenors>" +
        "</Bond>";
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(201, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void putBond_shouldReturn400_givenInvalidXml() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/bond");
        String request = "<Bond>" +
        "  <Gnome>British Petrolium</Gnome>" +
        "  <Ticker>LON:BP</Ticker>" +
        "  <Maturity>5Y</Maturity>" +
        "  <Issuer>British Petrolium</Issuer>" +
        "  <Tenors>" +
        "    <Tenor period=\"3m\" bps=\"40\"/>" +
        "    <Tenor period=\"6m\" bps=\"40\"/>" +
        "    <Tenor period=\"9m\" bps=\"40\"/>" +
        "    <Tenor period=\"1Y\" bps=\"40\"/>" +
        "    <Tenor period=\"2Y\" bps=\"160\"/>" +
        "    <Tenor period=\"3Y\" bps=\"160\"/>" +
        "    <Tenor period=\"4Y\" bps=\"160\"/>" +
        "    <Tenor period=\"5Y\" bps=\"160\"/>" +
        "  </Tenors>" +
        "</Bond>";
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(400, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void getBond_shouldReturn404_givenNoBondStored() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/bond/name/British%20Petrolium/maturity/12Y");
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        get(ClientResponse.class);
        assertEquals(404, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void getBond_shouldReturnBondXML_givenAStoredBond() throws UnsupportedEncodingException {
        CurveBuilder.BondBuilder builder = newBond();
        String request = builder.createXml();
        BondRepresentation repr = builder.createRepresentation();
        WebResource resource = Client.create().resource("http://localhost:9998/ms/marketdata/v1/bond");
        ClientResponse storeResponse = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(201, storeResponse.getClientResponseStatus().getStatusCode());


        String uri = "http://localhost:9998/ms/marketdata/v1/bond/name/" + URLEncoder.encode(repr.getName(), "UTF-8") + "/maturity/" + repr.getMaturity();
        resource = Client.create().resource(uri);
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        get(ClientResponse.class);

        assertEquals(200, response.getClientResponseStatus().getStatusCode());
        assertTrue(response.getEntity(String.class).contains("<Name>" + repr.getName() + "</Name>"));
    }

    @Test
    public void putLiborCurve_shouldSaveALiborCurveAndReplyWithA201() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/libor");
        String request = "<LiborCurve>" +
        "  <Name>Libor.USD</Name>" +
        "  <Currency>USD</Currency>" +
        "  <Tenors>" +
        "    <Tenor period=\"1imm\" bps=\"40\"/>" +
        "    <Tenor period=\"3imm\" bps=\"40\"/>" +
        "    <Tenor period=\"6imm\" bps=\"40\"/>" +
        "    <Tenor period=\"1Y\" bps=\"40\"/>" +
        "    <Tenor period=\"2Y\" bps=\"160\"/>" +
        "    <Tenor period=\"3Y\" bps=\"160\"/>" +
        "    <Tenor period=\"4Y\" bps=\"160\"/>" +
        "    <Tenor period=\"5Y\" bps=\"160\"/>" +
        "  </Tenors>" +
        "</LiborCurve>";
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(201, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void putLiborCurve_shouldReplyWithA400_givenAnInvalidLiborCurve() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/libor");
        String request = "<LiborCurve>" + // No name element
        "  <Currency>USD</Currency>" +
        "  <Tenors>" +
        "    <Tenor period=\"1imm\" bps=\"40\"/>" +
        "    <Tenor period=\"3imm\" bps=\"40\"/>" +
        "    <Tenor period=\"6imm\" bps=\"40\"/>" +
        "    <Tenor period=\"1Y\" bps=\"40\"/>" +
        "    <Tenor period=\"2Y\" bps=\"160\"/>" +
        "    <Tenor period=\"3Y\" bps=\"160\"/>" +
        "    <Tenor period=\"4Y\" bps=\"160\"/>" +
        "    <Tenor period=\"5Y\" bps=\"160\"/>" +
        "  </Tenors>" +
        "</LiborCurve>";
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(400, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void getLiborCurve_shouldReturn404_givenNoLiborCurveStored() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1/libor/name/libor.EUR");
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        get(ClientResponse.class);
        assertEquals(404, response.getClientResponseStatus().getStatusCode());
    }

    @Test
    public void getLiborCurve_shouldReturnLiborCurveXML_givenAStoredLiborCurve() throws UnsupportedEncodingException {
        CurveBuilder.LiborCurveBuilder builder = newLiborCurve();
        String request = builder.createXml();
        LiborCurveRepresentation repr = builder.createRepresentation();
        WebResource resource = Client.create().resource("http://localhost:9998/ms/marketdata/v1/libor");
        ClientResponse storeResponse = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        type(MediaType.APPLICATION_XML_TYPE).
        put(ClientResponse.class, request);
        assertEquals(201, storeResponse.getClientResponseStatus().getStatusCode());

        String uri = "http://localhost:9998/ms/marketdata/v1/libor/name/" + URLEncoder.encode(repr.getName(), "UTF-8");
        resource = Client.create().resource(uri);
        ClientResponse response = resource.accept(
        MediaType.APPLICATION_XML_TYPE).
        get(ClientResponse.class);

        assertEquals(200, response.getClientResponseStatus().getStatusCode());
        assertTrue(response.getEntity(String.class).contains("<Name>" + repr.getName() + "</Name>"));
    }

}
