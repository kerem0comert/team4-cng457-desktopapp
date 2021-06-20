package ProjectGUI.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product {
    private Integer productId;
    private String model;
    private Integer batteryLife;
    private String screenSize;
    private Integer price;

    private ArrayList<ExtraFeature> extraFeaturesList;
    private ArrayList<Review> reviewList;
}
