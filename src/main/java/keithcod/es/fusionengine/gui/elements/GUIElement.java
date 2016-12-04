package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.core.Color;

import org.joml.Vector2i;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class GUIElement {

    public boolean show = true;

    public List<GUIElement> children = new ArrayList<>();

    public Vector2i position = new Vector2i(0,0);
    public Vector2i size = new Vector2i(50, 50);

    protected Color color = Color.RED;

//    private Mesh mesh;

    private int vao;

    private int vertVbo;
    private int colorVbo;
    private int indicesVbo;
    private int uvVbo;

    private float[] verts;
    private float[] colors;
    private int[] indices;
    private float[] uvs;

    protected Texture texture;

//    public void build(Window window){
//        build(window, texture != null);
//    }

   // public void build(Window window, boolean useTexture) {
   public void build(Window window){
//        System.out.println(color.toString());

//        glDisable(GL_TEXTURE_2D);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

//        System.out.println("(" + size.x + "/" + size.y + ")");

        float x1 = -1.0f + ((float)position.x / window.getWidth())*2;
        float y1 = 1.0f - ((float)position.y / window.getHeight())*2;

        float x2 = -1.0f + (((float)position.x+size.x) / window.getWidth())*2;
        float y2 = 1.0f - (((float)position.y+size.y) / window.getHeight())*2;

//        System.out.println("("+x1 + ":" + y1 + ", " + x2 + ":" + y2 + ")");

        verts = new float[]{
                x1,  y1, -1.0f,
                x1, y2, -1.0f,
                x2, y2, -1.0f,
                x2,  y1, -1.0f,
        };

        if(texture != null){
            uvs = new float[]{
                    0.0f,0.0f,
                    0.0f,1.0f,
                    1.0f,1.0f,
                    1.0f,0.0f
            };
        }else {
            Vector3f c = color.v3();

            colors = new float[]{
                    c.x, c.y, c.z,
                    c.x, c.y, c.z,
                    c.x, c.y, c.z,
                    c.x, c.y, c.z
            };
        }
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

        if (texture != null){
            // Texture coordinates VBO
            uvVbo = glGenBuffers();
            FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(uvs.length);
            textCoordsBuffer.put(uvs).flip();
            glBindBuffer(GL_ARRAY_BUFFER, uvVbo);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        }else {
            // Colour VBO
            colorVbo = glGenBuffers();
            FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colors.length);
            colourBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        }

        // Index VBO
        indicesVbo = glGenBuffers();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        for(GUIElement el : children)
            el.build(window);

    }

    public void render(ShaderProgram shader){

        shader.bind();

        shader.setUniform("xColor", color.v4());

        if(texture == null) {
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisable(GL_TEXTURE_2D);

            shader.setUniform("textured", 0.0f);

            System.out.println("Rendering untextured UI element (" + color.v4() + ")");
        }else{
            shader.setUniform("textured", 1.0f);

            glDepthMask(false);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

            glEnable(GL_TEXTURE_2D);
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());

            shader.setUniform("xTexture", 0);
        }

        // Draw the mesh
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        if(texture != null){
            glDisable(GL_BLEND);
            glDisable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);
            glDepthMask(true);
        }

        shader.unbind();

        for(GUIElement el : children)
            if(el.show)
                el.render(shader);
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

        if(texture != null)
            glDeleteBuffers(uvVbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);

    }

}

