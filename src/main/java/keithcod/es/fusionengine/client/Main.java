package keithcod.es.fusionengine.client;

import keithcod.es.fusionengine.client.engine.GameEngine;
import keithcod.es.fusionengine.client.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = false;
            IGameLogic gameLogic = new Client();
            GameEngine gameEng = new GameEngine("Client Engine",  600, 480, vSync, gameLogic);
            ((Client)gameLogic).window = gameEng.getWindow();
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}
