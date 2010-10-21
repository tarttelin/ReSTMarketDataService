package com.pyruby.marketdata.model;

import org.junit.Test;

import static com.pyruby.marketdata.BondBuilder.newBond;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BondTest {
    
    @Test
    public void isValid_shouldReturnTrue_givenABondWithAllPropertiesSupplied() {
        assertTrue(newBond().createBond().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenABondWithNoName() {
        assertFalse(newBond().name(null).createBond().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenABondWithNoMaturity() {
        assertFalse(newBond().maturity(null).createBond().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenABondWithNoTenors() {
        assertFalse(newBond().withoutTenors().createBond().isValid());
    }

    @Test
    public void isValid_shouldReturnTrue_givenABondWithNoIssuer() {
        assertTrue(newBond().issuer(null).createBond().isValid());
    }

    @Test
    public void isValid_shouldReturnTrue_givenABondWithNoTicker() {
        assertTrue(newBond().ticker(null).createBond().isValid());
    }

}
