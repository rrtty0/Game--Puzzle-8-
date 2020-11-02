package com.gang.Searching;

import com.gang.Enums.RootOfTreeMode;
import com.gang.Enums.SearchingMode;
import com.gang.Nodes.Node;
import com.gang.Trees.SearchingTree;
import java.util.ArrayList;
import java.util.Hashtable;

public final class Searching {

    private static Hashtable<Integer, Node> openedNodesFromStart = new Hashtable<>();
    private static Hashtable<Integer, Node> openedNodesFromFinish = new Hashtable<>();
    private static boolean found = false;

    public Searching(SearchingMode searchingMode){
        SearchingTree treeFromStart = new SearchingTree(SearchingManager.startState, RootOfTreeMode.START, searchingMode);
        SearchingTree treeFromFinish = new SearchingTree(SearchingManager.finishState, RootOfTreeMode.FINISH, searchingMode);

        System.out.println("\nLOG:\n");
        switch (searchingMode){
            case BLIND_DFS:
                treeFromStart.DFS();
                break;

            case BLIND_PARALLEL_DFS:
                runParallelSearching(treeFromStart, treeFromFinish, searchingMode);
                break;

            case HEURISTIC_MANHATTAN_SEARCHING:
                treeFromStart.algorithmAStar();
                break;

            case HEURISTIC_MANHATTAN_PARALLEL_SEARCHING:
                runParallelSearching(treeFromStart, treeFromFinish, searchingMode);
                break;

            case HEURISTIC_STRANGER_POSITION_SEARCHING:
                treeFromStart.algorithmAStar();
                break;

            case HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING:
                runParallelSearching(treeFromStart, treeFromFinish, searchingMode);
                break;
        }

        outputResult();
    }

    private void runParallelSearching(SearchingTree treeFromStart, SearchingTree treeFromFinish, SearchingMode searchingMode){
        Runnable taskForTreeFromStart = new Runnable() {
            @Override
            public void run() {
                if(searchingMode == SearchingMode.BLIND_PARALLEL_DFS)
                    treeFromStart.DFS();
                else
                    if(searchingMode == SearchingMode.HEURISTIC_MANHATTAN_PARALLEL_SEARCHING
                    || searchingMode == SearchingMode.HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING)
                        treeFromStart.algorithmAStar();
            }
        };
        Runnable taskForTreeFromFinish = new Runnable() {
            @Override
            public void run() {
                if(searchingMode == SearchingMode.BLIND_PARALLEL_DFS)
                    treeFromFinish.DFS();
                else
                if(searchingMode == SearchingMode.HEURISTIC_MANHATTAN_PARALLEL_SEARCHING
                        || searchingMode == SearchingMode.HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING)
                    treeFromFinish.algorithmAStar();
            }
        };
        Thread searchFromStart = new Thread(taskForTreeFromStart);
        Thread searchFromFinish = new Thread(taskForTreeFromFinish);
        searchFromStart.start();
        searchFromFinish.start();
        try {
            searchFromStart.join();
            searchFromFinish.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void outputResult(){
        if (!getFound())
            System.out.println("RESULT: JOINT NODE DID NOT FIND!!");
        else {
            System.out.println("RESULT PATH:");
            ArrayList<Node> resultPath = SearchingTree.getPath();
            for (Node node : resultPath) {
                node.printNode();
                System.out.println();
            }
            System.out.println("PATH LENGTH: " + resultPath.size());
        }
    }

    public static void addNodeToStartTree(Node node) {
        openedNodesFromStart.putIfAbsent(node.hashCode(), node);
    }

    public static boolean containNodeAtStartTree(Node node) {
        if (openedNodesFromStart.get(node.hashCode()) != null)
            return true;
        return false;
    }

    public static void addNodeToFinishTree(Node node) {
        openedNodesFromFinish.putIfAbsent(node.hashCode(), node);
    }

    public static boolean containNodeAtFinishTree(Node node) {
        if (openedNodesFromFinish.get(node.hashCode()) != null)
            return true;
        return false;
    }

    public static void setToTrueFound() {
        found = true;
    }

    public static boolean getFound() {
        return found;
    }

    public static Node getNodeFromStartTree(Node nodeFromFinishTree) {
        return openedNodesFromStart.get(nodeFromFinishTree.hashCode());
    }

    public static Node getNodeFromFinishTree(Node nodeFromStartTree) {
        return openedNodesFromFinish.get(nodeFromStartTree.hashCode());
    }
}