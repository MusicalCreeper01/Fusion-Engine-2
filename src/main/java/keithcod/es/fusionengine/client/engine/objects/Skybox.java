package keithcod.es.fusionengine.client.engine.objects;

import de.matthiasmann.twl.utils.PNGDecoder;
import keithcod.es.fusionengine.client.Client;
import keithcod.es.fusionengine.client.engine.Utils;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Texture;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Skybox {

    int textureid;

    int vbo;
    int vao;

    public static ShaderProgram shader;

    float size = 10f;


    float cube_vertices[] = {  // Coordinates for the vertices of a cube.
            // front
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            // back
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
    };

    int cube_indices[] = {
            // front
            2, 1, 0,
            0, 3, 2,
            // top
            6, 5, 1,
            1, 2, 6,
            // back
            5, 6, 7,
            7, 4, 5,
            // bottom
            3, 0, 4,
            4, 7, 3,
            // left
            1, 5, 4,
            4, 0, 1,
            // right
            6, 2, 3,
            3, 7, 6,
    };

    Mesh mesh;

    public Skybox (String[] textures){
        if(textures.length < 5){
            System.err.println("Skybox initialized with texture array of a length < 6");
            return;
        }

        PNGDecoder[] decoders = new PNGDecoder[6];
        ByteBuffer[] buffers = new ByteBuffer[6];
        int i = 0;
        for(String s : textures){
            try {
                decoders[i] = new PNGDecoder(new FileInputStream(s));

                // Load texture contents into a byte buffer
                ByteBuffer buf = ByteBuffer.allocateDirect(
                        4 * decoders[i].getWidth() * decoders[i].getHeight());
                decoders[i].decode(buf, decoders[i].getWidth() * 4, PNGDecoder.Format.RGBA);
                buf.flip();

                buffers[i] = buf;

                System.out.println("Loaded skybox texture " + s + "...");
            }catch(Exception ex){
                System.err.println("Error loading skybox texture " + i + ": " + ex.getMessage());
            }
            ++i;
        }

        textureid = glGenTextures();

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureid);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, decoders[0].getWidth(), decoders[0].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[0]);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, decoders[1].getWidth(), decoders[1].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[1]);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, decoders[2].getWidth(), decoders[2].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[2]);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, decoders[3].getWidth(), decoders[3].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[3]);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, decoders[4].getWidth(), decoders[4].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[4]);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, decoders[5].getWidth(), decoders[5].getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffers[5]);



        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        System.out.println("Created skybox!");


        try {
            shader = new ShaderProgram();
            shader.createVertexShader(Utils.loadResource("/shaders/program/skybox.vert"));
            shader.createFragmentShader(Utils.loadResource("/shaders/program/skybox.frag"));
            shader.link();

            shader.createUniform("modelViewMatrix");

        }catch(Exception ex){
            ex.printStackTrace();
        }


//        int PVM    = glGetUniformLocation(shader.programId, "PVM");
//        int vertex = glGetAttribLocation(shader.programId, "vertex");


        /*int vbo_cube_vertices = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_cube_vertices);
        glBufferData(GL_ARRAY_BUFFER, cube_vertices, GL_STATIC_DRAW);
        //glBindBuffer(GL_ARRAY_BUFFER, 0);

        // cube indices for index buffer object

        int ibo_cube_indices = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo_cube_indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, cube_indices, GL_STATIC_DRAW);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glEnableVertexAttribArray(vertex);
        glVertexAttribPointer(vertex, 3, GL_FLOAT, false, 0, 0);*/

    mesh = new Mesh(cube_vertices, null, cube_indices, null);

    }

    public void render(){
        //glDrawElements(GL_QUADS, cube_indices.length, GL_UNSIGNED_SHORT, 0);

        glDepthMask(false);
        shader.bind();

        Matrix4f viewMatrix = Client.game().getRenderer().getTransformation().getViewMatrix(Client.game().thePlayer().getCamera());
        viewMatrix.m30 = 0;
        viewMatrix.m31 = 0;
        viewMatrix.m32 = 0;

        Matrix4f modelViewMatrix = Client.game().getRenderer().getTransformation().getBasicViewMatrix(new org.joml.Vector3f(0, 0, 0), new org.joml.Vector3f(20, 20, 20), viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureid);

        mesh.render();
        shader.unbind();
        glDepthMask(true);
    }

}
