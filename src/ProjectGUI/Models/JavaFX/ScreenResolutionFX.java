package ProjectGUI.Models.JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScreenResolutionFX {
    private SimpleStringProperty screenResolution;

    public ScreenResolutionFX() {
        screenResolution = new SimpleStringProperty();
    }

    public String getScreenResolution() {
        return screenResolution.get();
    }

    public void setScreenResolution(String newVal) {
        screenResolution.set(newVal);
    }

    public StringProperty screenResolutionProperty() {
        return screenResolution;
    }
}
