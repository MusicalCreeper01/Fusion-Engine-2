package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.client.engine.rendering.Texture;
import keithcod.es.fusionengine.gui.GUIManager;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;


import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class GUITruetypeText extends GUIElement {

    public Vector2i position = new Vector2i(0,0);
    public Vector2i size = new Vector2i(280, 60);

    private Font font;

    private String text;

    public GUITruetypeText(String fontpath, String text){
        this(fontpath, text, 0, 0);
    }

    public GUITruetypeText(String fontpath, String text, int x, int y){
        this(fontpath, text, 84.0f, x, y);
    }
    public GUITruetypeText(String fontpath, String text, float fontsize, int x, int y){
        try {
            this.position = new Vector2i(x, y);
            this.text = text;
            File file = new File(fontpath);
            font = Font.createFont(Font.TRUETYPE_FONT, file);

            font = font.deriveFont(Font.PLAIN, fontsize);

        }catch(IOException ex){
            System.err.println("Failed to load font " + fontpath);
        }catch (FontFormatException ex){
            System.err.println("Error with font format! " + fontpath);
        }
    }

    public void setText (String text){
        this.text = text;
        if(GUIManager.get() != null)
            GUIManager.get().build();
    }
    public void build(keithcod.es.fusionengine.client.engine.Window window) {

        if(font != null) {

            /*AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
            int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
            int textheight = (int)(font.getStringBounds(text, frc).getHeight())+5;

            this.size = new Vector2i(textwidth, textheight);
            super.size = this.size;

            super.build(window, true);*/

            {
                BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setFont(font);
                FontMetrics metrics = g.getFontMetrics();

                this.size = new Vector2i(metrics.stringWidth(text), metrics.getHeight());
                super.position = position;
                super.size = this.size;

                super.build(window, true);
            }

            BufferedImage image = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

            g.setPaint(new Color(0,0,0,0));
            g.fillRect(0,0,image.getWidth(), image.getHeight());

            g.setPaint(Color.white);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();

            FontRenderContext frc = g.getFontRenderContext();


            int fx = image.getWidth() - fm.stringWidth(text) - 5;
            int fy = (int)font.getLineMetrics(text, frc).getHeight()-fm.getDescent(); //fm.getHeight()-5;
            g.drawString(text, 0, fy);

            g.dispose();

            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));               // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }

            buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

            // You now have a ByteBuffer filled with the color data of each pixel.
            // Now just create a texture ID and bind it. Then you can load it using
            // whatever OpenGL method you want, for example:

            int textureID = glGenTextures(); //Generate texture ID
            glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

            //Setup wrap mode
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            //Send texel data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            this.texture = new Texture(textureID);

            /*for (int x = 0; x < image.getWidth(); ++x) {
                for (int y = 0; y < image.getHeight(); ++y) {
                    Color c = new Color(image.getRGB(x, y), true);
                    try {
                        texture.setPixel(new keithcod.es.fusionengine.core.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()), x, y);
                    } catch (Exception ex) {
                        System.out.println("Failed to set texture color from font drawing for (" + x + ":" + y + ") in texture (" + size.x + "," + size.y + ")");
                    }
                }
//                System.out.print("\n");
            }*/


            for(GUIElement el : children)
                el.build(window);

            /*if(tex != null)
                tex.combine(texture, position.x, position.y);*/

        }
//        return tex;
    }
}
