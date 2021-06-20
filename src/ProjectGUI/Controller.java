package ProjectGUI;

import ProjectGUI.Models.Constants;
import ProjectGUI.Models.JavaFX.BrandFX;
import ProjectGUI.Models.JavaFX.ProductFX;
import ProjectGUI.Models.JavaFX.ScreenSizeFX;
import ProjectGUI.Models.Range;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static ProjectGUI.Repository.*;

public class Controller {
    @FXML
    public TableColumn columnModels;
    @FXML
    public TableColumn<BrandFX, String> columnBrands;
    @FXML
    public TableView<BrandFX> tableViewBrand;
    @FXML
    public TextField textFieldBatteryLifeMin;
    @FXML
    public TextField textFieldBatteryLifeMax;
    @FXML
    public TableView<ScreenSizeFX> tableViewScreenSize;
    @FXML
    public TableColumn<ScreenSizeFX, String> columnScreenSize;
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
        tableViewBrand.setItems(getAllBrandsFX());
        tableViewScreenSize.setItems(getAllScreenSizesForComputersFX());
    }

    private void initTable() {
        initCols();
    }

    private void initCols() {
        columnName.setCellValueFactory(new PropertyValueFactory("model"));
        columnPrice.setCellValueFactory(new PropertyValueFactory("price"));
        columnBrands.setCellValueFactory(new PropertyValueFactory("brandName"));
        columnScreenSize.setCellValueFactory(new PropertyValueFactory("screenSize"));
    }

    public void getProductsPressed(ActionEvent event) {
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewProducts.setItems(FXCollections.observableArrayList(getComputersFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        null,
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        null,
                        null,
                        null,
                        null,
                        null)));
                break;
            case Constants.PHONE:
                tableViewProducts.setItems(FXCollections.observableArrayList(getPhonesFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        null,
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        null,
                        null)));
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
                    tableViewScreenSize.setItems(getAllScreenSizesForComputersFX());
                    tableViewBrand.setItems(getAllBrandsFX());
                    break;
                case Constants.PHONE:
                    paneFilterComputer.setVisible(false);
                    paneFilterPhone.setVisible(true);
                    selectedFilter = Constants.PHONE;
                    tableViewScreenSize.setItems(getAllScreenSizesForPhonesFX());
                    tableViewBrand.setItems(getAllBrandsFX());
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
