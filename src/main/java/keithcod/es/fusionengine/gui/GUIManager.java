package keithcod.es.fusionengine.gui;

import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.core.Texture;
import keithcod.es.fusionengine.gui.elements.GUIElement;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    List<GUIElement> elements = new ArrayList<>();
    Window window;

    Texture texture;
    Mesh mesh;

    public GUIManager(){

    }

    public void add (GUIElement element){
        elements.add(element);
    }

    public void init(Window window){

        this.window = window;
        texture = new Texture(window.width, window.height);
    }

    public void build(){
        for(GUIElement el : elements)
            texture = el.build(texture);

        try {
            System.out.println(texture.getPixel(0, 0).toString());
        }catch(Exception ex){}
    }

    public void render(){
//        for(GUIElement el : elements)
//            if(el.show)
//                el.render();



    }

    public int texture(){
        return texture.openGL();
    }

}
