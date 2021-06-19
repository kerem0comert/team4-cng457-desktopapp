package ProjectGUI;

public class Range {
    private Integer min;

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    private Integer max;

    Range(Integer min, Integer max){
        this.min = min;
        this.max = max;
    }
}
