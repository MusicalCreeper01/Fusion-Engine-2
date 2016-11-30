package keithcod.es.fusionengine.gui;

import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.gui.elements.GUIElement;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    List<GUIElement> elements = new ArrayList<>();
    Window window;

    public GUIManager(){

    }

    public void add (GUIElement element){
        elements.add(element);
    }

    public void init(Window window){
        this.window = window;
    }

    public void build(){
        for(GUIElement el : elements)
            el.build();
    }

    public void render(){
        for(GUIElement el : elements)
            if(el.show)
                el.render();
    }

}
