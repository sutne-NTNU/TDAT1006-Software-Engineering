package fx.controllers;

import Clientclasses.Client;
import Logic.Objects.Game;
import Logic.Objects.Party;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import Logic.Objects.Municipality;
import Logic.Objects.User;
import Logic.Stats.ArchivedGame;
import Logic.Stats.PlayerStat;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import javafx.concurrent.Task;
public class GuiLogic
{

	private final String black = "-fx-fill: #000000;";
	private final String white = "-fx-fill: #FFFFFF;";
	private final String shadow = "-fx-effect: innershadow(gaussian, rgba( 0, 0, 0, 0.4 ), 50, 10, 0, 0 );";

	/**Updates the lobby in the Gui, given the table in the gui and an ArrayList of users
	 * @param table the TableView in the gui
	 * @param users the ArrayList of updated users
	 * @see TableView
	 * @see User
	 */
	//Updates values in the Lobby TableView
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateLobby(TableView<User> table, ArrayList<User> users){
		table.setEditable(true);
		TableColumn username 	= table.getColumns().get(0);
		TableColumn party 		= table.getColumns().get(1);
		username.setCellValueFactory(new PropertyValueFactory<>("username"));
		party	.setCellValueFactory(new PropertyValueFactory<>("party"));
		table.getItems().setAll(users);
		table.setEditable(false);
	}

	/**Inserts values into the GameHistory TableView
	 * @param table a table in the gui
	 * @param hist an ArrayList of archived games
	 * @see TableView
	 * @see ArchivedGame
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateHistory(TableView<ArchivedGame> table, ArrayList<ArchivedGame> hist){
		table.setEditable(true);
		TableColumn date	= table.getColumns().get(0);
		TableColumn party	= table.getColumns().get(1);
		TableColumn rounds 	= table.getColumns().get(2);
		TableColumn roomsize= table.getColumns().get(3);
		TableColumn winner 	= table.getColumns().get(4);
		date	.setCellValueFactory(new PropertyValueFactory<>("date"));
		party	.setCellValueFactory(new PropertyValueFactory<>("played_as_party"));
		rounds	.setCellValueFactory(new PropertyValueFactory<>("rounds"));
		roomsize.setCellValueFactory(new PropertyValueFactory<>("roomsize"));
		winner	.setCellValueFactory(new PropertyValueFactory<>("winner"));
		table.getItems().setAll(hist);
		table.setEditable(false);
	}

	/** Inserts values into the player ArrayList in TableView
	 * @param table a tableView in the Gui
	 * @param topPlayers an ArrayList of playerStats
	 * @see PlayerStat
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateTopPlayers(TableView<PlayerStat> table, ArrayList<PlayerStat> topPlayers){
		table.setEditable(true);
		TableColumn username	= table.getColumns().get(0);
		TableColumn games_played= table.getColumns().get(1);
		TableColumn games_won 	= table.getColumns().get(2);
		TableColumn win_loss 	= table.getColumns().get(3);
		TableColumn date_joined = table.getColumns().get(4);
		username	.setCellValueFactory(new PropertyValueFactory<>("username"));
		games_played.setCellValueFactory(new PropertyValueFactory<>("gamecount"));
		games_won	.setCellValueFactory(new PropertyValueFactory<>("gamesWon"));
		win_loss	.setCellValueFactory(new PropertyValueFactory<>("win_loss"));
		date_joined	.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
		table.getItems().setAll(topPlayers);
		table.setEditable(false);
	}

	public void updatePersonalStats(Text[] fields, PlayerStat stat){
		fields[0].setText(stat.getUsername());
		fields[1].setText("Gamecount: " + stat.getGamecount());
		fields[2].setText("Wins: " + stat.getGamesWon());
		fields[3].setText(("Win/loss: " + stat.getWin_loss()));
		fields[4].setText("Joinedate: " + stat.getJoinDate());
	}

	/** Sets the feedback text.
	 * @param node the reference node
	 * @param gameFeedbackText the feedbacktext in the game window
	 * @param feedback blanked out feedback text
	 * @see Client#getMarked()
	 */
	//diverse feedback to the player through a textnode
	public void gameFeedbackTextLogic(Node node, Text gameFeedbackText, Text feedback){
		if(!Client.getMarked()){
			gameFeedbackText.setText(node.getId().substring(0, 1).toUpperCase() + node.getId().substring(1).replace("ID", ""));
			feedback.setText("");
			return;
		}
		feedback.setText("");
		String markedName = Client.getCurMun().getName();
		if(Client.getMarkedMun().contains(node.getId())) {  //checks if the mun you are hovering with your mouse contains in the marked mun array.
			gameFeedbackText.setText(markedName.substring(0, 1).toUpperCase() + markedName.substring(1).replace("ID", "") + " --> " + node.getId().substring(0, 1).toUpperCase() + node.getId().substring(1).replace("ID", ""));
			return;
		}else {
			feedback.setText("Du kan ikke angripe");
		}
		gameFeedbackText.setText(node.getId().substring(0, 1).toUpperCase() + node.getId().substring(1).replace("ID", ""));
	}


	public void setText(Text textfield, String text){
		Platform.runLater(new Runnable(){
			public void run(){
				textfield.setText(text);
			}

		});
	}

	/** Marks neighbouring municipalities
	 * @param node the refrence node
	 * @param municipalityborders Array of neighbours of the selected municipality
	 * @param munTiles Array of municipalities
	 * @param municipalitybordersDice Array of dice number of the neighbouring municipalities
	 * @param munTilesDice Array of municipality dice numbers
	 * @param munarray ArrayList of municipalities
	 * @param you User-object that is the current user
	 * @see Party#getColour()
	 * @see Municipality#getName()
	 * @see Client#setCurMun(Municipality)
	 * @see Client#setMarked(boolean)
	 * @see #removeShadow(SVGPath[][], SVGPath[], Text[][])
	 * @see #addShadow(Node, SVGPath[][], SVGPath[], Text[][], Text[], ArrayList, User)
	 */

	public void neighbours(Node node, SVGPath[][] municipalityborders, SVGPath[] munTiles, Text[][] municipalitybordersDice, Text[] munTilesDice, ArrayList<Municipality> munarray, User you) {
		boolean yourprovince = true;
		for(Municipality mun: munarray){
			for(SVGPath path:munTiles){
				if(mun.getName().equals(path.getId())){
					path.setFill(Color.web(mun.getOwner().getParty().getColour()));
					path.setStyle("");
				}
			}
			if(mun.getName().equals(node.getId()) && (mun.getOwner() == null||!(mun.getOwner().equals(you)))){
				yourprovince = false;
			}
		}
		if(!yourprovince){
			Client.setCurMun(null);
			Client.setMarked(false);
			return;
		}
		if(Client.getMarked() && node.getId().equals(Client.getCurMun().getName())){
			removeShadow(municipalityborders, munTiles, municipalitybordersDice);
			Client.setCurMun(null);
			Client.setMarked(false);
			return;
		}
		Client.setMarked(true);
		node.setStyle(black);
		removeShadow(municipalityborders, munTiles, municipalitybordersDice);
		addShadow(node, municipalityborders, munTiles, municipalitybordersDice, munTilesDice, munarray, you);
	}

	/** Adds a shadow to municipalities that border to reference node municipality
	 * @param node Reference node
	 * @param municipalityborders Array of neighbours of the selected municipality
	 * @param munTiles Array of municipalities
	 * @param municipalitybordersDice Array of dice number of the neighbouring municipalities
	 * @param munTilesDice Array of municipality dice numbers
	 * @param munarray ArrayList of municipalities
	 * @param you User-object that is the current user
	 * @see Client#setMarkedMun(ArrayList)
	 * @see
	 */

	private void addShadow(Node node, SVGPath[][] municipalityborders, SVGPath[] munTiles, Text[][] municipalitybordersDice, Text[] munTilesDice, ArrayList<Municipality> munarray, User you){
		ArrayList<String> temp = new ArrayList<>();
		for (int i = 0; i < munTiles.length; i++) {
			if (node.equals(munTiles[i])) {
				munTilesDice[i].setStyle(white);
				for (int j = 0; j < municipalityborders[i].length; j++) {
					for(Municipality mun:munarray){
						if(mun.getName().equals(municipalityborders[i][j].getId()) && !(mun.getOwner().equals(you))){
							municipalityborders[i][j].setStyle(municipalityborders[i][j].getStyle() + shadow);//getStyle overrides current style.
							municipalitybordersDice[i][j].setStyle(white);
							temp.add(municipalityborders[i][j].getId());
						}
					}
				}
			}
		}
		Client.setMarkedMun(temp);
	}

	/** Removes shadow from municipalities
	 * @param municipalityborders Array of neighbours of the selected municipality
	 * @param munTiles Array of municipalities
	 * @param municipalitybordersDice Array of dice number of the neighbouring municipalities
	 */
	private void removeShadow(SVGPath[][] municipalityborders, SVGPath[] munTiles, Text[][] municipalitybordersDice) {
		for (int i = 0; i < munTiles.length; i++) {
			for (int k = 0; k < municipalityborders[i].length; k++) {
				municipalityborders[i][k].setStyle(municipalityborders[i][k].getStyle().replace(shadow, ""));
				municipalitybordersDice[i][k].setStyle(municipalitybordersDice[i][k].getStyle().replace(white, ""));
			}
		}
	}

	/**
	 * @param source Reference node
	 * @param municipalityborders Array of neighbours of the selected municipality (reference node)
	 * @param munTiles Array of municipalities
	 * @return Municipality-object or Null if clicked municipality (reference node) is owned by the current player
	 * @see Municipality
	 * @see Game#getMunicipalities() ()
	 * @see Municipality#getOwner()
	 */
	//finds the municipality object corresponding to the SVGPath and returns it
	public Municipality checkdefence (Node source, SVGPath[][] municipalityborders, SVGPath[] munTiles){
		for (Municipality mun : Client.getGame().getMunicipalities()) {
			if (mun.getName().equals(source.getId()) && !(mun.getOwner().equals(Client.getCurMun().getOwner()))) {
				for (int i = 0; i < municipalityborders.length; i++) {
					for (int j = 0; j < municipalityborders[i].length; j++) {
						if (municipalityborders[i][j].getId().equals(mun.getName()) && munTiles[i].getId().equals(Client.getCurMun().getName())) {
							return mun;
						}
					}
				}
			}
		}
		return null;
	}

	/** Clears style from the map and updates the colours according to the municipality owner
	 * @param munTiles Array of municipalities
	 * @param munarray  ArrayList of municipalities
	 * @param dices Array of municipality dice numbers
	 * @see Client#setMarked(boolean)
	 */

	public void updateGUIMmap(SVGPath[] munTiles, ArrayList<Municipality> munarray, Text[] dices){
		Client.setMarked(false);
		for(Municipality mun: munarray){
			for(int i = 0; i<munTiles.length;i++){
				if(mun.getName().equals(munTiles[i].getId())){
					munTiles[i].setFill(Color.web(mun.getOwner().getParty().getColour()));
					munTiles[i].setStyle("");
					dices[i].setText(mun.getTroops()+ "");
					dices[i].setStyle(mun.getTroops()+ "");
				}
			}
		}
	}
}