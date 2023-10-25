package lwjgl;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.GL_FLOAT;

public class VertexArray {
	
	private int rendererId;
	
	public VertexArray() {
		rendererId = glGenVertexArrays();
		glBindVertexArray(rendererId);
		
	}
	
	public VertexArray(VertexBuffer vb, VertexBufferLayout layout) {
		rendererId = glGenVertexArrays();
		glBindVertexArray(rendererId);
		
		addBuffer(vb, layout);
		
	}
	
	public void addBuffer(VertexBuffer vb, VertexBufferLayout layout) {
		
		glBindVertexArray(rendererId);
		
		vb.bind();

		glEnableVertexAttribArray(0);
		
        glVertexAttribPointer(0, layout.getSize(), layout.getType(), layout.isNormalized(), layout.getStride(), 0);
	}
	
	public void bind() {
		glBindVertexArray(rendererId);
	}
	
	public void unBind() {
		glBindVertexArray(0);
	}
}
