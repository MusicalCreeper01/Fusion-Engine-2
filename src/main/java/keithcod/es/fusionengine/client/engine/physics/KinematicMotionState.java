package keithcod.es.fusionengine.client.engine.physics;

import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class KinematicMotionState extends MotionState {
    private Transform worldTransform;

    public KinematicMotionState() {
        worldTransform = new Transform();
        worldTransform.setIdentity();
    }

    @Override
    public Transform getWorldTransform(Transform out) {
        out.set(worldTransform);
        return out;
    }

    @Override
    public void setWorldTransform(Transform worldTrans) {
        worldTransform.set(worldTrans);
    }
}