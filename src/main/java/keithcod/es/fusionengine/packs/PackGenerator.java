package keithcod.es.fusionengine.packs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import keithcod.es.fusionengine.world.generation.Generator;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PackGenerator {

    public String name = "New Generator";
    public String desc = "";
    public String expression = "";


   /* public String type = "perlin";

    public class Options {
        public int seed = 404;
        public double persistence = 0.1d;
        public int octaves = 4;
        public double frequency = .05d;
        public double lacunarity = .01d;
    }
    public Options options;*/

   public void build(){
       new Generator(name, expression);
   }

    public static PackGenerator load(String path) throws FileNotFoundException, JsonSyntaxException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        PackGenerator data = gson.fromJson(reader, PackGenerator.class);
        //data.path = Paths.get(path).getParent().toString() + File.separator;
        return data;
    }

}
