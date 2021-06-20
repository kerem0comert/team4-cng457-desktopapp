package ProjectGUI.Models.JavaFX;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PhoneFX extends ProductFX {
    private SimpleIntegerProperty internalMemory;

    public PhoneFX() {
        super();
        internalMemory = new SimpleIntegerProperty();
    }

    public int getInternalMemory() {
        return internalMemory.get();
    }

    public void setInternalMemory(int newInternalMemory) {
        internalMemory.set(newInternalMemory);
    }

    public IntegerProperty internalMemoryProperty() {
        return internalMemory;
    }
}
