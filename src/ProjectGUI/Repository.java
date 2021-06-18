package ProjectGUI;


import com.sun.corba.se.impl.orbutil.closure.Constant;

import java.util.Arrays;

public final class Repository {
    private static Repository instance;

    private Repository(){}

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    private String appendNonNull(String fieldName, Object o){
        return o == null ? "" : fieldName + "=" + o ;
    }

    public void getComputers(Integer[] batteryLifeRange){
        StringBuilder url = new StringBuilder(Constants.BASE_URL + Constants.GET_COMPUTER);
        url.append(appendNonNull(Constants.MIN_BATTERY_LIFE, batteryLifeRange[0]));
        url.append(appendNonNull(Constants.MAX_BATTERY_LIFE, batteryLifeRange[1]));
        System.out.println(url.toString());
    }

    public void getPhones(String filter){
        
    }
}
