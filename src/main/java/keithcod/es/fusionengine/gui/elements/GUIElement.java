package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.core.Color;
import keithcod.es.fusionengine.core.Texture;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class GUIElement {

    public boolean show = true;

    public List<GUIElement> children = new ArrayList<>();

    public Vector2i position = new Vector2i(0,0);
    public Vector2i size = new Vector2i(50, 50);

    public Color color = Color.RED;

    private Texture texture;

    public Texture build(Texture tex) {
        texture = new Texture(size.x, size.y);

        for (int x = 0; x < size.x; ++x){
            for (int y = 0; y < size.y; ++y) {
                try {
                    texture.setPixel(color, x, y);
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.err.println("Error coloring element texture!");
                }
            }
        }

        for(GUIElement el : children)
            el.build(texture);

        if(tex != null)
            tex.combine(texture, position.x, position.y);

        return tex;
    }

    public void render(){
        for(GUIElement el : children)
            if(el.show)
                el.render();



    }

    public void dispose(){
        for(GUIElement el : children)
            el.dispose();


    }

}
