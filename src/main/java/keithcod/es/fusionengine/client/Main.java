package keithcod.es.fusionengine.client;

import keithcod.es.fusionengine.client.engine.GameEngine;
import keithcod.es.fusionengine.client.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new Fusion();
            GameEngine gameEng = new GameEngine("Fusion Engine",  600, 480, vSync, gameLogic);
            ((Fusion)gameLogic).window = gameEng.getWindow();
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}
