package com.example.othello.Player;

import java.io.Serializable;


public class Human extends Player implements Serializable {

    public Human(){

    }

    public Human(String name, char field){
        super(name,field);
    }

}
