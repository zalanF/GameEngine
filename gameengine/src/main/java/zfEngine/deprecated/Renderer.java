package zfEngine.deprecated;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;

import zfEngine.Shader;

public class Renderer {

	public Renderer() {

	}

	public void draw(VertexArray va, IndexBuffer ib, Shader shader) {
		shader.bind();
		va.bind();
		ib.bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		// can add unbinds

	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT);
	}

}
