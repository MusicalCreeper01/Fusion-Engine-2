package keithcod.es.fusionengine.world;

import jLibNoise.noise.module.Perlin;
import keithcod.es.fusionengine.client.Client;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.world.generation.Generator;
import keithcod.es.fusionengine.world.materials.MaterialBlock;
import org.joml.Vector3f;

import java.util.*;

public class World {

    public static final Object EMPTY = null;

    public String name = "New World";
    public UUID uuid = UUID.randomUUID();

    public int seed = 404;

    Map<ChunkPosition, Chunk> chunks = new HashMap<>();
    Map<ChunkPosition, Chunk> newchunks = new HashMap<>();

    public int time = 0;
    public float timef = 0;
//    public boolean morning = true;
    public static final int MAX_TIME = 40;


    public MaterialBlock getBlockAt(int x, int y, int z){
        int chunkx = (int)Math.floor((double)x/Chunk.CHUNK_SIZE);
        int chunky = (int)Math.floor((double)z/Chunk.CHUNK_SIZE);

        ChunkPosition key = new ChunkPosition(chunkx, chunky);
//        System.out.println(chunkx + "/" + chunky + " " + chunks.containsKey(new ChunkPosition(chunkx, chunky)));
        if(chunks.containsKey(key)){
//            System.out.println("valid chunk");
            Chunk chunk = chunks.get(key);

            int normalizedx = x - (Chunk.CHUNK_SIZE * chunkx);
            int normalizedz = z - (Chunk.CHUNK_SIZE * chunky);

            return chunk.getBlock(normalizedx, y, normalizedz);
        }else{

            return null;
        }

    }
    Timer timer;
    public World(){
        /*timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                ++time;
                if(time > MAX_TIME)
                    time = 0;

                System.out.println("World time: " + time);
            }
        }, 0, 1000);*/
    }

    boolean newChunks = false;


    public void generate(){
        final World instance = this;
        Generator gen = Generator.generators.get(0);

        (new Thread() {
            public void run() {
                Perlin myModule = new Perlin();
                myModule.setSeed(seed);
                myModule.setFrequency(.05d);
                myModule.setPersistence(0.1d);
                myModule.setOctaveCount(4);
                myModule.setLacunarity(.01d);

                newchunks.clear();

                int worldsize = 2;

                for(int cx = -worldsize; cx < worldsize; ++cx){
                    for(int cy = -worldsize; cy < worldsize; ++cy){
                        Chunk chunk = new Chunk(instance);

                        chunk.x = cx;
                        chunk.y = cy;

                        int xOffset = cx*Chunk.CHUNK_SIZE;
                        int yOffset = cy*Chunk.CHUNK_SIZE;
                        int heightscale = 5;
                        int base = 10;

                        for(int x = 0; x < Chunk.CHUNK_SIZE; ++x) {
                            for (int y = 0; y < Chunk.CHUNK_HEIGHT; ++y) {
                                for (int z = 0; z < Chunk.CHUNK_SIZE; ++z) {
                                    if(gen.use(xOffset+x, y, yOffset+z)){
                                        MaterialBlock b = MaterialBlock.getBlock(1);
//                                        System.out.println("(" + x + ", " + y +", " + z +") " + b.name);
                                        chunk.setBlock(b, x,y,z);
                                    }
                                }
                            }
                        }

                       /* for(int x = 0; x < Chunk.CHUNK_SIZE; ++x) {
                            for (int z = 0; z < Chunk.CHUNK_SIZE; ++z) {
                                Double v = ((myModule.getValue((xOffset+x), (yOffset+z), .1) + 1) / 2.0 * heightscale);
                                int p = v.intValue();
                                p += base;

                                for(int y = 0; y < p; ++y){
                                    chunk.setBlock(MaterialBlock.getBlock(1), x,y,z);
                                }
                            }
                        }*/

                        try {
                            newchunks.put(new ChunkPosition(cx,cy), chunk);
                            System.out.println("Created chunk (" + cx + ":" + cy + ")");
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }


                    }
                }
                for(ChunkPosition c : newchunks.keySet())
                    newchunks.put(c, newchunks.get(c));
                newChunks = true;

            }
        }).start();
    }

    long last = System.nanoTime ();
    long now;

    public void update(){
        if(newChunks){
            System.out.println("Rendering chunks...");
            newChunks = false;
            for(ChunkPosition p : newchunks.keySet()) {
                Chunk c = newchunks.get(p);
                chunks.put(p, c);
                try {
                    c.draw();
                    Client.game().getPhysics().addMesh(new Vector3f(Chunk.PHYSICAL_SIZE*c.x, 0, Chunk.PHYSICAL_SIZE*c.y), c.mesh);
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.out.println("Error drawing chunk " + c.x + ":" + c.y + "!");
                }
            }
        }

        now = System.nanoTime ();
        timef += (now - last)/1000000000f;

        last = now;
        time = (int)timef;

//        System.out.println(timef + ":" + time);

    }

    public void render(ShaderProgram shaderProgram){
        for(ChunkPosition pair : chunks.keySet()){
            Chunk chunk = chunks.get(pair);

            chunk.render(shaderProgram);
        }
    }

}
