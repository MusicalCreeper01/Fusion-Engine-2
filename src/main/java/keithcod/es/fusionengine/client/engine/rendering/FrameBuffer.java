package keithcod.es.fusionengine.client.engine.rendering;

import org.lwjgl.opengl.EXTFramebufferObject;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;


public class FrameBuffer {
    private int fbo;
    public int colortexture = -1;
    public int depthtexture = -1;
    private int depth;
    private int backbuffer;

    int width;
    int height;

    public boolean disposed = false;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;


        fbo = glGenFramebuffersEXT();
        if(colortexture == -1)
            colortexture = glGenTextures();
        if(depthtexture == -1)
            depthtexture = glGenTextures();

        depth = glGenRenderbuffersEXT();

        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        glBindTexture(GL_TEXTURE_2D, colortexture);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);               // make it linear filterd
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data

        glBindTexture(GL_TEXTURE_2D, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colortexture, 0); // attach it to the framebuffer

        //GL_DEPTH_ATTACHMENT
        depth = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depth);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depth);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glBindTexture(GL_TEXTURE_2D, depthtexture);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);               // make it linear filterd
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_FLOAT, (java.nio.ByteBuffer) null);

        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT );
        switch ( framebuffer ) {
            case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
                break;
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
            default:
                throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
        disposed = false;

    }

    public void resize(int width, int height){
        dispose();
        disposed = false;

        init(width, height);

        this.width = width;
        this.height = height;

    }

    public void bind(){
        glViewport (0, 0, width, height);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

//        glClearColor (0.0f, 0.0f, 0.0f, 0.5f);
        glClearColor (0.56f, 0.76f, 0.83f, 1f);
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void unbind(int width, int height){
        glBindTexture( GL_TEXTURE_2D, depthtexture);
        glCopyTexImage2D( GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, 0, 0, width, height, 0 );

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glClearColor (0.0f, 1.0f, 0.0f, 0.5f);
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glViewport(0,0,width,height);

        glEnable(GL_TEXTURE_2D);

        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, colortexture);

        glActiveTexture( GL_TEXTURE1 );
        glBindTexture( GL_TEXTURE_2D, depthtexture);
    }

    public void dispose(){
        if (fbo==0)
            return;
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glDeleteFramebuffersEXT(fbo);
        fbo = 0;
        disposed = true;
    }
}

/*
public class FrameBuffer {

//    public static boolean isSupported() {
//        return GL.getCapabilities().GL_EXT_framebuffer_object;
//    }

    private int fbo;
    public int texture;

    int width;
    int height;

    public FrameBuffer(){
//        if(!isSupported())
//            System.out.println("Frame buffers are not supported!");

    }

    public void init(int width, int height){
        this.width = width;
        this.height = height;

        fbo = glGenFramebuffersEXT();

        glBindFramebufferEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo );


        int depthbuffer = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthbuffer);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthbuffer);

        // Create Texture
//        texture = createTexture();
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, NULL);
        glBindTexture(GL_TEXTURE_2D, 0);
        // ./Create Texture


//        glFramebufferTexture2DEXT( GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0);
//        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0);


        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, texture, 0);

        int result = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER);

//        if (result!=GL_FRAMEBUFFER_COMPLETE) {
//            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
//            glDeleteFramebuffers(fbo);
//            throw new RuntimeException( "Failed to create FBO " + fbo );
//        }

        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT );
        switch ( framebuffer ) {
            case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
                break;
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                throw new RuntimeException( "FrameBuffer: " + fbo
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
            default:
                throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }

//        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public int createTexture(){
//         int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, texture);
//
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
//
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_INT, NULL);
//        return id;

        glEnable(GL_TEXTURE_2D);

        int id = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, id);

//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);

        return id;
    }

    public void bind(){
        if (fbo == 0)
            throw new IllegalStateException("can't use FBO as it has been destroyed..");
        glViewport( 0, 0, width, height );
        glBindFramebufferEXT( GL_FRAMEBUFFER_EXT, fbo );
//        glPushAttrib(GL_VIEWPORT_BIT);

//        glClear(GL_COLOR_BUFFER_BIT);
    }


    public void unbind(int dispwidth, int dispheight){
        if (fbo==0)
            return;
        glViewport( 0, 0, dispwidth, dispheight );
        glBindFramebufferEXT( GL_FRAMEBUFFER_EXT, 0);
//        glPopAttrib();
    }

    public void dispose(){
        if (fbo==0)
            return;
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glDeleteFramebuffersEXT(fbo);
        fbo = 0;
    }
}*/
