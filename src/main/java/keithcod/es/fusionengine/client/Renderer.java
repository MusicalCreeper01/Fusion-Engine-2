package keithcod.es.fusionengine.client;

import keithcod.es.fusionengine.client.engine.Utils;
import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.client.engine.rendering.FrameBuffer;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.client.engine.rendering.Transformation;
import keithcod.es.fusionengine.gui.GUIManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(75.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    private ShaderProgram postShaderProgram;
    private ShaderProgram guiShaderProgram;

    private Camera camera;
    private Window window;

    private final FrameBuffer frameBuffer;

    private GUIManager guiManager;

    public Renderer(Window window, Camera camera, GUIManager guiManager) {
        this.guiManager = guiManager;
        this.camera = camera;
        this.window = window;
        transformation = new Transformation();
        frameBuffer = new FrameBuffer();
    }

    public Transformation getTransformation (){
        return transformation;
    }

    Mesh mesh;

    boolean useFBO = true;

    public void init(Window window) throws Exception {
        if(useFBO)
            frameBuffer.init(window.getWidth(), window.getHeight());

        float size = 1.0f;
        float[] positions = new float[]{
                -size,  size, 0.0f, // upper left
                -size, -size, 0.0f, // lower left
                size, -size, 0.0f, // lower right
                size,  size, 0.0f, // upper right
        };

        float[] uvs = {
                0,1,
                0,0,
                1,0,
                1,1
        };

        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        if(useFBO)
            mesh = new Mesh(positions, uvs, indices, new Texture(frameBuffer.texture));

        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/program/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/program/fragment.fs"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        postShaderProgram = new ShaderProgram();
        postShaderProgram.createVertexShader(Utils.loadResource("/shaders/post/post.vert"));
        postShaderProgram.createFragmentShader(Utils.loadResource("/shaders/post/post.frag"));
        postShaderProgram.link();

        postShaderProgram.createUniform("gui_texture");

        guiShaderProgram = new ShaderProgram();
        guiShaderProgram.createVertexShader(Utils.loadResource("/shaders/program/gui.vert"));
        guiShaderProgram.createFragmentShader(Utils.loadResource("/shaders/program/gui.frag"));
        guiShaderProgram.link();

//        guiShaderProgram.createUniform("projectionMatrix");

        guiShaderProgram.createUniform("texture_sampler");


    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Matrix4f getViewMatrix(){
        return transformation.getViewMatrix(camera);
    }

    public Matrix4f getProjectionMatrix (Window window){
        return transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
    }


    Mesh box;
    public void render(Window window) {
        if(useFBO){
            if(window.isResized()) {
                frameBuffer.dispose();
                frameBuffer.resize(window.getWidth(), window.getHeight());
                window.setResized(false);
            }
            frameBuffer.bind();
        }else {
            if ( window.isResized() ) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }
            clear();
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
//        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);


//        world.render(shaderProgram);
        Client.game().getWorld().render(shaderProgram);



        if(box == null){
            try{
                box = Mesh.Box();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            glDisable(GL_CULL_FACE);
            Matrix4f viewMatrix = Client.game().getRenderer().getTransformation().getViewMatrix(Client.game().getCamera());
            Vector3f pos = Client.game().getPhysics().getBoxPosition();
            Matrix4f modelViewMatrix = Client.game().getRenderer().getTransformation().getBasicViewMatrix(new Vector3f(pos.x, pos.y, pos.z), new Vector3f(1, 1, 1), viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            box.render();
            glEnable(GL_CULL_FACE);
        }

        shaderProgram.unbind();


//        Client.game().getPhysics().drawDebug();

        if(useFBO) {
            frameBuffer.unbind(window.getWidth(), window.getHeight());

            postShaderProgram.bind();

//            glEnable(GL_BLEND);
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glEnable(GL_TEXTURE_2D);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, guiManager.texture());

            postShaderProgram.setUniform("gui_texture", 1);

            mesh.render();

//            glDisable(GL_BLEND);

            postShaderProgram.unbind();
        }


        /*guiShaderProgram.bind();

//        guiShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        guiShaderProgram.setUniform("texture_sampler", 0);
        glDisable(GL_CULL_FACE);
        guiManager.render();
        glEnable(GL_CULL_FACE);
        guiShaderProgram.unbind();*/



        /*glBegin(GL_QUADS);
        glVertex3f(-(window.width/2), -(window.height/2), 0);
        glTexCoord2f(0,0);
        glVertex3f((window.width/2), -(window.height/2), 0);
        glTexCoord2f(1,0);
        glVertex3f((window.width/2), (window.height/2), 0);
        glTexCoord2f(1,1);
        glVertex3f(-(window.width/2), (window.height/2), 0);
        glTexCoord2f(0,1);
        glEnd();*/
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        if(postShaderProgram != null)
            postShaderProgram.cleanup();
        frameBuffer.dispose();
    }
}
