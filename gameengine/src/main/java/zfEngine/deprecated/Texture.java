package zfEngine.deprecated;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {

	private int rendererId;
	private String filePath;
	private int width, height;

	public Texture(String filePath) {
		this.filePath = filePath;

		try (MemoryStack stack = MemoryStack.stackPush()) {

			// Have to use buffers since stbi requires it
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer bpp = stack.mallocInt(1);

			stbi_set_flip_vertically_on_load(true);

			ByteBuffer image = stbi_load(filePath, w, h, bpp, 4);
			
			if (image == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();
			
			rendererId = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, rendererId);

			// HAVE TO SPECIFY THESE 4
			//gl_linear
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE); // do not extend texture
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE); // do not extend texture

			// Store data on the GPU
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			
	        glGenerateMipmap(GL_TEXTURE_2D);
			
			image.clear();
			glBindTexture(GL_TEXTURE_2D, 0);
		}

		/*
		 * if (image.hasRemaining()) { stbi_image_free(image); }
		 */
	}

	public void bind(int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, rendererId);
	}

	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, rendererId);
	}

	public void unBind() {
		glBindTexture(GL_TEXTURE_2D, 0);

	}

	/*
	 * public int getWidth() { return width; }
	 * 
	 * public int getHeight() { return height; }
	 */

}
