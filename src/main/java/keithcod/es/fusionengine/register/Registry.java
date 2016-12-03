package keithcod.es.fusionengine.register;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Registry {

    public static final String DOMAIN = "";

    static Map<Domain, Integer> nextID = new HashMap<>();
    static Map<Domain, Map<Integer, String>> registeredTextures = new HashMap<>();
    public static Map<Domain, Integer> atlases = new HashMap<>();
    static Map<Domain, Map<Integer, Vector4f>> uvs = new HashMap<>();

    public static Integer getAtlas(int index){
        return atlases.get( (atlases.keySet().toArray())[ index ] );
    }

    public static int registerTexture(Domain domain, String s){
        if(!nextID.containsKey(domain))
            nextID.put(domain, 0);
        if(!registeredTextures.containsKey(domain))
            registeredTextures.put(domain, new HashMap<>());

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

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, atlaswidth, atlasheight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        int x = 0;

        for(int i : decoders.keySet()) {
            try {
                PNGDecoder decoder = decoders.get(i);

                ByteBuffer buf = ByteBuffer.allocateDirect(
                        4 * decoder.getWidth() * decoder.getHeight());
                decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                buf.flip();

                if(!uvs.containsKey(domain))
                    uvs.put(domain, new HashMap<>());

                uvs.get(domain).put(i, new Vector4f(x / atlaswidth, 0, (x+decoder.getWidth())/atlaswidth, decoder.getHeight() / atlasheight));

                glTexSubImage2D(GL_TEXTURE_2D, 0, x, 0, decoder.getWidth(), decoder.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buf);

                x += decoder.getWidth();

            }catch(IOException ex){
                System.out.println("Error adding texture to atlas! ");
            }

        }

        System.out.println(domain.name + ":atlas (" + atlaswidth + ":" + atlasheight + ")");

        atlases.put(domain, textureId);

    }
}
