package ProjectGUI;

public final class Repository {
    private static Repository instance;

    private Repository(){}

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public void getComputers(String filter){ }

    public void getPhones(String filter){ }
}
