package zfEngine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.stb.STBImage;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;


public class project {

	long window;
	int width = 1000;
	int height = 1000;
	int VBO, VAO, EBO, texture1, texture2;
	private Shader shader;
	
	Vector3f cameraPos = new Vector3f(0f,0f,3f);
	Vector3f cameraFront = new Vector3f(0f,0f,-1f);
	Vector3f cameraUp = new Vector3f(0f,1f,0f);
	
	
	float deltaTime = 0.0f;
	float lastFrame = 0.0f;
	
	boolean firstMouse = true;
	float yaw = -90f; 
	float pitch;
	float lastX = width/2;
	float lastY = height/2;
	
	float fov = 45.0f;
	
	//####################
	//##   Call backs   ##
	//####################
	// invokes some Java non-sense
	
	private static GLFWFramebufferSizeCallback resizeWindow = new GLFWFramebufferSizeCallback() {
		
		@Override
		public void invoke(long window, int width, int height) {
			glViewport(0,0, width, height);
			
		}
	};
	
	
	private GLFWKeyCallback keyboardInput = new GLFWKeyCallback() {
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
			/*float cameraSpeed = 2.5f * deltaTime;
			Vector3f v = new Vector3f();
			
			while(key == GLFW_KEY_W && action == GLFW_PRESS) {
				cameraFront.mul(cameraSpeed, v);
				cameraPos.add(v);					
			}
			if (key == GLFW_KEY_S && action == GLFW_PRESS) {
				cameraFront.mul(cameraSpeed, v);
				cameraPos.sub(v);
			}
			if (key == GLFW_KEY_A && action == GLFW_PRESS) {
				cameraFront.cross(cameraUp, v);
				v.normalize().mul(cameraSpeed);
				cameraPos.sub(v);
			}
			if (key == GLFW_KEY_D && action == GLFW_PRESS) {
				cameraFront.cross(cameraUp, v);
				v.normalize().mul(cameraSpeed);
				cameraPos.add(v);
			}*/
			
		}
	};
	
	private GLFWCursorPosCallback cursorPos = new GLFWCursorPosCallback() {
		
		@Override
		public void invoke(long window, double xpos, double ypos) {
			if(firstMouse) {
				lastX = (float) xpos;
				lastY = (float) ypos;
				firstMouse = false;
			}
			
			float xoffset = (float) (xpos - lastX);
			float yoffset = (float) (lastY - ypos);
			lastX = (float) xpos;
			lastY = (float) ypos;
			
			float sensitivity = 0.1f;
			xoffset *= sensitivity;
			yoffset *= sensitivity;
			
			yaw += xoffset;
			pitch += yoffset;
			
			if (pitch > 89.0f) {
				pitch = 89.0f;
			}
			if (pitch < -89.0f) {
				pitch = -89.0f;
			}
			
			Vector3f direction = new Vector3f();
			direction.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
			direction.y = (float) Math.sin(Math.toRadians(pitch));
			direction.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
			cameraFront = direction.normalize();
			
		}
	};
	
	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			fov -= (float) yoffset;
			if (fov < 1.0f) {
				fov = 1.0f;
			}
			if (fov > 45.0f) {
				fov = 45.0f;
			}
			
		}
	};
	
	//##############
	//##   Data   ##
	//##############
	float[] vertices = {
			//positions				//colors			//texture coords
			  0.5f,  0.5f, 0.0f,	1.0f, 0.0f, 0.0f,	1.0f, 1.0f, 	//top right
			  0.5f, -0.5f, 0.0f,	0.0f, 1.0f, 0.0f,	1.0f, 0.0f, 	//bottom right
			 -0.5f, -0.5f, 0.0f,	0.0f, 0.0f, 1.0f,	0.0f, 0.0f, 	//bottom left
			 -0.5f,  0.5f, 0.0f, 	1.0f, 1.0f, 0.0f,	0.0f, 1.0f, 	//top left
			 
	};
	
	float[] vertices_triangle = {
			//positions				//colors
			   0.5f, -0.5f, 0.0f,	1.0f, 0.0f, 0.0f, 	0.0f, 0.0f,	//bottom right
			  -0.5f, -0.5f, 0.0f,	0.0f, 1.0f, 0.0f,	1.0f, 0.0f,	//bottom left
			   0.0f,  0.5f, 0.0f,	0.0f, 0.0f, 1.0f,	0.5f, 1.0f //top
	};
	
	float vertices_cube[] = {
			//Position			  Texture
		    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
		     0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
		     0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
		     0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
		    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
		    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

		    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
		     0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
		     0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
		     0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
		    -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
		    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

		    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		    -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
		    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

		     0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		     0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
		     0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		     0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		     0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
		     0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		     0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
		     0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
		     0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
		    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
		    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

		    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
		     0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
		     0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		     0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
		    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
		};
	
	Vector3f cubePositions[] = {
			new Vector3f( 0.0f,  0.0f,  0.0f), 
			new Vector3f( 2.0f,  5.0f, -15.0f), 
			new Vector3f(-1.5f, -2.2f, -2.5f),  
			new Vector3f(-3.8f, -2.0f, -12.3f),  
			new Vector3f( 2.4f, -0.4f, -3.5f),  
			new Vector3f(-1.7f,  3.0f, -7.5f),  
			new Vector3f( 1.3f, -2.0f, -2.5f),  
			new Vector3f( 1.5f,  2.0f, -2.5f), 
			new Vector3f( 1.5f,  0.2f, -1.5f), 
			new Vector3f(-1.3f,  1.0f, -1.5f)  
		};
	
	int[] indices = {
			0, 1 ,3, //first triangle
			1, 2, 3  //second triangle
	};
	
	int[] indices_one_triangle = {
			0,1,2 //first triangle
	};
	
	//########################
	//##   Initialization   ##
	//########################
	private void init() {
		System.out.println("LWJGL Version" + Version.getVersion());
		
		// Check if GLFW set up properly
		if (!glfwInit()) {
			System.err.println("Unable to intialize GLFW");
		}

		// WindowHints
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		//glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE); 
		
		window = glfwCreateWindow(width, height, "Zalan's Window", NULL, NULL);
		if (window == NULL) {
			System.err.println("Failed to create GLFW window");
			glfwTerminate();
		}
		
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(0); // 0 = unlimited : 1 = monitor refresh rate
		
		/*First two parameters are the location of the lower left corner of the window
		 * Behind the scenes, OpenGL uses a coordinate scale from -1 to 1 (unit vector scale).
		 * By setting the corner, allows opengl to map that scale to the desired window size
		 */
		glViewport(0, 0, width, height);
		
		//###################
		//##   Callbacks   ##
		//###################
		
		glfwSetFramebufferSizeCallback(window, resizeWindow); //Allows for window to be resizable
		glfwSetKeyCallback(window, keyboardInput);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		glfwSetCursorPosCallback(window, cursorPos);	
		glfwSetScrollCallback(window, scrollCallback);
		
		//#################
		//##   Shaders   ##
		//#################
		
		shader = new Shader("src\\main\\resources\\shaders\\vertex.glsl", "src\\main\\resources\\shaders\\fragment.glsl");
		
		//#################
		//##   Buffers   ##
		//#################
		
		VAO = glGenVertexArrays();
		VBO = glGenBuffers();
		//EBO = glGenBuffers();
		
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices_cube, GL_STATIC_DRAW);
		
		//Vertex Attribute set-up
		//tells the computer how to interpret the vertex data
		
		// position attribute
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0); //sizeof(float) = 4 : CHANGE TO 5 to 8 IF USING COLOURS! 
		glEnableVertexAttribArray(0);
		// color attribute
		/*glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*4, 3*4); //sizeof(float) = 4 
		glEnableVertexAttribArray(1);*/
		// texture coordinates
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 5*4, 3*4); //sizeof(float) = 4 : CHANGE 3*4 to 6*4 IF USING COLOURS
		glEnableVertexAttribArray(2);
		
		
		//Element Array Buffer
		/*glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);*/
		
		//##################
		//##   Textures   ##
		//##################
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			IntBuffer nrChannels = stack.mallocInt(1);
			
			texture1 = glGenTextures();
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture1);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //texture filtering when minifying (mapping onto objects smaller than texture)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); // texture filtering when maximizing (mapping onto objects larger than texture)
			
			ByteBuffer image = stbi_load("src\\main\\resources\\textures\\container.jpg", width, height, nrChannels, 0);

			if (image == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}
						
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
			glGenerateMipmap(GL_TEXTURE_2D);
			
			stbi_image_free(image);
			
			
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer nr = stack.mallocInt(1);
			
			texture2 = glGenTextures();
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, texture2);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //texture filtering when minifying (mapping onto objects smaller than texture)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); // texture filtering when maximizing (mapping onto objects larger than texture)
			
			stbi_set_flip_vertically_on_load(true); //flag that flips images
			
			ByteBuffer data = stbi_load("src\\main\\resources\\textures\\awesomeface.png", w, h, nr, 0);

			if (data == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}
						
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
			glGenerateMipmap(GL_TEXTURE_2D);
			
			stbi_image_free(data);
			
			
		}
		shader.bind();
		shader.setUniform("texture1", 0);
		shader.setUniform("texture2", 1);
		
		//#########################
		//##   Transformations   ##
		//#########################
		/*Matrix4f trans = new Matrix4f(); //Automatically makes identity
		
		//All rotations have to be normalized
		trans.rotate((float)Math.toRadians(90), new Vector3f(0,0,1)); //Note rotation is around Z axis
		
		trans.scale(new Vector3f(0.5f, 0.5f, 0.5f));
				
		shader.setUniform("transform", trans);*/
		
		//#####################
		//##   Coordinates   ##
		//#####################
		//Transforming into world space
		Matrix4f model = new Matrix4f();
		model.rotate((float)Math.toRadians(-55), new Vector3f(1.0f, 0f, 0f));
		shader.setUniform("model", model);
		
		//Transforming into viewspace
		/*[Excerpt from learnopengl explaining the view matrix]
	
		 To move a camera backwards, is the same as moving the entire scene forward.
		 That is exactly what a view matrix does, we move the entire scene around
		 inversed to where we want the camera to move.
		 
		 */
		//We have to move the 'camera' backwards using the view matrix
		//so that the objects are not inside the 'camera'
		Matrix4f view = new Matrix4f();
		view.translate(0f, 0f, -3.0f);
		shader.setUniform("view", view);
		
		
		//Transforming into clip space
		Matrix4f projection = new Matrix4f();
		float aspectRatio = width/height;
		projection.perspective((float)Math.toRadians(45), aspectRatio, 0.1f, 100.0f);
		shader.setUniform("projection", projection);
		
		glEnable(GL_DEPTH_TEST); //Z-buffer
		
		//################
		//##   Camera   ##
		//################
		
		
		/*Vector3f cameraPos = new Vector3f(0f,0f,3f);
		Vector3f cameraTarget = new Vector3f(0f,0f,0f);
		Vector3f cameraDirection = new Vector3f();
		cameraPos.sub(cameraPos, cameraDirection);
		cameraDirection.normalize(); //actually pointing in reverse direction of what it is targeting
		
		//Right Axis
		Vector3f up = new  Vector3f(0f,1f,0f);
		Vector3f cameraRight = new Vector3f();
		up.cross(cameraDirection, cameraRight);
		cameraRight.normalize();
		
		//Up Axis
		//Vector3f cameraUp = new Vector3f();
		cameraDirection.cross(cameraRight, cameraUp);
		//cameraUp.normalize(); //might be unnecessary
		
		//LookAt Matrix
		view.identity();
		//view.lookAt(cameraPos, cameraTarget, up); //same as what we did for the view matrix above */
		Vector3f v = new Vector3f();
		view.lookAt(cameraPos, cameraPos.add(cameraFront, v), cameraUp);
		shader.setUniform("view", view);
		
		//Unbinding
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		shader.unBind();
		
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); //Wireframe drawing mode
		
	}
	
	private void loop() {
		while (!glfwWindowShouldClose(window)) {
			
			//rendering
			glClearColor(0.2f, 0.3f, 0.3f, 1.0f); //background color
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			shader.bind();
			
			//Timing
			float currentFrame = (float)glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;
			
			//Changing Coordinates via Uniforms
			/*double timeValue = glfwGetTime();
			float offsetChange = (float)((Math.sin(timeValue)));
			float y_change = (float)((Math.cos(timeValue)));*/
			
			//shader.setUniform("offset", 0.5f*offsetChange, 0.5f*y_change, 0f);
			
			//Transformations
			/*Matrix4f trans = new Matrix4f(); //Automatically makes idenity
			trans.translate(0.5f, -0.5f, 0.0f);
			trans.rotate((float)glfwGetTime(), new Vector3f(0,0,1));
			shader.setUniform("transform", trans);*/
			
			//Model
			//float time = (float)glfwGetTime();
			Matrix4f model = new Matrix4f();
			//model.rotate(time, new Vector3f(0.5f, 1.0f, 1.0f).normalize());
			//shader.setUniform("model", model);
			
			//View
			Matrix4f view = new Matrix4f();
			Vector3f v = new Vector3f();
			view.lookAt(cameraPos, cameraPos.add(cameraFront, v), cameraUp);
			//System.out.println(cameraPos.toString());
			shader.setUniform("view", view);
			
			//Projection
			Matrix4f proj = new Matrix4f();
			proj.perspective((float)Math.toRadians(fov), width/height, 0.1f, 100.0f);
			shader.setUniform("projection", proj);
			
			//Textures
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture1);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, texture2);
			
			glBindVertexArray(VAO);
			//glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			//glDrawArrays(GL_TRIANGLES, 0, 36);
			
			//Draw multiple Cubes
			for (int i = 0; i < 10; ++i) {
				model.identity();
				model.translate(cubePositions[i]);
				float angle = 20f*i;
				
				model.rotate((float)Math.toRadians(angle), new Vector3f(1f, 0.3f, 0.5f).normalize());
				shader.setUniform("model", model);
				
				glDrawArrays(GL_TRIANGLES, 0, 36);
			}
			
			
			//Drawing Second Object
			/*double time = glfwGetTime();
			
			trans.identity();
			trans.translate(-0.5f, 0.5f, 0f);
			
			float scaleFactor = (float)Math.sin(time);
			scaleFactor *= scaleFactor; //squaring the result to get rid of negative
			
			trans.scale(scaleFactor);
			shader.setUniform("transform", trans);
			
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);*/
			
			processInput(window);
			
			//check and call events, swap buffers
			glfwSwapBuffers(window); //glfw's way of rendering uses 2 buffers
			glfwPollEvents(); //Checks if events like keyboard input are triggered
		}
	}
	
	private void run() {
		try {
			init();
			loop();
			
			//if (debug != null) {
			//	debug.free();
			//}
			
			resizeWindow.free();
			keyboardInput.free();
			cursorPos.free();
			scrollCallback.free();
			
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			
		} finally {
			glfwDestroyWindow(window); //destroying the window before terminating GLFW prevents memory leak
			glfwTerminate();
			//GL.setCapabilities(null);
		}
	}
	
	void processInput(long window) {
		float cameraSpeed = 2.5f * deltaTime;
		Vector3f v = new Vector3f();
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
			cameraFront.mul(cameraSpeed, v);
			cameraPos.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
			cameraFront.mul(cameraSpeed, v);
			cameraPos.sub(v);
		}
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
			cameraFront.cross(cameraUp, v);
			v.normalize().mul(cameraSpeed);
			cameraPos.sub(v);
		}
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
			cameraFront.cross(cameraUp, v);
			v.normalize().mul(cameraSpeed);
			cameraPos.add(v);
		}
		//cameraPos.y = 0;
		
	}
	
	public static void main(String[] args) {
		new project().run();

	}

}
