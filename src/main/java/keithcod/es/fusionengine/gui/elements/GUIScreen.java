package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.Window;
import org.joml.Vector2i;

/**
 * Created by ldd20 on 11/29/2016.
 */
public class GUIScreen extends GUIElement {

    Window window;

    public GUIScreen(Window window){
        this.window = window;
        this.size = new Vector2i(window.getWidth(), window.getHeight());
    }

    /*@Override
    public void render(){
        super.render();


    }*/

}
