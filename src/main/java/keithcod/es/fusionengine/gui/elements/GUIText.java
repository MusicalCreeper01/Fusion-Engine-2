package keithcod.es.fusionengine.gui.elements;

import keithcod.es.fusionengine.core.*;
import keithcod.es.fusionengine.gui.GUIManager;
import org.joml.Vector2i;

import java.awt.*;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Created by ldd20 on 11/30/2016.
 */
public class GUIText extends GUIElement {

    public Vector2i position = new Vector2i(0,0);
    public Vector2i size = new Vector2i(280, 60);

    private Font font;

    private String text;

    public GUIText (String fontpath, String text){
        try {
            this.text = text;
            File file = new File(fontpath);
            font = Font.createFont(Font.TRUETYPE_FONT, file);

            font = font.deriveFont(Font.PLAIN, 48.0f);

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

    public Texture build(Texture tex) {
        if(!show)
            return tex;

        if(font != null) {

            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
            int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
            int textheight = (int)(font.getStringBounds(text, frc).getHeight())+5;

//            System.out.println("font size: " + font.getSize());

            size = new Vector2i(textwidth, textheight);

//            System.out.println("Texture size: (" + size.x + "," + size.y + ")");

//            size = new Vector2i((int) font.getStringBounds(text, new FontRenderContext(font.getTransform(), false, false)).getBounds().getWidth(), font.getSize());

            BufferedImage image = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

//            g.setColor(new Color(0, 0, 0, 0));
//            g.drawRect(0, 0, size.x, size.y);

//            g.setPaint(Color.red);
//            g.setColor(new Color(255, 0, 0, 255));
//            g.setFont(font);
//            g.drawString(text, 0, 0);

            g.setPaint(Color.red);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int fx = image.getWidth() - fm.stringWidth(text) - 5;
            int fy = fm.getHeight();
            g.drawString(text, 0, fy);

            g.dispose();

            this.texture = new Texture(size.x, size.y);

            for (int x = 0; x < image.getWidth(); ++x) {
                for (int y = 0; y < image.getHeight(); ++y) {
                    Color c = new Color(image.getRGB(x, y), true);
                    try {
//                        System.out.print(c.getRed() + ", ");
                        texture.setPixel(new keithcod.es.fusionengine.core.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()), x, y);
                    } catch (Exception ex) {
                        System.out.println("Failed to set texture color from font drawing for (" + x + ":" + y + ") in texture (" + size.x + "," + size.y + ")");
                    }
                }
//                System.out.print("\n");
            }


            for(GUIElement el : children)
                el.build(texture);

            if(tex != null)
                tex.combine(texture, position.x, position.y);

        }
        return tex;
    }
}
