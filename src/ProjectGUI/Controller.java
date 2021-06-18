package ProjectGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class Controller {
    //Filters Region
    @FXML private RadioButton radioButtonComputer;
    @FXML private RadioButton radioButtonPhone;
    @FXML private ToggleGroup toggleGroupProductType;

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize(){
        toggleGroupProductType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            switch(((RadioButton) newToggle).getText()){
                case Constants.COMPUTER:
                    System.out.println("Computer: " + ((RadioButton)newToggle).getText());
                    break;
                case  Constants.PHONE:
                    System.out.println("Phone: " + ((RadioButton)newToggle).getText());
                    break;
            }
                });
    }

}
