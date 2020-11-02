package com.gang.Trees;

import com.gang.Enums.*;
import com.gang.Nodes.Node;
import com.gang.Searching.Searching;
import java.util.*;

public final class SearchingTree {

    private Node root;
    private RootOfTreeMode rootOfTreeMode;
    private static Object synchronizedObject = new Object();
    private Stack<Node> stackStates = new Stack<>();
    private PriorityQueue<Node> queueStates = new PriorityQueue<>();
    private static ArrayList<Node> path = new ArrayList<>();
    private SearchingMode searchingMode;

    public SearchingTree(Byte[][] rootState, RootOfTreeMode rootOfTreeMode, SearchingMode searchingMode) {
        root = new Node(rootState, Action.NONE, null, 0, 0, searchingMode);
        root.findEmptyPosition();
        this.rootOfTreeMode = rootOfTreeMode;
        addNodeToThisTree(root);
        this.searchingMode = searchingMode;
        if(searchingMode.getValue() < 2)
            stackStates.push(root);
        else
            queueStates.add(root);
    }

    public void DFS() {
        while (!stackStates.empty()) {
            boolean hasOpened = false;
            Node v = stackStates.peek();
            stackStates.pop();
            synchronized (synchronizedObject) {
                if (!Searching.getFound()) {
                    System.out.println("Mode: " + rootOfTreeMode.getName() + ".BEGIN ITERATION\nSTACK: " + stackStates);
                    v.printNode();
                    if (containNodeAtThisTree(v) && v != root) {
                        hasOpened = true;
                        System.out.println("NODE WAS OPEN BEFORE!!");
                    } else {
                        addNodeToThisTree(v);
                        if (containNodeAtDualTree(v)) {
                            System.out.println("I FOUND!!!");
                            Searching.setToTrueFound();
                            makePath(v);
                            stackStates.clear();
                        }
                    }
                    addChildsToStack(v, hasOpened);
                    System.out.println("STACK: " + stackStates + "\n" + "Mode: " + rootOfTreeMode.getName() + ".END ITERATION\n");
                } else
                    stackStates.clear();
            }
        }
        synchronized (synchronizedObject){
            if (!Searching.getFound())
                System.out.println("I DID NOT FIND!! ------ mode: " + rootOfTreeMode.getName());
            System.out.println("I FINISHED ------ mode: " + rootOfTreeMode.getName() + "\n\n\n");
        }
    }

    public void algorithmAStar(){
        while (!queueStates.isEmpty()){
            boolean hasOpened = false;
            Node v = queueStates.peek();
            queueStates.poll();
            synchronized (synchronizedObject){
                if(!Searching.getFound()){
                    System.out.println("Mode: " + rootOfTreeMode.getName() + ".BEGIN ITERATION\nQUEUE: " + stackStates);
                    v.printNode();
                    if(containNodeAtThisTree(v) && v != root){
                        hasOpened = true;
                        System.out.println("NODE WAS OPEN BEFORE!!");
                    }
                    else {
                        addNodeToThisTree(v);
                        if(containNodeAtDualTree(v)){
                            System.out.println("I FOUND!!!");
                            Searching.setToTrueFound();
                            makePath(v);
                            queueStates.clear();
                        }
                    }
                    addChildsToQueue(v, hasOpened);
                    System.out.println("QUEUE: " + stackStates + "\n" + "Mode: " + rootOfTreeMode.getName() + ".END ITERATION\n");
                }
                else
                    queueStates.clear();
            }
        }
        synchronized (synchronizedObject){
            if (!Searching.getFound())
                System.out.println("I DID NOT FIND!! ------ mode: " + rootOfTreeMode.getName());
            System.out.println("I FINISHED ------ mode: " + rootOfTreeMode.getName() + "\n\n\n");
        }
    }

    private boolean containNodeAtThisTree(Node v){
        if(rootOfTreeMode == RootOfTreeMode.START)
            return Searching.containNodeAtStartTree(v);
        return Searching.containNodeAtFinishTree(v);
    }

    private boolean containNodeAtDualTree(Node v){
        if(rootOfTreeMode == RootOfTreeMode.START)
            return Searching.containNodeAtFinishTree(v);
        return Searching.containNodeAtStartTree(v);
    }

    private void addNodeToThisTree(Node v){
        if(rootOfTreeMode == RootOfTreeMode.START)
            Searching.addNodeToStartTree(v);
        else
            Searching.addNodeToFinishTree(v);
    }

    private void makePath(Node v){
        if(rootOfTreeMode == RootOfTreeMode.START)
            makePathIfFoundAtStartTree(v);
        else
            makePathIfFoundAtFinishTree(v);
    }

    private void makePathIfFoundAtStartTree(Node foundNode) {
        Node node = foundNode;
        while (node != null) {
            path.add(0, node);
            node = node.getParent();
        }

        Node equalNodeAtTreeFromFinish = Searching.getNodeFromFinishTree(foundNode);
        equalNodeAtTreeFromFinish = equalNodeAtTreeFromFinish.getParent();
        while (equalNodeAtTreeFromFinish != null) {
            path.add(equalNodeAtTreeFromFinish);
            equalNodeAtTreeFromFinish = equalNodeAtTreeFromFinish.getParent();
        }
    }

    private void makePathIfFoundAtFinishTree(Node foundNode) {
        Node equalNodeAtTreeFromStart = Searching.getNodeFromStartTree(foundNode);
        equalNodeAtTreeFromStart = equalNodeAtTreeFromStart.getParent();
        while (equalNodeAtTreeFromStart != null) {
            path.add(0, equalNodeAtTreeFromStart);
            equalNodeAtTreeFromStart = equalNodeAtTreeFromStart.getParent();
        }

        Node node = foundNode;
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
    }

    private void addChildsToStack(Node v, boolean hasOpened) {
        if (!hasOpened && !Searching.getFound() && v.getDepth() < searchingMode.getMaxDepth()) {
            if (v.openDownChild() != null)
                stackStates.push(v.getChilds()[Direction.DOWN.getValue()]);
            if (v.openRightChild() != null)
                stackStates.push(v.getChilds()[Direction.RIGHT.getValue()]);
            if (v.openTopChild() != null)
                stackStates.push(v.getChilds()[Direction.TOP.getValue()]);
            if (v.openLeftChild() != null)
                stackStates.push(v.getChilds()[Direction.LEFT.getValue()]);
        } else
            System.out.println("CHILD WILL NOT BE OPEN!!");
    }

    private void addChildsToQueue(Node v, boolean hasOpened) {
        if (!hasOpened && !Searching.getFound() && v.getDepth() < searchingMode.getMaxDepth()) {
            if (v.openDownChild() != null)
                queueStates.add(v.getChilds()[Direction.DOWN.getValue()]);
            if (v.openRightChild() != null)
                queueStates.add(v.getChilds()[Direction.RIGHT.getValue()]);
            if (v.openTopChild() != null)
                queueStates.add(v.getChilds()[Direction.TOP.getValue()]);
            if (v.openLeftChild() != null)
                queueStates.add(v.getChilds()[Direction.LEFT.getValue()]);
        } else
            System.out.println("CHILD WILL NOT BE OPEN!!");
    }

    public static ArrayList<Node> getPath() {
        return path;
    }
}
