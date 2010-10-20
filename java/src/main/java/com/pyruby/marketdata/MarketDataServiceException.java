package com.pyruby.marketdata;

import javax.xml.bind.JAXBException;

public class MarketDataServiceException extends Exception {

    public MarketDataServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
