package Logic.GLogicTests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Logic.Objects.Party;
import Logic.Objects.User;

public class UserTest {
    User test;
    User control;
    Party testP;

    @Before
    public void setUp(){
        testP = new Party("blue", "Genreic party", 0);
        test = new User(0, "Test");
        control = new User(1,"Control");
    }

    @After
    public void tearDown(){}

    @Test
    public void setPartyTest() {
        test = new User(2, "test", testP);
        control = test;
        Party testP2 = new Party("red", "Generic party", 0);
        assertEquals(test.getParty(), testP);
        test.setParty(testP2);
        assertEquals(control, test);
    }

    @Test
    public void toStringTest() {
        assertEquals(test.toString(), "ID: 0, username: Test");
    }

    @Test
    public void equalsTest() {
        assertEquals(test,test);
        assertTrue(test.equals(test));
        assertNotEquals(control,test);
        assertFalse(test.equals(control));
    }
}