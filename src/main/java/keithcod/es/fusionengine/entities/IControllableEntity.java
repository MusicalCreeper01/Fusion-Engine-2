package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.character.KinematicCharacterController;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public interface IControllableEntity {

    void control(boolean control);

    void move (Vector3f pos);

    void rotate (Quat4f rotation);

    void physics (DynamicsWorld physicsWorld);

    GhostObject getPhysicsBody();
    KinematicCharacterController getPhysicsController();

}
