/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ai;

/**
 *
 * @author Mitchell
 */
public class Node {
private int row, col, f, g, h, type;
private Node parent;
   
public Node(int r, int c, int t){
row = r;
col = c;
type = t;
parent = null;
//type 0 is traverseable, 1 is not
}

    Node() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
//mutator methods to set values
public void setF(){
f = g + h;
}
public void setG(int value){
g = value;
}
public void setH(int value){
h = value;
}
public void setParent(Node n){
parent = n;
}
//accessor methods to get values
public int getF(){
return f;
}
public int getG(){
return g;
}
public int getH(){
return h;
}
public Node getParent(){
return parent;
}
public int getRow(){
return row;
}
public int getCol(){
return col;
}
public boolean equals(Object in){
//typecast to Node
Node n = (Node) in;
return row == n.getRow() && col == n.getCol();
}
   
public String toString(){
return "Node: " + row + "_" + col;
}
public int getT(){ //added this because I do not see how I would solve without seeing if traversable
return type;
} 

}