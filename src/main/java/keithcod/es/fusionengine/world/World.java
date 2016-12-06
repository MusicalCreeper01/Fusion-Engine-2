package keithcod.es.fusionengine.world;

import jLibNoise.noise.module.Perlin;
import keithcod.es.fusionengine.client.Client;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.entities.Entity;
import keithcod.es.fusionengine.physics.Physics;
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

    private Physics physics;
    private List<Entity> entities = new ArrayList<>();

    public World(){
        physics = new Physics();
        physics.init(Client.game().window);
    }

    public Physics getPhysics (){
        return physics;
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

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

    boolean ready = false;

    public void update(){
        if(newChunks){
            System.out.println("Rendering chunks...");
            newChunks = false;
            for(ChunkPosition p : newchunks.keySet()) {
                Chunk c = newchunks.get(p);
                chunks.put(p, c);
                try {
                    c.draw();
                    drawSurrounding(c.x, c.y);
//                    Client.game().getPhysics().addMesh(new Vector3f(Chunk.PHYSICAL_SIZE*c.x, 0, Chunk.PHYSICAL_SIZE*c.y), c.mesh);
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.out.println("Error drawing chunk " + c.x + ":" + c.y + "!");
                }
            }
            ready = true;
        }

        if(ready) {
            now = System.nanoTime();
            timef += (now - last) / 1000000000f;

            last = now;
            time = (int) timef;

            for (Entity entity : entities)
                entity.update();

            if (physics != null)
                physics.update(60.0f);

        }
//        System.out.println(timef + ":" + time);

    }

    public void drawSurrounding(int x, int y){
        ChunkPosition c1 = new ChunkPosition(x+1, y);
        ChunkPosition c2 = new ChunkPosition(x-1, y);
        ChunkPosition c3 = new ChunkPosition(x, y+1);
        ChunkPosition c4 = new ChunkPosition(x, y-1);
        try {
            if (chunks.containsKey(c1))
                chunks.get(c1).draw();
        }catch(Exception ex){}

        try {
            if (chunks.containsKey(c2))
                chunks.get(c2).draw();
        }catch(Exception ex){}

        try {
            if (chunks.containsKey(c3))
                chunks.get(c3).draw();
        }catch(Exception ex){}

        try {
            if (chunks.containsKey(c4))
                chunks.get(c4).draw();
        }catch(Exception ex){}
    }

    public void render(ShaderProgram shaderProgram){

        /*shaderProgram.unbind();
        physics.drawDebug();
        shaderProgram.bind();*/

        for(ChunkPosition pair : chunks.keySet()){
            Chunk chunk = chunks.get(pair);

            chunk.render(shaderProgram);
        }
    }

}
