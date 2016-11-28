package keithcod.es.fusionengine.enviroment;

import jLibNoise.noise.module.Perlin;
import javafx.util.Pair;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class World {

    public static final int EMPTY = 0;

    public String name = "New World";
    public UUID uuid = UUID.randomUUID();

    public int seed = 404;

    Map<ChunkPosition, Chunk> chunks = new HashMap<>();



    public int getBlockAt(int x, int y, int z){
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

            return EMPTY;
        }

    }

    boolean newChunks = false;

    public void generate(){
        final World instance = this;
        (new Thread() {
            public void run() {
                Perlin myModule = new Perlin();
                myModule.setSeed(seed);
                myModule.setPersistence(0.1d);
                myModule.setOctaveCount(4);
                myModule.setFrequency(.05d);
                myModule.setLacunarity(.01d);

                Map<ChunkPosition, Chunk> newchunks = new HashMap<>();

                for(int cx = -2; cx < 2; ++cx){
                    for(int cy = -2; cy < 2; ++cy){
                        Chunk chunk = new Chunk(instance);

                        chunk.x = cx;
                        chunk.y = cy;

                        int xOffset = cx*Chunk.CHUNK_SIZE;
                        int yOffset = cy*Chunk.CHUNK_SIZE;
                        int heightscale = 5;
                        int base = 10;


                        //chunk.setBlock(1, 0,0,0);

                        for(int x = 0; x < Chunk.CHUNK_SIZE; ++x) {
                            for (int z = 0; z < Chunk.CHUNK_SIZE; ++z) {
                                Double v = ((myModule.getValue((xOffset+x)- 0.98, (yOffset+z)- 0.98, .1) + 1) / 2.0 * heightscale);
                                int p = v.intValue();
                                p += base;

                                for(int y = 0; y < p; ++y){
                                    chunk.setBlock(1, x,y,z);
                                }
                            }
                        }

                        try {
                            newchunks.put(new ChunkPosition(cx,cy), chunk);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }


                    }
                }
                for(ChunkPosition c : newchunks.keySet())
                    chunks.put(c, newchunks.get(c));
                newChunks = true;

            }
        }).start();
    }


    public void update(){
        if(newChunks){
            newChunks = false;
            for(Chunk c : chunks.values()) {
                try {
                    //c.draw();
                    c.drawobjecs();
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.out.println("Error drawing chunk " + c.x + ":" + c.y + "!");
                }
            }
        }
    }

    public void render(ShaderProgram shaderProgram){
        for(ChunkPosition pair : chunks.keySet()){
            Chunk chunk = chunks.get(pair);

            chunk.render(shaderProgram);
        }
    }

}
