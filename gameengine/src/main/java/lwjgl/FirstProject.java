package lwjgl;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;



public class FirstProject {
		
	private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
	
	private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		}
	};
	
	static Callback debugProc;
	
	public static void main(String[] args) {
		//Print current version to console as test
		System.out.println("LWJGL Version"+Version.getVersion());
		
		//Set error callback - have to be a strong reference bcuz of java
		//errorCallback.set(); //Idk if this is the same as below
		glfwSetErrorCallback(errorCallback);
		
		//Check if GLFW set up properly
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to intialize GLFW");
		}
		
		//WindowHints
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		
		//Create window
		long window = glfwCreateWindow(750, 750, "Simple Example", NULL, NULL);
		
		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window");
		}
		
		//Setting up key inputs
		glfwSetKeyCallback(window, keyCallback);
		
		//OpenGL context
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		glfwSwapInterval(1); //0 = unlimited : 1 = monitor refresh rate
		
        debugProc = GLUtil.setupDebugMessageCallback();

		
		/**
		 * Data
		 */
		
        //Vertex Buffer
        float[] positions = {
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f
        };

        //Index Buffer
        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };
		
		/**
		 * Buffer Objects and Rendering
		 */
        VertexArray va = new VertexArray();
        
        VertexBuffer vb = new VertexBuffer(positions);
        
        VertexBufferLayout layout = new VertexBufferLayout(2, GL_FLOAT, false);
      	va.addBuffer(vb, layout);
        
		IndexBuffer ibo = new IndexBuffer(indices);
		
		/**
		 * Shaders
		 */
		
		
		Shader shader = new Shader("src\\main\\resources\\vertex.glsl", "src\\main\\resources\\fragment.glsl");
		shader.bind();
		
		
		shader.setUniform4f("u_Color", 0.5f, 0.5f, 0.5f, 1.0f);
		        
        /**
         * Clearing states for test
         */
        //glBindVertexArray(0);
        //glUseProgram(0);
		
		va.unBind();
		shader.unBind();
		vb.unBind();
		ibo.unBind();
        
        
        float r = 0.0f;
        float increment = 0.05f;
        
		/*
		 * Render loop
		 */
		while (!glfwWindowShouldClose(window)) {
			//double time = glfwGetTime();
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			shader.bind();
			
			shader.setUniform4f("u_Color", r, 0.0f, 1.0f, 1.0f);			
			
			va.bind();
			ibo.bind();
			
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT,  0);
			
            if ( r > 1.0f) {
                increment = -0.05f;
            } else if (r < 0.0f) {
                increment = 0.05f;
            }

            r += increment;
			
			
			//GLFW uses double buffering
			glfwSwapBuffers(window);
			glfwPollEvents();
			//glfwWaitEvents() can be used if application only needs to be update on new input
			
		}
		
		glfwDestroyWindow(window);
		shader.delete();
		
		keyCallback.free();
		errorCallback.free();
		debugProc.free();
		
		glfwTerminate();
		
	}
	
	
}
