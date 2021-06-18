package ProjectGUI;


import com.sun.corba.se.impl.orbutil.closure.Constant;

import java.util.Arrays;

public final class Repository {
    private static Repository instance;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private String appendNonNull(String fieldName, Object o) {
        return o == null ? "" : fieldName + "=" + o;
    }

    //These are the fields of the parent Product, that is shared by both phone and computer
    private StringBuilder appendBaseFields(String model, String brand, Range batteryLife, String screenSize,
                                           Range priceRange){
        StringBuilder url = new StringBuilder(Constants.BASE_URL + Constants.GET_COMPUTER);
        url.append(appendNonNull(Constants.MODEL, model));
        url.append(appendNonNull(Constants.MODEL, brand));
        url.append(appendNonNull(Constants.MIN_BATTERY_LIFE, batteryLife.getMin()));
        url.append(appendNonNull(Constants.MAX_BATTERY_LIFE, batteryLife.getMax()));
        url.append(appendNonNull(Constants.SCREEN_SIZE, screenSize));
        url.append(appendNonNull(Constants.MIN_PRICE, priceRange.getMin()));
        url.append(appendNonNull(Constants.MAX_PRICE, priceRange.getMax()));
        return url;
    }

    public void getComputers(String model, String brand, Range batteryLife, String screenSize,
                             Range priceRange, String screenResolution, String processor, Range memory,
                             Range storageCapacity) {
        StringBuilder url = appendBaseFields(model, brand, batteryLife, screenSize, priceRange);
        url.append(appendNonNull(Constants.SCREEN_RESOLUTION, screenResolution));
        url.append(appendNonNull(Constants.PROCESSOR, processor));
        url.append(appendNonNull(Constants.MIN_MEMORY, memory.getMin()));
        url.append(appendNonNull(Constants.MAX_MEMORY, memory.getMax()));
        url.append(appendNonNull(Constants.MIN_STORAGE_CAPACITY, storageCapacity.getMin()));
        url.append(appendNonNull(Constants.MAX_STORAGE_CAPACITY, storageCapacity.getMax()));
        System.out.println(url.toString());
    }

    public void getPhones(String model, String brand, Range batteryLife, String screenSize,
                          Range priceRange, Range internalMemory) {
        StringBuilder url = appendBaseFields(model, brand, batteryLife, screenSize, priceRange);
    }
}
