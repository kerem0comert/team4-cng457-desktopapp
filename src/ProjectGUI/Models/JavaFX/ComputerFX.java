package ProjectGUI.Models.JavaFX;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ComputerFX extends ProductFX {
    private SimpleStringProperty screenResolution;
    private SimpleStringProperty processor;
    private SimpleIntegerProperty memory;
    private SimpleIntegerProperty storageCapacity;

    public ComputerFX() {
        super();
        screenResolution = new SimpleStringProperty();
        processor = new SimpleStringProperty();
        memory = new SimpleIntegerProperty();
        storageCapacity = new SimpleIntegerProperty();
    }

    public String getScreenResolution() {
        return screenResolution.get();
    }

    public void setScreenResolution(String newValue) {
        screenResolution.set(newValue);
    }

    public StringProperty screenResolutionProperty() {
        return screenResolution;
    }

    public String getProcessor() {
        return processor.get();
    }

    public void setProcessor(String newValue) {
        processor.set(newValue);
    }

    public StringProperty processorProperty() {
        return processor;
    }

    public int getMemory() {
        return memory.get();
    }

    public void setMemory(int newValue) {
        memory.set(newValue);
    }

    public IntegerProperty memoryProperty() {
        return memory;
    }

    public int getStorageCapacity() {
        return storageCapacity.get();
    }

    public void setStorageCapacity(int newValue) {
        storageCapacity.set(newValue);
    }

    public IntegerProperty storageCapacityProperty() {
        return storageCapacity;
    }
}
