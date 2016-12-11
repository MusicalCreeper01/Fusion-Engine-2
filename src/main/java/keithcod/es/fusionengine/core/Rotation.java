package keithcod.es.fusionengine.core;

/**
 * Created by ldd20 on 12/5/2016.
 */
public class Rotation {

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        check();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        check();
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        check();
    }

    protected float x = 0;
    protected float y = 0;
    protected float z = 0;

    public Rotation() {  }

    public Rotation (int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        check();
    }

    public void check(){
        /*x = check(x);
        y = check(y);
        z = check(z);*/
    }

    public float check(float i){
        if(i > 360)
            i = 0;
        if(i < 0)
            i = 360;
        return i;
    }
}
