package ProjectGUI;

import ProjectGUI.Models.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author      Hüseyin Can Ağdelen
 * @author      Kerem Cömert
 * @author      Yiğit Berkay Aslı
 * The main entry point of JavaFX.
 */
public class Main extends Application {
    private Controller controller;

    /**
     * The function acts as the main entry point.
     * @param args are not used.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main application view is loaded with the XML file using view binding. Then the view is shown on the screen,
     * with its controller receiving an instance of the stage.
     * @param primaryStage is the main and only view of GUI.
     * @throws Exception is thrown but not caught.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML));
        primaryStage.setTitle("Epey.com Clone");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setResizable(false);
        controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }


}
