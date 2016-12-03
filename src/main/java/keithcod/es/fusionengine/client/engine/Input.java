package keithcod.es.fusionengine.client.engine;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input {

    private static Vector2d previousPos;

    private static Vector2d currentPos;

    private static Vector2f displVec;

    private static boolean inWindow = false;

    private static boolean leftButtonPressed = false;

    private static boolean rightButtonPressed = false;

    private static GLFWCursorPosCallback cursorPosCallback;

    private static GLFWCursorEnterCallback cursorEnterCallback;

    private static GLFWMouseButtonCallback mouseButtonCallback;

    private static GLFWKeyCallback keyCallback;

    private static Window window;

    Map<Integer, Boolean> key = new HashMap<>();
    Map<Integer, Boolean> keyDown = new HashMap<>();

    public static boolean lockMouse = false;

    public Input() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        this.window = window;
        glfwSetCursorPosCallback(window.getWindowHandle(), cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {

                currentPos.x = xpos;
                currentPos.y = ypos;

            }
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            }
        });
        glfwSetKeyCallback(window.getWindowHandle(), keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        });
    }

//    List<>

    public static Vector2f getDisplVec() {
        return displVec;
    }

    public static void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if(!lockMouse) {
            glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
                double deltax = currentPos.x - previousPos.x;
                double deltay = currentPos.y - previousPos.y;
                boolean rotateX = deltax != 0;
                boolean rotateY = deltay != 0;
                if (rotateX) {
                    displVec.y = (float) deltax;
                }
                if (rotateY) {
                    displVec.x = (float) deltay;
                }
            }
        }else{ // http://forum.lwjgl.org/index.php?topic=5597.0
            glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            double deltaX = currentPos.x - window.getWidth()/2;
            double deltaY = currentPos.y - window.getHeight()/2;

            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;
            if (rotateX) {
                displVec.y = (float) deltaX;
            }
            if (rotateY) {
                displVec.x = (float) deltaY;
            }

//            System.out.println("Delta X = " + deltaX + " Delta Y = " + deltaY);

            glfwSetCursorPos(window.getWindowHandle(), window.getWidth()/2, window.getHeight()/2);
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public static boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public static boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public static boolean isKeyPressed(int key)
    {
        return (glfwGetKey(window.getWindowHandle(), key) == GLFW_PRESS);
    }

    public static boolean isKeyReleased(int key)
    {
        return (glfwGetKey(window.getWindowHandle(), key) == GLFW_RELEASE);
    }
}
