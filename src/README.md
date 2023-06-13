# Code Structure

The main entrypoint of the application is `client.App`.

The project was originally seperated into a `client` and a `server`. However, since the server is removed for the single-player version, the client directory is the only directory. The same goes for `client/utils`, which was originally intended to contain utility classes for both the client and server, but has since been repurposed to contain miscelainanous classes and config that don't really fit anywhere else.

Further, after the rewrite there is a much clearer distinction between the `MVC` (Model-View-Controller) parts of the application.


### Model

These classes define the underlying structure and behavior of the game such as rolling the dice, attack logic various game components etc.

### View

The view is seperated into the different scenes. Each scene has its own `Controller.java` (View Controller/ GUI Controller) and `layout.fxml` file. The controller is responsible for updating the view and handling user input. The fxml file is responsible for the layout of the scene. In addition there is a common `theme.css` for all scenes in addition to a individual `style.css` file for each scene to handle styling (colors, fonts, etc.).

### Controller

These controllers are not to be confused with the GUI Controller for each JavaFX Scene. Those are controllers that specifically only change the view of the GUI. 

These static controllers handle application state, logic and updates. These controllers then give relevant information to the GUI controllers which will update the view accordingly. If necessary (such as on clicks) the GUI Controllers can update the state of the static controllers themselves. As such these controller are the flue between the different parts of the application.
