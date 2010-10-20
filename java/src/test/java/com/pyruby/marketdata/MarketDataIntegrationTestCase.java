package com.pyruby.marketdata;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations={"/MarketDataIntegrationTestCase-context.xml"})
public class MarketDataIntegrationTestCase  extends AbstractTransactionalJUnit4SpringContextTests {
    
}
