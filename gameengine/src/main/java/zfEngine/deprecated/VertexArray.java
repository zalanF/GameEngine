package zfEngine.deprecated;

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

		/*for(int i = 0; i < layout.length(); i++) {
			glEnableVertexAttribArray(i);
			glVertexAttribPointer(i, layout.getSize(i), layout.getType(), layout.isNormalized(), layout.getStride(i), 0);
			
		}*/
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2*2, GL_FLOAT, false, 8*2, 0);
		//glEnableVertexAttribArray(1);
		//glVertexAttribPointer(1, 2, layout.getType(), layout.isNormalized(), 8, 0);
		//glVertexAttribPointer(0, layout.getSize(), layout.getType(), layout.isNormalized(), layout.getStride(), 0);
	}

	public void bind() {
		glBindVertexArray(rendererId);
	}

	public void unBind() {
		glBindVertexArray(0);
	}
}
