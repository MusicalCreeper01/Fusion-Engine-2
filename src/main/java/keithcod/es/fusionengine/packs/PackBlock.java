package keithcod.es.fusionengine.packs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import keithcod.es.fusionengine.core.Side;
import keithcod.es.fusionengine.core.Sides;
import keithcod.es.fusionengine.core.Utils;
import keithcod.es.fusionengine.register.Domain;
import keithcod.es.fusionengine.world.materials.MaterialBlock;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PackBlock {

    public String name = "MaterialBlock!";
    public String slug = null;

    public boolean opaque = false;

    public class Textures {
        public String all;
        public String sides  = null;
        public String topbottom = null;
        public String top    = null;
        public String bottom = null;
        public String left   = null;
        public String right  = null;
        public String front  = null;
        public String back   = null;
    }

    public Textures textures;

    public void generateSlug (){
        if(slug == null)
            slug = Utils.safeName(name);
    }

    public void build(Domain d){
        MaterialBlock b = new MaterialBlock(d, name, slug);

        if(textures.all != null){
            b.setTexture(textures.all);
        }

        if (textures.sides != null) {
            b.setTexture(Sides.Sides, textures.sides);
        }

        if (textures.topbottom != null) {
            b.setTexture(Sides.Sides, textures.topbottom);
        }

        if (textures.left != null) {
            b.setTexture(Side.Left, textures.left);
        }

        if (textures.right != null) {
            b.setTexture(Side.Right, textures.right);
        }
        if (textures.top != null) {
            b.setTexture(Side.Top, textures.top);
        }
        if (textures.bottom != null) {
            b.setTexture(Side.Bottom, textures.bottom);
        }

        if (textures.front != null) {
            b.setTexture(Side.Front, textures.front);
        }

        if (textures.back != null) {
            b.setTexture(Side.Back, textures.back);
        }
    }

    public static PackBlock load(String path) throws FileNotFoundException, JsonSyntaxException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        PackBlock data = gson.fromJson(reader, PackBlock.class);
        return data;
    }

}
