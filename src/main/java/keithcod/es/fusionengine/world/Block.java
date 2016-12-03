package keithcod.es.fusionengine.world;

import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.core.Side;
import keithcod.es.fusionengine.core.Sides;
import keithcod.es.fusionengine.core.Utils;
import keithcod.es.fusionengine.register.Domain;
import keithcod.es.fusionengine.register.Registry;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block extends Material{

    public static Map<Domain, ArrayList<Block>> blocks = new HashMap<>();

    public static Block GRASS = new Block(Domain.DEFAULT, "Grass Block", "grass_block");

    /*public static void registerBlocks(){
        GRASS.register("default");
    }*/


    private Map<Side, String> texturePaths = new HashMap<>();
    private Map<Side, Vector4f> textures = new HashMap<>();
    private int[] registryIDs = new int[6];

    public boolean opaque = false;

    public Block(String name){
        this(name, Utils.safeName(name));
    }
    public Block(String name, String slug){
        this(Domain.DEFAULT, name, slug);
    }

    public Block(Domain d, String name){
        this(d, name, Utils.safeName(name));
    }

    public Block(Domain domain, String name, String slug) {
        super();
        this.name = name;
        this.slug = Utils.safeName(slug);
        if(!blocks.containsKey(domain))
            blocks.put(domain, new ArrayList<>());
        blocks.get(domain).add(this);
    }

    public Mesh getPreview(){
        return null;
    }

    public void setTexture(Sides sides, String path){
        if(sides == Sides.Sides){
            texturePaths.put(Side.Right, path);
            texturePaths.put(Side.Left, path);
            texturePaths.put(Side.Front, path);
            texturePaths.put(Side.Back, path);
        }else if (sides == Sides.TopBottom){
            texturePaths.put(Side.Top, path);
            texturePaths.put(Side.Bottom, path);
        }
    }

    public void setTexture(Side side, String path){
        texturePaths.put(side, path);

        if(side == Side.Left || side == Side.Right || side == Side.Front || side == Side.Back){
            if(!texturePaths.containsKey(Side.Right)) texturePaths.put(Side.Right, path);
            if(!texturePaths.containsKey(Side.Left))  texturePaths.put(Side.Left, path);
            if(!texturePaths.containsKey(Side.Front)) texturePaths.put(Side.Front, path);
            if(!texturePaths.containsKey(Side.Back))  texturePaths.put(Side.Back, path);
        }
    }

    public void setTexture(String path){
        texturePaths.put(Side.Top, path);
        texturePaths.put(Side.Bottom, path);
        texturePaths.put(Side.Left, path);
        texturePaths.put(Side.Right, path);
        texturePaths.put(Side.Front, path);
        texturePaths.put(Side.Back, path);
    }

    public void register(String domain){
        registryIDs[0] = Registry.registerTexture(domain, texturePaths.get(Side.Top));
        registryIDs[1] = Registry.registerTexture(domain, texturePaths.get(Side.Bottom));
        registryIDs[2] = Registry.registerTexture(domain, texturePaths.get(Side.Left));
        registryIDs[3] = Registry.registerTexture(domain, texturePaths.get(Side.Right));
        registryIDs[4] = Registry.registerTexture(domain, texturePaths.get(Side.Front));
        registryIDs[5] = Registry.registerTexture(domain, texturePaths.get(Side.Back));
    }



}
