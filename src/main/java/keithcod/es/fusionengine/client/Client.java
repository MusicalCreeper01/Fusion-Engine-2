package keithcod.es.fusionengine.client;

import com.google.gson.JsonSyntaxException;
import keithcod.es.fusionengine.client.engine.*;
import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.physics.Physics;
import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.gui.elements.GUISolid;
import keithcod.es.fusionengine.extentions.packs.Pack;
import keithcod.es.fusionengine.register.Registry;
import keithcod.es.fusionengine.world.World;
import keithcod.es.fusionengine.gui.GUIManager;
import keithcod.es.fusionengine.gui.elements.GUITruetypeText;
import org.joml.Vector2f;
import org.joml.Vector2i;


import javax.vecmath.Vector3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.glfw.GLFW.*;

public class Client implements IGameLogic {

    private static Client INSTANCE;

//    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float MOUSE_SENSITIVITY = 0.8f;
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

    private Physics physics;

    private boolean menu = true;

    private GUIManager guiManager;

//    private GUITruetypeText fpsCounter;

    public Client() {
        /*std::cout << "OpenGL version: " << glGetString(GL_VERSION) << std::endl;
        std::cout << "GLSL version: " << glGetString(GL_SHADING_LANGUAGE_VERSION) << std::endl;
        std::cout << "Vendor: " << glGetString(GL_VENDOR) << std::endl;
        std::cout << "Renderer: " << glGetString(GL_RENDERER) << std::endl;*/



        INSTANCE = this;
        camera = new Camera();
        camera.setPosition(0,15,0);
        camera.setRotation(0,90,0);
        guiManager = new GUIManager();

//        fpsCounter = new GUITruetypeText("Roboto-Thin.ttf", "Hello world!");

        cameraInc = new Vector3f(0, 0, 0);
        world = new World();
        physics = new Physics();
        renderer = new Renderer(window, camera, guiManager);

    }

    public Renderer getRenderer(){
        return renderer;
    }

    public Camera getCamera(){
        return camera;
    }

    public static Client game(){
        return INSTANCE;
    }

    public Physics getPhysics(){
        return physics;
    }

    public World getWorld(){
        return world;
    }

    GUITruetypeText fpsLabel;

    @Override
    public void init(Window window) throws Exception {
        System.out.println("\nOpenGL Version: " + glGetString(GL_VERSION));
        System.out.println("GLSL Version: " +     glGetString(GL_SHADING_LANGUAGE_VERSION));
        System.out.println("GLFW Version: " +     glfwGetVersionString());
        System.out.println("Vendor Version: " +   glGetString(GL_VENDOR));
        System.out.println("Renderer Version: " + glGetString(GL_RENDERER)+"\n");
        System.out.println("Extensions: " +       glGetString(GL_EXTENSIONS)+"\n");

        preInit();

        register();

        renderer.init(window);
        guiManager.init(window);

        fpsLabel = new GUITruetypeText("EncodeSans-Regular.ttf", "x fps", 48, 10, 0);
        guiManager.add(fpsLabel);

        GUISolid solid = new GUISolid(new Texture(Registry.getAtlas(0)));

        solid.position = new Vector2i(100, 70);
        solid.size = new Vector2i(48, 16);

        guiManager.add(solid);

//        guiManager.add(new GUITruetypeText("EncodeSans-Regular.ttf", "Hi!", 48, 10, 40));

        guiManager.build();

        /*world.generate();
        physics.init(window);*/

        postInit();

    }

    public void preInit(){

        List<Pack> packs = new ArrayList<Pack>();

        File folder = new File("packs/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].isDirectory()) {
                String s = listOfFiles[i] + "/pack.json";
                if(Files.exists(Paths.get(s))){
                    System.out.println("Attempting to load pack \"" + s + "\"");
                    try {
                        Pack pack = Pack.load(s);

                        System.out.println("Loaded pack! \"" + pack.name + "\" by \"" + pack.authors[0].name + "\"");
                        packs.add(pack);
                    }catch(FileNotFoundException ex){

                    }catch(JsonSyntaxException ex){
                        System.out.println("Failed to load pack \"" + s + "\"! " + ex.getMessage());
                    }
                }
            }
        }

        System.out.println("Building " + packs.size() + " packs...");

        for(Pack p : packs)
            p.build();

    }

    public void register(){
//        MaterialBlock.register();

    }

    public void postInit(){

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

    int i = 0;

    @Override
    public void update(double delta, Input input) {
        if(physics != null)
            physics.update(60.0f);

        if (!window.isKeyPressed(GLFW_KEY_Z) && !window.isKeyPressed(GLFW_KEY_X)) {
//            camera.setPosition(camera.getPosition().x, physics.getPlayerHeight(), camera.getPosition().z);
        }else{
//            physics.setPlayerPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z));
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP * (float)delta, cameraInc.y * CAMERA_POS_STEP * (float)delta, cameraInc.z * CAMERA_POS_STEP * (float)delta);

        // Update camera based on mouse
        if (input.isRightButtonPressed()) {
            Vector2f rotVec = input.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY * (float)delta, rotVec.y * MOUSE_SENSITIVITY * (float)delta, 0);
        }else{
//            camera.moveRotation(0, .5f, 0);
        }

        world.update();

        ++i;
        if(i > 60) {
            if(fpsLabel != null)
                fpsLabel.setText(Time.fps + " fps");
            i = 0;
        }
    }



    @Override
    public void render(Window window) {

        renderer.render(window);

    }

    @Override
    public void cleanup() {
        guiManager.cleanup();
        renderer.cleanup();
        if(gameItems != null) {
            for (GameObject gameItem : gameItems) {
                gameItem.getMesh().cleanUp();
            }
        }
    }
}
