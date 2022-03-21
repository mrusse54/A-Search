/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ai;

/**
 *
 * @author Mitchell
 */
public class pair<x,y> { // Not sure if I am allowed to use javaFX.util.pairs so pair class is made
                    // this is needed because prio queue is natural order so equal F values 
                    // will not come out as fifo order
                    // using the example in class of clockwise check the the first one will have key of one
                    // next will have two etc so 1 < 2 one is out first if both have same value
   
    private int key; // key that will help with fifo order
    private Node node; // node 

    public pair(int key, Node node) { // constructor
        this.key = key;
        this.node = node;
    }
    
     public Node getNode() { //get the node
        return this.node;
    }

    public int getKey() { // get the key
        return this.key;
    }
    
    public String toString(){
return "Node: " + node + "_" + "key: " + key;
}
  
    public String fKey(){
return "Node: " + node + "_" + "Node F: " + node.getF() + "_" + "key: " + key;
}
}
