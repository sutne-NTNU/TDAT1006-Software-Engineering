package client.utils;

import javafx.scene.Scene;

/**
 * A container for a scene and its controller. Making it easy to access both from anywhere.
 *
 * Example usage:
 * <pre>
 * {@code App.login.controller.initialize();}
 *  </pre>
 * @param <T> The type of the controller.
 */
public class SceneData<T>
{
    public Scene scene;
    public T controller;

    public SceneData(Scene scene, T controller)
    {
        this.scene = scene;
        this.controller = controller;
    }
}
