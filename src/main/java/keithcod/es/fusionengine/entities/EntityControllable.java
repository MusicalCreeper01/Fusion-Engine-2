package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;


public class EntityControllable extends Entity implements IControllableEntity {

    boolean canJump = true;

    int speed = 10;

    int height = 2;
    int width = 1;

    RigidBody rigidbody = null;

    public EntityControllable(){

    }

    public void move (Vector3f pos){

    }

    public void rotate (Quat4f rot){

    }

    public void control (boolean control){

    }

    GhostObject ghost;
    KinematicCharacterController controller;

    public GhostObject getPhysicsBody (){
        return ghost;
    }

    public KinematicCharacterController getPhysicsController(){
        return controller;
    }

    public void physics (DynamicsWorld physicsWorld){
        Transform defaultTranform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1.0f));
        /*CapsuleShape shape = new CapsuleShape(width, height);
        CollisionShape collisionShape = shape;

        float mass = 1;
        Vector3f fallInertia = new Vector3f(0,0,0);
        collisionShape.calculateLocalInertia(mass,fallInertia);

        DefaultMotionState motionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 20, 0), 1.0f)));

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(mass,motionState,shape,fallInertia);
        rigidbody = new RigidBody(fallRigidBodyCI);*/

        PairCachingGhostObject ghostObject =  new PairCachingGhostObject();
        ghostObject.setWorldTransform(defaultTranform);

        Vector3f worldMin = new Vector3f(-1000f,-1000f,-1000f);
        Vector3f worldMax = new Vector3f(1000f,1000f,1000f);
        AxisSweep3 sweepBP = new AxisSweep3(worldMin, worldMax);

        sweepBP.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());

        ConvexShape shape = new CapsuleShape(width, height);

        ghostObject.setCollisionShape(shape);
//        ghostObject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
        ghostObject.setCollisionFlags(CollisionFlags.KINEMATIC_OBJECT);

        float stepHeight = 2.5f;

        controller = new KinematicCharacterController(ghostObject, shape, stepHeight);



        physicsWorld.addCollisionObject(ghostObject, CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));
        physicsWorld.addAction(controller);

        ghost = ghostObject;


    }
}