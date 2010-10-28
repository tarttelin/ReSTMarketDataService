package com.pyruby.marketdata.model;

import org.junit.Test;

import static com.pyruby.marketdata.CurveBuilder.newBond;
import static com.pyruby.marketdata.CurveBuilder.newLiborCurve;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LiborCurveTest {
    @Test
    public void isValid_shouldReturnTrue_givenALiborCurveWithAllPropertiesSupplied() {
        assertTrue(newLiborCurve().createCurve().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenALiborCurveWithNoName() {
        assertFalse(newLiborCurve().name(null).createCurve().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenALiborCurveWithNoMaturity() {
        assertFalse(newLiborCurve().currency(null).createCurve().isValid());
    }

    @Test
    public void isValid_shouldReturnFalse_givenALiborCurveWithNoTenors() {
        assertFalse(newLiborCurve().withoutTenors().createCurve().isValid());
    }

}
