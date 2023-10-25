package lwjgl;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.IntBuffer;

public class IndexBuffer {
	
	private int rendererId;
	
	public IndexBuffer(IntBuffer data) {
		rendererId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}
	
	public IndexBuffer(int[] data) {
		rendererId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);		
		
	}
	
	public void bind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererId);
		
	}
	
	public void unBind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}

}
