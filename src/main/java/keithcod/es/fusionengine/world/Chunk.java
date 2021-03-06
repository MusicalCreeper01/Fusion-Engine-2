package keithcod.es.fusionengine.world;

import keithcod.es.fusionengine.core.Side;
import keithcod.es.fusionengine.client.Client;
import keithcod.es.fusionengine.client.engine.objects.Mesh;
import keithcod.es.fusionengine.client.engine.rendering.ShaderProgram;
import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.register.Registry;
import keithcod.es.fusionengine.world.materials.MaterialBlock;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    public static final int CHUNK_SIZE = 32;
    public static final int CHUNK_HEIGHT = 128;

    public static final float BLOCK_SIZE = 1;
    public static final float BLOCK_SIZE_HALF = (float)1/2;

    public static final float PHYSICAL_SIZE = BLOCK_SIZE * CHUNK_SIZE;

    public int x = 0;
    public int y = 0;

    public MaterialBlock[][][] blocks = new MaterialBlock[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];

    public Mesh mesh;

    private World world;

    private int collider = -1;

    public Chunk(World world){
        this.world = world;
}

    public void setBlock(MaterialBlock block, int x, int y, int z){
        blocks[x][y][z] = block;
    }

    public MaterialBlock getBlock(int x, int y, int z){
        try {
            return blocks[x][y][z];
        }catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            System.out.println("getBlock("+x+", "+y+", "+z+") is invalid!");
            return null;
        }

    }

    public void draw() throws Exception {
        if(collider != -1)
            world.getPhysics().removeMesh(collider);

        List<Float> verts = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();
        List<Integer> tris = new ArrayList<>();
        Texture texture = new Texture(Registry.getAtlas(0));//"/textures/pillar.png");

        int i = 0;
        for(int x = 0; x < CHUNK_SIZE; ++x){
            for(int z = 0; z < CHUNK_SIZE; ++z){
                for(int y = 0; y < CHUNK_HEIGHT; ++y){
                    MaterialBlock block = getBlock(x, y, z);

                    if(block != World.EMPTY){

                        int worldx = (CHUNK_SIZE*this.x) + x;
                        int worldz = (CHUNK_SIZE*this.y) + z;

                        MaterialBlock posx = world.getBlockAt(worldx+1, y, worldz);
                        MaterialBlock posy = y < CHUNK_HEIGHT-1 ? world.getBlockAt(worldx, y+1, worldz) : null;
                        MaterialBlock posz = world.getBlockAt(worldx, y, worldz+1);

                        MaterialBlock negx = world.getBlockAt(worldx-1, y, worldz);
                        MaterialBlock negy = y > 0 ? world.getBlockAt(worldx, y-1, worldz) : null;
                        MaterialBlock negz = world.getBlockAt(worldx, y, worldz-1);

                        boolean top = posy == null ? true : false;
                        boolean bottom = negy == null ? true : false;

                        boolean front = posz == null ? true : false;
                        boolean back = negz == null ? true : false;

                        boolean right = posx == null ? true : false;
                        boolean left = negx == null ? true : false;


                        if(top) {
                            verts.add(x + (-BLOCK_SIZE_HALF)); verts.add(y + BLOCK_SIZE_HALF); verts.add(z + (-BLOCK_SIZE_HALF));
                            verts.add(x + BLOCK_SIZE_HALF);    verts.add(y + BLOCK_SIZE_HALF); verts.add(z + (-BLOCK_SIZE_HALF));
                            verts.add(x + (-BLOCK_SIZE_HALF)); verts.add(y + BLOCK_SIZE_HALF); verts.add(z + BLOCK_SIZE_HALF);
                            verts.add(x + BLOCK_SIZE_HALF);    verts.add(y + BLOCK_SIZE_HALF); verts.add(z + BLOCK_SIZE_HALF);

                            Vector4f uv = block.getUVs(Side.Top);

                            /*uvs.add(1.0f/3); uvs.add(0.5f);
                            uvs.add(0.0f); uvs.add(0.5f);
                            uvs.add(1.0f/3); uvs.add(0.0f);
                            uvs.add(0.0f); uvs.add(0.0f);*/

                            uvs.add(uv.z); uvs.add(uv.w);
                            uvs.add(uv.x); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.y);
                            uvs.add(uv.x); uvs.add(uv.y);

//                            System.out.println(uv.toString());


                            tris.add(i + 0); tris.add(i + 2); tris.add(i + 3);
                            tris.add(i + 1); tris.add(i + 0);  tris.add(i + 3);

                            i += 4;
                        }else{

                        }

                        if(bottom) {
                            verts.add(x + (-BLOCK_SIZE_HALF)); verts.add(y + -BLOCK_SIZE_HALF); verts.add(z + (-BLOCK_SIZE_HALF));
                            verts.add(x + BLOCK_SIZE_HALF);    verts.add(y + -BLOCK_SIZE_HALF); verts.add(z + (-BLOCK_SIZE_HALF));
                            verts.add(x + (-BLOCK_SIZE_HALF)); verts.add(y + -BLOCK_SIZE_HALF); verts.add(z + BLOCK_SIZE_HALF);
                            verts.add(x + BLOCK_SIZE_HALF);    verts.add(y + -BLOCK_SIZE_HALF); verts.add(z + BLOCK_SIZE_HALF);

                            Vector4f uv = block.getUVs(Side.Bottom);

                            /*uvs.add(0.0f/3); uvs.add(0.5f);
                            uvs.add(1.0f); uvs.add(0.5f);
                            uvs.add(0.0f/3); uvs.add(1.0f);
                            uvs.add(1.0f); uvs.add(1.0f);*/

                            uvs.add(uv.x); uvs.add(uv.y);
                            uvs.add(uv.z); uvs.add(uv.y);
                            uvs.add(uv.x); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.w);


                            tris.add(i + 3); tris.add(i + 2); tris.add(i + 0);
                            tris.add(i + 3); tris.add(i + 0);  tris.add(i + 1);

                            i += 4;
                        }

                        if (front) {
                            verts.add(x+(-BLOCK_SIZE_HALF)); verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+BLOCK_SIZE_HALF);
                            verts.add(x+(-BLOCK_SIZE_HALF)); verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+BLOCK_SIZE_HALF);
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+BLOCK_SIZE_HALF);
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+BLOCK_SIZE_HALF);

                            Vector4f uv = block.getUVs(Side.Front);

                            /*uvs.add((1.0f/3)*2); uvs.add(0.0f);
                            uvs.add((1.0f/3)*2); uvs.add(0.5f);
                            uvs.add((1.0f/3)*3); uvs.add(0.5f);
                            uvs.add((1.0f/3)*3); uvs.add(0.0f);*/

                            uvs.add(uv.x); uvs.add(uv.y);
                            uvs.add(uv.x); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.y);

                            tris.add(i+0);  tris.add(i+1);  tris.add(i+3);
                            tris.add(i+3);  tris.add(i+1);  tris.add(i+2);

                            i += 4;

                        }

                        if(back){
                            verts.add(x+(-BLOCK_SIZE_HALF)); verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+(-BLOCK_SIZE_HALF)); verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+(-BLOCK_SIZE_HALF));

                            Vector4f uv = block.getUVs(Side.Front);

                            /*uvs.add((1.0f/3)*2); uvs.add(0.0f);
                            uvs.add((1.0f/3)); uvs.add(0.0f);
                            uvs.add((1.0f/3)*2); uvs.add(0.5f);
                            uvs.add((1.0f/3)); uvs.add(0.5f);*/

                            uvs.add(uv.z); uvs.add(uv.y);
                            uvs.add(uv.x); uvs.add(uv.y);
                            uvs.add(uv.z); uvs.add(uv.w);
                            uvs.add(uv.x); uvs.add(uv.w);

                            tris.add(i+3); tris.add(i+2); tris.add(i+0);
                            tris.add(i+3); tris.add(i+0); tris.add(i+1);

                            i += 4;
                        }

                        if (right){
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+BLOCK_SIZE_HALF);
                            verts.add(x+BLOCK_SIZE_HALF);    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+BLOCK_SIZE_HALF);

                            Vector4f uv = block.getUVs(Side.Right);

                            /*uvs.add((1.0f/3)*2); uvs.add(0.5f);
                            uvs.add((1.0f/3)*2); uvs.add(1.0f);
                            uvs.add((1.0f/3)); uvs.add(1.0f);
                            uvs.add((1.0f/3)); uvs.add(0.5f);*/

                            uvs.add(uv.z); uvs.add(uv.y);
                            uvs.add(uv.z); uvs.add(uv.w);
                            uvs.add(uv.x); uvs.add(uv.w);
                            uvs.add(uv.x); uvs.add(uv.y);

                            tris.add(i+2); tris.add(i+1); tris.add(i+0);
                            tris.add(i+0); tris.add(i+3); tris.add(i+2);

                            i += 4;
                        }

                        if (left){
                            verts.add(x+(-BLOCK_SIZE_HALF));    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+(-BLOCK_SIZE_HALF));    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+(-BLOCK_SIZE_HALF));
                            verts.add(x+(-BLOCK_SIZE_HALF));    verts.add(y+(-BLOCK_SIZE_HALF)); verts.add(z+BLOCK_SIZE_HALF);
                            verts.add(x+(-BLOCK_SIZE_HALF));    verts.add(y+BLOCK_SIZE_HALF);    verts.add(z+BLOCK_SIZE_HALF);

//                            uvs.add((1.0f/3)); uvs.add(0.5f);
//                            uvs.add((1.0f/3)); uvs.add(1.0f);
//                            uvs.add((1.0f/3)*2); uvs.add(1.0f);
//                            uvs.add((1.0f/3)*2); uvs.add(0.5f);

                            Vector4f uv = block.getUVs(Side.Right);

                            /*uvs.add((1.0f/3)*2); uvs.add(0.5f);
                            uvs.add((1.0f/3)*2); uvs.add(1.0f);
                            uvs.add((1.0f/3)*3); uvs.add(1.0f);
                            uvs.add((1.0f/3)*3); uvs.add(0.5f);*/

                            uvs.add(uv.x); uvs.add(uv.y);
                            uvs.add(uv.x); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.w);
                            uvs.add(uv.z); uvs.add(uv.y);

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

        collider = world.getPhysics().addMesh(new Vector3f(Chunk.PHYSICAL_SIZE*x, 0, Chunk.PHYSICAL_SIZE*y), mesh);
    }

    public void render(ShaderProgram shaderProgram){
        if(mesh != null) {
            Matrix4f viewMatrix = Client.game().getRenderer().getTransformation().getViewMatrix(Client.game().thePlayer().getCamera());
            Matrix4f modelViewMatrix = Client.game().getRenderer().getTransformation().getBasicViewMatrix(new Vector3f(PHYSICAL_SIZE*x, 0, PHYSICAL_SIZE*y), new Vector3f(1, 1, 1), viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            mesh.render();
        }
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
