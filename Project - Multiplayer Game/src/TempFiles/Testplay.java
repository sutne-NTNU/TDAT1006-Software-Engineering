package TempFiles;


import javax.swing.JOptionPane;

public class Testplay {
	public static void main(String[] args){
		String input = "";
		int opponent;

		GameSetup test = new GameSetup();
		System.out.print("Reserving a spot...");
		int your_player = test.joinGame();

		if(your_player != -1) {
			System.out.print("You are player: " + your_player);
			System.out.print("\nFinding opponent...");
			if(your_player == 1) {opponent = 2;} else {opponent = 1;}
			test.waitForOpponent();
			System.out.print("ENEMY LOCATED! \nStarting game...\n\n");

			if(your_player != 1) { //player 1 will send a number first
				System.out.println("Waiting for opponent to send a message...");
				test.waitForChangeFrom(opponent);
				JOptionPane.showMessageDialog(null, "Opponent says: " + test.getMessage(opponent));
			}

			int round = 0;
			while(test.game_IsActive()){
				round++;
				input = JOptionPane.showInputDialog("Write a message! (write \"exit\" to quit)");
				if(input.equals("") || input.equals("exit")) {
					input = "exit";
					break;
				}
				test.sendMessage(your_player, input);
				System.out.println("Waiting for opponent to say something...");
				test.waitForChangeFrom(opponent);
				if(test.game_IsActive()) {
					JOptionPane.showMessageDialog(null, "Opponent says: " + test.getMessage(opponent));

				}
			}
			System.out.println("\nShutting down and resetting...");
			test.endGame();
			if(input.equals("exit")) {
				JOptionPane.showMessageDialog(null, "Opponent left the game\nafter round " + round);
			}else {
				JOptionPane.showMessageDialog(null, "You quit the game\nafter round " + round);
			}
		}else{
			System.out.print("	GAME FULL	");
		}
		test.endGame();      //if game gets lockeed
	}
} 
