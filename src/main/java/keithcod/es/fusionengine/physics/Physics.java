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
import java.util.HashMap;
import java.util.Map;

//http://gamedev.stackexchange.com/questions/41942/moving-the-jbullet-collision-body-to-with-the-player-object

public class Physics {

    protected DynamicsWorld dynamicsWorld = null;
    private RigidBody box;

    public static int nextcollider = 0;

    public Map<Integer, CollisionObject> colliders = new HashMap<>();


    public static int nextrigidbody = 0;

    public Map<Integer, RigidBody> rigidbodies = new HashMap<>();

    public Physics (){


    }


    JBulletDebugDrawer debugDraw;

    public void init(Window window){
        dynamicsWorld = createDynamicsWorld();

        debugDraw = new JBulletDebugDrawer(window);
        dynamicsWorld.setDebugDrawer(debugDraw);
        debugDraw.setDebugMode(DebugDrawModes.DRAW_WIREFRAME + DebugDrawModes.DRAW_AABB);


//        dynamicsWorld.setGravity(new Vector3f(0f, -30f, 0f));
        dynamicsWorld.setGravity(new Vector3f(0f, -100f, 0f));

        //box = createBoxBody();
//        box.setRestitution(0);

        //dynamicsWorld.addRigidBody(box);
        dynamicsWorld.addRigidBody(createGroundBody());
    }

    private DynamicsWorld createDynamicsWorld() {
        BroadphaseInterface broadphase = new DbvtBroadphase();
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        // provides discrete rigid body simulation
        return new DiscreteDynamicsWorld(
                dispatcher, broadphase, solver, collisionConfiguration);
    }

    /*private RigidBody createBoxBody() {
        CollisionShape colShape = new BoxShape(new Vector3f(0.5f,0.5f,0.5f));
//        CollisionShape colShape = new SphereShape(Chunk.BLOCK_SIZE_HALF);

        float mass = 1f;

        Vector3f localInertia = new Vector3f(0, 0, 0);
        colShape.calculateLocalInertia(mass, localInertia);

        Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 80, 0), 1.0f));
        MotionState state = new DefaultMotionState(transform);

        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, state, colShape, localInertia);

        return new RigidBody(rbInfo);
    }*/

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

        RigidBody rb = new RigidBody(rbInfo);
        rb.setRestitution(0.0f);

        return rb;
    }

    public void update(double delta) {
        if(dynamicsWorld != null)
            dynamicsWorld.stepSimulation((float)delta , 2, 1/60f);


//        Transform trans = new Transform();
//        trans = box.getWorldTransform(trans);
//        System.out.println(trans.origin.y);
    }

    public void drawDebug(){
        debugDraw.setDebugMode(DebugDrawModes.DRAW_WIREFRAME + DebugDrawModes.DRAW_AABB);
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

    public int addMesh(org.joml.Vector3f position, Mesh mesh){
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
            collisionShape.setMargin(0.1f);

            CollisionObject colObject = new CollisionObject();
            colObject.setCollisionShape(collisionShape);
            colObject.setWorldTransform(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(position.x, position.y, position.z), 1f)));



            colObject.setRestitution(0.0f);
            colObject.setFriction(0.0f);

            dynamicsWorld.addCollisionObject(colObject);

            int id = nextcollider;
            ++nextcollider;

            colliders.put(id, colObject);

            return id;

        } else {
            System.err.println("Failed to extract geometry from model. ");
        }
        return -1;
    }

    public RigidBody createRigidbody (CollisionShape shape, Vector3f pos) {
        return createRigidbody(shape, pos, 1f);
    }

    public RigidBody createRigidbody (CollisionShape shape, Vector3f pos, float mass){
        int id = nextrigidbody;
        ++nextrigidbody;

//        shape.setMargin(0.1f);

        Vector3f localInertia = new Vector3f(0, 0, 0);
        shape.calculateLocalInertia(mass, localInertia);

        Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), pos, 1.0f));
        MotionState state = new DefaultMotionState(transform);

        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, state, shape, localInertia);

        RigidBody rb = new RigidBody(rbInfo);

        rb.setRestitution(0.1f);
        rb.setHitFraction(0);

        dynamicsWorld.addRigidBody(rb);

        rigidbodies.put(nextrigidbody, rb);

        return rb;
    }

    public void removeMesh(int i){
        if(i == -1)
            return;

        if(colliders.containsKey(i)){
            CollisionObject co = colliders.get(i);

            dynamicsWorld.removeCollisionObject(co);
            colliders.remove(i);
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
