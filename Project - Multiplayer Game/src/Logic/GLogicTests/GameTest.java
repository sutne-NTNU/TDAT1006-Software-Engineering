package Logic.GLogicTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Logic.*;
import Logic.Objects.Game;
import Logic.Objects.IllegalException;
import Logic.Objects.Municipality;
import Logic.Objects.Party;
import Logic.Objects.User;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class GameTest {
    User testUser;
    User controlUser;
    Municipality testMun;
    Municipality controlMun;
    Party testParty;
    Party controlParty;
    Game test;
    ArrayList<Municipality> tempMuns;



    @Before
    public void setUp() throws IllegalException{
        testParty = new Party("blue", "Test", 0);
        controlParty = new Party("red", "Control", 1);
        testUser = new User(0, "Test", testParty);
        controlUser = new User(1,"Control", controlParty);
        testMun = new Municipality(0, "Test", testUser, true, 2);
        controlMun = new Municipality(1, "Control", controlUser, false, 6);
        User[] users ={testUser, controlUser};
        tempMuns = new ArrayList<>();
        tempMuns.add(testMun);
        tempMuns.add(controlMun);
        test = new Game(users, Logic.createMap(users));
        test.setMunicipalities(tempMuns);
        //test.setGameId(0);
    }

    @After
    public void tearDown(){
    }


    /*@Test
    public void roundTurnTest() {
        //Tests the game counters.
        //Also tests curUser methods
        assertEquals(0, test.getTurn());
        test.setCurrentPlayer(testUser);
        assertEquals(0, test.getCurUser().getUserid());
        test.endTurn();
        assertEquals(0, test.getTurn());
        test.endTurn();
        test.increaseRoundNumber();
        assertEquals(1, test.getRoundNumber());
    }*/


    @Test
    public void actionTest(){
        //Tests the attack method
        assertTrue(attackTest());
    }



    private boolean attackTest() {
    	//Game.battle(), Game.setOwner() is also covered here
    	//prob win attacker = 5/12
    	//prob loss attacker = 7/12
    	int win = 0;
    	int loss = 0;
    	for(int i = 0; i < 100000; i++ ){
    		Municipality[] res = test.attack(testMun, controlMun);
    		if (res[1] == null){
    			tempMuns.get(0).setOwner(testUser);
    			loss++;

    		}else if(res[0] != null){
    			tempMuns.get(1).setOwner(controlUser);
    			win++;
    		}
    	} 
    	//exp win/loss ratio 42/58, roughly 0.72
    	if(loss == 0) loss = 1;
    	double ratio = win/loss;
    	double upper = ratio * 1.1;
    	double lower = ratio * 0.9;
    	return ratio <= upper && ratio >= lower;
    }


//    @Test
//    public void municipalitiesTest() {
//        //Also tests update map
//        Municipality[] testArray = {testMun, controlMun};
//        assertEquals(tempMuns, test.getMuns());
//        test.updateMap();
//        assertArrayEquals(testArray, test.getMap());
//    }


    @Test
    public void joinMunicipalities() {
        //Will write if feature is implemented
    }

    @Test
    public void toStringTest() {
        test.setMunicipalities(tempMuns);
        String exp = "\tgameID: 0\n\tPlayers: \n\t- Test playing as Test" +"\n\t- Control playing as Control" + "\nControl makes the first move";
        String exp2 = "\tgameID: 0\n\tPlayers: \n\t- Test playing as Test" +"\n\t- Control playing as Control" + "\nTest makes the first move";
        assertTrue(test.toString().equals(exp) || test.toString().equals(exp2));
    }
}