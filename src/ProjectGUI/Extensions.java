package ProjectGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

//Here lies the extension functions that modify the behavior of GUI components in some way.
public class Extensions {

    //Make a text field accept only numeric values.
    //Extra points for making the function name as foolish as possible.
    public static void numberTextFieldify(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
