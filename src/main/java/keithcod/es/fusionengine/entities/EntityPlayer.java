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
        CapsuleShape shape = new CapsuleShape(Chunk.BLOCK_SIZE_HALF-0.02f, Chunk.BLOCK_SIZE-0.05f);
//        shape.setMargin(0.1f);
        rigidbody = position.world.getPhysics().createRigidbody(shape, new Vector3f(position.x, position.y, position.z), 1f);
//        rigidbody = position.world.getPhysics().createRigidbody(new BoxShape(new Vector3f(Chunk.BLOCK_SIZE_HALF, Chunk.BLOCK_SIZE, Chunk.BLOCK_SIZE_HALF)), new Vector3f(position.x, position.y, position.z), 1f);
        rigidbody.setRestitution(0);
        rigidbody.setFriction(0.1f);
        rigidbody.setAngularFactor(0.0f);

//        rigidbody.setAngularVelocity(new Vector3f(0,1,0));
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
//        System.out.println(position + ": " + loc);
        position = loc;
        if(rigidbody != null){
            Transform trans = new Transform();
            rigidbody.getWorldTransform(trans);

            trans.origin.set(position.x, position.y, position.z);
            rigidbody.setWorldTransform(trans);

        }
    }

    public void applyForce(float x1, float y, float z1){

        /*double angle = Math.toRadians(rotation.y);

        x = (float)Math.sin(angle) * z;
        y = (float)Math.cos(angle) * -1.0f * z;*/

        /*if ( z != 0 ) {
            x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            z += (float)Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if ( x != 0) {
            x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }*/

        float x = 0;
        float z = 0;

        System.out.println(x + ":" + z);

        if ( z1 != 0 ) {
            x += (float)Math.sin(Math.toRadians(rotation.getY())) * -1.0f * z1;
            z += (float)Math.cos(Math.toRadians(rotation.getY())) * z1;
        }
        if ( x1 != 0) {
            x += (float)Math.sin(Math.toRadians(rotation.getY() - 90)) * -1.0f * x1;
            z += (float)Math.cos(Math.toRadians(rotation.getY() - 90)) * x1;
        }

        /*double angle = Math.toRadians(rotation.getY());
        double sin = Math.sin( angle );
        double cos = Math.cos( angle );
        x = (float)(x * cos - z * sin);
        z = (float)(x * sin + z * cos);*/


        rigidbody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        rigidbody.setLinearVelocity(new Vector3f(x, y, z));
    }

    public void rotate(float x, float y, float z){
        rigidbody.setAngularVelocity(new Vector3f(x, y, z));
    }
}
