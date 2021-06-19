package ProjectGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    //region Buttons
    @FXML
    private Button buttonGetProducts;
    @FXML
    private Button buttonSortByPrice;
    @FXML
    private Button buttonCompare;
    //endregion

    //region Product
    @FXML
    private TableView<BasicProductInfo> tableViewProducts;

    @FXML
    private TableColumn<BasicProductInfo,String> columnName;

    @FXML
    private TableColumn<BasicProductInfo,String> columnPrice;
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
        initGetProductsListener();
    }

    private void initTable() {
        initCols();
    }

    private void initCols() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        editableCols();
    }

    private void editableCols() {
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());

        columnName.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue() );
        });

        columnPrice.setCellFactory(TextFieldTableCell.forTableColumn());

        columnPrice.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrice(e.getNewValue() );
        });

        // tableViewProducts.setEditable(true);      // No need to have it editable

    }

    public void getProductsPressed(ActionEvent event) throws IOException, ParseException {

        String mainURL = "";
        int product;    // 0 --> phone and 1 --> computer

        product = 1;    // Set the product

        if(product == 0){
            mainURL += "http://localhost:8080/getallphones";
        }

        else if(product == 1){
            mainURL += "http://localhost:8080/getallcomputers";
        }

        ObservableList<BasicProductInfo> data = FXCollections.observableArrayList();
        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL(mainURL).openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();

        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONArray array = (JSONArray) obj;

        String phone1 = array.get(0).toString();        // Just trying with first phone for now
        JSONObject jsonObject = (JSONObject) parser.parse(phone1);

        JSONArray reviews = (JSONArray) jsonObject.get("reviewList");

        System.out.print(jsonObject.get("type") +" " + jsonObject.get("price") + " " + jsonObject.get("productId") + " " + jsonObject.get("model") + " " + jsonObject.get("batteryLife") + " " + jsonObject.get("screenSize") + " ");

        if(product == 0){   // If phone
            System.out.println(jsonObject.get("internalMemory"));
        }

        else if(product == 1){  // If computer
            System.out.println(jsonObject.get("memory") + " " + jsonObject.get("processor") + " " + jsonObject.get("screenResolution") + " " + jsonObject.get("storageCapacity"));
        }

        //Brand is not coming seperately now, intead just brandId is returned....

        String responseBrand1 = "";

        if(jsonObject.get("brand").toString().length()<15){     // If only brandIs is present, we need another request

            HttpURLConnection connectionBrand1 = (HttpURLConnection)new URL("http://localhost:8080/getbrand/"+jsonObject.get("brand").toString()).openConnection();
            connectionBrand1.setRequestMethod("GET");
            int responsecodeBrand1 = connectionBrand1.getResponseCode();

            if(responsecodeBrand1 == 200){
                Scanner scanner = new Scanner(connectionBrand1.getInputStream());
                while(scanner.hasNextLine()){
                    responseBrand1 += scanner.nextLine();
                    responseBrand1 += "\n";
                }
                scanner.close();
            }
        }

        else{         // If brand is present as an object
            responseBrand1 += jsonObject.get("brand").toString();
        }

        JSONObject jsonObjectBrand1 = (JSONObject) parser.parse(responseBrand1); // Parse the brand seperately
        System.out.println(jsonObjectBrand1.get("brandName") + " " + jsonObjectBrand1.get("brandId")); //Print the brand seperately

        System.out.println("Product: " + jsonObject.get("type") + " " + jsonObjectBrand1.get("brandName") + " " + jsonObject.get("model") + " " + jsonObject.get("productId")); //Add to table

        data.add(new BasicProductInfo(jsonObjectBrand1.get("brandName") + " " + jsonObject.get("model"),jsonObject.get("price").toString()));




        String reviewList ="";
        for(int i =0;i<reviews.size();i++){

            String responseReview1 = "";

            if(reviews.get(i).toString().length()<15){

                HttpURLConnection connectionReview1 = (HttpURLConnection)new URL("http://localhost:8080/getreview/"+reviews.get(i).toString()).openConnection();
                connectionReview1.setRequestMethod("GET");
                int responsecodeRequest1 = connectionReview1.getResponseCode();

                if(responsecodeRequest1 == 200){
                    Scanner scanner = new Scanner(connectionReview1.getInputStream());
                    while(scanner.hasNextLine()){
                        responseReview1 += scanner.nextLine();
                        responseReview1 += "\n";
                    }
                    scanner.close();
                }


            }

            else{
                responseReview1 +=  reviews.get(i).toString();
            }

            JSONObject temp = (JSONObject) parser.parse(responseReview1);
            reviewList += temp.get("product") + " " + temp.get("rating") + " " + temp.get("comment") + " " + temp.get("reviewId");
            reviewList += "\n";

        }       // End of Reviews1 for loop

        System.out.println("Reviews:\n");

        if(reviews.size()==0)
            System.out.println("No reviews!\n");
        else
            System.out.println(reviewList);

        System.out.println("*****************");

        JSONArray extraFeatures = (JSONArray) jsonObject.get("extraFeaturesList");

        String extraFeaturesList ="";
        for(int i =0;i<extraFeatures.size();i++){

            String responseExtraFeatures1 = "";

            if(extraFeatures.get(i).toString().length()<15){

                HttpURLConnection connectionextraFeatures1 = (HttpURLConnection)new URL("http://localhost:8080/getExtraFeatures/"+extraFeatures.get(i).toString()).openConnection();
                connectionextraFeatures1.setRequestMethod("GET");
                int responsecodeExtraFeatures1 = connectionextraFeatures1.getResponseCode();

                if(responsecodeExtraFeatures1 == 200){
                    Scanner scanner = new Scanner(connectionextraFeatures1.getInputStream());
                    while(scanner.hasNextLine()){
                        responseExtraFeatures1 += scanner.nextLine();
                        responseExtraFeatures1 += "\n";
                    }
                    scanner.close();
                }


            }

            else{
                responseExtraFeatures1 += extraFeatures.get(i).toString();
            }

            JSONObject temp = (JSONObject) parser.parse(responseExtraFeatures1);
            extraFeaturesList += temp.get("efId") + " " + temp.get("featureName") +  " " + temp.get("description");
            extraFeaturesList += "\n";

        }

        System.out.println("ExtraFeatures:\n");
        if(extraFeatures.size()==0)
            System.out.println("No extra features!\n");
        else
            System.out.println(extraFeaturesList);
        System.out.println("*****************");

        for(int i=1;i<array.size();i++){    // Get the remaining phones other than the first one
            String requestURL = "";
            if (product == 0){
                requestURL  += "http://localhost:8080/getphone/" + array.get(i).toString();
            }

            else if(product == 1){
                requestURL  += "http://localhost:8080/getcomputer/" + array.get(i).toString();
            }

            String response2 = "";

            HttpURLConnection connection2 = (HttpURLConnection)new URL(requestURL).openConnection();
            connection2.setRequestMethod("GET");
            int responsecode2 = connection2.getResponseCode();

            if(responsecode2 == 200){
                Scanner scanner = new Scanner(connection2.getInputStream());
                while(scanner.hasNextLine()){
                    response2 += scanner.nextLine();
                    response2 += "\n";
                }
                scanner.close();
            }
            // Parse the remaining phones here !!

            JSONObject jsonObject2 = (JSONObject) parser.parse(response2);
            JSONArray reviews2 = (JSONArray) jsonObject2.get("reviewList");

            System.out.print(jsonObject2.get("type") + " " + jsonObject2.get("price") + " " + jsonObject2.get("productId") + " " + jsonObject2.get("model") + " " + jsonObject2.get("batteryLife") + " " + jsonObject2.get("screenSize") + " ");

            if(product == 0){   // If phone
                System.out.println(jsonObject2.get("internalMemory"));
            }

            else if(product == 1){  // If computer
                System.out.println(jsonObject2.get("memory") + " " + jsonObject2.get("processor") + " " + jsonObject2.get("screenResolution") + " " + jsonObject2.get("storageCapacity"));
            }

            String responseBrand2 = "";

            if(jsonObject2.get("brand").toString().length()<15){     // If only brandIs is present, we need another request

                HttpURLConnection connectionBrand2 = (HttpURLConnection)new URL("http://localhost:8080/getbrand/"+jsonObject2.get("brand").toString()).openConnection();
                connectionBrand2.setRequestMethod("GET");
                int responsecodeBrand2 = connectionBrand2.getResponseCode();

                if(responsecodeBrand2 == 200){
                    Scanner scanner = new Scanner(connectionBrand2.getInputStream());
                    while(scanner.hasNextLine()){
                        responseBrand2 += scanner.nextLine();
                        responseBrand2 += "\n";
                    }
                    scanner.close();
                }
            }

            else{         // If brand is present as an object
                responseBrand2 += jsonObject2.get("brand").toString();
            }

            JSONObject jsonObjectBrand2 = (JSONObject) parser.parse(responseBrand2);       // Parse the brand seperately
            System.out.println(jsonObjectBrand2.get("brandName") + " " + jsonObjectBrand2.get("brandId"));      // Print the brand seperately

            System.out.println("Product: " + jsonObject2.get("type") + " " + jsonObjectBrand2.get("brandName") + " " + jsonObject2.get("model") + " " + jsonObject2.get("productId")); //Add to table

            data.add(new BasicProductInfo(jsonObjectBrand2.get("brandName") + " " + jsonObject2.get("model"),jsonObject2.get("price").toString()));

            String reviewList2 ="";
            for(int j =0;j<reviews2.size();j++){

                String responseReview2 = "";

                if(reviews2.get(j).toString().length()<15){

                    HttpURLConnection connectionReview2 = (HttpURLConnection)new URL("http://localhost:8080/getreview/"+reviews2.get(j).toString()).openConnection();
                    connectionReview2.setRequestMethod("GET");
                    int responsecodeRequest2 = connectionReview2.getResponseCode();

                    if(responsecodeRequest2 == 200){
                        Scanner scanner = new Scanner(connectionReview2.getInputStream());
                        while(scanner.hasNextLine()){
                            responseReview2 += scanner.nextLine();
                            responseReview2 += "\n";
                        }
                        scanner.close();
                    }

                }

                else{
                    responseReview2 +=  reviews2.get(j).toString();
                }
                JSONObject temp = (JSONObject) parser.parse(responseReview2);
                reviewList2 += temp.get("product") + " " + temp.get("rating") +  " " + temp.get("comment") + " " +  temp.get("reviewId");
                reviewList2 += "\n";

            }
            System.out.println("Reviews:\n");
            if(reviews2.size()==0)
                System.out.println("No reviews!\n");
            else
                System.out.println(reviewList2);
            System.out.println("*****************");


            JSONArray extraFeatures2 = (JSONArray) jsonObject2.get("extraFeaturesList");

            String extraFeaturesList2 ="";
            for(int j =0;j<extraFeatures2.size();j++){

                String responseExtraFeatures2 = "";

                if(extraFeatures2.get(j).toString().length()<15){

                    HttpURLConnection connectionextraFeatures2 = (HttpURLConnection)new URL("http://localhost:8080/getExtraFeatures/"+extraFeatures2.get(j).toString()).openConnection();
                    connectionextraFeatures2.setRequestMethod("GET");
                    int responsecodeExtraFeatures2 = connectionextraFeatures2.getResponseCode();

                    if(responsecodeExtraFeatures2 == 200){
                        Scanner scanner = new Scanner(connectionextraFeatures2.getInputStream());
                        while(scanner.hasNextLine()){
                            responseExtraFeatures2 += scanner.nextLine();
                            responseExtraFeatures2 += "\n";
                        }
                        scanner.close();
                    }

                }

                else{
                    responseExtraFeatures2 += extraFeatures2.get(j).toString();
                }

                JSONObject temp = (JSONObject) parser.parse(responseExtraFeatures2);
                extraFeaturesList2 += temp.get("efId") + " " + temp.get("featureName") +  " " + temp.get("description");
                extraFeaturesList2 += "\n";

            }
            System.out.println("ExtraFeatures:\n");

            if(extraFeatures2.size()==0)
                System.out.println("No extra features!\n");
            else
                System.out.println(extraFeaturesList2);

            System.out.println("*****************");


        }

        tableViewProducts.setItems(data);


        // Insert the values into the table instead of creating an alert, alert is just used for testing
        // For the name column, we can use brandName + model
        // For the brandName, we might need another request or parse from extra feature list and match the brandId's

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Just Testing");

        a.setContentText("Test Content");
        a.show();
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
                    Repository.getInstance().getComputers();
                    break;
                case Constants.PHONE:
                    Range internalMemoryRange = new Range(getValue(textFieldInternalMemoryMin), getValue(textFieldInternalMemoryMax));
                    Repository.getInstance().getPhones("myfilters");
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
