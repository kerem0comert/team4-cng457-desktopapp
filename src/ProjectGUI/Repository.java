package ProjectGUI;


import ProjectGUI.Models.*;
import ProjectGUI.Models.JavaFX.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class that handles communication with back-end.
 */
public final class Repository {
    private static Repository instance;

    /**
     * The constructor is private, as the Singleton Pattern is implemented here.
     */
    private Repository() {
    }

    /**
     *
     * @return The singleton repository instance.
     */
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    /**
     * This method opens the necessary HTTPConnection with the back-end server and returns the response in form of
     * a JSONArray.
     * @param mainURL is the URL to which the HTTP response will be sent.
     * @return The JSONArray, containing the HTTP response.
     */
    private JSONArray getResponseAsJSON(String mainURL) {
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
     * Append arguments one by one to the base URL to build the query, pass if the field is not specified by the user.
     * This should ideally be done with solutions like UriComponentsBuilder, but the library just could not be
     * compiled after more an hour of trying. In order to keep my sanity, I went with the ugly implementation
     * @param fieldName The fieldName specified in the query
     * @param o is any value of type object, which is included in the query.
     * @return fieldName=o& or empty string.
     */
    private String appendNonNull(String fieldName, Object o) {
        return o == null ? "" : fieldName + "=" + o + "&";
    }


    /**
     * These are the fields of the parent ProductFX, that is shared by both phone and computer.
     * They can be present in both types of queries.
     * @param type The path to which the query is made. This will decide which table/tables will be used in back-end.
     * @param brand The requested brand as String.
     * @param batteryLife of type Range, which includes min and max values.
     * @param screenSize of type Range, which includes min and max values.
     * @param priceRange of type Range, which includes min and max values.
     * @return the URL dynamically built with the query parameters.
     */
    private StringBuilder appendBaseFields(String type, String brand, Range batteryLife, String screenSize,
                                           Range priceRange) {
        StringBuilder url = new StringBuilder(Constants.BASE_URL + type);
        url.append(appendNonNull(Constants.BRAND, brand));
        url.append(appendNonNull(Constants.MIN_BATTERY_LIFE, batteryLife == null ? null : batteryLife.getMin()));
        url.append(appendNonNull(Constants.MAX_BATTERY_LIFE, batteryLife == null ? null : batteryLife.getMax()));
        url.append(appendNonNull(Constants.SCREEN_SIZE, screenSize));
        url.append(appendNonNull(Constants.MIN_PRICE, priceRange == null ? null : priceRange.getMin()));
        url.append(appendNonNull(Constants.MAX_PRICE, priceRange == null ? null : priceRange.getMax()));
        return url;
    }

    public Product parseProduct(Object obj, Product prod) {
        int productIdFromJSON = ((Long) ((JSONObject) obj).get(Constants.PRODUCT_ID)).intValue();
        String modelFromJSON = ((JSONObject) obj).get(Constants.MODEL).toString();
        int batteryLifeFromJSON = ((Long) ((JSONObject) obj).get(Constants.BATTERY_LIFE)).intValue();
        String screenSizeFromJSON = ((JSONObject) obj).get(Constants.SCREEN_SIZE).toString();
        int priceFromJSON = ((Long) ((JSONObject) obj).get(Constants.PRICE)).intValue();
        String brandFromJSON = ((JSONObject) ((JSONObject) obj).get(Constants.BRAND)).get(Constants.BRAND_NAME).toString();

        prod.setProductId(productIdFromJSON);
        prod.setModel(brandFromJSON + " " + modelFromJSON);
        prod.setBatteryLife(batteryLifeFromJSON);
        prod.setScreenSize(screenSizeFromJSON);
        prod.setPrice(priceFromJSON);

        JSONArray extraFeatures = (JSONArray) ((JSONObject) obj).get(Constants.EXTRA_FEATURES_LIST);

        JSONArray reviews = (JSONArray) ((JSONObject) obj).get("reviewList");


        ArrayList<ExtraFeature> newExtraFeaturesList = new ArrayList<>();
        ArrayList<Review> newReviewList = new ArrayList<>();

        for (Object extraFeature : extraFeatures) {
            String featureNameJSON = ((JSONObject) extraFeature).get(Constants.FEATURE_NAME).toString();
            String featureDescriptionJSON = ((JSONObject) extraFeature).get(Constants.DESCRIPTION).toString();

            ExtraFeature newExtraFeature = new ExtraFeature();
            newExtraFeature.setFeatureName(featureNameJSON);
            newExtraFeature.setDescription(featureDescriptionJSON);
            newExtraFeaturesList.add(newExtraFeature);
        }

        prod.setExtraFeaturesList(newExtraFeaturesList);

        for (Object review : reviews) {
            String commentJSON = ((JSONObject) review).get(Constants.COMMENT).toString();
            String ratingJSON = ((JSONObject) review).get(Constants.RATING).toString();

            Review newReview = new Review();
            newReview.setComment(commentJSON);
            newReview.setRating(Integer.valueOf(ratingJSON));
            newReviewList.add(newReview);
        }

        prod.setReviewList(newReviewList);


        return prod;
    }


    /**
     * Gets the data into a JSONArray from baseUrl/getAllBrands. It then iterates over the array to fill
     * the brandFXList to be displayed in GUI table.
     * @return the list of brand rows.
     */
    public ObservableList<BrandFX> getAllBrandsFX() {
        ObservableList<BrandFX> brandFXList = FXCollections.observableArrayList();

        StringBuilder url = appendBaseFields(Constants.GET_ALL_BRANDS, null, null, null, null);

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            String brandNameJSON = ((JSONObject) obj).get(Constants.BRAND_NAME).toString();
            BrandFX newBrandFX = new BrandFX();
            newBrandFX.setBrandName(brandNameJSON);
            brandFXList.add(newBrandFX);
        }

        return brandFXList;

    }

    /**
     *
     * @return
     */
    public ObservableList<ScreenSizeFX> getAllScreenSizesForComputersFX() {
        ObservableList<ScreenSizeFX> screenSizeFXList = FXCollections.observableArrayList();

        StringBuilder url = appendBaseFields(Constants.GET_ALL_SCREEN_SIZES_FOR_COMPUTERS, null, null, null, null);

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            String screenSizeJSON = ((String) obj);
            ScreenSizeFX newScreenSizeFX = new ScreenSizeFX();
            newScreenSizeFX.setScreenSize(screenSizeJSON);
            screenSizeFXList.add(newScreenSizeFX);
        }

        return screenSizeFXList;

    }

    /**
     * Construct the URL necessary to send a request for screenResolutions. The response is returned as a JSONArray,
     * which is then mapped to an ObservableList of {@link ScreenResolutionFX} objects that can be used by the
     * table view
     * @return a list of {@link ScreenResolutionFX} objects for table view to display as a column.
     */
    public ObservableList<ScreenResolutionFX> getAllScreenResolutionsForComputersFX() {
        ObservableList<ScreenResolutionFX> screenResolutionFXList = FXCollections.observableArrayList();

        StringBuilder url = appendBaseFields(Constants.GET_ALL_SCREEN_RESOLUTIONS_FOR_COMPUTERS, null,
                null, null, null);

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            String screenResolutionJSON = ((String) obj);
            ScreenResolutionFX newScreenResolutionFX = new ScreenResolutionFX();
            newScreenResolutionFX.setScreenResolution(screenResolutionJSON);
            screenResolutionFXList.add(newScreenResolutionFX);
        }

        return screenResolutionFXList;

    }

    /**
     *
     * @return
     */
    public ObservableList<ProcessorFX> getAllProcessorsForComputersFX() {
        ObservableList<ProcessorFX> ProcessorFXList = FXCollections.observableArrayList();

        StringBuilder url = appendBaseFields(Constants.GET_ALL_PROCESSORS_FOR_COMPUTERS, null,
                null, null, null);

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            String ProcessorJSON = ((String) obj);
            ProcessorFX newProcessorFX = new ProcessorFX();
            newProcessorFX.setProcessor(ProcessorJSON);
            ProcessorFXList.add(newProcessorFX);
        }

        return ProcessorFXList;

    }

    /**
     *
     * @return
     */
    public ObservableList<ScreenSizeFX> getAllScreenSizesForPhonesFX() {
        ObservableList<ScreenSizeFX> screenSizeFXList = FXCollections.observableArrayList();

        StringBuilder url = appendBaseFields(Constants.GET_ALL_SCREEN_SIZES_FOR_PHONES, null,
                null, null, null);

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            String screenSizeJSON = ((String) obj);
            ScreenSizeFX newScreenSizeFX = new ScreenSizeFX();
            newScreenSizeFX.setScreenSize(screenSizeJSON);
            screenSizeFXList.add(newScreenSizeFX);
        }

        return screenSizeFXList;

    }

    /**
     * The method constructs the url for the filtered computers to be retrieved and the retrieval is made into
     * a JSON Array, which is parsed to list of {@link Phone} objects.
     * @param brand
     * @param batteryLife
     * @param screenSize
     * @param priceRange
     * @param internalMemory
     * @return
     */
    public ArrayList<Phone> getPhonesAsModel(String brand, Range batteryLife, String screenSize,
                                             Range priceRange, Range internalMemory) {
        StringBuilder url = appendBaseFields(Constants.GET_PHONE, brand, batteryLife, screenSize, priceRange);
        url.append(appendNonNull(Constants.MIN_INTERNAL_MEMORY, internalMemory == null ? null : internalMemory.getMin()));
        url.append(appendNonNull(Constants.MAX_INTERNAL_MEMORY, internalMemory == null ? null : internalMemory.getMax()));

        ArrayList<Phone> phones = new ArrayList<Phone>();

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            Phone newPhone = (Phone) parseProduct(obj, new Phone());
            int internalMemoryFromJSON = ((Long) ((JSONObject) obj).get(Constants.INTERNAL_MEMORY)).intValue();
            newPhone.setInternalMemory(internalMemoryFromJSON);
            phones.add(newPhone);
        }

        Controller.fetchedProducts = new ArrayList<>(phones);

        return phones;
    }

    /**
     *
     * The params are not explained seperately, as they are simply parameters present in the database that will
     * be mapped to columns in tableView.
     * @param brand
     * @param batteryLife
     * @param screenSize
     * @param priceRange
     * @param internalMemory
     * @return
     */
    public ObservableList<PhoneFX> getPhonesFX(String brand, Range batteryLife, String screenSize,
                                               Range priceRange, Range internalMemory) {
        ObservableList<PhoneFX> phonesFX = FXCollections.observableArrayList();

        ArrayList<Phone> phones = getPhonesAsModel(brand, batteryLife, screenSize, priceRange, internalMemory);

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

    /**
     * The method constructs the url for the filtered computers to be retrieved and the retrieval is made into
     * a JSON Array, which is parsed to list of {@link Computer} objects.
     * The params are not explained seperately, as they are simply parameters present in the database that will
     * be mapped to columns in tableView.
     * @param brand
     * @param batteryLife
     * @param screenSize
     * @param priceRange
     * @param screenResolution
     * @param processor
     * @param memory
     * @param storageCapacity
     * @return an arrayList of computers to be used by the tableView.
     */
    public ArrayList<Computer> getComputersAsModel(String brand, Range batteryLife, String screenSize,
                                                   Range priceRange, String screenResolution, String processor, Range memory,
                                                   Range storageCapacity) {
        StringBuilder url = appendBaseFields(Constants.GET_COMPUTER, brand, batteryLife, screenSize, priceRange);
        url.append(appendNonNull(Constants.SCREEN_RESOLUTION, screenResolution));
        url.append(appendNonNull(Constants.PROCESSOR, processor));
        url.append(appendNonNull(Constants.MIN_MEMORY, memory == null ? null : memory.getMin()));
        url.append(appendNonNull(Constants.MAX_MEMORY, memory == null ? null : memory.getMax()));
        url.append(appendNonNull(Constants.MIN_STORAGE_CAPACITY, storageCapacity == null ? null : storageCapacity.getMin()));
        url.append(appendNonNull(Constants.MAX_STORAGE_CAPACITY, storageCapacity == null ? null : storageCapacity.getMax()));

        ArrayList<Computer> computers = new ArrayList<>();

        JSONArray jsonArray = getResponseAsJSON(url.toString());

        for (Object obj : jsonArray) {
            Computer newComputer = (Computer) parseProduct(obj, new Computer());

            String screenResolutionFromJSON = ((JSONObject) obj).get(Constants.SCREEN_RESOLUTION).toString();
            String processorFromJSON = ((JSONObject) obj).get(Constants.PROCESSOR).toString();
            int memoryFromJSON = ((Long) ((JSONObject) obj).get(Constants.MEMORY)).intValue();
            int storageCapacityFromJSON = ((Long) ((JSONObject) obj).get(Constants.STORAGE_CAPACITY)).intValue();

            newComputer.setScreenResolution(screenResolutionFromJSON);
            newComputer.setProcessor(processorFromJSON);
            newComputer.setMemory(memoryFromJSON);
            newComputer.setStorageCapacity(storageCapacityFromJSON);

            computers.add(newComputer);
        }

        Controller.fetchedProducts = new ArrayList<>(computers);

        return computers;
    }

    /**
     * This method iterates through the computers list and creates the Observable list that is used by
     * the tableView.
     * The params are not explained seperately, as they are simply parameters present in the database that will
     * be mapped to columns in tableView.
     * @param brand
     * @param batteryLife
     * @param screenSize
     * @param priceRange
     * @param screenResolution
     * @param processor
     * @param memory
     * @param storageCapacity
     * @return an observableList to be used by the tableView that contains details for each filtered computer.
     */
    public ObservableList<ComputerFX> getComputersFX(String brand, Range batteryLife, String screenSize,
                                                     Range priceRange, String screenResolution, String processor, Range memory,
                                                     Range storageCapacity) {
        ObservableList<ComputerFX> computerFX = FXCollections.observableArrayList();

        ArrayList<Computer> computers = getComputersAsModel(brand, batteryLife, screenSize, priceRange,
                screenResolution, processor, memory, storageCapacity);

        for (Computer computer : computers) {
            ComputerFX newComputerFX = new ComputerFX();

            newComputerFX.setMemory(computer.getMemory());
            newComputerFX.setProcessor(computer.getProcessor());
            newComputerFX.setModel(computer.getModel());
            newComputerFX.setPrice(computer.getPrice());
            newComputerFX.setScreenResolution(computer.getScreenResolution());
            newComputerFX.setBatteryLife(computer.getBatteryLife());
            newComputerFX.setProductId(computer.getProductId());
            newComputerFX.setScreenSize(computer.getScreenSize());

            computerFX.add(newComputerFX);
        }

        return computerFX;
    }
}
