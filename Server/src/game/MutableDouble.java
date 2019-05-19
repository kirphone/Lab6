package game;

public class MutableDouble implements Comparable<MutableDouble> {
    private double value;

    public MutableDouble(double _value) {
        value = _value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double _value) {
        value = _value;
    }

    @Override
    public int compareTo(MutableDouble mutableDouble) {
        return Double.compare(this.getValue(), mutableDouble.getValue());
    }
}