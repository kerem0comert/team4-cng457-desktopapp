package ProjectGUI.Models.JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProcessorFX {
    private SimpleStringProperty processor;

    public ProcessorFX() {
        processor = new SimpleStringProperty();
    }

    public String getProcessor() {
        return processor.get();
    }

    public void setProcessor(String newVal) {
        processor.set(newVal);
    }

    public StringProperty processorProperty() {
        return processor;
    }
}
