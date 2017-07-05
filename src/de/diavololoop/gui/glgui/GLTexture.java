package de.diavololoop.gui.glgui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GLTexture {

    public final int textureID;
    public final int width;
    public final int height;

    public GLTexture(BufferedImage image){
        this(image, GL11.GL_LINEAR);
    }

    public GLTexture(BufferedImage image, int filter){
        textureID = GL11.glGenTextures();
        width = image.getWidth();
        height = image.getHeight();

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for(int j = image.getHeight()-1; j >= 0; --j){
            for(int i = 0; i < image.getWidth(); ++i){
                int argb = image.getRGB(i,  j);
                buffer.put((byte) (argb>>16 & 0xFF));
                buffer.put((byte) (argb>>8 & 0xFF));
                buffer.put((byte) (argb & 0xFF));
                buffer.put((byte) (argb>>24 & 0xFF));
            }
        }

        buffer.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);
    }

    @Override
    public void finalize(){
        GL11.glDeleteTextures(textureID);
    }


}
