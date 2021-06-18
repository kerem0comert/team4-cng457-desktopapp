package ProjectGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {


    // *********************** Feature Product ***********************

    @FXML
    private TableView<BasicProductInfo> tableViewProducts;

    @FXML
    private TableColumn<BasicProductInfo,String> columnName;

    @FXML
    private TableColumn<BasicProductInfo,String> columnPrice;

    public void initialize(URL location, ResourceBundle resources){
        initTable();

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

    private void loadData(){        // Implement this functionality inside the controller, under the "add to table comments"

        final ObservableList<BasicProductInfo> table_data = FXCollections.observableArrayList(
                new BasicProductInfo("Jacob", "500"),
                new BasicProductInfo("Isabella", "1000"),
                new BasicProductInfo("Ethan", "1500")
        );

       /* columnName.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        columnPrice.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );*/

        tableViewProducts.setItems(table_data);

    }

    public void getProductsPressed(ActionEvent event) throws IOException, ParseException {

        ObservableList<BasicProductInfo> data = FXCollections.observableArrayList();
        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getallphones").openConnection();
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

        System.out.println(jsonObject.get("type") +" " + jsonObject.get("price") + " " + jsonObject.get("productId") + " " + jsonObject.get("model") + " " + jsonObject.get("batteryLife") + " " + jsonObject.get("screenSize") + " "+ jsonObject.get("internalMemory"));

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

            System.out.println("Phone: " + jsonObjectBrand1.get("brandName") + " " + jsonObject.get("model") + " " + jsonObject.get("productId")); //Add to table

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

        System.out.println("Reviews:");

        if(reviews.size()==0)
            System.out.println("No reviews!");
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

        System.out.println("ExtraFeatures:");
        if(extraFeatures.size()==0)
            System.out.println("No extra features!");
        else
            System.out.println(extraFeaturesList);
        System.out.println("*****************");

        for(int i=1;i<array.size();i++){    // Get the remaining phones other than the first one
            String requestURL = "http://localhost:8080/getphone/" + array.get(i).toString();
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

            System.out.println(jsonObject2.get("type") + " " + jsonObject2.get("price") + " " + jsonObject2.get("productId") + " " + jsonObject2.get("model") + " " + jsonObject2.get("batteryLife") + " " + jsonObject2.get("screenSize") + " "+ jsonObject2.get("internalMemory"));



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

                System.out.println("Phone: " + jsonObjectBrand2.get("brandName") + " " + jsonObject2.get("model") + " " + jsonObject2.get("productId")); //Add to table

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
            System.out.println("Reviews:");
            if(reviews2.size()==0)
                System.out.println("No reviews!");
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
            System.out.println("ExtraFeatures:");

            if(extraFeatures2.size()==0)
                System.out.println("No extra features!");
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


    // *********************** Feature Product ***********************

}
