package keithcod.es.fusionengine.entities;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.linearmath.Transform;
import keithcod.es.fusionengine.core.Location;
import keithcod.es.fusionengine.core.Rotation;
import keithcod.es.fusionengine.world.World;

import javax.vecmath.Vector3f;

/**
 * Created by ldd20 on 12/5/2016.
 */
public class EntityPhysical extends Entity{



    public EntityPhysical (Location position){
        this.position = position;
        rigidbody = position.world.getPhysics().createRigidbody(new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f)), new Vector3f(position.x, position.y, position.z), 1f);
    }

    @Override
    public void update(){
        Transform trans = new Transform();
        rigidbody.getWorldTransform(trans);

        System.out.println(trans.origin);

        position.set(trans.origin.x, trans.origin.y, trans.origin.z);
    }

}
