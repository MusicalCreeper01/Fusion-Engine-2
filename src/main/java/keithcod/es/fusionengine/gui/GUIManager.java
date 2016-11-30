package keithcod.es.fusionengine.gui;

import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.core.Texture;
import keithcod.es.fusionengine.gui.elements.GUIElement;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    private static GUIManager INSTANCE;

    List<GUIElement> elements = new ArrayList<>();
    Window window;

    Texture texture;
    Mesh mesh;

    public GUIManager(){
        INSTANCE = this;
    }

    public static GUIManager get(){
        return INSTANCE;
    }

    public void add (GUIElement element){
        elements.add(element);
    }

    public void init(Window window){

        this.window = window;
        texture = new Texture(window.width, window.height);
    }

    public void resize(){
        texture = new Texture(window.width, window.height);
        build();
    }

    public void build(){
        for(GUIElement el : elements)
            texture = el.build(texture);

        try {
            System.out.println(texture.getPixel(0, 0).toString());
        }catch(Exception ex){}
    }

    public int texture(){
        return texture.openGL();
    }

}
