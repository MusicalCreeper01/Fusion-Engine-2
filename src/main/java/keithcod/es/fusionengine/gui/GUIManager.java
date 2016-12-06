package keithcod.es.fusionengine.gui;

import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.gui.elements.GUIElement;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GUIManager {

    private static GUIManager INSTANCE;

    List<GUIElement> elements = new ArrayList<>();
    Window window;

//    Mesh mesh;

    public GUIManager(){
        INSTANCE = this;
    }

    public static GUIManager get(){
        return INSTANCE;
    }

    public void add (GUIElement element){
        elements.add(element);
        build();
    }

    public void init(Window window){

        this.window = window;
    }

    public void resize(){
        build();

    }

    public void build(){
        for(GUIElement el : elements)
            el.build(window);
    }

    public void render (ShaderProgram shader){
//        glDisable(GL_DEPTH_TEST);

        for(GUIElement el : elements)
            el.render(shader);
        shader.unbind();
//        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup (){
        for(GUIElement el : elements)
            el.dispose();
    }

}
