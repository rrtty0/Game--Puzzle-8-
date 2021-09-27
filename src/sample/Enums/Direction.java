package sample.Enums;

public enum Direction {

    LEFT(0),
    TOP(1),
    RIGHT(2),
    DOWN(3);

    private int value;

    private Direction(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}