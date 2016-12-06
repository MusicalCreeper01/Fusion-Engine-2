package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.Transform;
import keithcod.es.fusionengine.core.Location;
import keithcod.es.fusionengine.core.Rotation;
import keithcod.es.fusionengine.world.Chunk;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by ldd20 on 11/28/2016.
 */
public class EntityPlayer extends Entity{

    protected Location position;
    protected Rotation rotation;

    public EntityPlayer (Location position){
        rigidbody = position.world.getPhysics().createRigidbody(new BoxShape(new Vector3f(Chunk.BLOCK_SIZE_HALF, Chunk.BLOCK_SIZE, Chunk.BLOCK_SIZE_HALF)), new Vector3f(position.x, position.y, position.z), 1f);
        rigidbody.setRestitution(0);
    }

    @Override
    public void update(){
        Transform trans = new Transform();
        rigidbody.getWorldTransform(trans);

//        System.out.println(trans.origin);

        position.set(trans.origin.x, trans.origin.y, trans.origin.z);
    }

}
