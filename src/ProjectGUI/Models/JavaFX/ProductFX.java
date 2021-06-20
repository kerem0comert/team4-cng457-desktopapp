package ProjectGUI.Models.JavaFX;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class ProductFX {
    protected SimpleIntegerProperty productId;
    protected SimpleStringProperty model;
    protected SimpleIntegerProperty batteryLife;
    protected SimpleStringProperty screenSize;
    protected SimpleIntegerProperty price;

    public ProductFX() {
        productId = new SimpleIntegerProperty();
        model = new SimpleStringProperty();
        batteryLife = new SimpleIntegerProperty();
        screenSize = new SimpleStringProperty();
        price = new SimpleIntegerProperty();
    }

    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int newProductId) {
        productId.set(newProductId);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String newModel) {
        model.set(newModel);
    }

    public StringProperty modelProperty() {
        return model;
    }

    public int getBatteryLife() {
        return batteryLife.get();
    }

    public void setBatteryLife(int newBatteryLife) {
        batteryLife.set(newBatteryLife);
    }

    public IntegerProperty batteryLifeProperty() {
        return batteryLife;
    }

    public String getScreenSize() {
        return screenSize.get();
    }

    public void setScreenSize(String newScreenSize) {
        screenSize.set(newScreenSize);
    }

    public StringProperty screenSizeProperty() {
        return screenSize;
    }

    public int getPrice() {
        return price.get();
    }

    public void setPrice(int newProductId) {
        price.set(newProductId);
    }

    public IntegerProperty priceProperty() {
        return price;
    }
}
