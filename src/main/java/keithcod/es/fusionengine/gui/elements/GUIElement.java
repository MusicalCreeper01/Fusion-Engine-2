package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.core.Color;
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

    public Color color = Color.WHITE;

    private int vbo;
    private int vao;
    private int vertexCount;

    public void build(){
        for(GUIElement el : children)
            el.build();


        System.out.println("Building element..");

        float[] positions = {
//            position.x, position.y, 0,
//            position.x + size.x, position.y, 0,
//            position.x + size.x, position.y + size.y, 0,
                0,0,0,
                0.2f,0,0,
                0.2f,0.2f,0

        };

        vertexCount = positions.length / 3;
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(positions.length);
        verticesBuffer.put(positions).flip();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);


    }

    public void render(){
        for(GUIElement el : children)
            if(el.show)
                el.render();

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

//        System.out.println("Rendering element..");

    }

    public void dispose(){
        for(GUIElement el : children)
            el.dispose();

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

}
