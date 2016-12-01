package keithcod.es.fusionengine.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {

    private int width;
    private int height;

    public int[][] r;
    public int[][] g;
    public int[][] b;
    public int[][] a;

    private int id = -1;

    int pbo;

    public Texture(int width, int height){
        this.width = width;
        this.height = height;

        pbo = glGenBuffers();
        glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pbo);
        glBufferData(GL_PIXEL_UNPACK_BUFFER, (width*height*4)*Float.BYTES, GL_STREAM_COPY);
        glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);

        //pixels = new Color[width][height];
        r = new int[width][height];
        g = new int[width][height];
        b = new int[width][height];
        a = new int[width][height];
    }

    public void setPixel(Color color, int x, int y) throws Exception{
        if(x < 0 || y < 0 || x > width || y > height)
            throw new Exception("Cannot set pixel " + x + ":" + y + " which is out of the Texture bounds (" + width + ":" + height + ")");

//        System.out.println("Setting (" + x + ":" + y + ") to (" + color.getR() + "," + color.getG() + "," + color.getB() + "," + color.getA() + ") which was " + r[x][y] + "," + g[x][y] + "," + b[x][y] + "," + a[x][y] + ")");

        r[x][y] = color.getR();
        g[x][y] = color.getG();
        b[x][y] = color.getB();
        a[x][y] = color.getA();


    }

    public Color getPixel(int x, int y) throws Exception{
        if(x < 0 || y < 0 || x > width || y > height)
            throw new Exception("Cannot get pixel " + x + ":" + y + " which is out of the Texture bounds (" + width + ":" + height + ")");
        return new Color(r[x][y], g[x][y], b[x][y], a[x][y]);
    }

    public void combine(Texture texture, int xoffset, int yoffset){

        System.out.println("Combining texture (" + texture.width + ":" + texture.height + ") with (" + width + ":" + height + ")");

        int xbound = this.width < texture.width ? this.width : texture.width;
        int ybound = this.height < texture.height ? this.height : texture.height;

        int originx = 0;
        int originy = 0;

        for(int x = xoffset; x < xbound; ++x){
            for(int y = yoffset; y < ybound; ++y){

                try {
                    setPixel(texture.getPixel(originx, originy), x, y);
                }catch(Exception ex){
                    System.err.println(ex.getMessage());
                }

                ++originy;
            }
            originy = 0;
            ++originx;
        }
    }

    public int[] toIntArray(){
        int[] colors = new int[(width*height)*4];

        int i = 0;
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){

                colors[i] = r[x][y];
                colors[i+1] = g[x][y];
                colors[i+2] = b[x][y];
                colors[i+3] = a[x][y];



                i += 4;
            }
        }
        return colors;
    }

    public float[] toFloatArray(){
        float[] colors = new float[(width*height)*4];

        int i = 0;
        for(int y = height-1; y > 0; --y){
            for(int x = 0; x < width; ++x){

                colors[i] = (( r[x][y] * 1.0f) / 255);
                colors[i+1] = (( g[x][y] * 1.0f) / 255);
                colors[i+2] = (( b[x][y] * 1.0f) / 255);
                colors[i+3] = (( a[x][y] * 1.0f) / 255);

                i += 4;
            }
        }
        return colors;
    }



    public int openGL(boolean recreate){
        glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pbo);

        ByteBuffer bb = glMapBuffer(GL_PIXEL_UNPACK_BUFFER, GL_WRITE_ONLY);
        bb.asFloatBuffer().put(toFloatArray());
        glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER);

        if(id == -1 || recreate){
            id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_FLOAT, 0);//toFloatArray());

            glBindTexture(GL_TEXTURE_2D, 0);
        }else{
            glBindTexture(GL_TEXTURE_2D, id);

            glTexSubImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_FLOAT, 0);//toFloatArray());

            glBindTexture(GL_TEXTURE_2D, 0);
        }

        glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);

        return id;

    }

}
