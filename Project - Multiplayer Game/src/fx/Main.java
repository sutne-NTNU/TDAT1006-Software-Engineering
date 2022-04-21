package fx;

import Clientclasses.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fx.controllers.Controller;

import Logic.Objects.*;
import Logic.Logic;

import javax.swing.*;

public class Main extends Application {
    public static Scene scene1, scene2, scene3, scene4, scene5, scene6;
    public static Controller controller1, controller2, controller3, controller4, controller5, controller6;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
            Main.primaryStage = primaryStage;

            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/login_fxml.fxml"));
            Parent layout1 = loader1.load();
            controller1 = loader1.getController();
            FXMLLoader.load(getClass().getResource("/view/login_fxml.fxml"));
            scene1 = new Scene(layout1, 1366, 768);
            scene1.getStylesheets().add("/styles/theme.css");

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/registrer_fxml.fxml"));
            Parent layout2 = loader2.load();
            controller2 = loader2.getController();
            FXMLLoader.load(getClass().getResource("/view/registrer_fxml.fxml"));
            scene2 = new Scene(layout2, 1366, 768);
            scene2.getStylesheets().add("/styles/theme.css");

            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/view/main_menu_fxml.fxml"));
            Parent layout3 = loader3.load();
            FXMLLoader.load(getClass().getResource("/view/main_menu_fxml.fxml"));
            controller3 = loader3.getController();
            scene3 = new Scene(layout3, 1366, 768);
            scene3.getStylesheets().add("/styles/theme.css");

            FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/view/gamewindow_fxml.fxml"));
            Parent layout4 = loader4.load();
            FXMLLoader.load(getClass().getResource("/view/gamewindow_fxml.fxml"));
            controller4 = loader4.getController();
            scene4 = new Scene(layout4, 1366, 768);
            scene4.getStylesheets().add("/styles/theme.css");

        FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/view/stats_fxml.fxml"));
            Parent layout5 = loader5.load();
            FXMLLoader.load(getClass().getResource("/view/stats_fxml.fxml"));
            controller5 = loader5.getController();
            scene5 = new Scene(layout5, 1366, 768);
            scene5.getStylesheets().add("/styles/theme.css");

            FXMLLoader loader6 = new FXMLLoader(getClass().getResource("/view/lobby_fxml.fxml"));
            Parent layout6 = loader6.load();
            FXMLLoader.load(getClass().getResource("/view/lobby_fxml.fxml"));
            controller6 = loader6.getController();
            scene6 = new Scene(layout6, 1366, 768);
            scene6.getStylesheets().add("/styles/theme.css");


        Controller.setScenes(scene1, scene2, scene3, scene4, scene5, scene6);
        Controller.setBackround();


        Client.setGame(null);

        testing(false);

        //logs a user out when quitting the application
        primaryStage.setOnCloseRequest(event -> {controller1.changeMusic(true); controller1.stopMusic(); Client.quit(); System.out.println("Krysset ut vindu - logger av...");});
        primaryStage.setOnShowing(event -> {
            controller1.videoClip(true);});
        //Setup of the main window
        primaryStage.setTitle("Kommunekrigen");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    
    
    private static void testing(boolean testing) throws Exception{
    	if(testing) {
    		User myPlayer = new User(3,"Ola", new Party("#5155db", "Høyre", 3));
            Client.setUser(myPlayer);
            User[] users = {Client.getUser(), new User(4,"Kari",new Party("#e24444", "Arbeiderpartiet", 2))};
            Game game = new Game(users, Logic.createMap(users));
            game.setCurrentPlayer(myPlayer);
            Client.setGame(game);
    	}
    }
}

