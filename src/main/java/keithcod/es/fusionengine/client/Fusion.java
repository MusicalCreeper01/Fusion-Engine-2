package keithcod.es.fusionengine.client;

import keithcod.es.fusionengine.client.engine.GameObject;
import keithcod.es.fusionengine.client.engine.IGameLogic;
import keithcod.es.fusionengine.client.engine.Input;
import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.client.engine.rendering.FrameBuffer;
import keithcod.es.fusionengine.enviroment.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Fusion implements IGameLogic {

    private static Fusion INSTANCE;

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private final Renderer renderer;

    private final Camera camera;
    private final Vector3f cameraInc;

    private GameObject[] gameItems;

    public World world;
    public  Window window;



    public Fusion() {

        INSTANCE = this;
        camera = new Camera();
        camera.setPosition(0,2,0);
        camera.setRotation(0,90,0);
        renderer = new Renderer(window, camera);
        cameraInc = new Vector3f(0, 0, 0);
        world = new World();

    }

    public Renderer getRenderer(){
        return renderer;
    }

    public Camera getCamera(){
        return camera;
    }

    public static Fusion game(){
        return INSTANCE;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        world.generate();

    }

    @Override
    public void input(Window window, Input mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, Input input) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (input.isRightButtonPressed()) {
            Vector2f rotVec = input.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }else{
//            camera.moveRotation(0, .5f, 0);
        }

        world.update();
    }

    @Override
    public void render(Window window) {
        //renderer.render(window, camera, gameItems);

        renderer.render(window, world);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        if(gameItems != null) {
            for (GameObject gameItem : gameItems) {
                gameItem.getMesh().cleanUp();
            }
        }
    }
}
