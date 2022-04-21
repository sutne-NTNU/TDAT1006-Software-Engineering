package Logic;

import java.util.Random;
import java.util.ArrayList;
import Clientclasses.*;
import Logic.Objects.*;

/**
 * a class that contains static methods and attributes to calculate game logic
 */

public class Logic {
	private final static Random DICE = new Random();
	private final static String[] municipalitynames = new String[]
			{	"lierneID", "snaasaID", "grongID", "overhallaID", "namsosID", "steinkjerID", "inderoyID", "namdaleidID", "verdalID", "flatangerID", 
				"osenID", "roanID", "afjordID", "verranID", "rinndalID", "merakerID", "stjordalID", "levangerID", "leksvikID", "rissaID", "bjugnID", 
				"tydalID","selbuID", "malvikID", "trondheimID", "klaebuID", "melhusID", "agdenesID", "skaunID", "orkdalID", "meldalID", "surnadalID", 
				"rorosID", "holtalenID", "midtregauldalID", "sunndalID", "rennebuID", "oppdalID", "snillfjordID", "hemneID", "hitraID", "aureID"	};

	/**
	 * rolls one or more dices
	 * @param numdice how many dices shall be rolled, could be done as one huge dice, but we like doing it as if it was alot of dice
	 * @return the total value of the dices
	 */
	private static int rollDice(int numdice){
		int dicesum = 0;
		for(int i = 0; i<numdice;i++){
			dicesum += DICE.nextInt(6)+1;
		}
		return dicesum;
	}

	/**
	 * rolls to hands of dice, then checks which hand has the higher value.
	 * @param atkdice number of dice the attacker has
	 * @param defdice number of dice the defender has
	 * @return true if attacker dice is bigger than defenderdice, giving the defender an edge as it will return false if atkrolldice is less than or equals defrolldice
	 */
	public static boolean invasion(int atkdice, int defdice){
		int atkRollDice = rollDice(atkdice);
		int defRollDice = rollDice(defdice);
		Client.setRollDiceResults(atkRollDice + "-" + defRollDice);
		return atkRollDice > defRollDice;
	}

	/**
	 *
	 *This method creates a map for the game, with 2-5 players
	 *
	 * @param userlist - an array of users
	 * @return	an Arraylist of municipalities, the map of a game
	 * @throws IllegalException - an exception that is thrown when something not legal according to logic is done
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })

	public static ArrayList<Municipality> createMap(User[] userlist) throws IllegalException {
		if (userlist.length < 2 || userlist.length > 5) throw new IllegalException("Not a valid number of players");
		//number of municipalities on map
		int totalsize = 42;
		//number of municipalities per user
		int peruser = totalsize / userlist.length;
		//number of cities on map
		int citynum = DICE.nextInt(7) + 6;
		//creating an empty ArrayList of municipalities
		ArrayList<Municipality> municipalities = new ArrayList<>();
		//randomly placing cities on map
		ArrayList<Integer> citylist = citylist(citynum, totalsize);
		//array of arraylists containing the municipality numbers
		ArrayList<Integer>[] usermunis;
		ArrayList<Integer>[] municipalitytroops;


		//Creates map for two players
		if (userlist.length == 2) {
			ArrayList<Integer> usermunicipalities1 = usermunicipalities(peruser, totalsize);
			ArrayList<Integer> usermunicipalities2 = usermunicipalities(peruser, usermunicipalities1, totalsize);
			ArrayList[] temparray = {usermunicipalities1, usermunicipalities2};
			usermunis = temparray;
			ArrayList<Integer> temp1 = giveTroops(peruser);
			ArrayList<Integer> temp2 = giveTroops(peruser);
			ArrayList[] anothertemp = {temp1, temp2};
			municipalitytroops = anothertemp;

			//Creates map for three players
		} else if (userlist.length == 3) {
			ArrayList<Integer> usermunicipalities1 = usermunicipalities(peruser, totalsize);
			ArrayList<Integer> usermunicipalities2 = usermunicipalities(peruser, usermunicipalities1, totalsize);
			ArrayList<Integer> usermunicipalities3 = usermunicipalities(usermunicipalities1, usermunicipalities2, totalsize);
			ArrayList[] temparray = {usermunicipalities1, usermunicipalities2, usermunicipalities3};
			usermunis = temparray;
			ArrayList<Integer> temp1 = giveTroops(peruser);
			ArrayList<Integer> temp2 = giveTroops(peruser);
			ArrayList<Integer> temp3 = giveTroops(peruser);
			ArrayList[] anothertemp = {temp1, temp2, temp3};
			municipalitytroops = anothertemp;

			//Creates map for four players
		} else if (userlist.length == 4) {
			ArrayList<Integer> usermunicipalities1 = usermunicipalities(peruser, totalsize);
			ArrayList<Integer> usermunicipalities2 = usermunicipalities(peruser, usermunicipalities1, totalsize);
			ArrayList<Integer>[] arrayint = new ArrayList[]{usermunicipalities1, usermunicipalities2};
			ArrayList<Integer> usermunicipalities3 = usermunicipalities(peruser, arrayint, totalsize);
			arrayint = new ArrayList[]{usermunicipalities1, usermunicipalities2, usermunicipalities3};
			ArrayList<Integer> usermunicipalities4 = usermunicipalities(peruser, arrayint, totalsize);
			ArrayList[] temparray = {usermunicipalities1, usermunicipalities2, usermunicipalities3, usermunicipalities4};
			usermunis = temparray;

			ArrayList<Integer> temp1 = giveTroops(peruser);
			ArrayList<Integer> temp2 = giveTroops(peruser);
			ArrayList<Integer> temp3 = giveTroops(peruser);
			ArrayList<Integer> temp4 = giveTroops(peruser);
			ArrayList[] anothertemp = {temp1, temp2, temp3, temp4};
			municipalitytroops = anothertemp;

			//Creates map for five players
		} else if (userlist.length == 5) {
			ArrayList<Integer> usermunicipalities1 = usermunicipalities(peruser, totalsize);
			ArrayList<Integer> usermunicipalities2 = usermunicipalities(peruser, usermunicipalities1, totalsize);
			ArrayList<Integer>[] arrayint = new ArrayList[]{usermunicipalities1, usermunicipalities2};
			ArrayList<Integer> usermunicipalities3 = usermunicipalities(peruser, arrayint, totalsize);
			arrayint = new ArrayList[]{usermunicipalities1, usermunicipalities2, usermunicipalities3};
			ArrayList<Integer> usermunicipalities4 = usermunicipalities(peruser, arrayint, totalsize);
			arrayint = new ArrayList[]{usermunicipalities1, usermunicipalities2, usermunicipalities3, usermunicipalities4};
			ArrayList<Integer> usermunicipalities5 = usermunicipalities(peruser, arrayint, totalsize);
			ArrayList[] temparray = {usermunicipalities1, usermunicipalities2, usermunicipalities3, usermunicipalities4, usermunicipalities5};
			usermunis = temparray;

			ArrayList<Integer> temp1 = giveTroops(peruser);
			ArrayList<Integer> temp2 = giveTroops(peruser);
			ArrayList<Integer> temp3 = giveTroops(peruser);
			ArrayList<Integer> temp4 = giveTroops(peruser);
			ArrayList<Integer> temp5 = giveTroops(peruser);
			ArrayList[] anothertemp = {temp1, temp2, temp3, temp4, temp5};
			municipalitytroops = anothertemp;

		} else {
			throw new IllegalException("yo ma homeboi, ya can't do this, it's illegal. you need to talk to judge joe and pay your fine");
		}


		//generates the municipalities for the map. The if sentence checks whether the municipality is a city and who owns it. lessercounter == owner
		for (int i = 0; i < totalsize; i++) {
			int lessercounter = 0;
			for (ArrayList<Integer> usermuni : usermunis) {
				if (citylist.contains(i) && usermuni.contains(i)) {
					municipalities.add(new Municipality(i, municipalitynames[i], userlist[lessercounter], true, municipalitytroops[lessercounter].get(0)));
					municipalitytroops[lessercounter].remove(0);
					continue;
				} else if (usermuni.contains(i)) {
					municipalities.add(new Municipality(i, municipalitynames[i], userlist[lessercounter], false, municipalitytroops[lessercounter].get(0)));
					municipalitytroops[lessercounter].remove(0);
					continue;
				}
				lessercounter++;
			}
			if(municipalities.size() <= i) {
				if (citylist.contains(i))
					municipalities.add(new Municipality(i, municipalitynames[i], new User(0, "", new Party("#FFFFFF", "null", -1)), true, 0));
				else {
					municipalities.add(new Municipality(i, municipalitynames[i], new User(0, "", new Party("#FFFFFF", "null", -1)), false, 0));
				}
			}

		}
		return municipalities;
	}

	/**
	 * Checks whether a player owns a municipality or not
	 * @param mun the municipality to check if the player owns
	 * @param player the user who might own the municipality
	 * @return true if the player owns the municipality
	 */
	public static boolean isOwner(Municipality mun, User player){
		return (mun.getOwner().equals(player));
	}

	private static ArrayList<Integer> giveTroops(int count){
		ArrayList<Integer> array = new ArrayList<>();
		int numtroops = 60;
		for(int i = 0; i<count; i++){
			int muntroops = rollDice(1);
			while(muntroops > numtroops){
				if(muntroops > 3) {
					muntroops -= 2;
				}else if (numtroops <= 0){
					muntroops = 1;
					break;
				}else{
					muntroops--;
				}
			}
			array.add(muntroops);
			numtroops -= muntroops;
		}
		return array;
	}


	/**
	 * randomly generates number from 0-47 until it can return an arraylist of size num, which will be given to a user
	 * @param num
	 * @param totalsize
	 * @return
	 */
	private static ArrayList<Integer> usermunicipalities(int num, int totalsize){
		ArrayList<Integer> list = new ArrayList<>();
		while(list.size()<num){
			int randnum = DICE.nextInt(totalsize);
			if(!list.contains(randnum)){
				list.add(randnum);
			}
		}
		return list;
	}
	
	//same as above, just that it excludes values in input arraylist
	private static ArrayList<Integer> usermunicipalities(int num, ArrayList<Integer>otherusermunicipalities, int totalsize){
		ArrayList<Integer> list = new ArrayList<>();
		while(list.size()<num){
			int randnum = DICE.nextInt(totalsize);
			if(!list.contains(randnum) && !otherusermunicipalities.contains(randnum)){
				list.add(randnum);
			}
		}
		return list;
	}

	private static boolean containsint(ArrayList<Integer>[] intlistlist, Integer num){
		for(ArrayList<?> intlist:intlistlist){
			if(intlist.contains(num)) return false;
		}
		return true;
	}

	private static ArrayList<Integer> usermunicipalities(int num, ArrayList<Integer>[] otherusermunicipalities, int totalsize){
		ArrayList<Integer> list = new ArrayList<>();
		while(list.size()<num){
			int randnum = DICE.nextInt(totalsize);
			if(!list.contains(randnum) && containsint(otherusermunicipalities, randnum)){
				list.add(randnum);
			}
		}
		return list;
	}
	
	//just fills an arraylist up with all remaining values not already in the other arraylists.
	private static ArrayList<Integer> usermunicipalities(ArrayList<Integer> user3municipalities, ArrayList<Integer> user2municipalities, int munißipalitycount){
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i<munißipalitycount;i++){
			if(!user3municipalities.contains(i) && !user2municipalities.contains(i)){
				list.add(i);
			}
		}
		return list;
	}

	//randomly generates unique numbers 0-totalsize until it has filled num.
	private static ArrayList<Integer> citylist(int num, int totalsize){
		ArrayList<Integer>citylist = new ArrayList<>();
		while(citylist.size() < num){
			int randnum = DICE.nextInt(totalsize);
			if(!citylist.contains(randnum)){
				citylist.add(randnum);
			}
		}
		return citylist;
	}
}


