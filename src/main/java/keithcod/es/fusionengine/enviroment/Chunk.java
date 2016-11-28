package keithcod.es.fusionengine.enviroment;

import keithcod.es.fusionengine.client.Fusion;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.world.Position;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk {

    public static final int CHUNK_SIZE = 32;
    public static final int CHUNK_HEIGHT = 128;

    public static final float BLOCK_SIZE = 0.5f;

    public int x = 0;
    public int y = 0;

//    public Map<Position, Integer> blocks= new HashMap<>();

    public int[][][] blocks = new int [CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];

    private Mesh mesh;

    private World world;

    public Chunk(World world){
        this.world = world;
}

    public void setBlock(int id, int x, int y, int z){
        blocks[x][y][z] = id;
    }

    public int getBlock(int x, int y, int z){
//        if(x < 0 || y < 0 || z < 0) {
//
//            return 0;
//        }
        try {
            return blocks[x][y][z];
        }catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            System.out.println("getBlock("+x+", "+y+", "+z+") is invalid!");
            return 0;
        }

    }

//    public void setBlock(int id, int x, int y, int z){
//        blocks.put(new Position(x, y, z), id);
//    }
//
//    public int getBlock(int x, int y, int z){
//        return blocks.containsKey(new Position(x, y, z)) ? blocks.get(new Position(x,y,z)) : World.EMPTY;
//    }

    public void draw() throws Exception{
        List<Float> verts = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();
        List<Integer> tris = new ArrayList<>();
        Texture texture = new Texture("/textures/faces.png");

        int i = 0;
        for(int x = 0; x < CHUNK_SIZE; ++x){
            for(int z = 0; z < CHUNK_SIZE; ++z){
                for(int y = 0; y < CHUNK_HEIGHT; ++y){
                    int block = getBlock(x, y, z);

                    if(block != World.EMPTY){

                        int worldx = (CHUNK_SIZE*this.x) + x;
                        int worldz = (CHUNK_SIZE*this.y) + z;

                        int posx = world.getBlockAt(worldx+1, y, worldz);
                        int posy = y < CHUNK_HEIGHT-1 ? world.getBlockAt(worldx, y+1, worldz) : 0;
                        int posz = world.getBlockAt(worldx, y, worldz+1);

                        int negx = world.getBlockAt(worldx-1, y, worldz);
                        int negy = y > 0 ? world.getBlockAt(x, y-1, worldz) : 0;
                        int negz = world.getBlockAt(worldx, y, worldz-1);

                        boolean top = posy == 0 ? true : false;
                        boolean bottom = negy == 0 ? true : false;

                        boolean front = posz == 0 ? true : false;
                        boolean back = negz == 0 ? true : false;

                        boolean right = posx == 0 ? true : false;
                        boolean left = negx == 0 ? true : false;

//


//                        System.out.println(x +"/" + y +"/" + z);

                        if(top) {
                            verts.add(x + (-BLOCK_SIZE)); verts.add(y + BLOCK_SIZE); verts.add(z + (-BLOCK_SIZE));
                            verts.add(x + BLOCK_SIZE);    verts.add(y + BLOCK_SIZE); verts.add(z + (-BLOCK_SIZE));
                            verts.add(x + (-BLOCK_SIZE)); verts.add(y + BLOCK_SIZE); verts.add(z + BLOCK_SIZE);
                            verts.add(x + BLOCK_SIZE);    verts.add(y + BLOCK_SIZE); verts.add(z + BLOCK_SIZE);


                        /* top */
                            /*uvs.add(0.0f); uvs.add(0.5f); //0.0f 0.5f 9
                            uvs.add(0.5f); uvs.add(0.5f); //0.5f 0.5f 10
                            uvs.add(0.0f); uvs.add(1.0f); //0.0f 1.0f  11
                            uvs.add(0.5f); uvs.add(1.0f); //0.5f 0.1f  12*/

                            uvs.add(0.0f); uvs.add(0.5f); //0.0f 0.5f 9
                            uvs.add(1.0f/3); uvs.add(0.5f); //0.5f 0.5f 10
                            uvs.add(0.0f); uvs.add(0.0f); //0.0f 1.0f  11
                            uvs.add(1.0f/3); uvs.add(0.0f); //0.5f 0.1f  12


                            tris.add(i + 0); tris.add(i + 2); tris.add(i + 3);
                            tris.add(i + 1); tris.add(i + 0);  tris.add(i + 3);

                            i += 4;
                        }else{

                        }

                        if(bottom) {
                            verts.add(x + (-BLOCK_SIZE)); verts.add(y + -BLOCK_SIZE); verts.add(z + (-BLOCK_SIZE));
                            verts.add(x + BLOCK_SIZE);    verts.add(y + -BLOCK_SIZE); verts.add(z + (-BLOCK_SIZE));
                            verts.add(x + (-BLOCK_SIZE)); verts.add(y + -BLOCK_SIZE); verts.add(z + BLOCK_SIZE);
                            verts.add(x + BLOCK_SIZE);    verts.add(y + -BLOCK_SIZE); verts.add(z + BLOCK_SIZE);


                        /* top */
//                            uvs.add(0.5f); uvs.add(0.0f); //0.0f 0.5f 9
//                            uvs.add(1.0f); uvs.add(0.0f); //0.5f 0.5f 10
//                            uvs.add(0.5f); uvs.add(0.5f); //0.0f 1.0f  11
//                            uvs.add(1.0f); uvs.add(0.5f); //0.5f 0.1f  12

                            uvs.add(0.0f/3); uvs.add(0.5f); //0.0f 0.5f 9
                            uvs.add(1.0f); uvs.add(0.5f); //0.5f 0.5f 10
                            uvs.add(0.0f/3); uvs.add(1.0f); //0.0f 1.0f  11
                            uvs.add(1.0f); uvs.add(1.0f); //0.5f 0.1f  12


                            tris.add(i + 3); tris.add(i + 2); tris.add(i + 0);
                            tris.add(i + 3); tris.add(i + 0);  tris.add(i + 1);

                            i += 4;
                        }

                        if (front) {
                            verts.add(x+(-BLOCK_SIZE)); verts.add(y+BLOCK_SIZE);    verts.add(z+BLOCK_SIZE);
                            verts.add(x+(-BLOCK_SIZE)); verts.add(y+(-BLOCK_SIZE)); verts.add(z+BLOCK_SIZE);
                            verts.add(x+BLOCK_SIZE);    verts.add(y+(-BLOCK_SIZE)); verts.add(z+BLOCK_SIZE);
                            verts.add(x+BLOCK_SIZE);    verts.add(y+BLOCK_SIZE);    verts.add(z+BLOCK_SIZE);

                            uvs.add((1.0f/3)); uvs.add(0.0f); //0.0f, 0.0f 1
                            uvs.add((1.0f/3)); uvs.add(0.5f); //0.0f 0.5f 2
                            uvs.add((1.0f/3)*2); uvs.add(0.5f); //0.5f 0.5f 3
                            uvs.add((1.0f/3)*2); uvs.add(0.0f); // 0.5f 0.0f 4

                            tris.add(i+0);  tris.add(i+1);  tris.add(i+3);
                            tris.add(i+3);  tris.add(i+1);  tris.add(i+2);

                            i += 4;

                        }

                        if(back){
                            verts.add(x+(-BLOCK_SIZE)); verts.add(y+BLOCK_SIZE);    verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+BLOCK_SIZE);    verts.add(y+BLOCK_SIZE);    verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+(-BLOCK_SIZE)); verts.add(y+(-BLOCK_SIZE)); verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+BLOCK_SIZE);    verts.add(y+(-BLOCK_SIZE)); verts.add(z+(-BLOCK_SIZE));

                            uvs.add((1.0f/3)*3); uvs.add(0.0f); //0.0f 0.0f 5
                            uvs.add((1.0f/3)*2); uvs.add(0.0f); //0.5f 0.0f 6
                            uvs.add((1.0f/3)*3); uvs.add(0.5f); //0.0f 0.5f 7
                            uvs.add((1.0f/3)*2); uvs.add(0.5f); //0.5f 0.5f 8

                            tris.add(i+3); tris.add(i+2); tris.add(i+0);
                            tris.add(i+3); tris.add(i+0); tris.add(i+1);

                            i += 4;
                        }

                        if (right){
                            verts.add(x+BLOCK_SIZE);    verts.add(y+BLOCK_SIZE);    verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+BLOCK_SIZE);    verts.add(y+(-BLOCK_SIZE)); verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+BLOCK_SIZE);    verts.add(y+(-BLOCK_SIZE)); verts.add(z+BLOCK_SIZE);
                            verts.add(x+BLOCK_SIZE);    verts.add(y+BLOCK_SIZE);    verts.add(z+BLOCK_SIZE);

                            uvs.add((1.0f/3)*3); uvs.add(0.5f);
                            uvs.add((1.0f/3)*3); uvs.add(1.0f);
                            uvs.add((1.0f/3)*2); uvs.add(1.0f);
                            uvs.add((1.0f/3)*2); uvs.add(0.5f);

                            tris.add(i+2); tris.add(i+1); tris.add(i+0);
                            tris.add(i+0); tris.add(i+3); tris.add(i+2);

                            i += 4;
                        }

                        if (left){
                            verts.add(x+(-BLOCK_SIZE));    verts.add(y+BLOCK_SIZE);    verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+(-BLOCK_SIZE));    verts.add(y+(-BLOCK_SIZE)); verts.add(z+(-BLOCK_SIZE));
                            verts.add(x+(-BLOCK_SIZE));    verts.add(y+(-BLOCK_SIZE)); verts.add(z+BLOCK_SIZE);
                            verts.add(x+(-BLOCK_SIZE));    verts.add(y+BLOCK_SIZE);    verts.add(z+BLOCK_SIZE);

                            uvs.add((1.0f/3)); uvs.add(0.5f);
                            uvs.add((1.0f/3)); uvs.add(1.0f);
                            uvs.add((1.0f/3)*2); uvs.add(1.0f);
                            uvs.add((1.0f/3)*2); uvs.add(0.5f);

                            tris.add(i+0); tris.add(i+1); tris.add(i+2);
                            tris.add(i+2); tris.add(i+3); tris.add(i+0);

                            i += 4;

                        }

                    }

                }
            }
        }

        float[] vertsa = convertFloats(verts);

        System.out.println(vertsa.length);
        mesh = new Mesh(vertsa, convertFloats(uvs), convertIntegers(tris), texture);
    }

    public void render(ShaderProgram shaderProgram){
        if(mesh != null) {
            Matrix4f viewMatrix = Fusion.game().getRenderer().getTransformation().getViewMatrix(Fusion.game().getCamera());
            Matrix4f modelViewMatrix = Fusion.game().getRenderer().getTransformation().getBasicViewMatrix(new Vector3f((BLOCK_SIZE * CHUNK_SIZE)*x, 0, (BLOCK_SIZE * CHUNK_SIZE)*y), new Vector3f(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE), viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            mesh.render();
//
//            GL11.glBegin(GL11.GL_LINES);
//            GL11.glVertex2f(10, 10);
//            GL11.glVertex2f(20, 20);
//            GL11.glEnd();
        }
//        System.out.println("rendered chunk");
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public static float[] convertFloats(List<Float> floats)
    {
        float[] ret = new float[floats.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = floats.get(i).floatValue();
        }
        return ret;
    }

}
