package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;
import keithcod.es.fusionengine.core.Location;
import keithcod.es.fusionengine.core.Rotation;
import keithcod.es.fusionengine.world.Chunk;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class EntityPlayer extends Entity{

    public EntityPlayer (Location position){
        CapsuleShape shape = new CapsuleShape(Chunk.BLOCK_SIZE_HALF-0.2f, Chunk.BLOCK_SIZE-0.05f);
//        BoxShape shape = new BoxShape(new Vector3f(Chunk.BLOCK_SIZE_HALF-0.5f, Chunk.BLOCK_SIZE-0.05f, Chunk.BLOCK_SIZE_HALF-0.5f));

        rigidbody = position.world.getPhysics().createRigidbody(shape, new Vector3f(position.x, position.y, position.z), 1f);
        rigidbody.setRestitution(0);
        rigidbody.setDamping(0.3f, 0.2f);
        rigidbody.setFriction(0.1f);
        rigidbody.setAngularFactor(0.0f);

        rigidbody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);

    }

    @Override
    public void update(){
        Transform trans = new Transform();
        rigidbody.getWorldTransform(trans);

        this.position.set(trans.origin.x, trans.origin.y, trans.origin.z);

        Quat4f rot = new Quat4f();
        trans.getRotation(rot);

        QuaternionUtil.setEuler(rot, (float)Math.toRadians(rotation.getY()), 0, 0);

        trans.setRotation(rot);

        rigidbody.setWorldTransform(trans);

    }

    public void move(Location loc){
        position = loc;
        if(rigidbody != null){
            Transform trans = new Transform();
            rigidbody.getWorldTransform(trans);

            trans.origin.set(position.x, position.y, position.z);
            rigidbody.setWorldTransform(trans);

        }
    }

    public void applyForce(float x1, float y, float z1){
        float x = 0;
        float z = 0;

        if ( z1 != 0 ) {
            x += (float)Math.sin(Math.toRadians(rotation.getY())) * -1.0f * z1;
            z += (float)Math.cos(Math.toRadians(rotation.getY())) * z1;
        }
        if ( x1 != 0) {
            x += (float)Math.sin(Math.toRadians(rotation.getY() - 90)) * -1.0f * x1;
            z += (float)Math.cos(Math.toRadians(rotation.getY() - 90)) * x1;
        }

        y *= 4;

//        rigidbody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        rigidbody.setLinearVelocity(new Vector3f(x, y, z));
    }

    public void rotate(float x, float y, float z){
        rigidbody.setAngularVelocity(new Vector3f(x, y, z));
    }
}
