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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Accepts input and converts it to commands for the model or view. Also calls the necessary Repository methods
 * for talking with back-end. It is the "C" in our "MVC" architecture.
 */
public class Controller {
    public static List<Product> fetchedProducts = null;
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
    @FXML
    public TableColumn<ProductInformationFX, String> columnProduct3;
    @FXML
    public TableColumn<ReviewFX, String> columnProduct1Review;
    @FXML
    public TableColumn<ReviewFX, String> columnProduct2Review;
    @FXML
    public TableColumn<ReviewFX, String> columnProduct3Review;
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
    //endregion
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
    //region ProductFX
    @FXML
    private TableView<ProductFX> tableViewProducts;
    @FXML
    private TableColumn<ProductFX, String> columnName;
    @FXML
    private TableView<ReviewFX> tableViewReviews;
    //endregion
    @FXML
    private TableColumn<ProductFX, Integer> columnPrice;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * The main entry point for creating the controller logic of the XML elements.
     */
    @FXML
    public void initialize() {
        initComparisonCols();
        initReviewCols();
        initTextFields();
        initToggleGroupListener();
        //initGetProductsListener();
        fillFilters();
    }

    /**
     * Once the app is started, the filters should show the possible operations that can be performed, depending
     * on what data is returned from the back-end. The tableViews are filled as such.
     */
    private void fillFilters() {
        tableViewBrand.setItems(Repository.getInstance().getAllBrandsFX());
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewScreenSize.setItems(Repository.getInstance().getAllScreenSizesForComputersFX());
                tableViewScreenResolution.setItems(Repository.getInstance().getAllScreenResolutionsForComputersFX());
                tableViewProcessor.setItems(Repository.getInstance().getAllProcessorsForComputersFX());
                break;
            case Constants.PHONE:
                tableViewScreenSize.setItems(Repository.getInstance().getAllScreenSizesForPhonesFX());
                break;
        }
        tableViewFeatures.setItems(FXCollections.observableArrayList());
        tableViewProducts.setItems(FXCollections.observableArrayList());
        tableViewReviews.setItems(FXCollections.observableArrayList());
    }

    /**
     * The columns that contain the necessary data that is used for comparison is initialized.
     * The cell value factory needs to be set to specify how to populate cells within a single TableColumn.
     */
    private void initComparisonCols() {
        columnName.setCellValueFactory(new PropertyValueFactory("model"));
        columnPrice.setCellValueFactory(new PropertyValueFactory("price"));
        columnBrands.setCellValueFactory(new PropertyValueFactory("brandName"));
        columnScreenSize.setCellValueFactory(new PropertyValueFactory("screenSize"));
        columnScreenResolution.setCellValueFactory(new PropertyValueFactory("screenResolution"));
        columnProcessor.setCellValueFactory(new PropertyValueFactory("processor"));
        columnFeatures.setCellValueFactory(new PropertyValueFactory("featureName"));
        columnProduct1.setCellValueFactory(new PropertyValueFactory("feature1"));
        columnProduct2.setCellValueFactory(new PropertyValueFactory("feature2"));
        columnProduct3.setCellValueFactory(new PropertyValueFactory("feature3"));

        tableViewProducts.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        columnProduct1Review.setCellValueFactory(new PropertyValueFactory("review1"));
        columnProduct2Review.setCellValueFactory(new PropertyValueFactory("review2"));
        columnProduct3Review.setCellValueFactory(new PropertyValueFactory("review3"));
    }

    /**
     * The same operation as {@link #initComparisonCols()}. Crucial difference is with the mouse click, as a double
     * click on any given cell results in a dialog pop-up, which displays the comment for that review.
     */
    private void initReviewCols() {
        columnProduct1Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else {
                        openDialog(1);
                    }
                }
            });
            return cell;
        });

        columnProduct2Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else {
                        openDialog(2);
                    }
                }
            });
            return cell;
        });

        columnProduct3Review.setCellFactory(tc -> {
            TableCell<ReviewFX, String> cell = new TableCell<ReviewFX, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseClicked(e -> {

                if (e.getClickCount() == 2) {

                    if (cell.getText() == "" || cell.getText() == null) {

                    } else {
                        openDialog(3);
                    }
                }
            });
            return cell;
        });

    }

    /**
     * Open an AlertDialog that displays the comment for a given cell.
     * @param column is the index at which the click occurred. This should return any of the three description fields.
     */
    private void openDialog(int column) {

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Comment");

        if (tableViewReviews.getSelectionModel().getSelectedItem() == null)
            return;

        ReviewFX selectedReview = tableViewReviews.getSelectionModel().getSelectedItem();

        if (column == 1)
            a.setContentText(selectedReview.getDescription1());

        else if (column == 2)
            a.setContentText(selectedReview.getDescription2());

        else
            a.setContentText(selectedReview.getDescription3());

        a.show();
    }

    /**
     * Referenced by the table XML element via data-binding. Whenever an arrow key is pressed or the user selects
     * another row, its data should be retrieved for display in the Comparison View. The columns change depending
     * on the current filter being by phone or by computer. The product can also contain extraFeatures
     * and they should also be retrieved accordingly. The reviews are also retrieved and the ASCII "★" char
     * is appended for the existence of each point, and the "☆" for its non-existence, from a total of 5.
     * @param event is the default argument to be provided by the framework when a method is called from XML via
     * data-binding, but we needn't use it here.
     */
    public void selectedProductChanged(InputEvent event) {
        if (tableViewProducts.getSelectionModel().getSelectedItems().isEmpty())
            return;

        ObservableList<ProductFX> selectedItems = tableViewProducts.getSelectionModel().getSelectedItems();

        if (selectedItems.size() > 1) {
            ObservableList<ProductInformationFX> informationFXList = FXCollections.observableArrayList();
            ProductInformationFX newInformationFX;

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Press");
            newInformationFX.setFeature1("Compare");
            newInformationFX.setFeature2("");
            informationFXList.add(newInformationFX);
            tableViewFeatures.setItems(informationFXList);

            ObservableList<ReviewFX> reviewFxList = FXCollections.observableArrayList();
            ReviewFX newReviewFX;

            newReviewFX = new ReviewFX();
            newReviewFX.setReview1("Press");
            newReviewFX.setReview2("Compare");
            newReviewFX.setReview3("");
            reviewFxList.add(newReviewFX);
            tableViewReviews.setItems(reviewFxList);

            return;
        }

        Product selectedProduct = fetchedProducts.stream()
                .filter(x -> x.getProductId().intValue() == tableViewProducts.getSelectionModel().getSelectedItem().getProductId())
                .collect(Collectors.toList()).get(0);

        ObservableList<ProductInformationFX> informationFXList = FXCollections.observableArrayList();
        ObservableList<ReviewFX> ReviewFXList = FXCollections.observableArrayList();

        ProductInformationFX newInformationFX;
        ReviewFX newReviewFX;

        if (selectedProduct.getReviewList() != null || !selectedProduct.getReviewList().isEmpty()) {
            ArrayList<Review> reviews = selectedProduct.getReviewList();

            for (Review review : reviews) {
                newReviewFX = new ReviewFX();
                String rating = "";

                for (int i = 0; i < review.getRating(); i++) {
                    rating += "★";
                }

                for (int i = 0; i < 5 - review.getRating(); i++) {
                    rating += "☆";
                }

                newReviewFX.setReview1(rating);
                newReviewFX.setDescription1(review.getComment());
                ReviewFXList.add(newReviewFX);
            }
        }

        tableViewReviews.setItems(ReviewFXList);

        newInformationFX = new ProductInformationFX();
        newInformationFX.setFeatureName("Model");
        newInformationFX.setFeature1(selectedProduct.getModel().split(" ")[1]);
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
                newInformationFX.setFeature1(((Computer) selectedProduct).getScreenResolution());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Processor");
                newInformationFX.setFeature1(((Computer) selectedProduct).getProcessor());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Memory");
                newInformationFX.setFeature1(((Computer) selectedProduct).getMemory().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);

                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Storage Capacity");
                newInformationFX.setFeature1(((Computer) selectedProduct).getStorageCapacity().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
                break;
            case Constants.PHONE:
                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("Internal Memory");
                newInformationFX.setFeature1(((Phone) selectedProduct).getInternalMemory().toString());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
                break;
        }

        if (selectedProduct.getExtraFeaturesList() != null || !selectedProduct.getExtraFeaturesList().isEmpty()) {
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

            for (ExtraFeature extraFeature : extraFeatures) {
                newInformationFX = new ProductInformationFX();
                newInformationFX.setFeatureName("");
                newInformationFX.setFeature1(extraFeature.getFeatureName());
                newInformationFX.setFeature2("");
                informationFXList.add(newInformationFX);
            }
        }
        tableViewFeatures.setItems(informationFXList);
    }

    /**
     *The user can select up to three products to add to the comparison by pressing the "Compare" button, which
     * triggers this method. The method gets the fields necessary, and gets extraFeatures if they exist for each of
     * the items that are added to the comparison.
     * @param event is the default argument to be provided by the framework when a method is called from XML via
     *data-binding, but we needn't use it here.
     */
    public void comparePressed(ActionEvent event) {
        ObservableList<ProductFX> selectedItems = tableViewProducts.getSelectionModel().getSelectedItems();

        if (selectedItems.size() <= 1)
            return;

        ArrayList<Product> selectedProducts = new ArrayList<Product>();

        for (ProductFX productFX : selectedItems) {
            selectedProducts.add(fetchedProducts.stream()
                    .filter(x -> x.getProductId().intValue() == productFX.getProductId())
                    .collect(Collectors.toList()).get(0));
        }

        // Create new thread to fill information table
        new Thread(() -> {
            ObservableList<ProductInformationFX> informationFXList = FXCollections.observableArrayList();

            ProductInformationFX newInformationFX;

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Model");
            newInformationFX.setFeature1(selectedProducts.get(0).getModel().split(" ")[1]);
            newInformationFX.setFeature2(selectedProducts.get(1).getModel().split(" ")[1]);
            if (selectedProducts.size() > 2)
                newInformationFX.setFeature3(selectedProducts.get(2).getModel().split(" ")[1]);
            informationFXList.add(newInformationFX);

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Price");
            newInformationFX.setFeature1(selectedProducts.get(0).getPrice().toString());
            newInformationFX.setFeature2(selectedProducts.get(1).getPrice().toString());
            if (selectedProducts.size() > 2)
                newInformationFX.setFeature3(selectedProducts.get(2).getPrice().toString());

            informationFXList.add(newInformationFX);

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Battery Life");
            newInformationFX.setFeature1(selectedProducts.get(0).getBatteryLife().toString());
            newInformationFX.setFeature2(selectedProducts.get(1).getBatteryLife().toString());
            if (selectedProducts.size() > 2)
                newInformationFX.setFeature3(selectedProducts.get(2).getBatteryLife().toString());
            informationFXList.add(newInformationFX);

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Screen Size");
            newInformationFX.setFeature1(selectedProducts.get(0).getScreenSize());
            newInformationFX.setFeature2(selectedProducts.get(1).getScreenSize());
            if (selectedProducts.size() > 2)
                newInformationFX.setFeature3(selectedProducts.get(2).getScreenSize());
            informationFXList.add(newInformationFX);

            switch (selectedFilter) {
                case Constants.COMPUTER:
                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("Screen Resolution");
                    newInformationFX.setFeature1(((Computer) selectedProducts.get(0)).getScreenResolution());
                    newInformationFX.setFeature2(((Computer) selectedProducts.get(1)).getScreenResolution());
                    if (selectedProducts.size() > 2)
                        newInformationFX.setFeature3(((Computer) selectedProducts.get(2)).getScreenResolution());
                    informationFXList.add(newInformationFX);

                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("Processor");
                    newInformationFX.setFeature1(((Computer) selectedProducts.get(0)).getProcessor());
                    newInformationFX.setFeature2(((Computer) selectedProducts.get(1)).getProcessor());
                    if (selectedProducts.size() > 2)
                        newInformationFX.setFeature3(((Computer) selectedProducts.get(2)).getProcessor());
                    informationFXList.add(newInformationFX);

                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("Memory");
                    newInformationFX.setFeature1(((Computer) selectedProducts.get(0)).getMemory().toString());
                    newInformationFX.setFeature2(((Computer) selectedProducts.get(1)).getMemory().toString());
                    if (selectedProducts.size() > 2)
                        newInformationFX.setFeature3(((Computer) selectedProducts.get(2)).getMemory().toString());

                    informationFXList.add(newInformationFX);

                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("Storage Capacity");
                    newInformationFX.setFeature1(((Computer) selectedProducts.get(0)).getStorageCapacity().toString());
                    newInformationFX.setFeature2(((Computer) selectedProducts.get(1)).getStorageCapacity().toString());
                    if (selectedProducts.size() > 2)
                        newInformationFX.setFeature3(((Computer) selectedProducts.get(2)).getStorageCapacity().toString());

                    informationFXList.add(newInformationFX);
                    break;
                case Constants.PHONE:
                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("Internal Memory");
                    newInformationFX.setFeature1(((Phone) selectedProducts.get(0)).getInternalMemory().toString());
                    newInformationFX.setFeature2(((Phone) selectedProducts.get(1)).getInternalMemory().toString());
                    if (selectedProducts.size() > 2)
                        newInformationFX.setFeature3(((Phone) selectedProducts.get(2)).getInternalMemory().toString());
                    informationFXList.add(newInformationFX);
                    break;
            }

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("");
            informationFXList.add(newInformationFX);

            newInformationFX = new ProductInformationFX();
            newInformationFX.setFeatureName("Extra Features:");
            informationFXList.add(newInformationFX);

            int extraFeaturesListIndex = informationFXList.size() - 1;

            if (selectedProducts.get(0).getExtraFeaturesList() != null || !selectedProducts.get(0).getExtraFeaturesList().isEmpty()) {
                ArrayList<ExtraFeature> extraFeatures = selectedProducts.get(0).getExtraFeaturesList();

                for (ExtraFeature extraFeature : extraFeatures) {
                    newInformationFX = new ProductInformationFX();
                    newInformationFX.setFeatureName("");
                    newInformationFX.setFeature1(extraFeature.getFeatureName());
                    informationFXList.add(newInformationFX);
                }
            }

            if (selectedProducts.get(1).getExtraFeaturesList() != null || !selectedProducts.get(1).getExtraFeaturesList().isEmpty()) {
                ArrayList<ExtraFeature> extraFeatures = selectedProducts.get(1).getExtraFeaturesList();

                int i = extraFeaturesListIndex;

                for (ExtraFeature extraFeature : extraFeatures) {
                    i++;
                    if (informationFXList.size() >= i + 1) {
                        newInformationFX = informationFXList.get(i);
                        newInformationFX.setFeature2(extraFeature.getFeatureName());
                    } else {
                        newInformationFX = new ProductInformationFX();
                        newInformationFX.setFeatureName("");
                        newInformationFX.setFeature2(extraFeature.getFeatureName());
                        informationFXList.add(newInformationFX);
                    }
                }
            }

            if (selectedProducts.size() > 2) {
                if (selectedProducts.get(2).getExtraFeaturesList() != null || !selectedProducts.get(2).getExtraFeaturesList().isEmpty()) {
                    ArrayList<ExtraFeature> extraFeatures = selectedProducts.get(2).getExtraFeaturesList();

                    int i = extraFeaturesListIndex;

                    for (ExtraFeature extraFeature : extraFeatures) {
                        i++;
                        if (informationFXList.size() >= i + 1) {
                            newInformationFX = informationFXList.get(i);
                            newInformationFX.setFeature3(extraFeature.getFeatureName());
                        } else {
                            newInformationFX = new ProductInformationFX();
                            newInformationFX.setFeatureName("");
                            newInformationFX.setFeature3(extraFeature.getFeatureName());
                            informationFXList.add(newInformationFX);
                        }
                    }
                }
            }
            tableViewFeatures.setItems(informationFXList);
        }).start();

        // Create new thread to fill review table
        new Thread(() -> {
            ObservableList<ReviewFX> ReviewFXList = FXCollections.observableArrayList();
            ReviewFX newReviewFX;

            if (selectedProducts.get(0).getReviewList() != null && !selectedProducts.get(0).getReviewList().isEmpty()) {
                ArrayList<Review> reviews = selectedProducts.get(0).getReviewList();

                for (Review review : reviews) {
                    newReviewFX = new ReviewFX();
                    String rating = "";

                    for (int i = 0; i < review.getRating(); i++) {
                        rating += "★";
                    }

                    for (int i = 0; i < 5 - review.getRating(); i++) {
                        rating += "☆";
                    }

                    newReviewFX.setReview1(rating);
                    newReviewFX.setDescription1(review.getComment());
                    ReviewFXList.add(newReviewFX);
                }
            }

            if (selectedProducts.size() > 1) {
                int i = 0;

                if (selectedProducts.get(1).getReviewList() != null && !selectedProducts.get(1).getReviewList().isEmpty()) {
                    ArrayList<Review> reviews = selectedProducts.get(1).getReviewList();

                    for (Review review : reviews) {

                        if (ReviewFXList.size() > i)
                        {
                            newReviewFX = ReviewFXList.get(i);
                            String rating = "";

                            for (int j = 0; j < review.getRating(); j++) {
                                rating += "★";
                            }

                            for (int j = 0; j < 5 - review.getRating(); j++) {
                                rating += "☆";
                            }

                            newReviewFX.setReview2(rating);


                            newReviewFX.setDescription2(review.getComment());
                        } else {
                            newReviewFX = new ReviewFX();
                            String rating = "";

                            for (int j = 0; j < review.getRating(); j++) {
                                rating += "★";
                            }

                            for (int j = 0; j < 5 - review.getRating(); j++) {
                                rating += "☆";
                            }

                            newReviewFX.setReview2(rating);
                            newReviewFX.setDescription2(review.getComment());
                            ReviewFXList.add(newReviewFX);
                        }
                        i++;
                    }
                }


            }

            if (selectedProducts.size() > 2) {
                int i = 0;

                if (selectedProducts.get(2).getReviewList() != null && !selectedProducts.get(2).getReviewList().isEmpty()) {
                    ArrayList<Review> reviews = selectedProducts.get(2).getReviewList();

                    for (Review review : reviews) {

                        if (ReviewFXList.size() > i)
                        {
                            newReviewFX = ReviewFXList.get(i);
                            String rating = "";

                            for (int j = 0; j < review.getRating(); j++) {
                                rating += "★";
                            }

                            for (int j = 0; j < 5 - review.getRating(); j++) {
                                rating += "☆";
                            }

                            newReviewFX.setReview3(rating);


                            newReviewFX.setDescription3(review.getComment());
                        } else {
                            newReviewFX = new ReviewFX();
                            String rating = "";

                            for (int j = 0; j < review.getRating(); j++) {
                                rating += "★";
                            }

                            for (int j = 0; j < 5 - review.getRating(); j++) {
                                rating += "☆";
                            }

                            newReviewFX.setReview3(rating);
                            newReviewFX.setDescription3(review.getComment());
                            ReviewFXList.add(newReviewFX);
                        }
                        i++;
                    }
                }
            }
            tableViewReviews.setItems(ReviewFXList);
        }).start();
    }

    /**
     * This method is called from the getProducts button in Gui.fxml file. It will retrieve the columns necessary
     * for computers and phones from back-end depending on which filter is selected.
     * @param event is the default argument to be provided by the framework when a method is called from XML via
     * data-binding, but we needn't use it here.
     */
    public void getProductsPressed(ActionEvent event) {
        switch (selectedFilter) {
            case Constants.COMPUTER:
                tableViewProducts.setItems(FXCollections.observableArrayList(Repository.getInstance().getComputersFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        new Range(getValue(textFieldBatteryLifeMin), getValue(textFieldBatteryLifeMax)),
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax)),
                        tableViewScreenResolution.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenResolution.getSelectionModel().getSelectedItem().getScreenResolution(),
                        tableViewProcessor.getSelectionModel().getSelectedItem() == null ? null : tableViewProcessor.getSelectionModel().getSelectedItem().getProcessor(),
                        new Range(getValue(textFieldMemoryMin), getValue(textFieldMemoryMax)),
                        new Range(getValue(textFieldStorageCapacityMin), getValue(textFieldStorageCapacityMax)))));
                break;
            case Constants.PHONE:
                tableViewProducts.setItems(FXCollections.observableArrayList(Repository.getInstance().getPhonesFX(
                        tableViewBrand.getSelectionModel().getSelectedItem() == null ? null : tableViewBrand.getSelectionModel().getSelectedItem().getBrandName(),
                        new Range(getValue(textFieldBatteryLifeMin), getValue(textFieldBatteryLifeMax)),
                        tableViewScreenSize.getSelectionModel().getSelectedItem() == null ? null : tableViewScreenSize.getSelectionModel().getSelectedItem().getScreenSize(),
                        new Range(getValue(textFieldPriceMin), getValue(textFieldPriceMax)),
                        new Range(getValue(textFieldInternalMemoryMin), getValue(textFieldInternalMemoryMax)))));
                break;
        }

    }

    /**
     *
     * @param event is the default argument to be provided by the framework when a method is called from XML via
     * data-binding, but we needn't use it here.
     */
    public void sortByPriceAction(ActionEvent event) {

        if (columnPrice.getSortType() == TableColumn.SortType.DESCENDING)
            columnPrice.setSortType(TableColumn.SortType.ASCENDING);

        else
            columnPrice.setSortType(TableColumn.SortType.DESCENDING);

        tableViewProducts.getSortOrder().add(columnPrice);
        tableViewProducts.sort();

    }

    /**
     * Toggle group in the Filter view controls whether the computer or phone gets selected. In either case,
     * the filter options must be made visible and invisible, depending on the selection. For example,
     * the internal memory filter should not be shown if the user selected computer.
     */
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

    /**
     * All text fields in filters must accept only numeric input.
     */
    private void initTextFields() {
        Extensions.numberTextFieldify(textFieldMemoryMin);
        Extensions.numberTextFieldify(textFieldMemoryMax);
        Extensions.numberTextFieldify(textFieldInternalMemoryMin);
        Extensions.numberTextFieldify(textFieldInternalMemoryMax);
    }

    /**
     * Since we can be sure that all text fields only accept numeric input, the value can safely be parsed into an
     * integer. However, if the field is left empty, then an exception is thrown as the conversion is not successful.
     * So we can assume that no filtering exists for that field when making a back-end request.
     * @param textField whose value is being captured
     * @return an Integer or null if the text field's value cannot be parsed into an integer.
     */
    private Integer getValue(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
