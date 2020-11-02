package com.gang.Searching;

import com.gang.Enums.SearchingMode;

import java.util.Scanner;

public abstract class SearchingManager {

    public final static Byte[][] startState = {{6,0,8},{5,2,1},{4,3,7}};
    public final static Byte[][] finishState = {{1,2,3},{8,0,4},{7,6,5}};

    public static void start(){
        System.out.println("0) BLIND_DFS");
        System.out.println("1) BLIND_PARALLEL_DFS");
        System.out.println("2) HEURISTIC_MANHATTAN_SEARCHING");
        System.out.println("3) HEURISTIC_MANHATTAN_PARALLEL_SEARCHING");
        System.out.println("4) HEURISTIC_STRANGER_POSITION_SEARCHING");
        System.out.println("5) HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING");
        Scanner in = new Scanner(System.in);
        int variant;
        do{
            System.out.print("Choose variant: ");
            variant = in.nextInt();
            switch (variant){
                case 0:
                    new Searching(SearchingMode.BLIND_DFS);
                    break;
                case 1:
                    new Searching(SearchingMode.BLIND_PARALLEL_DFS);
                    break;
                case 2:
                    new Searching(SearchingMode.HEURISTIC_MANHATTAN_SEARCHING);
                    break;
                case 3:
                    new Searching(SearchingMode.HEURISTIC_MANHATTAN_PARALLEL_SEARCHING);
                    break;
                case 4:
                    new Searching(SearchingMode.HEURISTIC_STRANGER_POSITION_SEARCHING);
                    break;
                case 5:
                    new Searching(SearchingMode.HEURISTIC_PARALLEL_STRANGER_POSITION_SEARCHING);
                    break;
                default:
                    System.out.println("Try input variant again!");
            }
        }while (variant < 0 || variant > 5);

    }
}
