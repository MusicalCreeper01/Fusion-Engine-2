package keithcod.es.fusionengine.core;

import keithcod.es.fusionengine.world.World;

public class Location {

    public float x = 0;
    public float y = 0;
    public float z = 0;

    public final World world;

    public Location (World world){
        this.world = world;
    }

    public Location (World world, int x, int y, int z){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
