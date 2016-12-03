package keithcod.es.fusionengine.world.materials;

import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.core.Utils;

/**
 * Created by ldd20 on 12/2/2016.
 */
public abstract class Material {

    public int id = 0;
    public String name = "New Material!";
    public String slug = Utils.safeName(name);

    public Material (){

    }

    abstract Mesh getPreview();
}
