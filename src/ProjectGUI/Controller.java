package ProjectGUI;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

public class Controller {


    // *********************** Feature Product ***********************
    public void getProductsPressed(ActionEvent event) throws IOException, ParseException {

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

     //   System.out.println(response);

        String phone1 = array.get(0).toString();        // Just trying with first phone for now

        JSONObject jsonObject = (JSONObject) parser.parse(phone1);
     //   System.out.println(jsonObject);

        JSONArray reviews = (JSONArray) jsonObject.get("reviewList");

        System.out.println(jsonObject.get("type") +" " + jsonObject.get("price") + " " + jsonObject.get("productId") + " " + jsonObject.get("model") + " " + jsonObject.get("batteryLife") + " " + jsonObject.get("screenSize") + " "+ jsonObject.get("brand") + " "+ jsonObject.get("internalMemory"));

        String reviewList ="";
        for(int i =0;i<reviews.size();i++){
            JSONObject temp = (JSONObject) reviews.get(i);
            reviewList += temp.get("product") + " " + temp.get("rating") +  " " + temp.get("comment") + " " +  temp.get("reviewId");
            reviewList += "\n";
        }

        System.out.println(reviewList);

        JSONArray extraFeatures = (JSONArray) jsonObject.get("extraFeaturesList");

        String extraFeaturesList ="";
        for(int i =0;i<extraFeatures.size();i++){
            JSONObject temp = (JSONObject) extraFeatures.get(i);
            extraFeaturesList += temp.get("efId") + " " + temp.get("featureName") +  " " + temp.get("description");
            extraFeaturesList += "\n";
        }

        System.out.println(extraFeaturesList);

        for(int i=1;i<array.size();i++){
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

            System.out.println(jsonObject2.get("type") + " " + jsonObject2.get("price") + " " + jsonObject2.get("productId") + " " + jsonObject2.get("model") + " " + jsonObject2.get("batteryLife") + " " + jsonObject2.get("screenSize") + " "+ jsonObject2.get("brand") + " "+ jsonObject2.get("internalMemory"));

            String reviewList2 ="";
            for(int j =0;j<reviews2.size();j++){
                JSONObject temp = (JSONObject) reviews2.get(j);
                reviewList2 += temp.get("product") + " " + temp.get("rating") +  " " + temp.get("comment") + " " +  temp.get("reviewId");
                reviewList2 += "\n";
            }
            System.out.println("*****************");
            System.out.println(reviewList2);
            System.out.println("*****************");


            JSONArray extraFeatures2 = (JSONArray) jsonObject2.get("extraFeaturesList");

            String extraFeaturesList2 ="";
            for(int j =0;j<extraFeatures2.size();j++){
                JSONObject temp = (JSONObject) extraFeatures2.get(j);
                extraFeaturesList2 += temp.get("efId") + " " + temp.get("featureName") +  " " + temp.get("description");
                extraFeaturesList2 += "\n";
            }
            System.out.println("*****************");
            System.out.println(extraFeaturesList2);
            System.out.println("*****************");
        }


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
