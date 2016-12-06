package keithcod.es.fusionengine.physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DebugDrawModes;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import keithcod.es.fusionengine.client.engine.Window;
import keithcod.es.fusionengine.client.engine.objects.Mesh;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//http://gamedev.stackexchange.com/questions/41942/moving-the-jbullet-collision-body-to-with-the-player-object

public class Physics {

    protected DynamicsWorld dynamicsWorld = null;
    private RigidBody box;

    public Physics (){


    }


    JBulletDebugDrawer debugDraw;

    public void init(Window window){
        dynamicsWorld = createDynamicsWorld();

        debugDraw = new JBulletDebugDrawer(window);
        dynamicsWorld.setDebugDrawer(debugDraw);



        dynamicsWorld.setGravity(new Vector3f(0f, -10f, 0f));

        //box = createBoxBody();
//        box.setRestitution(0);

        //dynamicsWorld.addRigidBody(box);
        dynamicsWorld.addRigidBody(createGroundBody());
    }

    private DynamicsWorld createDynamicsWorld() {
        /*// collision configuration contains default setup for memory, collision setup
        DefaultCollisionConfiguration collisionConfiguration
                = new DefaultCollisionConfiguration();
        // calculates exact collision given a list possible colliding pairs
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        // the maximum size of the collision world. Make sure objects stay
        // within these boundaries. Don't make the world AABB size too large, it
        // will harm simulation quality and performance
        Vector3f worldAabbMin = new Vector3f(-1000, -1000, -1000);
        Vector3f worldAabbMax = new Vector3f( 1000,  1000,  1000);
        // maximum number of objects
        final int maxProxies = 1024;
        // Broadphase computes an conservative approximate list of colliding pairs
        BroadphaseInterface broadphase = new AxisSweep3(
                worldAabbMin, worldAabbMax, maxProxies);

        // constraint (joint) solver
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();*/

        BroadphaseInterface broadphase = new DbvtBroadphase();
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        // provides discrete rigid body simulation
        return new DiscreteDynamicsWorld(
                dispatcher, broadphase, solver, collisionConfiguration);
    }

    private RigidBody createBoxBody() {
        CollisionShape colShape = new BoxShape(new Vector3f(0.5f,0.5f,0.5f));
//        CollisionShape colShape = new SphereShape(Chunk.BLOCK_SIZE_HALF);

        float mass = 1f;

        Vector3f localInertia = new Vector3f(0, 0, 0);
        colShape.calculateLocalInertia(mass, localInertia);

        Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 80, 0), 1.0f));
        MotionState state = new DefaultMotionState(transform);

        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, state, colShape, localInertia);

        return new RigidBody(rbInfo);
    }

    private RigidBody createGroundBody() {
        // collision object is a horisantal plane located at y = 0
        Vector3f normal = new Vector3f(0, 1, 0);

        CollisionShape colShape = new StaticPlaneShape(normal, 0);

        // 0 mass means body is static
        float mass = 0f;

        Vector3f localInertia = new Vector3f(0, 0, 0);
        colShape.calculateLocalInertia(mass, localInertia);

        Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1.0f));
        MotionState state = new DefaultMotionState(transform);

        // create and return
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, state, colShape, localInertia);

        return new RigidBody(rbInfo);
    }

    public void update(float fps) {
        if(dynamicsWorld != null)
            dynamicsWorld.stepSimulation(1 / fps, 10);


//        Transform trans = new Transform();
//        trans = box.getWorldTransform(trans);
//        System.out.println(trans.origin.y);
    }

    public void drawDebug(){
        debugDraw.setDebugMode(DebugDrawModes.DRAW_WIREFRAME | DebugDrawModes.DRAW_AABB | DebugDrawModes.ENABLE_CCD);
        dynamicsWorld.debugDrawWorld();

//        System.out.println("Debug mode: " + debugDraw.getDebugMode());

        debugDraw.setDebugMode(DebugDrawModes.NO_DEBUG);

    }

    public org.joml.Vector3f getBoxPosition(){
        Transform trans = new Transform();
        trans = box.getWorldTransform(trans);

//        System.out.println(trans.origin.toString());

        return new org.joml.Vector3f(trans.origin.x, trans.origin.y, trans.origin.z);
    }

    public void addMesh(org.joml.Vector3f position, Mesh mesh){
        float[] coords = mesh.getVertices();
        int[] indices = mesh.getIndices();

        if (indices.length > 0) {

            IndexedMesh indexedMesh = new IndexedMesh();
            indexedMesh.numTriangles = indices.length / 3;
            indexedMesh.triangleIndexBase = ByteBuffer.allocateDirect(indices.length*Integer.BYTES).order(ByteOrder.nativeOrder());
            indexedMesh.triangleIndexBase.rewind();
            indexedMesh.triangleIndexBase.asIntBuffer().put(indices);
            indexedMesh.triangleIndexStride = 3 * Integer.BYTES;
            indexedMesh.numVertices = coords.length / 3;
            indexedMesh.vertexBase = ByteBuffer.allocateDirect(coords.length*Float.BYTES).order(ByteOrder.nativeOrder());
            indexedMesh.vertexBase.rewind();
            indexedMesh.vertexBase.asFloatBuffer().put(coords);
            indexedMesh.vertexStride = 3 * Float.BYTES;

            TriangleIndexVertexArray vertArray = new TriangleIndexVertexArray();
            vertArray.addIndexedMesh(indexedMesh);

            boolean useQuantizedAabbCompression = false;
            BvhTriangleMeshShape meshShape = new BvhTriangleMeshShape(vertArray, useQuantizedAabbCompression);
//            meshShape.setLocalScaling(new Vector3f(0.5f, 0.5f, 0.5f));

            CollisionShape collisionShape = meshShape;

            CollisionObject colObject = new CollisionObject();
            colObject.setCollisionShape(collisionShape);
            colObject.setWorldTransform(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(position.x, position.y, position.z), 1f)));
            dynamicsWorld.addCollisionObject(colObject);

        } else {
            System.err.println("Failed to extract geometry from model. ");
        }

    }

    /*DiscreteDynamicsWorld dynamicsWorld;
    EntityPlayer player;

    public Physics (){
        BroadphaseInterface broadphase = new DbvtBroadphase();
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        Vector3f gravityOut = new Vector3f(0, -1, 0);
        dynamicsWorld.setGravity(gravityOut);
        dynamicsWorld.getGravity(gravityOut);
        System.out.println("Gravity: " + gravityOut.x + "/" + gravityOut.y + "/" + gravityOut.z);

        // setup our collision shapes
        CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 5);
//        CollisionShape fallShape = new SphereShape(1);

        PairCachingGhostObject floorObject = new PairCachingGhostObject();
        floorObject.setCollisionShape(groundShape);
        floorObject.setCollisionFlags(CollisionFlags.KINEMATIC_OBJECT);

        dynamicsWorld.addCollisionObject(floorObject, CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));

        player = new EntityPlayer();
        player.physics(dynamicsWorld);

    }



    public void update(float fps){
        player.getPhysicsController().preStep(dynamicsWorld);
        dynamicsWorld.stepSimulation(1/fps, 10);
    }

    public float getPlayerHeight(){
        Transform position = new Transform();
        if(player != null)
                player.getPhysicsBody().getWorldTransform(position);
        return position.origin.y;
    }*/
}
