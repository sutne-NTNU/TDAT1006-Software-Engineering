package Logic.GLogicTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Logic.Objects.Municipality;
import Logic.Objects.User;

import static org.junit.Assert.*;

public class MunicipalityTest {


    User[] testUser;

    Municipality[] testMun;

    @Before
    public void  setUp() {
        testUser = new User[]{new User(1, "Jan"), new User(2, "Birk")
                , new User(3, "Sebastian"), new User(4, "Sivert")};

        testMun = new Municipality[]{new Municipality(1, "Melhus", testUser[0], false, 1)
                ,new Municipality(2, "Trondheim", testUser[1], true, 2)
                ,new Municipality(3, "Oppdal", testUser[2], true, 8)
                ,new Municipality(4, "Verran", testUser[3], true, 15)};
    }

    @After
    public void tearDown(){

    }

    @Test
    public void getOwnerTest() {
        for(int i = 0; i < testMun.length; i++) {
            User resultOwner = testMun[i].getOwner();
            User expOwner = testUser[i];
            assertSame(expOwner, resultOwner);
        }
    }

    @Test
    public void getNameTest() {
        for(int i = 0; i < testMun.length; i++) {
            String resultUserName = testMun[i].getOwner().getUsername();
            String expUserName = testUser[i].getUsername();
            assertSame(expUserName, resultUserName);
        }
    }

    @Test
    public void setOwnerTest() {
        testMun[1].setOwner(new User(5, "Haakon"));
        User newOwner = testMun[1].getOwner();
        User expOwner = new User(5, "Haakon");
        assertEquals(expOwner, newOwner);
    }

    @Test
    public void getTroopsTest() {
        int[] troops = new int[testMun.length];
        int[] expTroops = {1,2,8,15};

        for(int i = 0; i < testMun.length; i ++){
            troops[i] = testMun[i].getTroops();
        }
        assertArrayEquals(expTroops, troops);
    }

    @Test
    public void getIdTest() {
        int[] id = new int[testMun.length];
        int[] expId = {1,2,3,4};

        for(int i = 0; i < testMun.length; i ++){
            id[i] = testMun[i].getID();
        }
        assertArrayEquals(expId, id);
    }

    @Test
    public void getCityTest() {
        boolean[] city = new boolean[testMun.length];
        boolean[] expCity = {false, true, true, true};

        for(int i = 0; i < testMun.length; i++){
            city[i] = testMun[i].getCity();
        }
        assertArrayEquals(expCity, city);
    }

    @Test
    public void getValueTest() {
        int value = 1;
        assertTrue(value == 1);
    }

//    @Test
//    public void attacktroopsTest() {
//        testMun[0].Attacktroops();
//        int troopsNow = testMun[0].getTroops();
//        int expTroopsNow = 1;
//        assertEquals(expTroopsNow, troopsNow);
//    }

    @Test
    public void addTroopsTest() {

    }

    @Test
    public void setTroopsTest() {
        testMun[3].setTroops(4);
        int troopsNow = testMun[3].getTroops();
        int expTroopsNow = 4;
        assertEquals(expTroopsNow, troopsNow);
    }

    @Test
    public void attackMunicipalityTest() {
    	boolean expWinner = false;
    	boolean expWinner2 = false;
    	Municipality[] result = testMun[1].attackMunicipality(testMun[3]); //troops = 2 defending troops = 15
    	if(result[0].getTroops() == 1 && result[1] == null)
    	{
    		expWinner = true;
    	}

    	Municipality[] result2 = testMun[3].attackMunicipality(testMun[0]); //troops = 15 attacking troops = 1
    	if(result2[0].getTroops() == 1 && result2[1].getTroops() == 14)
    	{
    		expWinner2 = true;
    	}
    	assertEquals(expWinner, true);
    	assertEquals(expWinner2, true);
    }

//    @Test
//    public void canAttackTest() {
//        boolean[] canattack = new boolean[testMun.length];
//        boolean[] expCanAttack = {false, true, true, true};
//        for(int i = 0; i < testMun.length; i++){
//            canattack[i] = testMun[i].canAttack();
//            assertEquals(expCanAttack[i], canattack[i]);
//        }
//
//    }

    //return name + " - " +  "owner: " + owner.getUsername() + ", Troops: " + troops + ", city: " + city;
    @Test
    public void toStringTest() {
        String[] tostrings = new String[testMun.length];
        String[] tostringtest = new String[testMun.length];
        for(int i = 0; i < testMun.length; i++) {
            tostrings[i] = testMun[i].toString();
            tostringtest[i] =  testMun[i].getName() + " - " +  "owner: " + testMun[i].getOwner().getUsername() +
                    ", Troops: " + testMun[i].getTroops() + ", city: " + testMun[i].getCity();
        }
        assertArrayEquals(tostringtest, tostrings);
    }

    @Test
    public void equalsTest() {

        for(int i = 0; i < testMun.length - 1; i++){
            assertEquals(testMun[i], testMun[i]);
            assertNotEquals(testMun[i], testUser[i]);
        }
    }
}