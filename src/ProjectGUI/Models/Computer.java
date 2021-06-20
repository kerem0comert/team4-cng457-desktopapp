package ProjectGUI.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Computer extends Product {
    private String screenResolution;
    private String processor;
    private Integer memory;
    private Integer storageCapacity;
}
