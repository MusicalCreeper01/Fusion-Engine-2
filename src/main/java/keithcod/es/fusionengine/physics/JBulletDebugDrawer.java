package keithcod.es.fusionengine.physics;

import com.bulletphysics.linearmath.IDebugDraw;
import keithcod.es.fusionengine.client.Client;
import keithcod.es.fusionengine.client.engine.Utils;
import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import org.joml.Matrix4f;

import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


/**
 * Created by ldd20 on 11/29/2016.
 */
public class JBulletDebugDrawer extends IDebugDraw {

    ShaderProgram debugShader;
    Window window;

    public JBulletDebugDrawer (Window window){
        this.window = window;

        try {
            debugShader = new ShaderProgram();
            debugShader.createVertexShader(Utils.loadResource("/shaders/debug/debug.vert"));
            debugShader.createFragmentShader(Utils.loadResource("/shaders/debug/debug.frag"));
            debugShader.link();

            debugShader.createUniform("projectionMatrix");
            debugShader.createUniform("modelViewMatrix");
            debugShader.createUniform("color");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void drawLine(Vector3f from, Vector3f to, Vector3f color) {

        glDisable(GL_CULL_FACE);

        float lineCoordinates[] = { from.x, from.y, from.z,
                to.x, to.y, to.z};
        int vertexArray, vertexBuffer;

        glLineWidth(5.0f);

        vertexArray = glGenVertexArrays();
        vertexBuffer = glGenBuffers();

        glBindVertexArray(vertexArray);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);

        debugShader.bind();

        glBufferData(GL_ARRAY_BUFFER, lineCoordinates, GL_STATIC_DRAW);

        glVertexAttribPointer(
                0,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
                3,                  // size
                GL_FLOAT,           // type
                false,           // normalized?
                3*Float.BYTES, // stride
                0            // array buffer offset
        );

        debugShader.setUniform("projectionMatrix", Client.game().getRenderer().getProjectionMatrix(window));

        Matrix4f viewMatrix = Client.game().getRenderer().getTransformation().getViewMatrix(Client.game().getCamera());
        Matrix4f modelViewMatrix = Client.game().getRenderer().getTransformation().getBasicViewMatrix(new org.joml.Vector3f(0, 0, 0), new org.joml.Vector3f(1, 1, 1), viewMatrix);
        debugShader.setUniform("modelViewMatrix", modelViewMatrix);

        //Sends the sprite's color information in the the shader
        debugShader.setUniform("color", new org.joml.Vector4f(color.x, color.y, color.z, 1));

        glEnableVertexAttribArray(0);

        // Draw the line
        glDrawArrays(GL_LINES, 0, 2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        debugShader.unbind();

        glDisableVertexAttribArray(0);
        glDeleteBuffers(vertexBuffer);
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArray);

        glEnable(GL_CULL_FACE);
    }

    @Override
    public void drawAabb(Vector3f from, Vector3f to, Vector3f color) {
        super.drawAabb(from, to, color);

    }

    @Override
    public void drawContactPoint(Vector3f vector3f, Vector3f vector3f1, float v, int i, Vector3f vector3f2) {

    }

    @Override
    public void reportErrorWarning(String s) {
        System.err.println("[JBullet] " + s);
    }

    @Override
    public void draw3dText(Vector3f vector3f, String s) {

    }

    int debugMode = 0;

    @Override
    public void setDebugMode(int i) {
        debugMode = i;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }
}
