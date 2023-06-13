package client.controller;

import client.App;

public class UserController
{
    public static String username = "sutne";
    public static String password = "1234";

    public static void register(String username, String password /*, String email */)
    {
        UserController.username = username;
        UserController.password = password;
        App.setScene(App.login);
        App.login.controller.playBackgroundVideo();
    }

    public static boolean login(String username, String password)
    {
        if (username == null || password == null) return false;
        if (!username.equals(UserController.username)) return false;
        if (!password.equals(UserController.password)) return false;
        App.setScene(App.menu);
        App.login.controller.stopBackgroundVideo();
        return true;
    }

    public static void signOut()
    {
        UserController.username = null;
        UserController.password = null;
        App.setScene(App.register);
    }
}
