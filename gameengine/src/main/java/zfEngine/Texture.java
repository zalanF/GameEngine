package zfEngine;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

enum TextureWrapping {
	REPREAT(GL_REPEAT),
	MIRRORED_REPREAT(GL_MIRRORED_REPEAT),
	CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
	CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER);
	
	public final int GL;
	
	private TextureWrapping(int GL) {
		this.GL = GL;
	}
}

enum TextureFilters {
	NEAREST(GL_NEAREST),
	LINEAR(GL_LINEAR),
	NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST),
	LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
	NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
	LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);
	
	public final int GL;
	
	private TextureFilters(int GL) {
		this.GL = GL;
	}
}

enum FileType {
	JPG,
	PNG
}

public class Texture {
	
	private int rendererId;
	private int textureUnit;
	
	private int width;
	private int height;
	
	public Texture() {
		rendererId = glGenTextures();
		this.textureUnit = 0;
	}
	
	public Texture(int textureUnit) {
		rendererId = glGenTextures();
		this.textureUnit = textureUnit;
	}
	
	public Texture(String filePath, int textureUnit) {
		rendererId = glGenTextures();
		this.textureUnit = textureUnit;
		
		bind();
		
		setTextureWrapping(TextureWrapping.REPREAT, TextureWrapping.REPREAT);
		setTextureFiltering(TextureFilters.LINEAR_MIPMAP_LINEAR, TextureFilters.LINEAR);
		
		createTexture(loadImage(filePath), getFileType(filePath));
	}
	
	
	public void bind() {
		glActiveTexture(GL_TEXTURE0+textureUnit);
		glBindTexture(GL_TEXTURE_2D, rendererId);
	}
	
	/**
	 * 
	 * @param textureUnit
	 */
	public void bind(int textureUnit) {
		this.textureUnit = textureUnit;
		glActiveTexture(GL_TEXTURE0+textureUnit);
		glBindTexture(GL_TEXTURE_2D, rendererId);
	}
	
	
	public void unbind() {
		glActiveTexture(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
	}
	
	public int getTextureUnit() {
		return this.textureUnit;
	}
	
	
	public void setTextureWrapping(TextureWrapping sAxis, TextureWrapping rAxis) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, sAxis.GL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, rAxis.GL);
	}
	
	public void setTextureFiltering(TextureFilters minifying, TextureFilters magnifying) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minifying.GL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magnifying.GL);
	}
	
	public ByteBuffer loadImage(String filePath) {
		ByteBuffer image;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			IntBuffer nrChannels = stack.mallocInt(1);
			
			stbi_set_flip_vertically_on_load(true);
			
			image = stbi_load(filePath, widthBuffer, heightBuffer, nrChannels, 0);
			
			if (image == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}	
			
			width = widthBuffer.get();
			height = heightBuffer.get();
			
			stbi_image_free(image);
		
		}
		
		return image;
		
	}
	
	public void createTexture(ByteBuffer image, FileType fileType) {	
		
		//NO NEED TO CHECK IF FileType IS INVALID SINCE loadImage WILL 
		//THROW AN ERROR IF THE FILE IS INVALID
		
		if (fileType == FileType.JPG) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);

		} else if (fileType == FileType.PNG) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		}

		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public FileType getFileType(String filePath) {
		FileType ft = null;
		
		if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
			ft = FileType.JPG;
		} else if (filePath.endsWith(".png")) {
			ft = FileType.PNG;
		}
		
		
		return ft;

	}
}
