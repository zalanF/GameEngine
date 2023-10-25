package lwjgl;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.FloatBuffer;

public class VertexBuffer {
	
	private int rendererId;
	
	public VertexBuffer(FloatBuffer data) {
		
		rendererId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, rendererId);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		
	}
	
	public VertexBuffer(float[] data) {
		
		rendererId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, rendererId);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);		
		
	}
	
	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, rendererId);
		
	}
	
	public void unBind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
	}

}
