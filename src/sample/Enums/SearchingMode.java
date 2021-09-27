package sample.Enums;

public enum SearchingMode {

    BLIND_DFS(0),
    BLIND_PARALLEL_DFS(1),
    HEURISTIC_MANHATTAN_SEARCHING(2),
    HEURISTIC_MANHATTAN_PARALLEL_SEARCHING(3),
    HEURISTIC_STRANGER_POSITION_SEARCHING(4),
    HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING(5);

    private int value;
    private int maxDepth;

    private SearchingMode(int value){
        this.value = value;
        switch (value){
            case 0:
                maxDepth = 40;
                break;
            case 1:
                maxDepth = 18;
                break;
            case 2:
                maxDepth = 25;
                break;
            case 3:
                maxDepth = 12;
                break;
            case 4:
                maxDepth = 25;
                break;
            case 5:
                maxDepth = 12;
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
