package ProjectGUI.Models.JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScreenSizeFX {
    private SimpleStringProperty screenSize;

    public ScreenSizeFX() {
        screenSize = new SimpleStringProperty();
    }

    public String getScreenSize() {
        return screenSize.get();
    }

    public void setScreenSize(String newVal) {
        screenSize.set(newVal);
    }

    public StringProperty screenSizeProperty() {
        return screenSize;
    }
}
