package ProjectGUI;

import org.springframework.web.util.UriComponentsBuilder;

public final class Repository {
    private static Repository instance;

    private Repository(){}

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public void getComputers(Integer[] filter){
        String s = UriComponentsBuilder
                .fromUriString(Constants.BASE_URL)
                .build()
                .toString();
        System.out.println(s);
    }

    public void getPhones(String filter){ }
}
