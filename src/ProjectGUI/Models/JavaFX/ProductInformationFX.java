package ProjectGUI.Models.JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductInformationFX {
    private SimpleStringProperty featureName;
    private SimpleStringProperty feature1;
    private SimpleStringProperty feature2;
    private SimpleStringProperty feature3;

    public ProductInformationFX() {
        featureName = new SimpleStringProperty();
        feature1 = new SimpleStringProperty();
        feature2 = new SimpleStringProperty();
        feature3 = new SimpleStringProperty();
    }

    public String getFeatureName() {
        return featureName.get();
    }

    public void setFeatureName(String newVal) {
        featureName.set(newVal);
    }

    public StringProperty featureNameProperty() {
        return featureName;
    }

    public String getFeature1() {
        return feature1.get();
    }

    public void setFeature1(String newVal) {
        feature1.set(newVal);
    }

    public StringProperty feature1Property() {
        return feature1;
    }

    public String getFeature2() {
        return feature2.get();
    }

    public void setFeature2(String newVal) {
        feature2.set(newVal);
    }

    public StringProperty feature2Property() {
        return feature2;
    }

    public String getFeature3() {
        return feature3.get();
    }

    public void setFeature3(String newVal) {
        feature3.set(newVal);
    }

    public StringProperty feature3Property() {
        return feature3;
    }
}
