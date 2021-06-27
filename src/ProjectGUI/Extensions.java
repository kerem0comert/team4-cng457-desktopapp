package ProjectGUI;

import javafx.scene.control.TextField;

/**
 * Here lies the extension functions that modify the behavior of GUI components in some way.
 */
public class Extensions {

    /**
     * Make a text field accept only numeric values. Extra points for making the function name as foolish as possible.
     * This is made possible by adding a listener for each key pressed, and if it does not pass the "only digits"
     * regex, the input is replaced by empty string, effectively destroying it.
     * @param textField to be modified.
     */
    public static void numberTextFieldify(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
