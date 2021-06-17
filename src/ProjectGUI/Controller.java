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

        System.out.println(jsonObject.get("type") + " " + jsonObject.get("productId") + " " + jsonObject.get("model") + " " + jsonObject.get("batteryLife") + " " + jsonObject.get("screenSize") + " "+ jsonObject.get("brand") + " "+ jsonObject.get("internalMemory"));

        String reviewList ="";
        for(int i =0;i<reviews.size();i++){
            JSONObject temp = (JSONObject) reviews.get(i);
            reviewList += temp.get("product") + " " + temp.get("rating") +  " " + temp.get("comment") + " " +  temp.get("reviewId");
            reviewList += "\n";
        }

        System.out.println(reviewList);

        JSONArray extraFeatures = (JSONArray) jsonObject.get("extraFeaturesList");

        String extraFeaturesList ="";
        for(int i =0;i<reviews.size();i++){
            JSONObject temp = (JSONObject) extraFeatures.get(i);
            extraFeaturesList += temp.get("efId") + " " + temp.get("featureName") +  " " + temp.get("description");
            extraFeaturesList += "\n";
        }

        System.out.println(extraFeaturesList);

        // Insert the values into the table instead of creating an alert, alert is just used for testing
        // For the name column, we can use brandName + model
        // For the brandName, we might need another request or parse from extra feature list and match the brandId's

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Just Testing");

        a.setContentText("Test Content");
        a.show();
    }

}
