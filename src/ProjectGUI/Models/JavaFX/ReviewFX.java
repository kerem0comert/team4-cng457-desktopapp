package ProjectGUI.Models.JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReviewFX {
    private SimpleStringProperty review1;
    private SimpleStringProperty review2;
    private SimpleStringProperty review3;

    public ReviewFX() {
        review1 = new SimpleStringProperty();
        review2 = new SimpleStringProperty();
        review3 = new SimpleStringProperty();
    }

    public String getReview1() {
        return review1.get();
    }

    public void setReview1(String newVal) {
        review1.set(newVal);
    }

    public StringProperty review1Property() {
        return review1;
    }

    public String getReview2() {
        return review2.get();
    }

    public void setReview2(String newVal) {
        review2.set(newVal);
    }

    public StringProperty review2Property() {
        return review2;
    }

    public String getReview3() {
        return review3.get();
    }

    public void setReview3(String newVal) {
        review3.set(newVal);
    }

    public StringProperty review3Property() {
        return review3;
    }
}
