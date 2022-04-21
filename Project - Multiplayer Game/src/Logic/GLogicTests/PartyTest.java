package Logic.GLogicTests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Logic.Objects.Party;

public class PartyTest {

    Party test;
    Party control;

    @Before
    public void setUp(){
        test = 		new Party("blue", "generic party", 0);
        control = 	new Party("red", "generic party", 1);
    }

    @After
    public void tearDown(){}

    @Test
    public void equalsTest() {
        assertEquals(test,test);
        assertTrue(test.equals(test));
        assertNotEquals(control,test);
        assertFalse(test.equals(control));
    }

    @Test
    public void toStringTest() {
        assertEquals(test.toString(), "0: generic party, blue");
        assertEquals(control.toString(), "1: generic party, red");
    }
}