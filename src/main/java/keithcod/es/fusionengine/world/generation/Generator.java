package keithcod.es.fusionengine.world.generation;

import jLibNoise.noise.module.Perlin;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static List<Generator> generators = new ArrayList<>();

    public String name = "Unnamed Generator";

    public GeneratorModule[] modules;

    public Generator (String name, GeneratorModule[] modules){
        this.name = name;
        this.modules = modules;

        generators.add(this);
    }

    Evaluator evaluator;

    public boolean use(int x, int y, int z){
        int h = 0;

        for(GeneratorModule gen : modules){
            Perlin myModule = new Perlin();
            myModule.setSeed(404);
            myModule.setFrequency(gen.frequency);
            myModule.setPersistence(gen.persistence);
            myModule.setOctaveCount(gen.octaves);
            myModule.setLacunarity(gen.lacunarity);

            h += (myModule.getValue((x), (z), .1) + 1) / 2.0*gen.scale;
        }

        return y < h;
    }

}
