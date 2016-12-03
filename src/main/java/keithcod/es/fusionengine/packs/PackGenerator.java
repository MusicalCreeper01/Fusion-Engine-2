package keithcod.es.fusionengine.packs;

public class PackGenerator {

    public String name = "New Generator";
    public String desc = "";
    public String type = "perlin";

    public class Options {
        public int seed = 404;
        public double persistence = 0.1d;
        public int octaves = 4;
        public double frequency = .05d;
        public double lacunarity = .01d;
    }
    public Options options;


}
