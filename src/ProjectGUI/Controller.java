package ProjectGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {
    private String selectedFilter;

    //region Filters
    @FXML
    private RadioButton radioButtonComputer;
    @FXML
    private RadioButton radioButtonPhone;
    @FXML
    private ToggleGroup toggleGroupProductType;
    @FXML
    private Pane paneFilterPhone;
    @FXML
    private Pane paneFilterComputer;
    @FXML
    private TextField textFieldInternalMemoryMin;
    @FXML
    private TextField textFieldInternalMemoryMax;
    @FXML
    private TextField textFieldBatteryLifeMin;
    @FXML
    private TextField textFieldBatteryLifeMax;
    //endregion

    //region Buttons
    @FXML
    private Button buttonGetProducts;
    @FXML
    private Button buttonSortByPrice;
    @FXML
    private Button buttonCompare;
    //endregion

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        initTextFields();
        initToggleGroupListener();
        initGetProductsListener();
    }

    private void initToggleGroupListener() {
        paneFilterPhone.setVisible(false); //Initially not visible as computer is pre-selected
        selectedFilter = Constants.COMPUTER;

        toggleGroupProductType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            switch (((RadioButton) newToggle).getText()) {
                case Constants.COMPUTER:
                    paneFilterPhone.setVisible(false);
                    paneFilterComputer.setVisible(true);
                    selectedFilter = Constants.COMPUTER;
                    break;
                case Constants.PHONE:
                    paneFilterComputer.setVisible(false);
                    paneFilterPhone.setVisible(true);
                    selectedFilter = Constants.PHONE;
                    break;
            }
        });
    }

    private void initTextFields() {
        Extensions.numberTextFieldify(textFieldBatteryLifeMin);
        Extensions.numberTextFieldify(textFieldBatteryLifeMax);
        Extensions.numberTextFieldify(textFieldInternalMemoryMin);
        Extensions.numberTextFieldify(textFieldInternalMemoryMax);
    }

    private void initGetProductsListener() {
        buttonGetProducts.setOnAction(e -> {
            switch (selectedFilter) {
                case Constants.COMPUTER:
                    Integer[] batteryLifeRange = {
                            Integer.parseInt(textFieldBatteryLifeMin.getText()),
                            Integer.parseInt(textFieldBatteryLifeMax.getText()),
                    };
                    Repository.getInstance().getComputers(batteryLifeRange);
                    break;
                case Constants.PHONE:
                    Integer[] internalMemoryRange = {
                            Integer.parseInt(textFieldInternalMemoryMin.getText()),
                            Integer.parseInt(textFieldInternalMemoryMax.getText()),
                    };
                    Repository.getInstance().getPhones("myfilters");
                    break;
            }
        });
    }

}
