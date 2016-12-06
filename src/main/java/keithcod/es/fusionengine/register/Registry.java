package keithcod.es.fusionengine.register;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.lwjgl.opengl.ARBTextureStorage.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Registry {

//    public static final String DOMAIN = "";

    static Map<Domain, Integer> nextID = new HashMap<>();
    static Map<Domain, LinkedHashMap<Integer, String>> registeredTextures = new HashMap<>();
    public static Map<Domain, Integer> atlases = new HashMap<>();
    static Map<Domain, LinkedHashMap<Integer, Vector4f>> uvs = new HashMap<>();

    public static Integer getAtlas(int index){
        return atlases.get( (atlases.keySet().toArray())[ index ] );
    }

    public static int registerTexture(Domain domain, String s){

        if(!nextID.containsKey(domain))
            nextID.put(domain, 0);
        if(!registeredTextures.containsKey(domain))
            registeredTextures.put(domain, new LinkedHashMap<>());

        //TODO: make more efficient method
        if(registeredTextures.get(domain).containsValue(s)){
            for (Map.Entry<Integer, String> e : registeredTextures.get(domain).entrySet()) {
                if(e.getValue().equals(s))
                        return e.getKey();
            }
        }


        int id = nextID.get(domain);
        nextID.put(domain, ++id);



        registeredTextures.get(domain).put(id, s);

        return id;
    }

    public static void buildAtlas(Domain domain){
        long start = System.currentTimeMillis();

        if(!registeredTextures.containsKey(domain)){
            System.err.println("Cannot build atlas " + domain  + " because it doesn't exist!");
        }
        //float[] atlas = new float[2048*2048*3];
//        byte[] atlas = new byte[2048*2048*3];

        // Load texture contents into a byte buffer
//        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 2048 * 2048);

        /*byte[] atlas = new byte[2048 * 2048 * 4];
        int targetX = 0;
        int targetY = 0;*/

        Map<Integer, PNGDecoder> decoders = new HashMap<>();

        int atlaswidth = 0;
        int atlasheight = 0;

        for(Integer i: registeredTextures.get(domain).keySet()){
            String s = registeredTextures.get(domain).get(i);
            try {
                // Load Texture file
                PNGDecoder decoder = new PNGDecoder(new FileInputStream(s));

                System.out.println("Loading texture " + i + " to atlas: " + s);

                decoders.put(i, decoder);
                atlaswidth += decoder.getWidth();
                if(decoder.getHeight() > atlasheight) atlasheight = decoder.getHeight();
            }catch(IOException ex){
                System.err.println("Error adding texture \"" + s + "\" to the atlas, cannot read file!");
                Vector4f v4 = new Vector4f(0,0,1,1);
                if(!uvs.containsKey(domain))
                    uvs.put(domain, new LinkedHashMap<>());
                uvs.get(domain).put(i, v4);
            }catch(NullPointerException ex){
                System.err.println("Error adding texture \"" + s + "\" to the atlas, cannot find texture file!");
            }
        }

        glActiveTexture(GL_TEXTURE0);
        // Create a new OpenGL texture
        int textureId = 0;
        if(atlases.containsKey(domain))
            textureId = atlases.get(domain);
        else
            textureId = glGenTextures();

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexStorage2D(GL_TEXTURE_2D, 3, GL_RGBA8, atlaswidth, atlasheight);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, atlaswidth, atlasheight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 3);

//        glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE);

        int x = 0;

        for(int i : decoders.keySet()) {
            try {
                PNGDecoder decoder = decoders.get(i);

                ByteBuffer buf = ByteBuffer.allocateDirect(
                        4 * decoder.getWidth() * decoder.getHeight());
                decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                buf.flip();

                if(!uvs.containsKey(domain))
                    uvs.put(domain, new LinkedHashMap<>());

                Vector4f v4 = new Vector4f(((float)x+0.001f) / atlaswidth, ((float)0)+0.001f, ((float)x+decoder.getWidth()-0.001f)/atlaswidth, ((float)decoder.getHeight()-0.001f) / atlasheight);
                uvs.get(domain).put(i, v4);

                System.out.println(x + ":(" + atlaswidth + "," + atlasheight + ") (" + decoder.getWidth() + "," + decoder.getHeight() + ")");
//                System.out.println(((float)x+decoder.getWidth())+ "/" + atlaswidth);
                System.out.println("(" + v4.x + ", " + v4.y + ", " + v4.z + ", " + v4.w + ")");

                glTexSubImage2D(GL_TEXTURE_2D, 0, x, 0, decoder.getWidth(), decoder.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buf);

                x += decoder.getWidth();

            }catch(IOException ex){
                System.out.println("Error adding texture to atlas! ");
            }

        }

        glEnable(GL_TEXTURE_2D);
        glGenerateMipmap(GL_TEXTURE_2D);

        System.out.println(domain.name + ":atlas (" + atlaswidth + ":" + atlasheight + ")");

        atlases.put(domain, textureId);


        System.out.println("Took " + ((start - System.currentTimeMillis())/1000) + "s to create atlas");
    }

    public static Vector4f getUVs(Domain domain, int id){
        if(uvs.containsKey(domain))
            if(uvs.get(domain).containsKey(id))
                return uvs.get(domain).get(id);

        return new Vector4f(0,0,1,1);
    }
}


