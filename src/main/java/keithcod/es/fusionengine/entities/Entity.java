package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import keithcod.es.fusionengine.core.Location;
import keithcod.es.fusionengine.core.Rotation;
import keithcod.es.fusionengine.world.World;

import javax.vecmath.Vector3f;

public class Entity {

    public float mass = 1;

    public RigidBody rigidbody;

    protected Location position;
    protected Rotation rotation;

    public Entity(){

    }

    /*public Entity (Location position) {
        this.position = position;
        rigidbody = position.world.getPhysics().createRigidbody(new BoxShape(new Vector3f(1, 1, 1)), new Vector3f(position.x, position.y, position.z), 0f);

    }

    */
    public void update(){}

    public void move(Location loc){
        
    }

}
