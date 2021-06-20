package ProjectGUI;

import ProjectGUI.Models.*;
import ProjectGUI.Models.JavaFX.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public TableView<ProcessorFX> tableViewProcessor;
    @FXML
    public TableColumn<ProcessorFX, String> columnProcessor;
    @FXML
    public TableView<ScreenResolutionFX> tableViewScreenResolution;
    @FXML
    public TableColumn<ScreenResolutionFX, String> columnScreenResolution;
    @FXML
    public TableColumn<ProductInformationFX, String> columnProduct1;
    @FXML
    public TableColumn<ProductInformationFX, String> columnProduct2;
    @FXML
    public TableColumn<ProductInformationFX, String> columnFeatures;
    @FXML
    public TableView<ProductInformationFX> tableViewFeatures;
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
    private TableView<ReviewFX> tableViewReviews;

    @FXML
    public TableColumn<ReviewFX, String> columnProduct1Review;
    @FXML
    public TableColumn<ReviewFX, String> columnProduct2Review;
    @FXML
    public TableColumn<ReviewFX, String> columnProduct3Review;


    @FXML
    private TableColumn<ProductFX, Integer> columnPrice;
    //endregion

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static List<Product> fetchedProducts = null;

    @FXML
    public void initialize() {
        initTable();
        initTextFields();
        initToggleGroupListener();
        //initGetProductsListener();
        fillFilters();

    }

    private void fillFilters()
    {
        tableViewBrand.setItems(getAllBrandsFX());
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewScreenSize.setItems(getAllScreenSizesForComputersFX());
                tableViewScreenResolution.setItems(getAllScreenResolutionsForComputersFX());
                tableViewProcessor.setItems(getAllProcessorsForComputersFX());
                break;
            case Constants.PHONE:
                tableViewScreenSize.setItems(getAllScreenSizesForPhonesFX());
                break;
        }
        tableViewFeatures.setItems(FXCollections.observableArrayList());
        tableViewProducts.setItems(FXCollections.observableArrayList());
    }

    private void initTable() {
        initCols();
    }

    private void initCols() {
        columnName.setCellValueFactory(new PropertyValueFactory("model"));
        columnPrice.setCellValueFactory(new PropertyValueFactory("price"));
        columnBrands.setCellValueFactory(new PropertyValueFactory("brandName"));
        columnScreenSize.setCellValueFactory(new PropertyValueFactory("screenSize"));
        columnScreenResolution.setCellValueFactory(new PropertyValueFactory("screenResolution"));
        columnProcessor.setCellValueFactory(new PropertyValueFactory("processor"));
        columnFeatures.setCellValueFactory(new PropertyValueFactory("featureName"));
        columnProduct1.setCellValueFactory(new PropertyValueFactory("feature1"));
        columnProduct2.setCellValueFactory(new PropertyValueFactory("feature2"));
        columnProduct1Review.setCellValueFactory(new PropertyValueFactory("review1"));
        columnProduct2Review.setCellValueFactory(new PropertyValueFactory("review2"));
        columnProduct3Review.setCellValueFactory(new PropertyValueFactory("review3"));

        setCellFactories();

    }

    private void setCellFactories() {   // For reviews table

        columnProduct1Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else{
                        openDialog(1);
                    }
                }
            });
            return cell ;
        });

        columnProduct2Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else{
                        openDialog(2);
                    }
                }
            });
            return cell ;
        });

        columnProduct3Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else{
                        openDialog(3);
                    }
                }
            });
            return cell ;
        });

    }

    private void openDialog(int column) {

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Comment");

        if (tableViewReviews.getSelectionModel().getSelectedItem() == null)
            return;

       ReviewFX selectedReview = tableViewReviews.getSelectionModel().getSelectedItem();

       if(column == 1)
           a.setContentText(selectedReview.getDescription1());

       else if(column == 2)
           a.setContentText(selectedReview.getDescription2());

       else
           a.setContentText(selectedReview.getDescription3());

        a.show();

    }

    public void selectedProductChanged(InputEvent event)
    {
        if (tableViewProducts.getSelectionModel().getSelectedItem() == null)
            return;

        Product selectedProduct = fetchedProducts.stream()
                .filter(x -> x.getProductId().intValue() == tableViewProducts.getSelectionModel().getSelectedItem().getProductId())
                .collect(Collectors.toList()).get(0);

        ObservableList<ProductInformationFX> informationFXList = FXCollections.observableArrayList();
        ObservableList<ReviewFX> ReviewFXList = FXCollections.observableArrayList();

        ProductInformationFX newInformationFX;
        ReviewFX newReviewFX;


        newInformationFX = new ProductInformationFX();
        newInformationFX.setFeatureName("Model");
        newInformationFX.setFeature1(selectedProduct.getModel());
        newInformationFX.setFeature2("");
        informationFXList.add(newInformationFX);

        newInformationFX = new ProductInformationFX();
        newInformationFX.setFeatureName("Price");
        newInformationFX.setFeature1(selectedProduct.getPrice().toString());
        newInformationFX.setFeature2("");
        informationFXList.add(newInformationFX);

        newInformationFX = new ProductInformationFX();
        newInformationFX.setFeatureName("Battery Life");
        newInformationFX.setFeature1(selectedProduct.getBatteryLife().toString());
        newInformationFX.setFeature2("");
        informationFXList.add(newInformationFX);

        newInformationFX = new ProductInformationFX();
        newInformationFX.setFeatureName("Screen Size");
        newInformationFX.setFeature1(selectedProduct.getScreenSize());
        newInformationFX.setFeature2("");
        informationFXList.add(newInformationFX);

        switch (selectedFilter) {
            case Constants.COMPUTER:
                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Screen Resolution");
                newInformationFX.setFeature1(((Computer)selectedProduct).getScreenResolution());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Processor");
                newInformationFX.setFeature1(((Computer)selectedProduct).getProcessor());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Memory");
                newInformationFX.setFeature1(((Computer)selectedProduct).getMemory().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Storage Capacity");
                newInformationFX.setFeature1(((Computer)selectedProduct).getStorageCapacity().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
                break;
            case Constants.PHONE:
                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Internal Memory");
                newInformationFX.setFeature1(((Phone)selectedProduct).getInternalMemory().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
                break;
        }

        if (selectedProduct.getExtraFeaturesList() != null || !selectedProduct.getExtraFeaturesList().isEmpty())
        {
            ArrayList<ExtraFeature> extraFeatures = selectedProduct.getExtraFeaturesList();

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("");
            newInformationFX.setFeature1("");
            newInformationFX.setFeature2("");
            informationFXList.add(newInformationFX);

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Extra Features:");
            newInformationFX.setFeature1("");
            newInformationFX.setFeature2("");
            informationFXList.add(newInformationFX);

            for (ExtraFeature extraFeature: extraFeatures)
            {
                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("");
                newInformationFX.setFeature1(extraFeature.getFeatureName());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
            }


        }

        tableViewFeatures.setItems(informationFXList);

        if (selectedProduct.getReviewList() != null || !selectedProduct.getReviewList().isEmpty())
        {
            ArrayList<Review> reviews = selectedProduct.getReviewList();

            for (Review review: reviews)
            {
                newReviewFX = new ReviewFX();
                String rating = "";

                for(int i=0;i<review.getRating();i++){
                    rating += "★";
                }

                for(int i=0;i<5-review.getRating();i++){
                    rating += "☆";
                }

                newReviewFX.setReview1(rating);
                newReviewFX.setDescription1(review.getComment());
                ReviewFXList.add(newReviewFX);
            }
        }

        tableViewReviews.setItems(ReviewFXList);

    }

    public void getProductsPressed(ActionEvent event) {
        Range priceRange = new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax));
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewProducts.setItems(FXCollections.observableArrayList(getComputersFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        new Range(getValue(textFieldBatteryLifeMin), getValue(textFieldBatteryLifeMax)),
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax)),
                        tableViewScreenResolution.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenResolution.getSelectionModel().getSelectedItem().getScreenResolution(),
                        tableViewProcessor.getSelectionModel().getSelectedItem() == null ? null : tableViewProcessor.getSelectionModel().getSelectedItem().getProcessor(),
                        new Range(getValue(textFieldMemoryMin), getValue(textFieldMemoryMax)),
                        new Range(getValue(textFieldStorageCapacityMin),getValue(textFieldStorageCapacityMax)))));
                break;
            case Constants.PHONE:
                tableViewProducts.setItems(FXCollections.observableArrayList(getPhonesFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        new Range(getValue(textFieldBatteryLifeMin), getValue(textFieldBatteryLifeMax)),
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax)),
                        new Range(getValue(textFieldInternalMemoryMin), getValue(textFieldInternalMemoryMax)))));
                break;
        }

    }

    public void sortByPriceAction(ActionEvent event) {

        if(columnPrice.getSortType()==TableColumn.SortType.DESCENDING)
            columnPrice.setSortType(TableColumn.SortType.ASCENDING);

        else
            columnPrice.setSortType(TableColumn.SortType.DESCENDING);

        tableViewProducts.getSortOrder().add(columnPrice);
        tableViewProducts.sort();

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

            fillFilters();
        });
    }

    private void initTextFields() {
        Extensions.numberTextFieldify(textFieldMemoryMin);
        Extensions.numberTextFieldify(textFieldMemoryMax);
        Extensions.numberTextFieldify(textFieldInternalMemoryMin);
        Extensions.numberTextFieldify(textFieldInternalMemoryMax);
    }

    private Integer getValue(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (Exception e) {
            return null; //If the text is empty, the casting would not succeed.
        }
    }

  /*  @FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {

            if(tableViewReviews.getSelectionModel().getSelectedItem().getReview1() == ""){
                System.out.println("Empty");
            }
            else
                System.out.println("not empty");

        }
    }*/


}
