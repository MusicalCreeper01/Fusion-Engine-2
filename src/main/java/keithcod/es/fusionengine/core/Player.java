package keithcod.es.fusionengine.core;

import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.entities.EntityPlayer;

import java.util.UUID;

public class Player extends EntityPlayer{

    public String name = "Kisu";

    public UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());

    Camera camera;

    public Player(Location pos){
        super(pos);

        camera = new Camera();

        rotation = new Rotation();
        position = pos;
    }

    @Override
    public void update(){
        super.update();
        camera.setPosition(position.x, position.y, position.z);
    }

    public void movePosition(float x, float y, float z){
        applyForce(x, y, z);
    }

    public void moveRotation(float x, float y, float z) {
        rotation.setX(rotation.getX() + x);
        rotation.setY(rotation.getY() + y);
        rotation.setZ(rotation.getZ() + z);

        camera.moveRotation(x, y, z);

        this.rotate(0, y, 0);
    }

    public Camera getCamera(){
        return camera;
    }

}
