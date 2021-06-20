package ProjectGUI;

import ProjectGUI.Models.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initPrimaryStage(primaryStage);
    }

    private void initPrimaryStage(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML));
        primaryStage.setTitle("Epey.com Clone");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setResizable(false);
        controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}
