/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Mitchell
 */
public class star {

    public static Comparator<pair> fComp = new Comparator<pair>() { //changes the compare method to work with pair

        @Override
        public int compare(pair one, pair two) {

            int i = one.getNode().getF() - two.getNode().getF(); // calc F difference

            if (i == 0) { // if i = 0 so F are the same
                return (int) (one.getKey() - two.getKey()); // if the key for one is small then it will be returned first
                // if not then two is returned 
                // this will help with prio queue being natural order instead of fifo
            }

            return (int) (one.getNode().getF() - two.getNode().getF()); // return this is i is not 0 
        }

    };

    public static Node map[][] = new Node[15][15]; // [row][column]

    public static int test = 0;

    public static int userStartColumn; // user input for start

    public static int userStartRow; // user input for start

    public static int userGoalColumn; //  user input for goal

    public static int userGoalRow; // user input for goal

    public static ArrayList<Node> closedList = new ArrayList<Node>(); //closed list

    public static ArrayList<Node> path = new ArrayList<Node>();

    public static ArrayList<Node> openListTest = new ArrayList<Node>(); //

    public static PriorityQueue<pair> openList
            = new PriorityQueue<pair>(2, fComp); // prio queue is the built in min heap api, but it uses natural order so comparator is changed to account

    public static pair currentNode; //pair for the openlist

    public static pair testNode;

    public static boolean goalHit = false;

    public static int iter = 0;

    public static int keyValue = 0;

    public static Node printer;

    public static void main(String[] args) {

        boolean choice = false;

        generateBoard();

        do {

            resetBoard();

            displayBoard();

            keyValue = 0;

            goalHit = false;

            openList
                    = new PriorityQueue<pair>(2, fComp);

            path = new ArrayList<Node>();

            closedList = new ArrayList<Node>(); //closed list

            currentNode = null;

            printer = null;

            Scanner myScanner = new Scanner(System.in);
            System.out.println("Please enter the row and column, 0-14, of the starting node: ");
            System.out.println("Enter row:");
            userStartRow = myScanner.nextInt();
            System.out.println("Enter column:");
            userStartColumn = myScanner.nextInt();
            System.out.println("Please enter the row and column, 0-14, of the goal node: ");
            System.out.println("Enter row:");
            userGoalRow = myScanner.nextInt();
            System.out.println("Enter column:");
            userGoalColumn = myScanner.nextInt();

            ++keyValue;

            testNode = new pair(keyValue, map[userStartRow][userStartColumn]);

            testNode.getNode().setG(0);

            testNode.getNode().setParent(null);

            calcH(testNode.getNode());

            testNode.getNode().setF();

            openList.add(testNode);

            int p = 0;

            //now that start node is added to openlist begin while loop
            while (!openList.isEmpty() && !goalHit) {
                currentNode = openList.poll();
                goalChecker();
                if (goalHit) {
                    printPath();
                    break;
                }
                neighborFinder();
                closedList.add(currentNode.getNode());

            }

            if (goalHit == false) { // if goal is not hit prompt the user
                System.out.println("The goal was not reach.");
                System.out.println("Do you want to pick new nodes?");
                System.out.println("Enter Y to continue or N to quit");
                String userInput = myScanner.next();
                if (userInput.equalsIgnoreCase("Y")) { //if the user enters y for yes it still accepts it
                    choice = true;
                } else { //if anything else is entered
                    choice = false;
                }

            }

            if (goalHit == true) { // if goal is hit then do not restart
                choice = false;
            }

        } while (choice);

    }

    public static void resetBoard() { //you need to reset values for nodes on the board without changing anything else

        for (int row = 0; row < 15; row++) { // row
            for (int col = 0; col < 15; col++) { // column
                map[row][col].setG(0);
                map[row][col].setH(0);
                map[row][col].setParent(null);
                map[row][col].setF();
            }
        }

    }

    public static void generateBoard() {

        for (int row = 0; row < 15; row++) { // row
            for (int col = 0; col < 15; col++) { // column

                int x = (int) (Math.random() * (10 - 1 + 1) + 1); //generates values 1-10 inclusive

                if (x == 1) { // if = 1 1/10 chance

                    Node l = new Node(row, col, 1);

                    map[row][col] = l;

                } else {

                    Node O = new Node(row, col, 0);

                    map[row][col] = O;

                }
            }
        }
    }

    public static void displayBoard() {
        for (int row = 0; row < 15; row++) { //prints the board
            for (int col = 0; col < 15; col++) {
                if (col == 14) {
                    System.out.println(map[row][col].getT());
                } else {
                    System.out.print(map[row][col].getT() + ",");
                }

            }
        }
    }

    public static void calcH(Node n) { // gets the H value pretty from the current node to goal node set by user
        // no diagonal so easy to do

        int hCol = (userGoalColumn - n.getCol()) * 10;

        int hRow = (userGoalRow - n.getRow()) * 10;

        if (hCol <= -1) { // Math.abs was not giving negative so if is used
            hCol *= -1;
        }

        if (hRow <= -1) {
            hRow *= -1;
        }

        int h = hCol + hRow;

        n.setH(h);

    }

    public static void neighborFinder() { //Clockwise so `12,1.5,3,4.5,6,7.5,9,10.5 Matching this with the lecture order of nodes are added to open list

        int row = 0;

        int col = 0;

        row = currentNode.getNode().getRow();

        col = currentNode.getNode().getCol();

        if ((row - 1) >= 0) { //12

            row -= 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }
                    if (parentCheckerVertHori(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 10;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);

                    }
                }
            }
        }

        row = currentNode.getNode().getRow(); // end of 12

        if ((row - 1) >= 0 && (col + 1) <= 14) { // 1.5

            row -= 1;

            col += 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }
                    if (parentCheckerDia(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 14;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }
        }

        row = currentNode.getNode().getRow();

        col = currentNode.getNode().getCol(); // end of 1.5

        if ((col + 1) <= 14) { // 3

            col += 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }

                    if (parentCheckerVertHori(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 10;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }
        }

        col = currentNode.getNode().getCol(); //end of 3

        if ((row + 1) <= 14 && (col + 1) <= 14) { //4.5

            row += 1;

            col += 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());
                    }

                    if (parentCheckerDia(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 14;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }
        }

        row = currentNode.getNode().getRow();

        col = currentNode.getNode().getCol(); // end of 4.5

        if ((row + 1) <= 14) { //6

            row += 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }

                    if (parentCheckerVertHori(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 10;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);

                    }
                }
            }
        }

        row = currentNode.getNode().getRow(); // end of 6

        if ((row + 1) <= 14 && (col - 1) >= 0) { //7.5

            row += 1;

            col -= 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }
                    if (parentCheckerDia(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 14;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }
        }

        row = currentNode.getNode().getRow();

        col = currentNode.getNode().getCol(); // end of 7.5

        if ((col - 1) >= 0) { // 9

            col -= 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }

                    if (parentCheckerVertHori(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 10;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }
        }

        col = currentNode.getNode().getCol(); // end of 9

        if ((row - 1) >= 0 && (col - 1) >= 0) { // 10.5

            row -= 1;

            col -= 1;

            if (map[row][col].getT() == 0) {

                if (closedChecker(map[row][col])) {

                    if (map[row][col].getParent() == null) {

                        map[row][col].setParent(currentNode.getNode());

                    }
                    if (parentCheckerDia(map[row][col])) {

                        int i = currentNode.getNode().getG();

                        i += 14;

                        map[row][col].setG(i);

                        calcH(map[row][col]);

                        map[row][col].setF();

                        keyValue++;

                        pair p = new pair(keyValue, map[row][col]);

                        openList.add(p);
                    }
                }
            }

        }

        row = currentNode.getNode().getRow();

        col = currentNode.getNode().getCol();

    }

    public static void goalChecker() {

        if (currentNode.getNode().getRow() == userGoalRow && currentNode.getNode().getCol() == userGoalColumn) {
            System.out.println("Goal Reached");
            goalHit = true;

        } else {

        }

    }

    public static boolean parentCheckerDia(Node n) {

        if (!(currentNode.getNode().getRow() == n.getParent().getRow() && currentNode.getNode().getCol() == n.getParent().getCol())) {
            int pG = currentNode.getNode().getG() + 14; //test new possible G
            int cG = n.getG(); // get current G
            if (pG < cG) { // if possible G is bigger than current G
                int i = currentNode.getNode().getG();

                i += 14;

                n.setG(i);

                calcH(n);

                n.setF();
                n.setParent(currentNode.getNode());
                return false; // better G is  found
            } else {
                return false;// was not found
            }
        } else {
            return true;
        }

    }

    public static boolean parentCheckerVertHori(Node n) {

        if (!(currentNode.getNode().getRow() == n.getParent().getRow() && currentNode.getNode().getCol() == n.getParent().getCol())) {
            int pG = currentNode.getNode().getG() + 10; //test new possible G
            int cG = n.getG(); // get current G
            if (pG < cG) { // if possible G is bigger than current G
                int i = currentNode.getNode().getG();

                i += 10;

                n.setG(i);

                calcH(n);

                n.setF();

                n.setParent(currentNode.getNode());
                return false; // better G is  found
            } else {
                return false; //better G was not found
            }
        } else {
            return true;
        }

    }

    public static boolean closedChecker(Node n) {

        if (!closedList.isEmpty()) {
            for (int i = 0; i < closedList.size(); i++) {
                if (n.getRow() == closedList.get(i).getRow() && n.getCol() == closedList.get(i).getCol()) {
                    return false;
                }

            }
        }
        return true;
    }

    public static void printPath() {

        printer = currentNode.getNode();
        for (int i = 0; i < closedList.size(); i++) {
            path.add(printer);
            printer = printer.getParent();
            if (printer.getParent() == null) {
                path.add(printer);
                break;
            }

        }

        System.out.println("Path taken to get to goal node:");

        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.println(path.get(i));
        }
    }

}
