package com.schackteleers.projectrpg.game;

import com.schackteleers.projectrpg.engine.core.Engine;
import com.schackteleers.projectrpg.engine.core.IGameLogic;
import org.lwjgl.system.Library;
import org.lwjgl.system.SharedLibrary;

/**
 * @author Stijn Schack
 * @since 21/04/2017
 */
public class Main {
    public static void main(String[] args) {

        IGameLogic game = new Game();
        try {
            Engine engine = new Engine(game);
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
