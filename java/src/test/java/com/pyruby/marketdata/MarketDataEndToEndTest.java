package com.pyruby.marketdata;

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

        // HERE IS HOW YOU ADD A FILTER
        //jersey.addFilter(new MyFilter(), "HibernateSessionFilter", null);

        ws.addGrizzlyAdapter(jersey);
        ws.start();

    }

    @After
    public void tearDown() {
        ws.stop();
    }

    @Test
    public void put_shouldSaveABondAndReplyWithA201() {
        Client c = Client.create();
        WebResource resource = c.resource("http://localhost:9998/ms/marketdata/v1");
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
    public void put_shouldReturn400_givenInvalidXml() {
        fail("Todo");
    }

    @Test
    public void get_shouldReturn404_givenNoBondStored() {
        fail("Todo");
    }

    @Test
    public void get_shouldReturnBondXML_givenAStoredBond() {
        fail("Carry on from here");
    }
}
