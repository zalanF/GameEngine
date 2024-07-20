package zfEngine.deprecated;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import zfEngine.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FirstProject {

	long window;
	int width = 1250;
	int height = 1000;

	private Renderer renderer;
	private Shader shader;
	private VertexArray va;
	private IndexBuffer ib;
	private Texture texture;
	private float r, increment;
	int vao, vbo, ibo;

	private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

	/*private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		}
	};*/
	
	void processInput(long window) {
		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetWindowShouldClose(window, true);
		}
	}

	private static Callback debugProc;

	private void init() {
		// Print current version to console as test
		System.out.println("LWJGL Version" + Version.getVersion());

		// Set error callback - have to be a strong reference bcuz of java
		// errorCallback.set(); //Idk if this is the same as below
		glfwSetErrorCallback(errorCallback);

		// Check if GLFW set up properly
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to intialize GLFW");
		}

		// WindowHints
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		// Create window
		window = glfwCreateWindow(width, height, "My Window", NULL, NULL);

		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window");
		}

		// Setting up key inputs
		//glfwSetKeyCallback(window, keyCallback);
		processInput(window);
		
		// OpenGL context
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(1); // 0 = unlimited : 1 = monitor refresh rate

		debugProc = GLUtil.setupDebugMessageCallback();

		/**
		 * Data
		 */

		// Vertex Buffer
		float[] positions = { -0.5f, -0.5f, 0.0f, 0.0f, // 0
				0.5f, -0.5f, 1.0f, 0.0f, // 1
				0.5f, 0.5f, 1.0f, 1.0f, // 2
				-0.5f, 0.5f, 0.0f, 1.0f // 3
		};
		
		float[] positions2 = { -0.5f, -0.5f, // 0
				0.5f, -0.5f,  // 1
				0.5f, 0.5f, // 2
				-0.5f, 0.5f // 3
		};
		
		float[] positions3 = { 0.0f, 0.0f, // 0
				1.0f, 0.0f,  // 1
				1.0f, 1.0f, // 2
				0.0f, 1.0f // 3
		};
		
		/**
		 * Write up on the issues with loading the texture:
		 * I believe that LWJGL vastly oversimplifies the process by which data is bound.
		 * The reprecutions of this is that assigning multiple values through one array is not really a thing.
		 * Potentially, this could be fixed by using buffers since they may work differently.
		 * Overall, i think the solution is that the texture mapping and vetrices of the object must be in a seperate array, and bound seperately
		 * I dont know how that will work though since 
		 */

		// Index Buffer
		int[] indices = { 0, 1, 2, 
						  2, 3, 0 };

		/**
		 * Buffer Objects and Rendering
		 */
		va = new VertexArray();
		/*vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
		
			//VBO
		vbo = glGenBuffers();
		
		FloatBuffer positionsBuffer = stack.callocFloat(positions2.length);
		positionsBuffer.put(positions2).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 4*2, 0);
		
		//TEXTURE
		//Bind the texture coordinates to the vbo to map the texture appropriately 
		FloatBuffer textCoordsBuffer = stack.callocFloat(positions2.length);
        textCoordsBuffer.put(0, positions2);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 4*2, 0);
		
		//IBO
        ibo = glGenBuffers();
        IntBuffer indicesBuffer = stack.callocInt(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindVertexArray(vao);
		
		}*/
		VertexBuffer vb = new VertexBuffer(positions2);
		//vb.addSubData(positions3, 4*2*4+1);

		//VertexBufferLayout layout = new VertexBufferLayout(2, GL_FLOAT, false);
		VertexBufferLayout layout = new VertexBufferLayout(GL_FLOAT, false);
		layout.pushf(2);
		//layout.pushf(2);
		va.addBuffer(vb, layout);

		ib = new IndexBuffer(indices);

		/**
		 * Shaders
		 */

		shader = new Shader("src\\main\\resources\\shaders\\vertex.glsl",
				"src\\main\\resources\\shaders\\fragment.glsl");
		shader.bind();
		shader.setUniform("u_Color", 0.5f, 0.5f, 0.5f, 1.0f);

		/**
		 * Textures
		 */
		texture = new Texture("src\\main\\resources\\textures\\forsenE.png");
		texture.bind();
		shader.setUniform("u_Texture", 0); // matching texture slot (0 by default)
		/**
		 * Clearing states for test
		 */

		va.unBind();
		shader.unbind();
		vb.unBind();
		ib.unBind();
		
		/*glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);*/

		
		renderer = new Renderer();

		r = 0.0f;
		increment = 0.05f;

	}

	private void loop() {
		while (!glfwWindowShouldClose(window)) {
			// double time = glfwGetTime();
			
			processInput(window);
			
			renderer.clear();
			
			/*glClear(GL_COLOR_BUFFER_BIT);*/
			
			shader.bind();
			shader.setUniform("u_Color", r, 0.0f, 1.0f, 1.0f);
			
			
			/*glBindVertexArray(vao);
	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);*/

			ib.bind();
			/*glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);*/
			renderer.draw(va, ib, shader);

			if (r > 1.0f) {
				increment = -0.05f;
			} else if (r < 0.0f) {
				increment = 0.05f;
			}

			r += increment;

			// GLFW uses double buffering
			glfwSwapBuffers(window);
			glfwPollEvents();
			// glfwWaitEvents() can be used if application only needs to be update on new
			// input
		}
	}

	private void run() {
		try {
			init();
			loop();

			glfwDestroyWindow(window);
			shader.delete();

			if (debugProc != null) {
				debugProc.free();
			}

			//keyCallback.free();
			errorCallback.free();

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			glfwTerminate();

		}
	}

	public static void main(String[] args) {
		new FirstProject().run();
	}

}
