package keithcod.es.fusionengine.core;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.beans.ConstructorProperties;

public class Color {

    public static Color WHITE = new Color(255, 255, 255);
    public static Color BLACK = new Color(255, 255, 255);

    public static Color RED = new Color(255, 0, 0);
    public static Color GREEN = new Color(0, 255, 0);
    public static Color BLUE = new Color(0, 0, 255);

    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int a = 0;

    @ConstructorProperties({"red", "green", "blue", "alpha"})
    public Color (int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @ConstructorProperties({"red", "green", "blue"})
    public Color (int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    public int getR(){
        return r;
    }

    public int getG(){
        return g;
    }

    public int getB(){
        return b;
    }

    public int getA(){
        return a;
    }

    public Vector4f v4 (){
        return new Vector4f(r/255.0f, g/255.0f, b/255.0f, a/255.0f);
    }

    public Vector3f v3 (){
        return new Vector3f(r/255.0f, g/255.0f, b/255.0f);
    }

    @Override
    public String toString(){
        return "(" + r + "," + g + "," + b + "," + a + ")";
    }
}
