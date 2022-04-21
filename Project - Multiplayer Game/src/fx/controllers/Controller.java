package fx.controllers;
 
import Clientclasses.Client;
import fx.Main;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.input.ZoomEvent;
//import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;

import Logic.Objects.*;
import Logic.Stats.*;

public class Controller {

	public static Slider volumeSlider;
	@FXML private Slider volumeSlider1;
	@FXML private Slider volumeSlider2;
	@FXML private Slider volumeSlider3;
	@FXML private Slider volumeSlider4;
	@FXML private Slider volumeSlider5;
	@FXML private Slider volumeSlider6;

	@FXML  private SVGPath lierneID;
	@FXML  private SVGPath snaasaID;
	@FXML  private SVGPath grongID;
	@FXML  private SVGPath overhallaID;
	@FXML  private SVGPath namsosID;
	@FXML  private SVGPath steinkjerID;
	@FXML  private SVGPath inderoyID;
	@FXML  private SVGPath namdaleidID;
	@FXML  private SVGPath verdalID;
	@FXML  private SVGPath flatangerID;
	@FXML  private SVGPath osenID;
	@FXML  private SVGPath roanID;
	@FXML  private SVGPath afjordID;
	@FXML  private SVGPath verranID;
	@FXML  private SVGPath rinndalID;
	@FXML  private SVGPath merakerID;
	@FXML  private SVGPath stjordalID;
	@FXML  private SVGPath levangerID;
	@FXML  private SVGPath leksvikID;
	@FXML  private SVGPath rissaID;
	@FXML  private SVGPath bjugnID;
	@FXML  private SVGPath tydalID;
	@FXML  private SVGPath selbuID;
	@FXML  private SVGPath malvikID;
	@FXML  private SVGPath trondheimID;
	@FXML  private SVGPath klaebuID;
	@FXML  private SVGPath melhusID;
	@FXML  private SVGPath agdenesID;
	@FXML  private SVGPath skaunID;
	@FXML  private SVGPath orkdalID;
	@FXML  private SVGPath meldalID;
	@FXML  private SVGPath surnadalID;
	@FXML  private SVGPath rorosID;
	@FXML  private SVGPath holtalenID;
	@FXML  private SVGPath midtregauldalID;
	@FXML  private SVGPath sunndalID;
	@FXML  private SVGPath rennebuID;
	@FXML  private SVGPath oppdalID;
	@FXML  private SVGPath snillfjordID;
	@FXML  private SVGPath hemneID;
	@FXML  private SVGPath hitraID;
	@FXML  private SVGPath aureID;

	@FXML  private Text lierneDice;
	@FXML  private Text snaasaDice;
	@FXML  private Text grongDice;
	@FXML  private Text overhallaDice;
	@FXML  private Text namsosDice;
	@FXML  private Text steinkjerDice;
	@FXML  private Text inderoyDice;
	@FXML  private Text namdaleidDice;
	@FXML  private Text verdalDice;
	@FXML  private Text flatangerDice;
	@FXML  private Text osenDice;
	@FXML  private Text roanDice;
	@FXML  private Text afjordDice;
	@FXML  private Text verranDice;
	@FXML  private Text rinndalDice;
	@FXML  private Text merakerDice;
	@FXML  private Text stjordalDice;
	@FXML  private Text levangerDice;
	@FXML  private Text leksvikDice;
	@FXML  private Text rissaDice;
	@FXML  private Text bjugnDice;
	@FXML  private Text tydalDice;
	@FXML  private Text selbuDice;
	@FXML  private Text malvikDice;
	@FXML  private Text trondheimDice;
	@FXML  private Text klaebuDice;
	@FXML  private Text melhusDice;
	@FXML  private Text agdenesDice;
	@FXML  private Text skaunDice;
	@FXML  private Text orkdalDice;
	@FXML  private Text meldalDice;
	@FXML  private Text surnadalDice;
	@FXML  private Text rorosDice;
	@FXML  private Text holtalenDice;
	@FXML  private Text midtregauldalDice;
	@FXML  private Text sunndalDice;
	@FXML  private Text rennebuDice;
	@FXML  private Text oppdalDice;
	@FXML  private Text snillfjordDice;
	@FXML  private Text hemneDice;
	@FXML  private Text hitraDice;
	@FXML  private Text aureDice;



	//Login/Register
	@FXML public TextField username;
	@FXML public PasswordField password;
	@FXML public Text loginFeedback;



	@FXML public MediaView introVideo;
	

	@FXML public TextField regUsername;
	@FXML public TextField regEmail;
	@FXML public PasswordField regPassword;
	@FXML public PasswordField regPassword_check;
	@FXML public Text registrationFeedback;

	//Stats
	@FXML private TableView<ArchivedGame> history;
	@FXML private TableView<PlayerStat> topPlayers;

	//Lobby
	@FXML private TableView<User> lobbytable;
	@FXML private Button btnReady;
	@FXML private Button btnChangeParty;
	@FXML private Text readyField;

	//Chat
	@FXML private TextField messagetosend;
	@FXML private TextFlow flowbox;
	@FXML private ScrollPane allmessages;

	//Game
	@FXML private Pane MapPane;
	@FXML private Text selected_Municipality;
	@FXML private Text feedback_DiceResult;
	@FXML private Text feedback_LostWon;
	@FXML private Text playerTurnText;
	@FXML private VBox turnText;
	@FXML private Text winnerName;
	@FXML private Text winnerParty;
	@FXML private Text winnerState;
	@FXML private VBox winnerText;
	@FXML private VBox gameButtons;
	@FXML private VBox gameEndedButtons;
	@FXML private VBox gameFeedback;
	@FXML private Button btnEndTurn;
	private double pressedX, pressedY;

	private static Scene scene1, scene2, scene3, scene4, scene5, scene6;
	private static Thread musicThread;
	private static Backround music;
	private GuiLogic guiLogic = new GuiLogic();
	private GuiChat guiChat = new GuiChat();

	/*private static AudioClip clickSound1 = new AudioClip(Paths.get("src/fx/Image/clickSound1.mp3").toUri().toString());
	private static AudioClip clickSound2 = new AudioClip(Paths.get("src/fx/Image/clickSound2.mp3").toUri().toString());
	private static AudioClip clickSound3 = new AudioClip(Paths.get("src/fx/Image/clickSound3.mp3").toUri().toString());
	private static AudioClip loginsound = new AudioClip(Paths.get("src/fx/Image/loginsound.wav").toUri().toString());
	private static AudioClip blunk = new AudioClip(Paths.get("src/fx/Image/blunk.wav").toUri().toString());*/

	private static Duration currentTime = new Duration(0);
	private MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("/Image/intro.mp4").toExternalForm()));
/**
	 * @param scene11 scene where user logs in (log in screen)
	 * @param scene22 scene where user registers a user (register user screen)
	 * @param scene33 scene that shows main menu (main menu screen)
	 * @param scene44 scene that shows game window (game screen with board and game chat)
	 * @param scene55 scene where user can view stats (stats screen with high score and personal stats)
	 * @param scene66 scene where users are in lobby before game starts (lobby screen)
	 */
	public static void setScenes(Scene scene11, Scene scene22, Scene scene33, Scene scene44, Scene scene55, Scene scene66) {
		scene1 = scene11;
		scene2 = scene22;
		scene3 = scene33;
		scene4 = scene44;
		scene5 = scene55;
		scene6 = scene66;
	}





	/*
	 *
	 * HACK BUTTONS (WILL BE REMOVED OR CHANGED)
	 *
	 */

	/** Takes the user to the main menu.
	 * @param event Action event when a user clicks main menu button.<BR>
	 * @see ActionEvent
	 */
	public void btnGoToMainMenu(ActionEvent event){
		//audioClips("clicksound1", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setScene(scene3);
		videoClip(false);
	}

	public void btnGoToGameWindow(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Main.controller4.MapPane.setOpacity(1.0);
		Main.controller4.turnText.setVisible(true);
		Main.controller4.gameButtons.setVisible(true);
		Main.controller4.gameFeedback.setVisible(true);
		Main.controller4.btnEndTurn.setVisible(true);

		Main.controller4.gameEndedButtons.setVisible(false);
		Main.controller4.winnerText.setVisible(false);
		Main.controller4.updateMap();
		changeMusic(true);
		primaryStage.setScene(scene4);
	}















	/**
	 * Sets background music for scene
	 */
	public static void setBackround(){
		music = new Backround();
		musicThread = new Thread(music);
		musicThread.start();
	}
	/** Creates an AudioClip-object, and plays a clip based on input name of music file
	 * @param name Name of the music file
	 * @param volume Volume of the music file when it is played
	 */
	//Method that let you choose a audio clip to play.
/*	public static void audioClips(String name, int volume) {
        if(name.equals("clickSound1")) {
			clickSound1.play(volume);
		}
		if(name.equals("clickSound2")) {
			clickSound2.play(volume);
		}
		if(name.equals("clickSound3")) {
			clickSound3.play(volume);
		}
        if(name.equals("loginsound")) {
            loginsound.play(volume);
        }
		if(name.equals("blunk")) {
			blunk.play(volume);
		}
	}*/

	public void videoClip(boolean playVideo){
		Main.controller1.introVideo.setMediaPlayer(player);
		player.setAutoPlay(true);
		player.setCycleCount(100);
		if(playVideo) {
			player.setStartTime(currentTime);
			player.play();
		}
		else{
			currentTime = player.getCurrentTime();
			player.stop();
		}
	}
	/** Changes the music to in-game music.
	 * @param inGame True if player is in game, false if not
	 */
	public void changeMusic(boolean inGame){
		music.changeScene(inGame);
	}

	/**
	 * Stops the background music
	 */
	public void stopMusic(){
		Backround.stop();
	}

	/**
	 * Updates the lobby table when a user join the table, and updates the textfield with number of ready users
	 */
	public void updateLobby() {
//		GuiLogic gl = new GuiLogic();
		Platform.runLater(new Runnable() {
			public void run() {
				guiLogic.updateLobby(lobbytable, Client.getLobbyUsers());
			}
		});


		int players = Client.getLobbyUsers().size();
		if (players < 2) {
			readyField.setText(Client.playersReady + "/2 Spillere klare");
		} else {
			readyField.setText(Client.playersReady + "/" + players + " Spillere klare");
		}
	}


	/** Recieve a message from user
	 * @param message ChatMessage-object for users to receive
	 * @see GuiChat#messageReceived(ChatMessage, TextFlow)
	 * @see ChatMessage
	 */
	public void receiveMessage(ChatMessage message){
//		GuiChat chat = new GuiChat();
		guiChat.messageReceived(message, flowbox);
	}
	/** Thread for receiving message
	 * @param message ChatMessage-object from user
	 * @see #receiveMessage(ChatMessage)
	 */
	public static void receivemessagetemp(ChatMessage message){
		Platform.runLater(new Runnable(){
			public void run(){
				Main.controller4.receiveMessage(message);
				Main.controller6.receiveMessage(message);
			}
		});
	}
	/**
	 * Updates map
	 * @see #getMunTiles()
	 * @see #getMunTilesDice()
	 * @see GuiLogic#updateGUIMmap(SVGPath[], ArrayList, Text[])
	 */
	public void refreshMap(){
		SVGPath [] munTiles = new SVGPath[]{lierneID, snaasaID, grongID, overhallaID, namsosID, steinkjerID, inderoyID, namdaleidID, verdalID,
				flatangerID, osenID, roanID, afjordID, verranID, rinndalID, merakerID, stjordalID, levangerID, leksvikID, rissaID, bjugnID,
				tydalID, selbuID, malvikID, trondheimID, klaebuID, melhusID, agdenesID, skaunID, orkdalID, meldalID,surnadalID, rorosID, holtalenID,
				midtregauldalID, sunndalID, rennebuID, oppdalID, snillfjordID, hemneID, hitraID, aureID};

		Text[] munTilesDice = new Text[]{lierneDice, snaasaDice, grongDice, overhallaDice, namsosDice, steinkjerDice, inderoyDice, namdaleidDice,
				verdalDice, flatangerDice, osenDice, roanDice, afjordDice, verranDice, rinndalDice, merakerDice, stjordalDice, levangerDice,
				leksvikDice, rissaDice, bjugnDice, tydalDice, selbuDice, malvikDice, trondheimDice, klaebuDice, melhusDice, agdenesDice, skaunDice,
				orkdalDice, meldalDice,surnadalDice, rorosDice, holtalenDice, midtregauldalDice, sunndalDice, rennebuDice, oppdalDice, snillfjordDice,
				hemneDice, hitraDice, aureDice};

		guiLogic.updateGUIMmap(munTiles, Client.getGame().getMunicipalities(),munTilesDice);
	}

	/**
	 * Disables the button that ends a users turn, as long as it is not that users turn.
	 * When a user has their turn, the button is enabled.
	 * Updates map after a user has made moves (or not) and clicked the end turn button.
	 * Sets the text that notifies the users whose turn it is, to the current players name and party color.
	 * Clears the dice roll result feedback text.
	 */


	public void updateMap(){
//		Platform.runLater(new Runnable(){
//			public void run(){
				if(Client.getGame() == null || Client.getGame().getCurrentPlayer() == null || !(Client.getGame().getCurrentPlayer().equals(Client.getUser())))
				{
					Main.controller4.btnEndTurn.setDisable(true);
				}
				else
				{
					Main.controller4.btnEndTurn.setDisable(false);
				}
				Main.controller4.playerTurnText.setText(Client.getGame().getCurrentPlayer().getUsername());
				Main.controller4.playerTurnText.setFill(Color.web(Client.getGame().getCurrentPlayer().getParty().getColour()));
				Main.controller4.feedback_LostWon.setText("");
				Main.controller4.feedback_DiceResult.setText("");
				Main.controller4.refreshMap();
//			}
//		});
	}

	/**
	 * Button that sets scene to game window scene.
	 * {@link #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)}
	 */
	public void goToGameWindow() {
		Stage primaryStage = (Stage)flowbox.getScene().getWindow();
		Main.controller4.MapPane.setOpacity(1.0);
		Main.controller4.turnText.setVisible(true);
		Main.controller4.gameButtons.setVisible(true);
		Main.controller4.gameFeedback.setVisible(true);
		Main.controller4.btnEndTurn.setVisible(true);

		Main.controller4.gameEndedButtons.setVisible(false);
		Main.controller4.winnerText.setVisible(false);
		Main.controller4.updateMap();
		changeMusic(true);
		Platform.runLater(new Runnable(){
			public void run(){
				primaryStage.setScene(scene4);
			}
		});
	}

	public void updateWinner(User user, String winnerstate) {
		Main.controller4.winnerName.setText(user.getUsername());
		Main.controller4.winnerName.setFill(Color.web(user.getParty().getColour()));
		Main.controller4.winnerParty.setText(user.getParty().getPartyName());
		Main.controller4.winnerParty.setFill(Color.web(user.getParty().getColour()));
		Main.controller4.winnerState.setText(winnerstate);
		Main.controller4.winnerText.setVisible(true);
		Main.controller4.gameEndedButtons.setVisible(true);

		Main.controller4.MapPane.setOpacity(0.6);
		Main.controller4.gameFeedback.setVisible(false);
		Main.controller4.gameButtons.setVisible(false);
		Main.controller4.turnText.setVisible(false);
		Main.controller4.btnEndTurn.setVisible(false);
	}

	/**
	 * Clears chat for a user
	 */
	public void clearChat(){
		flowbox.getChildren().clear();
	}

	/**
	 * @param event sets volume from MouseEvent when cursor is on volume slider
	 * @see MouseEvent
	 */
	public void changeVolume(MouseEvent event) {
		volumeSlider = (Slider) event.getSource();
		Main.controller1.volumeSlider1.setValue(volumeSlider.getValue());
		Main.controller2.volumeSlider2.setValue(volumeSlider.getValue());
		Main.controller3.volumeSlider3.setValue(volumeSlider.getValue());
		Main.controller4.volumeSlider4.setValue(volumeSlider.getValue());
		Main.controller5.volumeSlider5.setValue(volumeSlider.getValue());
		Main.controller6.volumeSlider6.setValue(volumeSlider.getValue());
		float volume = (float) volumeSlider.getValue();
		if (volume < -19.0f) {
			Backround.setVolume(-1000.0f);
		} else {Backround.setVolume(volume);}
		}


	/**
	 * @param node Creates FadeTransition-object and PauseTransition-object so the chosen node can stay and fade on screen.
	 * @param fadeMillis Duration of fade
	 * @param waitMillis Duration of node before fade
	 * @see FadeTransition
	 * @see PauseTransition
	 * @see SequentialTransition
	 */
	//SequentialTransition. This method can be use for fading out nodes. fadeMillis let you choose how long the fading will tak.
//	//Pause Millis let you choose how long the node will stay on screen before its start fade out.
//	public static void fadeOut(Node node, int fadeMillis, int waitMillis){
//		FadeTransition fade = new FadeTransition(Duration.millis(fadeMillis), node);
//		PauseTransition pauseTransition = new PauseTransition(Duration.millis(waitMillis));
//		fade.setFromValue(1);
//		fade.setToValue(0);
//		SequentialTransition seqT = new SequentialTransition(node, pauseTransition, fade);
//		seqT.play();
//	}

	/**
	 * @param node Node that fades in
	 * @param fadeMillis Duration of fade
	 * @param waitMillis Duration of node on screen
	 * @see FadeTransition
	 * @see PauseTransition
	 * @see SequentialTransition
	 */
//	public static void fadeIn(Node node, int fadeMillis, int waitMillis){
//		FadeTransition fade = new FadeTransition(Duration.millis(fadeMillis), node);
//		PauseTransition pauseTransition = new PauseTransition(Duration.millis(waitMillis));
//		fade.setFromValue(0);
//		fade.setToValue(1);
//		SequentialTransition seqT = new SequentialTransition(node, pauseTransition, fade);
//		seqT.play();
//	}

















	/*
	 *
	 *
	 *
	 * Buttons
	 *
	 *
	 *
	 */








	/** Button that logs user in, and takes user to main menu, if correct username and password is entered.
	 * If wrong username and password is entered, prompt text appears on screen to allert user, and user is not logged in.
	 * @param event Action event when user clicks button
	 * @see ActionEvent
	 */
	public void btnLogin(ActionEvent event){
		//audioClips("clickSound1", 1);

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		if(username.getText().trim().contentEquals("") || password.getText().contentEquals(""))
		{
			loginFeedback.setText("Vennligst fyll ut feltene");
			return;
		}
		if(Client.login(username.getText().trim(), password.getText()))
		{
			primaryStage.setScene(scene3);
			videoClip(false);
			username.setText("");
			loginFeedback.setText("");
		}
		else
		{
			guiLogic.setText(loginFeedback, "Feil brukernavn eller passord");

			//lagger som faen
//			loginFeedback.setText("FEIL BRUKERNAVN ELLER PASSORD");
		}
		password.setText("");
	}

	/** Button that sets scene to scene for registering new user.
	 * @param event Action event that sends user to scene
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 * @see ActionEvent
	 */
	public void btnGoToRegistrer(ActionEvent event) {
		//audioClips("clickSound1", 1);
		videoClip(false);
		password.setText("");
		username.setText("");
		loginFeedback.setText("");
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setScene(scene2);
	}

	/** Registers a user if textfields corresponding to username, password, verify password and email are filled in correctly.
	 * @param event Action event when user clicks button to register user
	 * @see Client#register(String, String, String)
	 * @see ActionEvent
	 */
	public void btnRegister(ActionEvent event){
		//audioClips("loginsound", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		if(regEmail.getText().trim().equals("") || regPassword.getText().equals("") || regUsername.getText().trim().equals("") || regPassword_check.getText().equals(""))
		{
			registrationFeedback.setText("Vennligst fyll ut alle feltene");
			return;
		}
		else if(!regEmail.getText().contains("@") || !regEmail.getText().contains(".") || regEmail.getText().length() < 5)
		{
			registrationFeedback.setText("Oppgi en gyldig Epost addresse");
			return;
		}
		else if(regPassword.getText().length() < 3)
		{
			registrationFeedback.setText("Passordet er for kort");
		}
		else if(regPassword.getText().contains("!"))
		{
			registrationFeedback.setText("vennligst ikke bruk tegn i passordet (!.; etc.)");
			regPassword.setText("");
			regPassword_check.setText("");
		}
		else if(regPassword.getText().equals(regPassword_check.getText()))
		{
			if(Client.register(regUsername.getText().trim(), regPassword.getText(), regEmail.getText().trim()))
			{
				registrationFeedback.setText("");
				primaryStage.setScene(scene1);
				regUsername.setText("");
				regEmail.setText("");
				regPassword.setText("");
				regPassword_check.setText("");
			}
			else
			{
				registrationFeedback.setText("Brukernavn eller Epost er allerede registrert");
			}
		}
		else
		{
			registrationFeedback.setText("Passordene var ikke like!");
			regPassword.setText("");
			regPassword_check.setText("");
		}
	}

	/** Button that sets scene to login scene.
	 * @param event Action event when user clicks button to go back to log in screen
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 * @see ActionEvent
	 */
	public void btnGoToLogin(ActionEvent event) {
		regUsername.setText("");
		regEmail.setText("");
		regPassword.setText("");
		regPassword_check.setText("");
		registrationFeedback.setText("");
		//audioClips("clickSound1", 1);
		videoClip(true);
		if(Client.getUser() != null) {
			Client.logoff();
		}
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();//getting the primary stage event from Application
		primaryStage.setScene(scene1);
	}

	/** Button that sets scene to lobby scene
	 * @param event Action event when user clicks button to join lobby
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 * @see ActionEvent
	 */
	public void btnJoinLobby(ActionEvent event){
		//audioClips("clickSound1", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Client.joinLobby();
		Main.controller6.btnReady.setDisable(false);
		Main.controller6.btnChangeParty.setDisable(false);
		primaryStage.setScene(scene6);
	}

	@FXML private Text usernamest;
	@FXML private Text gamecount;
	@FXML private Text wins;
	@FXML private Text winloss;
	@FXML private Text joined;

	/** Sets scene to stats scene.
	 * @param event Action event when user clicks button to go to stats
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 * @see ActionEvent
	 */
	public void btnGoToStats(ActionEvent event) {
		//audioClips("clickSound1", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setScene(scene5);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.controller5.updateStats();
			}
		});
	}

	public void updateStats(){
		Text[] fields = {usernamest, gamecount, wins, winloss, joined};
		Stats stats = Client.getStats();

/*		System.out.println(stats.getGameHistory().get(0));
		System.out.println(stats.getAllPlayerStats().get(0));
		System.out.println(stats.getPersonalStats());*/
		GuiLogic gui = new GuiLogic();

		gui.updatePersonalStats(fields, stats.getPersonalStats());
		gui.updateHistory(history, stats.getGameHistory());
		gui.updateTopPlayers(topPlayers, stats.getAllPlayerStats());
	}


	/** Sets scene to log in scene
	 * @param event Action event when user clicks log of button
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 * @see ActionEvent
	 */
	public void btnLogoff(ActionEvent event){
		//audioClips("clickSound1", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Client.logoff();
		primaryStage.setScene(scene1);
		videoClip(true);
	}


	/** Sets user to ready on button click
	 * @param event Action event when user clicks ready button, with button sound
	 * @see javafx.scene.Node#setDisable(boolean)
	 * @see #audioClips(String, int)
	 * @see Client#ready()
	 */
	public void btnReady(ActionEvent event){
		//audioClips("clickSound1", 1);
		Main.controller6.btnReady.setDisable(true);
		Main.controller6.btnChangeParty.setDisable(true);
		Client.ready();
	}

	/** Changes users Party-object
	 * @param event Action event when user clicks on change party button
	 * @see #audioClips(String, int)
	 * @see Client#changeParty()
	 */
	public void btnChangeParty(ActionEvent event) {
		//audioClips("clickSound1", 1);
		Client.changeParty();
	}

	/** Transforms a message if needed.
	 * 	Takes a ChatMessage-object and sends it to Client-class
	 * @param event Action event when user clicks send button
	 * @see #receiveMessage(ChatMessage)
	 * @see GuiChat#transformMessage(String)
	 * @see Client
	 * @see Client#sendMessage(ChatMessage)
	 */
	public void btnSendMessage(ActionEvent event){
//		GuiChat chat = new GuiChat();
		allmessages.setVvalue(1.0d);
		String themessage = guiChat.transformMessage(messagetosend.getText().trim().toLowerCase());
		if(themessage == null) {
			return;
		}
		ChatMessage msg = guiChat.sendMessage(flowbox, themessage, messagetosend);
		if(flowbox.getScene().equals(scene4)){
			Main.controller6.receiveMessage(msg);
		}else{
			Main.controller4.receiveMessage(msg);
		}
		Client.sendMessage(msg);
	}

	/** Sets scene to main menu scene.
	 *  Clears chat for user who leaves game.
	 * @param event Action event when user clicks leave lobby button
	 * @see #audioClips(String, int)
	 * @see Client#leaveLobby()
	 * @see #clearChat()
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 */
	public void btnLeaveLobby(ActionEvent event) {
		//audioClips("clickSound1", 1);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Client.leaveLobby();
		primaryStage.setScene(scene3);
		Main.controller6.clearChat();
		Main.controller4.clearChat();
	}



	//Methods that handles Animated zoom and scroll in game window.
	AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

	/** Method that zooms in or out on scroll
	 * @param event Scroll event when a user scrolls
	 * @see AnimatedZoomOperator#scrollZoom(Node, double, double, double)
	 * @see ScrollEvent
	 */
	public void ScrollEventHandler(ScrollEvent event) {
		double zoomFactor = 1.5;
		if (event.getDeltaY() <= 0) {
			zoomFactor = 1 / zoomFactor; // zoom out
		}
		zoomOperator.scrollZoom(MapPane, zoomFactor, event.getSceneX(), event.getSceneY());
	}

	/** Zooms the map when user interacts with mouse pad
	 * @param event Zoom event when user pinches mouse pad (or opposite of pinch)
	 * @see AnimatedZoomOperator#zoom(Node, double, double, double)
	 * @see ZoomEvent
	 */
	public void ZoomEventHandler(ZoomEvent event) {
		double zoomFactor = event.getZoomFactor();
		if (event.getZoomFactor() <= 0) {
			zoomFactor = 1 / zoomFactor; // zoom out
		}
		zoomOperator.zoom(MapPane, zoomFactor, event.getSceneX(), event.getSceneY());
	}

	/** Gets coordinates of cursors when user clicks on map
	 * @param event Mouse event when user presses primary mouse button on map
	 * @see MouseEvent
	 */
	public void MousePressedEventHandler(MouseEvent event) {
		pressedX = event.getX();
		pressedY = event.getY();
	}

	/** Moves the map when primary mouse button is pressed, and mouse is dragged
	 * @param event Mouse event when user drags the mouse after pressing map
	 * @see Node
	 * @see MouseEvent
	 */
	public void MouseDraggedEventHandler(MouseEvent event) {
		MapPane.setTranslateX(MapPane.getTranslateX() + event.getX() - pressedX);
		MapPane.setTranslateY(MapPane.getTranslateY() + event.getY() - pressedY);
		event.consume();
	}

	/**
	 * Resets map coordinates to origin and zero zoom
	 * @see Node
	 */
	public void btnResetMap(){
		//audioClips("clickSound1", 1);
		MapPane.setScaleX(1);
		MapPane.setScaleY(1);
		MapPane.setTranslateX(0);
		MapPane.setTranslateY(0);
	}
	/** Sets text on node click, if text is available for node
	 * @param event Mouse event when user clicks a node
	 * @see GuiLogic#gameFeedbackTextLogic(Node, Text, Text)
	 * @see Node
	 */



	/*
	 *
	 * Lag happens after you win a municipality, if you loose,
	 * or select a municipality that cant attack anyone its stops
	 *
	 */




	public void setMunicipalityText(MouseEvent event){
//		GuiLogic gui = new GuiLogic();
		/*Node node = (Node) event.getSource(); // get node id
		guiLogic.gameFeedbackTextLogic(node,selected_Municipality,feedback_DiceResult);*/
	}

	/**
	 * Removes the prompt text that appears when a user attempts to login with wrong password or username.
	 * @param event Mouse event when selecting municipality
	 */

	public void removeMunicipalityText(MouseEvent event){
		//selected_Municipality.setText("");
	}

	/** Marks municipalitys on click.
	 * Checks attacker municipality against defender municipality and whick player won the battle.
	 * Sets texts for what municipality is marked, what municipality is attacking and what municipality is defending,
	 * on attacking users screen
	 * @param event Mouse event when user clicks a municipality
	 * @see #getMunTiles()
	 * @see #getMunTilesDice()
	 * @see #getMunicipalityborders()
	 * @see #getMunicipalitybordersDice()
	 * @see GuiLogic#checkdefence(Node, SVGPath[][], SVGPath[])
	 * @see GuiLogic#neighbours(Node, SVGPath[][], SVGPath[], Text[][], Text[], ArrayList, User)
	 * @see Client#getCurMun()
	 * @see Client#setCurMun(Municipality)
	 * @see Client#attackLost(Municipality)
	 * @see Client#attackWon(Municipality[])
	 * @see Client#getRollDiceResults()
	 * @see #refreshMap()
	 * @see Client#setMarked(boolean)
	 * @see Game#attack(Municipality, Municipality)
	 * @see Municipality#getName()
	 */
	public void btnMarkMunicipality(MouseEvent event) {
		if(Client.getGame() == null || Client.getGame().getCurrentPlayer() == null || !(Client.getGame().getCurrentPlayer().equals(Client.getUser()))) return;
		Task task = new Task<Void>() {
			@Override
			public Void call() {
				SVGPath[] munTiles = Main.controller4.getMunTiles();
				Text[] munTilesDice = Main.controller4.getMunTilesDice();
				Text[][] municipalitybordersDice = Main.controller4.getMunicipalitybordersDice();
				SVGPath[][] municipalityborders = Main.controller4.getMunicipalityborders();

				Node source = (Node) event.getSource(); // get node id
				Municipality defendingmun = null;
				if (Client.getCurMun() != null) {
					defendingmun = guiLogic.checkdefence(source, municipalityborders, munTiles);
				}
				if (defendingmun != null) {
					String gameFBT = "";
					Municipality[] result = Client.getGame().attack(Client.getCurMun(), defendingmun);
					if(result == null) { //something has gone horribly wrong
						gameFBT = "OOPSIE";
					}
					if(result[1] == null) //lost
					{
						gameFBT = "Du tapte i " + result[0].getName().replace("ID", "");
						Client.attackLost(result[0]); //sends the losing municipality to the other players
					}
					else //won
					{

						gameFBT = "Du vant, " + defendingmun.getName().substring(0, 1).toUpperCase() + defendingmun.getName().substring(1).replace("ID", "") + " støtter nå " + defendingmun.getOwner().getParty().getPartyName();
						Client.attackWon(result);	//sends both involved municipalities to the other players
					}
					String[] diceResult = Client.getRollDiceResults().split("-");
					feedback_LostWon.setText(gameFBT);
					feedback_DiceResult.setText(diceResult[0] + "    -    " + diceResult[1]);
					Client.setCurMun(null);
					Client.setMarked(false);
					refreshMap();
				} else {
					for(Municipality mun:Client.getGame().getMunicipalities()){
						if(mun.getName().equals(source.getId())){
							if(!(mun.getOwner().equals(Client.getUser()))){
								feedback_DiceResult.setText("du eier ikke denne kommunen");
								return null;
							}
							if(Client.getCurMun() == null && mun.getTroops() <= 1 && mun.getOwner().equals(Client.getUser())) {
								feedback_DiceResult.setText("ikke nok representanter");
								return null;
							}
							Client.setCurMun(mun);
							selected_Municipality.setText(mun.getName().replace("ID","").substring(0, 1).toUpperCase() + mun.getName().replace("ID","").substring(1));
						}
					}
					guiLogic.neighbours(source, municipalityborders, munTiles, municipalitybordersDice, munTilesDice, Client.getGame().getMunicipalities(), Client.getUser());
				}
				return null;
		}};
		new Thread(task).start();
//		GuiLogic guiLogic = new GuiLogic();


		}



	/** Button that ends users turn.
	 * 	Updates map according to changes made
	 * @param event Action event when user clicks end turn button
	 * @see Client#endTurn()
	 * @see #getMunTiles()
	 * @see #getMunTilesDice()
	 * @see GuiLogic#updateGUIMmap(SVGPath[], ArrayList, Text[])
	 * @see #updateMap()
	 */
	public void btnEndTurn(ActionEvent event){
        //audioClips("loginsound", 1);
		Client.endTurn();
		refreshMap();
		updateMap();
	}

	/** Button for user to leave game.
	 * 	Sets scene to main menu.
	 * 	Changes music.
	 * 	Clears user chat for user who left game.
	 * @param event Action event when user clicks leave game button
	 * @see Client#leaveGame()
	 * @see #changeMusic(boolean)
	 * @see #clearChat()
	 * @see #setScenes(Scene, Scene, Scene, Scene, Scene, Scene)
	 */
	public void btnLeaveGame(ActionEvent event) {
		//audioClips("clickSound1", 1);
		Client.leaveGame();
		changeMusic(false);
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setScene(scene3);
		Main.controller6.clearChat();
		Main.controller4.clearChat();
	}



	

	/** Gets a list of municipalities
	 * @return SVGPath-list of municipalities
	 */
	private SVGPath[] getMunTiles(){
		return (new SVGPath[]{lierneID, snaasaID, grongID, overhallaID, namsosID, steinkjerID, inderoyID, namdaleidID, verdalID,
				flatangerID, osenID, roanID, afjordID, verranID, rinndalID, merakerID, stjordalID, levangerID, leksvikID, rissaID, bjugnID,
				tydalID, selbuID, malvikID, trondheimID, klaebuID, melhusID, agdenesID, skaunID, orkdalID, meldalID, surnadalID, rorosID,
				holtalenID, midtregauldalID, sunndalID, rennebuID, oppdalID, snillfjordID, hemneID, hitraID, aureID});

	}

	/** Gets an array of municipality dice numbers
	 * @return SVGPath-array of Municipality dice numbers
	 */
	private Text[] getMunTilesDice(){
		return (new Text[]{lierneDice, snaasaDice, grongDice, overhallaDice, namsosDice, steinkjerDice, inderoyDice, namdaleidDice,
				verdalDice, flatangerDice, osenDice, roanDice, afjordDice, verranDice, rinndalDice, merakerDice, stjordalDice, levangerDice,
				leksvikDice, rissaDice, bjugnDice, tydalDice, selbuDice, malvikDice, trondheimDice, klaebuDice, melhusDice, agdenesDice, skaunDice,
				orkdalDice, meldalDice, surnadalDice, rorosDice, holtalenDice, midtregauldalDice, sunndalDice, rennebuDice, oppdalDice, snillfjordDice,
				hemneDice, hitraDice, aureDice});
	}

	/** Gets a two-dimensional array where each element is a list of
	 * municipality dice numbers of a municipality that border to a specific municipality. An element contains neighbouring
	 * municipalities dice numbers of a municipality
	 * that is the corresponding element in the list returned in {@link #getMunTilesDice()} ()}
	 * @return
	 * @see #getMunTilesDice() ()
	 */
	private Text[][] getMunicipalitybordersDice(){
		return (new Text[][]{
				/*lierneDice*/			{snaasaDice, grongDice},
				/*snaasaDice*/			{lierneDice, grongDice, steinkjerDice, verdalDice, overhallaDice},
				/*grongDice*/			{lierneDice, snaasaDice, overhallaDice},
				/*overhallaDice*/		{grongDice, namsosDice, steinkjerDice},
				/*namsosDice*/			{overhallaDice, steinkjerDice, namdaleidDice},
				/*steinkjerDice*/		{verdalDice, snaasaDice, overhallaDice, namsosDice, namdaleidDice, verranDice, inderoyDice},
				/*inderoyDice*/			{leksvikDice, verranDice, levangerDice, steinkjerDice},
				/*namdaleidDice*/		{namsosDice, flatangerDice, osenDice, roanDice, steinkjerDice, verranDice},
				/*verdalDice*/			{steinkjerDice, snaasaDice, levangerDice, merakerDice},
				/*flatangerDice*/		{namdaleidDice, osenDice},
				/*osenDice*/			{flatangerDice, roanDice, namdaleidDice},
				/*roanDice*/			{osenDice, afjordDice, namdaleidDice},
				/*afjordDice*/			{verranDice, roanDice, bjugnDice, rissaDice, namdaleidDice},
				/*verranDice*/			{afjordDice, rissaDice, namdaleidDice, inderoyDice, steinkjerDice},
				/*rinndalDice*/			{surnadalDice, meldalDice, oppdalDice, rennebuDice, hemneDice},
				/*merakerDice*/			{verdalDice, tydalDice, selbuDice, stjordalDice, levangerDice},
				/*stjordalDice*/		{merakerDice, levangerDice, verdalDice, selbuDice},
				/*levangerDice*/		{verdalDice, merakerDice, stjordalDice},
				/*leksvikDice*/			{inderoyDice, rissaDice, verranDice},
				/*rissaDice*/			{leksvikDice, bjugnDice, afjordDice, agdenesDice, verranDice},
				/*bjugnDice*/			{rissaDice, afjordDice, agdenesDice},
				/*tydalDice*/			{rorosDice, holtalenDice, selbuDice, merakerDice},
				/*selbuDice*/			{stjordalDice, malvikDice, trondheimDice, klaebuDice, midtregauldalDice, holtalenDice, tydalDice, merakerDice},
				/*malvikDice*/			{stjordalDice, klaebuDice, selbuDice, trondheimDice},
				/*trondheimDice*/		{malvikDice, selbuDice, klaebuDice, skaunDice},
				/*klaebuDice*/			{trondheimDice, malvikDice, selbuDice, melhusDice},
				/*melhusDice*/			{trondheimDice, klaebuDice, midtregauldalDice, skaunDice, meldalDice},
				/*agdenesDice*/			{snillfjordDice, rissaDice, orkdalDice},
				/*skaunDice*/			{melhusDice, orkdalDice, trondheimDice},
				/*orkdalDice*/			{agdenesDice, snillfjordDice, skaunDice, hemneDice, meldalDice},
				/*MeldalDice*/			{melhusDice, orkdalDice, rinndalDice, rennebuDice, midtregauldalDice},
				/*surnadalDice*/		{sunndalDice, rinndalDice, aureDice, hemneDice, oppdalDice},
				/*rorosDice*/			{tydalDice, holtalenDice},
				/*holtalenDice*/		{rorosDice, tydalDice, selbuDice, midtregauldalDice},
				/*midtregauldalDice*/	{holtalenDice, selbuDice, melhusDice, rennebuDice, meldalDice},
				/*sunndalDice*/			{oppdalDice, surnadalDice, aureDice},
				/*rennebuDice*/			{oppdalDice, midtregauldalDice, meldalDice, rinndalDice,},
				/*oppdalDice*/			{sunndalDice, surnadalDice, rinndalDice, rennebuDice},
				/*snillfjordDice*/		{agdenesDice, hemneDice, orkdalDice, hitraDice},
				/*hemneDice*/			{hitraDice, aureDice, snillfjordDice, rinndalDice, surnadalDice, orkdalDice},
				/*hitraDice*/			{hemneDice, snillfjordDice, aureDice},
				/*aureDice*/			{hitraDice, hemneDice, surnadalDice, sunndalDice}
		});
	}

	/** Gets a two-dimensional array where each element is a list of
	 * municipalities that border to a specific municipality. An element contains neighbours to a municipality
	 * that is the corresponding element in the list returned in {@link #getMunTiles()}
	 * @return
	 * @see #getMunTiles()
	 */
	private SVGPath[][] getMunicipalityborders(){
		return (new SVGPath[][]{
				/*lierne*/			{snaasaID, grongID},
				/*snaasa*/			{lierneID, grongID, steinkjerID, verdalID, overhallaID},
				/*grong*/			{lierneID, snaasaID, overhallaID},
				/*overhalla*/		{grongID, namsosID, steinkjerID},
				/*namsos*/			{overhallaID, steinkjerID, namdaleidID},
				/*steinkjer*/		{verdalID, snaasaID, overhallaID, namsosID, namdaleidID, verranID, inderoyID},
				/*inderoy*/			{leksvikID, verranID, levangerID, steinkjerID},
				/*namdaleid*/		{namsosID, flatangerID, osenID, roanID, steinkjerID, verranID},
				/*verdal*/			{steinkjerID, snaasaID, levangerID, merakerID},
				/*flatanger*/		{namdaleidID, osenID},
				/*osen*/			{flatangerID, roanID, namdaleidID},
				/*roan*/			{osenID, afjordID, namdaleidID},
				/*afjord*/			{verranID, roanID, bjugnID, rissaID, namdaleidID},
				/*verran*/			{afjordID, rissaID, namdaleidID, inderoyID, steinkjerID},
				/*rinndal*/			{surnadalID, meldalID, oppdalID, rennebuID, hemneID},
				/*merakerID*/		{verdalID, tydalID, selbuID, stjordalID, levangerID},
				/*stjordalID*/		{merakerID, levangerID, verdalID, selbuID},
				/*levangerID*/		{verdalID, merakerID, stjordalID},
				/*leksvikID*/		{inderoyID, rissaID, verranID},
				/*rissaID*/			{leksvikID, bjugnID, afjordID, agdenesID, verranID},
				/*bjugnID*/			{rissaID, afjordID, agdenesID},
				/*tydalID*/			{rorosID, holtalenID, selbuID, merakerID},
				/*selbuID*/			{stjordalID, malvikID, trondheimID, klaebuID, midtregauldalID, holtalenID, tydalID, merakerID},
				/*malvikID*/		{stjordalID, klaebuID, selbuID, trondheimID},
				/*trondheimID*/		{malvikID, selbuID, klaebuID, skaunID},
				/*klaebuID*/		{trondheimID, malvikID, selbuID, melhusID},
				/*melhusID*/		{trondheimID, klaebuID, midtregauldalID, skaunID, meldalID},
				/*agdenesID*/		{snillfjordID, rissaID, orkdalID},
				/*skaunID*/			{melhusID, orkdalID, trondheimID},
				/*orkdalID*/		{agdenesID, snillfjordID, skaunID, hemneID, meldalID},
				/*MeldalID*/		{melhusID, orkdalID, rinndalID, rennebuID, midtregauldalID},
				/*surnadalID*/		{sunndalID, rinndalID, aureID, hemneID, oppdalID},
				/*rorosID*/			{tydalID, holtalenID},
				/*holtalenID*/		{rorosID, tydalID, selbuID, midtregauldalID},
				/*midtregauldalID*/	{holtalenID, selbuID, melhusID, rennebuID, meldalID},
				/*sunndalID*/		{oppdalID, surnadalID, aureID},
				/*rennebuID*/		{oppdalID, midtregauldalID, meldalID, rinndalID,},
				/*oppdalID*/		{sunndalID, surnadalID, rinndalID, rennebuID},
				/*snillfjordID*/	{agdenesID, hemneID, orkdalID, hitraID},
				/*hemneID*/			{hitraID, aureID, snillfjordID, rinndalID, surnadalID, orkdalID},
				/*hitraID*/			{hemneID, snillfjordID, aureID},
				/*aureID*/			{hitraID, hemneID, surnadalID, sunndalID}
		});
	}
}
