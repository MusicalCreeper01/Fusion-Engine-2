package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.core.Color;

public class GUISolid extends GUIElement {

    public GUISolid (Color color){
        this.color = color;
    }

    public GUISolid (Texture texture){
        this.texture = texture;
        this.color = Color.WHITE;
    }

    public GUISolid (Texture texture, Color color){
        this.texture = texture;
        this.color = color;
    }


}
