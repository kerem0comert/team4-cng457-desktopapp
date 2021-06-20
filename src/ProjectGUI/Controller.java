package ProjectGUI;

import ProjectGUI.Models.Constants;
import ProjectGUI.Models.JavaFX.PhoneFX;
import ProjectGUI.Models.JavaFX.ProductFX;
import ProjectGUI.Models.Range;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static ProjectGUI.Repository.getComputersFX;
import static ProjectGUI.Repository.getPhonesFX;

public class Controller {
    @FXML
    public TableColumn columnModels;
    @FXML
    public TableColumn columnBrands;
    @FXML
    public TableView tableViewBrand;
    @FXML
    public TextField textFieldBatteryLifeMin;
    @FXML
    public TextField textFieldBatteryLifeMax;
    @FXML
    public TableView tableViewScreenSize;
    @FXML
    public TableColumn columnScreenSize;
    @FXML
    public TextField textFieldStorageCapacityMin;
    @FXML
    public TextField textFieldStorageCapacityMax;
    @FXML
    public TableView tableViewProcessor;
    @FXML
    public TableColumn columnProcessor;
    @FXML
    public TableView tableViewScreenResolution;
    @FXML
    public TableColumn columnScreenResolution;
    private String selectedFilter;
    //region Filters
    @FXML
    private RadioButton radioButtonComputer;
    @FXML
    private RadioButton radioButtonPhone;
    @FXML
    private ToggleGroup toggleGroupProductType;
    @FXML
    private TextField textFieldPriceMin;
    @FXML
    private TextField textFieldPriceMax;
    @FXML
    private Pane paneFilterPhone;
    @FXML
    private Pane paneFilterComputer;
    @FXML
    private TextField textFieldInternalMemoryMin;
    @FXML
    private TextField textFieldInternalMemoryMax;
    @FXML
    private TextField textFieldMemoryMin;
    @FXML
    private TextField textFieldMemoryMax;
    @FXML
    private TableView tableViewModel;
    //endregion
    //region Buttons
    @FXML
    private Button buttonGetProducts;
    @FXML
    private Button buttonSortByPrice;
    @FXML
    private Button buttonCompare;
    //endregion

    //region ProductFX
    @FXML
    private TableView<ProductFX> tableViewProducts;

    @FXML
    private TableColumn<ProductFX, String> columnName;

    @FXML
    private TableColumn<ProductFX, Integer> columnPrice;
    //endregion

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        initTable();
        initTextFields();
        initToggleGroupListener();
        //initGetProductsListener();
    }

    private void initTable() {
        initCols();
    }

    private void initCols() {
        columnName.setCellValueFactory(new PropertyValueFactory("model"));
        columnPrice.setCellValueFactory(new PropertyValueFactory("price"));
    }

    public void getProductsPressed(ActionEvent event) {
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewProducts.setItems(FXCollections.observableArrayList(getComputersFX(null, null, null, null, null, null, null, null)));
                break;
            case Constants.PHONE:
                tableViewProducts.setItems(FXCollections.observableArrayList(getPhonesFX(null, null, null, null, null)));
                break;
        }

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
        Extensions.numberTextFieldify(textFieldMemoryMin);
        Extensions.numberTextFieldify(textFieldMemoryMax);
        Extensions.numberTextFieldify(textFieldInternalMemoryMin);
        Extensions.numberTextFieldify(textFieldInternalMemoryMax);
    }

    private void initGetProductsListener() {
        buttonGetProducts.setOnAction(e -> {
            Range priceRange = new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax));
            switch (selectedFilter) {
                case Constants.COMPUTER:
                    Range memoryRange = new Range(getValue(textFieldMemoryMin), getValue(textFieldMemoryMax));
                    //Repository.getInstance().getComputers();
                    break;
                case Constants.PHONE:
                    Range internalMemoryRange = new Range(getValue(textFieldInternalMemoryMin), getValue(textFieldInternalMemoryMax));
                    // Repository.getInstance().getPhones("myfilters");
                    break;
            }
        });
    }

    private Integer getValue(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (Exception e) {
            return null; //If the text is empty, the casting would not succeed.
        }
    }
}
