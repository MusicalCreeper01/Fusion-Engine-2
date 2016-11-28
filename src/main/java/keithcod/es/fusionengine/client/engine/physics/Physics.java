package keithcod.es.fusionengine.client.engine.physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Physics {

    DiscreteDynamicsWorld dynamicsWorld;
    RigidBody playerRigidBody;

    public Physics (){
        BroadphaseInterface broadphase = new DbvtBroadphase();
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);


        // setup our collision shapes
        CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 5);
        CollisionShape fallShape = new SphereShape(1);

// setup the motion state
        DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, -1, 0), 1.0f)));

        RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0,0,0));
        RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);

        dynamicsWorld.addRigidBody(groundRigidBody); // add our ground to the dynamic world..

// setup the motion state for the ball
        DefaultMotionState fallMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1.0f)));

//This we're going to give mass so it responds to gravity
        int mass = 1;

        Vector3f fallInertia = new Vector3f(0,0,0);
        fallShape.calculateLocalInertia(mass,fallInertia);

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(mass,fallMotionState,fallShape,fallInertia);
        playerRigidBody = new RigidBody(fallRigidBodyCI);

//now we add it to our physics simulation
        dynamicsWorld.addRigidBody(playerRigidBody);
    }

    public void run(){
        dynamicsWorld.stepSimulation(1/60.f, 10);

        Transform trans = new Transform();
        playerRigidBody.getMotionState().getWorldTransform(trans);

        System.out.println("player height: " + trans.origin.y);
    }

    public float getHeight(){
        Transform trans = new Transform();
        playerRigidBody.getMotionState().getWorldTransform(trans);
        return trans.origin.y;
    }

    public void setHeight(float height){

        Transform trans = new Transform();
        playerRigidBody.getCenterOfMassTransform(trans);
        trans.origin.y = height;
        playerRigidBody.setCenterOfMassTransform(trans);
    }
}
