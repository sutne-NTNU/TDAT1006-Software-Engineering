package Logic.GLogicTests;

import Logic.Logic;
import Logic.Objects.IllegalException;
import Logic.Objects.Municipality;
import Logic.Objects.User;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class LogicTest {

    User[] testusers;
    Municipality[] testmuns;
    
    @Before
    public void setUp(){
        testusers = new User[]
        		{	new User(2, "Birk"),
        			new User(3, "Sebastian"), 
        			new User(4, "Sivert")};
    }

    @After
    public void tearDown(){
    }

    @Test
    public void invasionTest() {


        int attackWinsDice = 6;
        int defenseLosesDice = 2;

        boolean winner = Logic.invasion(attackWinsDice, defenseLosesDice);
        boolean expWinner = true;

        int attackLosesDice = 1;
        int defenceWinsDice = 5;

        boolean loser = Logic.invasion(attackLosesDice, defenceWinsDice);
        boolean exploser = false;

        assertEquals(expWinner, winner);
        assertEquals(exploser, loser);
    }

    @Test
    public void createMapTest()  {
        ArrayList<Municipality> created = new ArrayList<>();
        try {
            created = Logic.createMap(testusers);
        }catch (IllegalException ie){
            ie.printStackTrace();
        }
        int[] expPeruser = {14, 14, 14};
        int[] peruser = new int[3];
        for(Municipality mun: created){
            if(mun.getOwner().equals(testusers[0])){
                peruser[0] ++;
            }else if(mun.getOwner().equals(testusers[1])){
                peruser[1] ++;
            }else{
                peruser[2] ++;
            }
        }
        for(int i = 0; i < 3; i++){
            assertEquals(expPeruser[i], peruser[i]);
        }


    }
    @Test
    public void isOwnerTest() {
        try {
            ArrayList<Municipality> created = Logic.createMap(testusers);
            for(Municipality mun: created){
                if(mun.getOwner().equals(testusers[0])){
                    assertTrue(Logic.isOwner(mun, testusers[0]));
                }else{
                    assertFalse(Logic.isOwner(mun, testusers[0]));
                }
            }
        }catch (IllegalException ie){
            ie.printStackTrace();
        }
    }
}