package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.core.Color;
import keithcod.es.fusionengine.core.Texture;
import org.joml.Vector2i;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
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

//    private Mesh mesh;

    private int vao;

    private int vertVbo;
    private int colorVbo;
    private int indicesVbo;

    private float[] verts;
    private float[] colors;
    private int[] indices;

    public void build() {

        glDisable(GL_TEXTURE_2D);

        vao =  glGenVertexArrays();
        glBindVertexArray(vao);

        /*verts = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
        };

        indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        Vector3f c = color.v3();

        colors = new float[]{
                c.x, c.y, c.z,
                c.x, c.y, c.z,
                c.x, c.y, c.z,
                c.x, c.y, c.z
        };*/

        verts = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
        };
        Vector3f c = color.v3();

        colors = new float[]{
                c.x, c.y, c.z,
                c.x, c.y, c.z,
                c.x, c.y, c.z,
                c.x, c.y, c.z
        };
        indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Position VBO
        vertVbo = glGenBuffers();
        FloatBuffer posBuffer = BufferUtils.createFloatBuffer(verts.length);
        posBuffer.put(verts).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Colour VBO
        colorVbo = glGenBuffers();
        FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colors.length);
        colourBuffer.put(colors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Index VBO
        indicesVbo = glGenBuffers();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        for(GUIElement el : children)
            el.build();

    }

    public void render(){
        glDisable(GL_TEXTURE_2D);

        // Draw the mesh
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(GUIElement el : children)
            if(el.show)
                el.render();


    }

    public void dispose(){
        for(GUIElement el : children)
            el.dispose();

        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertVbo);
        glDeleteBuffers(colorVbo);
        glDeleteBuffers(indicesVbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);

    }

}

