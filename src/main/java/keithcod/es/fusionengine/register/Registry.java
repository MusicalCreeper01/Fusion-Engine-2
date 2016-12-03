package keithcod.es.fusionengine.register;

import de.matthiasmann.twl.utils.PNGDecoder;
import keithcod.es.fusionengine.client.engine.rendering.Texture;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Registry {

    public static final String DOMAIN = "";

    static Map<String, Integer> nextID = new HashMap<>();
    static Map<String, Map<Integer, String>> registeredTextures = new HashMap<>();

    public static int registerTexture(String domain, String s){
        int id = nextID.get(domain);
        nextID.put(domain, ++id);

        if(!registeredTextures.containsKey(domain))
            registeredTextures.put(domain, new HashMap<>());

        registeredTextures.get(domain).put(id, s);

        return id;
    }

    public static void buildAtlas(String domain){
        if(!registeredTextures.containsKey(domain)){
            System.err.println("Cannot build atlas " + domain  + " because it doesn't exist!");
        }
        //float[] atlas = new float[2048*2048*3];
//        byte[] atlas = new byte[2048*2048*3];

        // Load texture contents into a byte buffer
//        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 2048 * 2048);

        byte[] atlas = new byte[2048 * 2048 * 4];
        int targetX = 0;
        int targetY = 0;

        for(Integer i: registeredTextures.get(domain).keySet()){
            String s = registeredTextures.get(domain).get(i);
            try {
                // Load Texture file
                PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(s));

                // Load texture contents into a byte buffer
                ByteBuffer buf = ByteBuffer.allocateDirect(
                        4 * decoder.getWidth() * decoder.getHeight());
                decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                buf.flip();

                byte[] img = new byte[buf.remaining()];
                buf.get(img);

                if (targetX + decoder.getWidth() > 2048) {
                    targetX = 0;
                    targetY += decoder.getHeight();
                }

                for(int sourceY = 0; sourceY < decoder.getHeight(); ++sourceY) {
                    for(int sourceX = 0; sourceX < decoder.getWidth(); ++sourceX) {
                        int from = (sourceY * 128 * 4) + (sourceX * 4); // 4 bytes per pixel (assuming RGBA)
                        int to = ((targetY + sourceY) * 512 * 4) + ((targetX + sourceX) * 4); // same format as source

                        for(int channel = 0; channel < 4; ++channel) {
                            atlas[to + channel] = img[from + channel];
                        }
                    }
                }

            }catch(IOException ex){
                System.err.println("Error adding texture \"" + s + "\" to the atlas");
            }

        }



        Map<Integer, String> textures = registeredTextures.get(domain);

    }
}
