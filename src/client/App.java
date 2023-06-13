package client;

import client.utils.SceneData;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{
    private static final int WINDOW_WIDTH = 1366;
    private static final int WINDOW_HEIGHT = 768;
    private static Stage stage;
    private static SceneData<?> currentScene;

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    public static SceneData<client.view.scenes.register.Controller> register;
    public static SceneData<client.view.scenes.login.Controller> login;
    public static SceneData<client.view.scenes.menu.Controller> menu;
    public static SceneData<client.view.scenes.lobby.Controller> lobby;
    public static SceneData<client.view.scenes.game.Controller> game;

    @Override
    public void start(Stage stage) throws IOException
    {
        App.stage = stage;
        App.stage.setTitle("Kommune-Krigen");
        App.stage.setResizable(false);

        App.register = this.initScene("register");
        App.login = this.initScene("login");
        App.menu = this.initScene("menu");
        App.lobby = this.initScene("lobby");
        App.game = this.initScene("game");

        App.setScene(App.menu);
        App.stage.show();
    }

    /**
     * Sets the scene of the stage. If the controller implements Initializable, the initialize
     * method will be called.<br/><br/>
     *
     * Example usage:
     * <pre>
     * {@code App.setScene(App.login);}
     * </pre>
     *
     * @param sceneData The scene to set.
     */
    public static void setScene(SceneData<?> sceneData)
    {
        if (sceneData.controller instanceof Initializable)
        {
            ((Initializable)sceneData.controller).initialize(null, null);
        }
        currentScene = sceneData;
        stage.setScene(sceneData.scene);
    }

    /**
     * @return the current scene on the stage with its controller.
     */
    public static SceneData<?> getCurrentScene()
    {
        return currentScene;
    }

    /**
     * Returns the proper URL of a resource for loading relative to the App class.
     * @param path The path of the resource. Example: {@code "resources/audio/click.mp3"}
     * @see #getURL(String)
     */
    public static String getPath(String path)
    {
        return App.getURL(path).toExternalForm();
    }

    /**
     * Returns the proper URL of a resource for loading relative to the App class.
     * @param path The path of the resource. Example: {@code "resources/audio/click.mp3"}
     */
    public static URL getURL(String path)
    {
        URL url = App.class.getResource(path);
        if (url == null)
        {
            URL root = App.class.getResource("");
            throw new RuntimeException("Resource not found: " + root + path);
        }
        return url;
    }

    /**
     * Initializes a scene from a FXML layout file.
     *
     * @param name The name of the scene, which is the name of the folder in the view/scenes folder.
     * @throws IOException If the file is not found.
     */
    private <T> SceneData<T> initScene(String name) throws IOException
    {
        String fxmlPath = "view/scenes/" + name + "/layout.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getURL(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        T controller = fxmlLoader.getController();
        return new SceneData<T>(scene, controller);
    }
}
