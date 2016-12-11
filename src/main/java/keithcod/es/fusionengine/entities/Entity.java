package keithcod.es.fusionengine.entities;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import keithcod.es.fusionengine.core.Location;
import keithcod.es.fusionengine.core.Rotation;

public class Entity {

    public float mass = 1;

    public RigidBody rigidbody;

    public Location position;
    public Rotation rotation;

    public Entity(){

    }

    public void update(){}



}
