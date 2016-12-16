package keithcod.es.fusionengine.client;

import keithcod.es.fusionengine.client.engine.Utils;
import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.client.engine.objects.Skybox;
import keithcod.es.fusionengine.client.engine.rendering.FrameBuffer;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Transformation;
import keithcod.es.fusionengine.gui.GUIManager;
import org.joml.Matrix4f;

import javax.vecmath.Vector3f;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {

    /**
     * Field of View in Radians
     */
    public static final float FOV = (float) Math.toRadians(75.0f);

    public static final float Z_NEAR = 0.01f;

    public static final float Z_FAR = 200.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    private ShaderProgram postShaderProgram;
    private ShaderProgram guiShaderProgram;

//    private Camera camera;
    private Window window;

    private final FrameBuffer frameBuffer;
    private final FrameBuffer depthFrameBuffer;

    private GUIManager guiManager;

    //private Skybox skybox;

    public Renderer(Window window, GUIManager guiManager) {
        this.guiManager = guiManager;
//        this.camera = camera;
        this.window = window;
        transformation = new Transformation();
        frameBuffer = new FrameBuffer();
        depthFrameBuffer = new FrameBuffer();
    }

    public Transformation getTransformation (){
        return transformation;
    }

    Mesh mesh;

    boolean useFBO = false;

    int RAND_MAX = 32767;
    Vector3f[] kernel;

    public float rand (){
        Random r = new Random();
        return r.nextInt();
    }

    //Mesh m;

    public void init(Window window) throws Exception {
        if(useFBO) {
            frameBuffer.init(window.getWidth(), window.getHeight());
            depthFrameBuffer.init(window.getWidth(), window.getHeight());
        }

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
            mesh = new Mesh(positions, uvs, indices, null);//new Texture(frameBuffer.colortexture));

        //skybox = new Skybox(new String[] {"skybox2.png", "skybox2.png", "skybox2_top.png", "skybox2_bottom.png", "skybox2.png", "skybox2.png"});

        //m = Mesh.BasicBox();

        int KERNEL_SIZE = 128;
        kernel = new Vector3f[KERNEL_SIZE];


        for (int i = 0 ; i < KERNEL_SIZE ; i++ ) {
            float scale = (float)i / (float)(KERNEL_SIZE);
            Vector3f v = new Vector3f();
            v.x = 2.0f * (float)rand()/RAND_MAX - 1.0f;
            v.y = 2.0f * (float)rand()/RAND_MAX - 1.0f;
            v.z = 2.0f * (float)rand()/RAND_MAX - 1.0f;
            // Use an acceleration function so more points are
            // located closer to the origin
            v.x *= (0.1f + 0.9f * scale * scale);
            v.y *= (0.1f + 0.9f * scale * scale);
            v.z *= (0.1f + 0.9f * scale * scale);

            kernel[i] = v;
        }

        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/program/simple.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/program/block.frag"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        postShaderProgram = new ShaderProgram();
        postShaderProgram.createVertexShader(Utils.loadResource("/shaders/post/post.vert"));
        postShaderProgram.createFragmentShader(Utils.loadResource("/shaders/post/post.frag"));
        postShaderProgram.link();

        postShaderProgram.createUniform("colorbuffer");
        postShaderProgram.createUniform("depthbuffer");
        postShaderProgram.createUniform("gKernel");

        guiShaderProgram = new ShaderProgram();
        guiShaderProgram.createVertexShader(Utils.loadResource("/shaders/program/gui.vert"));
        guiShaderProgram.createFragmentShader(Utils.loadResource("/shaders/program/gui.frag"));
        guiShaderProgram.link();

//        guiShaderProgram.createUniform("projectionMatrix");

        guiShaderProgram.createUniform("xTexture");
        guiShaderProgram.createUniform("xColor");
        guiShaderProgram.createUniform("textured");


    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public Matrix4f getViewMatrix(){
        return transformation.getViewMatrix(Client.game().thePlayer().getCamera());
    }

    public Matrix4f getProjectionMatrix (Window window){
        return transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
    }

    /*public void render(Window window) {
//        render(window, camera);
    }*/

//    Mesh box;
    public void render(Window window) {
//        this.camera = cam;

        if(useFBO){
            if(frameBuffer.disposed)
                return;
            if(window.isResized()) {
                frameBuffer.resize(window.getWidth(), window.getHeight());
                guiManager.resize();
                window.setResized(false);
            }
            frameBuffer.bind();
        }else {
            if ( window.isResized() ) {
                guiManager.resize();
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }
            clear();
        }

        //skybox.render();

        shaderProgram.bind();

        // Update projection Matrix
        //Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        //shaderProgram.setUniform("projectionMatrix", getProjectionMatrix(window));
        shaderProgram.setUniform("texture_sampler", 0);


        Matrix4f viewMatrix = Client.game().getRenderer().getTransformation().getViewMatrix(Client.game().thePlayer().getCamera());
        Matrix4f modelViewMatrix = Client.game().getRenderer().getTransformation().getBasicViewMatrix(new org.joml.Vector3f(0, 15, 0), new org.joml.Vector3f(1, 1, 1), viewMatrix);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

        //m.render();

        Client.game().getWorld().render(shaderProgram);

        shaderProgram.unbind();


//        Client.game().getPhysics().drawDebug();

        if(useFBO) {
            frameBuffer.unbind(window.getWidth(), window.getHeight());

            depthFrameBuffer.bind();

            Client.game().getWorld().render(shaderProgram);

            depthFrameBuffer.unbind(window.getWidth(), window.getHeight());

            postShaderProgram.bind();

            postShaderProgram.setUniform("colorbuffer", 0);
            postShaderProgram.setUniform("depthbuffer", 1);
            postShaderProgram.setUniform("gKernel", kernel);

            mesh.render();

            postShaderProgram.unbind();
        }

        guiShaderProgram.bind();

        guiShaderProgram.setUniform("xTexture", 0);
        guiManager.render(guiShaderProgram);

        guiShaderProgram.unbind();
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
