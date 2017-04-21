package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.Engine;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
public class Main {
    public static void main(String[] args) {
        try {
            Engine engine = new Engine();
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
