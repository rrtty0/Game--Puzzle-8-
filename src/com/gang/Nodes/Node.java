package com.gang.Nodes;

import com.gang.Enums.*;
import com.gang.Searching.SearchingManager;

public final class Node implements Comparable{

    public static final Integer widthNode = 3;
    public static final Integer heightNode = 3;

    private Byte[][] data;
    private Byte[] emptyPosition;
    private Node parent;
    private Node[] childs = {null, null, null, null};
    private Action action;
    private Integer depth;
    private int heuristic = 0;
    private static SearchingMode searchingMode;
    private int evalutionFunction = 0;
    private int distance = 0;

    public Node(Byte[][] data, Action action, Node parent, Integer depth, Integer distance, SearchingMode searchingMode){
        this.data = data;
        this.action = action;
        this.parent = parent;
        this.depth = depth;
        this.searchingMode = searchingMode;
        if(searchingMode.getValue() >= 2){
            this.distance = distance;
            setHeuristic();
            evalutionFunction = distance + heuristic;
        }
    }

    public Node(Byte[][] data, Action action, Node parent, Byte[] emptyPosition, Integer depth, Integer distance){
        this.data = data;
        this.action = action;
        this.parent = parent;
        this.emptyPosition = emptyPosition;
        this.depth = depth;
        if(searchingMode.getValue() >= 2){
            this.distance = distance;
            setHeuristic();
            setEvalutionFunction();
        }
    }

    private boolean equalNodes(Byte[][] data){
        for(int i = 0; i < heightNode; ++i)
            for (int j = 0; j < widthNode; ++j){
                if(this.data[i][j] != data[i][j])
                    return false;
            }

        return true;
    }

    public Byte[] findEmptyPosition(){
        for(int i = 0; i < heightNode; ++i)
            for (int j = 0; j < widthNode; ++j){
                if(this.data[i][j] == 0){
                    emptyPosition = new Byte[]{(byte)i, (byte)j};
                    j = widthNode;
                    i = heightNode;
                }
            }

        return emptyPosition;
    }

    private void setHeuristic(){
        if(searchingMode.getValue() == 2 || searchingMode.getValue() == 3)
            setManhattanDistance();
        else
            if(searchingMode.getValue() <= 1)
                setCountOfNumbersOnStrangerPosition();
    }

    private void setEvalutionFunction(){
        evalutionFunction = Math.max(parent.evalutionFunction, distance + heuristic);
    }

    private void setManhattanDistance(){
        for(int i = 0; i < Node.heightNode; ++i)
            for(int j = 0; j < Node.widthNode; ++j)
                for(int i1 = 0; i1 < Node.heightNode; ++i1)
                    for(int j1 = 0; j1 < Node.widthNode; ++j1)
                        if(getData()[i][j] == SearchingManager.finishState[i1][j1])
                            heuristic += Math.abs((i - i1) + (j - j1));
    }

    private void setCountOfNumbersOnStrangerPosition(){
        for(int i = 0; i < Node.heightNode; ++i)
            for(int j = 0; j < Node.widthNode; ++j)
                if(getData()[i][j] != SearchingManager.finishState[i][j])
                    ++heuristic;
    }

    public void printNode(){
        for(int i = 0; i < widthNode; ++i){
            for(int j = 0; j < heightNode; ++j){
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("depth: " + depth);
        if (searchingMode.getValue() >= 2){
            System.out.println("g(v): " + distance);
            System.out.println("h(v): " + heuristic);
            System.out.println("f(v): " + evalutionFunction);
        }
    }

    public Byte[] getEmptyPosition(){
        return emptyPosition.clone();
    }

    public Action getAction() {
        return action;
    }

    public Byte[][] getData() {
        return data;
    }

    public Node getParent(){
        return parent;
    }

    public Node[] getChilds(){
        return childs;
    }

    public Integer getDepth(){
        return depth;
    }

    public Byte[][] copyData(){
        Byte[][] copyData = new Byte[heightNode][widthNode];

        for (int i = 0; i < heightNode; ++i)
            for (int j = 0; j < widthNode; ++j)
                copyData[i][j] = data[i][j];

        return copyData;
    }

    public Node openLeftChild() {
        Node leftChild = null;
        Byte[] newEmptyPosition = getEmptyPosition();
        if(newEmptyPosition[1] > 0 && action != Action.RIGHT)
        {
            Byte[][] newData = copyData();
            newData[newEmptyPosition[0]][newEmptyPosition[1]] = newData[newEmptyPosition[0]][newEmptyPosition[1] - 1];
            newData[newEmptyPosition[0]][newEmptyPosition[1] - 1] = 0;
            leftChild = new Node(newData, Action.LEFT, this, new Byte[]{newEmptyPosition[0], (byte)(newEmptyPosition[1] - 1)}, depth + 1, distance + 1);
        }
        childs[Direction.LEFT.getValue()] = leftChild;

        return leftChild;
    }

    public Node openTopChild(){
        Node topChild = null;
        Byte[] newEmptyPosition = getEmptyPosition();
        if(newEmptyPosition[0] > 0 && action != Action.DOWN)
        {
            Byte[][] newData = copyData();
            newData[newEmptyPosition[0]][newEmptyPosition[1]] = newData[newEmptyPosition[0] - 1][newEmptyPosition[1]];
            newData[newEmptyPosition[0] - 1][newEmptyPosition[1]] = 0;
            topChild = new Node(newData, Action.TOP, this, new Byte[]{(byte)(newEmptyPosition[0] - 1), newEmptyPosition[1]}, depth + 1, distance + 1);
        }
        childs[Direction.TOP.getValue()] = topChild;

        return topChild;
    }

    public Node openRightChild() {
        Node rightChild = null;
        Byte[] newEmptyPosition = getEmptyPosition();
        if(newEmptyPosition[1] < widthNode - 1 && action != Action.LEFT)
        {
            Byte[][] newData = copyData();
            newData[newEmptyPosition[0]][newEmptyPosition[1]] = newData[newEmptyPosition[0]][newEmptyPosition[1] + 1];
            newData[newEmptyPosition[0]][newEmptyPosition[1] + 1] = 0;
            rightChild = new Node(newData, Action.RIGHT, this, new Byte[]{newEmptyPosition[0], (byte)(newEmptyPosition[1] + 1)}, depth + 1, distance + 1);
        }
        childs[Direction.RIGHT.getValue()] = rightChild;

        return rightChild;
    }

    public Node openDownChild() {
        Node downChild = null;
        Byte[] newEmptyPosition = getEmptyPosition();
        if(newEmptyPosition[0] < Node.heightNode - 1 && action != Action.TOP)
        {
            Byte[][] newData = copyData();
            newData[newEmptyPosition[0]][newEmptyPosition[1]] = newData[newEmptyPosition[0] + 1][newEmptyPosition[1]];
            newData[newEmptyPosition[0] + 1][newEmptyPosition[1]] = 0;
            downChild = new Node(newData, Action.DOWN, this, new Byte[]{(byte)(newEmptyPosition[0] + 1), newEmptyPosition[1]}, depth + 1, distance + 1);
        }
        childs[Direction.DOWN.getValue()] = downChild;

        return downChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return equalNodes(node.data);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < heightNode; ++i)
            for (int j = 0; j < widthNode; ++j)
                hashCode += data[i][j]*Math.pow(10, widthNode*i + j);
        return hashCode;
    }

    @Override
    public int compareTo(Object o) {
        if(this == o)
            return 0;

        if(o == null)
            throw new NullPointerException();

        if(getClass() != o.getClass())
            throw new IllegalArgumentException();

        Node other = (Node) o;
        if(evalutionFunction < other.evalutionFunction)
            return -1;
        if(evalutionFunction > other.evalutionFunction)
            return 1;
        return 0;
    }

}
