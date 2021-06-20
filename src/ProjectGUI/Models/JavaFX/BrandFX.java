package ProjectGUI.Models.JavaFX;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BrandFX {
    private SimpleStringProperty brandName;

    public BrandFX() {
        brandName = new SimpleStringProperty();
    }

    public String getBrandName() {
        return brandName.get();
    }

    public void setBrandName(String newVal) {
        brandName.set(newVal);
    }

    public StringProperty brandNameProperty() {
        return brandName;
    }
}
