package keithcod.es.fusionengine.world.materials;

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

public class MaterialBlock extends Material{

    public static Map<Domain, ArrayList<MaterialBlock>> blocks = new HashMap<>();

    public static MaterialBlock getBlock(Domain d, String slug){
        if(!blocks.containsKey(d))
            return null;

        for(MaterialBlock block : blocks.get(d)){
            if(block.slug.equals(slug) || block.name.equals(slug))
                return block;
        }
        return null;
    }

    public static MaterialBlock getBlock(int i) {
        int lasti = 0;
        for(Domain d : blocks.keySet()){
            ArrayList<MaterialBlock> bs = blocks.get(d);
            if(i < lasti + bs.size()){
                return bs.get(i - lasti);
            }else{
                lasti += bs.size();
            }
        }
        return null;
    }

    public static MaterialBlock GRASS = new MaterialBlock(Domain.DEFAULT, "Grass MaterialBlock", "grass_block");

    /*public static void registerBlocks(){
        GRASS.register("default");
    }*/


    private Map<Side, String> texturePaths = new HashMap<>();
    private Map<Side, Vector4f> textures = new HashMap<>();
    private int[] registryIDs = new int[6];

    public boolean opaque = false;

    private Domain domain = Domain.DEFAULT;

    public MaterialBlock(String name){
        this(name, Utils.safeName(name));
    }
    public MaterialBlock(String name, String slug){
        this(Domain.DEFAULT, name, slug);
    }

    public MaterialBlock(Domain d, String name){
        this(d, name, Utils.safeName(name));
    }

    public MaterialBlock(Domain domain, String name, String slug) {
        super();
        this.name = name;
        this.slug = Utils.safeName(slug);
        this.domain = domain;

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

    public void build(){
        registryIDs[0] = Registry.registerTexture(domain, texturePaths.get(Side.Top));
        registryIDs[1] = Registry.registerTexture(domain, texturePaths.get(Side.Bottom));
        registryIDs[2] = Registry.registerTexture(domain, texturePaths.get(Side.Left));
        registryIDs[3] = Registry.registerTexture(domain, texturePaths.get(Side.Right));
        registryIDs[4] = Registry.registerTexture(domain, texturePaths.get(Side.Front));
        registryIDs[5] = Registry.registerTexture(domain, texturePaths.get(Side.Back));
    }



}
