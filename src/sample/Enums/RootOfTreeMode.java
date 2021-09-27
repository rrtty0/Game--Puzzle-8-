package sample.Enums;

public enum RootOfTreeMode {

    START(0),
    FINISH(1);

    private int value;
    private String name;

    private RootOfTreeMode(int value){
        this.value = value;
        if(value == 0)
            name = "START";
        else
            name = "FINISH";
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
