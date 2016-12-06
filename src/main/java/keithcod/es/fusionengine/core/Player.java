package keithcod.es.fusionengine.core;

import keithcod.es.fusionengine.client.engine.objects.Camera;
import keithcod.es.fusionengine.entities.EntityPhysical;
import keithcod.es.fusionengine.entities.EntityPlayer;

import javax.vecmath.Vector3f;
import java.util.UUID;

public class Player extends EntityPlayer{

    public String name = "KawaiiKisu";

    public UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());

//    EntityPlayer entity;
    Camera camera;

    public Player(Location pos){
        super(pos);

//        entity = new EntityPlayer(pos);
        camera = new Camera();

        rotation = new Rotation();
        setPosition(pos);
        setRotation(rotation);
    }

    @Override
    public void update(){
        super.update();

        setPosition(this.position);
        setRotation(this.rotation);
    }

    public void setPosition(Location location){
        this.position = location;

        //entity.move(new Vector3f(location.x, location.y, location.z));
        camera.setPosition(location.x, location.y, location.z);
    }

    public void movePosition(float x, float y, float z){
        position.x += x;
        position.y += y;
        position.z += z;
        camera.movePosition(x, y, z);
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setRotation(Rotation rot) {
        rotation.x = rot.x;
        rotation.y = rot.y;
        rotation.z = rot.z;
    }

    public void moveRotation(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
        camera.moveRotation(x, y, z);
    }

    public Camera getCamera(){
        return camera;
    }

}
