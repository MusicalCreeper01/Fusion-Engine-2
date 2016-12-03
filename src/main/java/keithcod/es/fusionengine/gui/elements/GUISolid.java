package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.core.Color;

public class GUISolid extends GUIElement {

    public GUISolid (Color color){
        super.color = color;
    }

    public GUISolid (Texture texture){
        super.texture = texture;
        super.color = Color.WHITE;
    }

    public GUISolid (Texture texture, Color color){
        super.texture = texture;
        super.color = color;
    }
}
