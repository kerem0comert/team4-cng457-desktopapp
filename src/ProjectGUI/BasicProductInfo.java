package ProjectGUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BasicProductInfo {

    private SimpleStringProperty name;
    private SimpleStringProperty price;

    public BasicProductInfo(String name, String price) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleStringProperty(price);
    }

    public String getName() {
        return name.get();
    }

    public String getPrice() {
        return price.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }
}
