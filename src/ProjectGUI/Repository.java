package ProjectGUI;


import ProjectGUI.Models.Constants;
import ProjectGUI.Models.JavaFX.PhoneFX;
import ProjectGUI.Models.Phone;
import ProjectGUI.Models.Product;
import ProjectGUI.Models.Range;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class Repository {

    private static JSONArray GetResponseAsJSON(String mainURL) {
        try {
            String response = "";
            HttpURLConnection connection = (HttpURLConnection) new URL(mainURL).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    response += scanner.nextLine();
                    response += "\n";
                }
                scanner.close();
                connection.disconnect();
                return (JSONArray) new JSONParser().parse(response);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    /**
     * This should ideally be done with solutions like UriComponentsBuilder, but the library just could not be
     * compiled after more an hour of trying. In order to keep my sanity, I went with the ugly implementation.
     */
    private static String appendNonNull(String fieldName, Object o) {
        return o == null ? "" : fieldName + "=" + o + "&";
    }

    //These are the fields of the parent ProductFX, that is shared by both phone and computer
    private static StringBuilder appendBaseFields(String type, String model, String brand, Range batteryLife, String screenSize,
                                                  Range priceRange) {
        StringBuilder url = new StringBuilder(Constants.BASE_URL + type);
        url.append(appendNonNull(Constants.MODEL, model));
        url.append(appendNonNull(Constants.BRAND, brand));
        url.append(appendNonNull(Constants.MIN_BATTERY_LIFE, batteryLife == null ? null : batteryLife.getMin()));
        url.append(appendNonNull(Constants.MAX_BATTERY_LIFE, batteryLife == null ? null : batteryLife.getMax()));
        url.append(appendNonNull(Constants.SCREEN_SIZE, screenSize));
        url.append(appendNonNull(Constants.MIN_PRICE, priceRange == null ? null : priceRange.getMin()));
        url.append(appendNonNull(Constants.MAX_PRICE, priceRange == null ? null : priceRange.getMax()));
        return url;
    }

    public static void getComputers(String model, String brand, Range batteryLife, String screenSize,
                                    Range priceRange, String screenResolution, String processor, Range memory,
                                    Range storageCapacity) {
        StringBuilder url = appendBaseFields(Constants.GET_COMPUTER, model, brand, batteryLife, screenSize, priceRange);
        url.append(appendNonNull(Constants.SCREEN_RESOLUTION, screenResolution));
        url.append(appendNonNull(Constants.PROCESSOR, processor));
        url.append(appendNonNull(Constants.MIN_MEMORY, memory.getMin()));
        url.append(appendNonNull(Constants.MAX_MEMORY, memory.getMax()));
        url.append(appendNonNull(Constants.MIN_STORAGE_CAPACITY, storageCapacity.getMin()));
        url.append(appendNonNull(Constants.MAX_STORAGE_CAPACITY, storageCapacity.getMax()));
        System.out.println(url.toString());
    }

    public static Product parseProduct(Object obj, Product prod) {
        int productIdFromJSON = ((Long) ((JSONObject) obj).get("productId")).intValue();
        String modelFromJSON = ((JSONObject) obj).get("model").toString();
        int batteryLifeFromJSON = ((Long) ((JSONObject) obj).get("batteryLife")).intValue();
        String screenSizeFromJSON = ((JSONObject) obj).get("screenSize").toString();
        int priceFromJSON = ((Long) ((JSONObject) obj).get("price")).intValue();

        prod.setProductId(productIdFromJSON);
        prod.setModel(modelFromJSON);
        prod.setBatteryLife(batteryLifeFromJSON);
        prod.setScreenSize(screenSizeFromJSON);
        prod.setPrice(priceFromJSON);

        return prod;
    }

    public static ArrayList<Phone> getPhonesAsModel(String model, String brand, Range batteryLife, String screenSize,
                                                    Range priceRange, Range internalMemory) {
        StringBuilder url = appendBaseFields(Constants.GET_PHONE, model, brand, batteryLife, screenSize, priceRange);
        url.append(appendNonNull(Constants.MIN_INTERNAL_MEMORY, internalMemory == null ? null : internalMemory.getMin()));
        url.append(appendNonNull(Constants.MAX_INTERNAL_MEMORY, internalMemory == null ? null : internalMemory.getMax()));

        ArrayList<Phone> phones = new ArrayList<Phone>();

        JSONArray jsonArray = GetResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            Phone newPhone = (Phone) parseProduct(obj, new Phone());
            int internalMemoryFromJSON = ((Long) ((JSONObject) obj).get("internalMemory")).intValue();
            newPhone.setInternalMemory(internalMemoryFromJSON);
            phones.add(newPhone);
        }

        return phones;
    }

    public static ObservableList<PhoneFX> getPhonesFX(String model, String brand, Range batteryLife, String screenSize,
                                                      Range priceRange, Range internalMemory) {
        ObservableList<PhoneFX> phonesFX = FXCollections.observableArrayList();

        ArrayList<Phone> phones = getPhonesAsModel(model, brand, batteryLife, screenSize, priceRange, internalMemory);

        for (Phone phone : phones) {
            PhoneFX newPhoneFX = new PhoneFX();
            newPhoneFX.setProductId(phone.getProductId());
            newPhoneFX.setModel(phone.getModel());
            newPhoneFX.setBatteryLife(phone.getBatteryLife());
            newPhoneFX.setScreenSize(phone.getScreenSize());
            newPhoneFX.setPrice(phone.getPrice());
            newPhoneFX.setInternalMemory(phone.getInternalMemory());
            phonesFX.add(newPhoneFX);
        }

        return phonesFX;
    }
}
