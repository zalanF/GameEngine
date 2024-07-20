package zfEngine;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;

import org.joml.primitives.Spheref;


public class project {

	long window;
	int width = 1250;
	int height = 1250;
	int VBO, VAO, EBO, texture1, texture2;
	private Shader shader;

	Window win;
	Texture tex1, tex2;
	
	Camera cam;
	
	float deltaTime = 0.0f;
	float lastFrame = 0.0f;
	
	boolean firstMouse = true;
	float yaw = -90f; 
	float pitch;
	float lastX = width/2;
	float lastY = height/2;
	
	float fov = 45.0f;
	
	Cubef cube;
	
	float x, y, z;
	boolean isFullscreen = false;
	boolean isWireframe = false;
	float x_component = 0;
	float phi = 0f; 
	
	//####################
	//##   Call backs   ##
	//####################
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

			cam.processMouseMovement(xoffset, yoffset, true);
		}
	};
	
	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			
			cam.processMouseScroll(yoffset);
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
	
	float[] vertices_cube = {
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
	
	Vector3f[] cubePositions = {
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
	
	float[] vertices_cube_indexed = {
			//Positions				Colours
			0.0f,	0.0f,	0.0f,	1.0f,	0.0f,	0.0f,
			1.0f,	0.0f,	0.0f,	0.0f,	1.0f,	0.0f,
			0.0f,	1.0f,	0.0f,	0.0f,	0.0f,	1.0f,
			1.0f,	1.0f,	0.0f,	1.0f,	1.0f,	1.0f,
			
			0.0f,	0.0f,	1.0f,	1.0f,	0.0f,	0.0f,
			1.0f,	0.0f,	1.0f,	0.0f,	1.0f,	0.0f,
			0.0f,	1.0f,	1.0f,	0.0f,	0.0f,	1.0f,
			1.0f,	1.0f,	1.0f,	1.0f,	1.0f,	1.0f
	};
	
	int[] indices_cube = {
			0,	3,	1,	0,	2,	3,	//Back
			1,	3,	7,	1,	7,	5,	//Right
			5,	4,	7,	4,	6,	7,	//Front
			4,	6,	0,	6,	2,	0,	//Left
			2,	3,	7,	2,	7,	6,	//Top
			0,	1,	5,	0,	5,	4 	//Bottom
	};
	
	 float[] cube_vertices= {
			    // front
			    -1.0f, -1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			    // back
			    -1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f
			  };
	
	
	int[] cube_elements = {
			// front
			0, 1, 2,
			2, 3, 0,
			// right
			1, 5, 6,
			6, 2, 1,
			// back
			7, 6, 5,
			5, 4, 7,
			// left
			4, 0, 3,
			3, 7, 4,
			// bottom
			4, 5, 1,
			1, 0, 4,
			// top
			3, 2, 6,
			6, 7, 3
		};
	
	float[] tri_vert = {
		-0.5f,	-0.5f, 0.0f,
		0.0f,	0.5f,	0.0f,
		0.5f,	-0.5f,	0.0f
	};
	
	//########################
	//##   Initialization   ##
	//########################
	private void init() {
		System.out.println("LWJGL Version" + Version.getVersion());
		
		win = new Window(width, height, "Zalan's Window", false);
		win.setVsync(true);
		
		window = win.getWindow();

		x = y = z = 0;
		
		//###################
		//##   Callbacks   ##
		//###################
		
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		glfwSetCursorPosCallback(window, cursorPos);	
		glfwSetScrollCallback(window, scrollCallback);
		
		//#################
		//##   Shaders   ##
		//#################
		
		shader = new Shader("src\\main\\resources\\shaders\\vertex2.glsl", "src\\main\\resources\\shaders\\fragment2.glsl");
		
		//#################
		//##   Buffers   ##
		//#################
		
		/*VAO = glGenVertexArrays();
		VBO = glGenBuffers();
		//EBO = glGenBuffers();
		
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices_cube, GL_STATIC_DRAW);*/
		
		//Vertex Attribute set-up
		//tells the computer how to interpret the vertex data
		
		// position attribute
		/*glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0); //sizeof(float) = 4 : CHANGE TO 5 to 8 IF USING COLOURS! 
		glEnableVertexAttribArray(0);*/
		// color attribute
		/*glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*4, 3*4); //sizeof(float) = 4 
		glEnableVertexAttribArray(1);*/
		// texture coordinates
		/*glVertexAttribPointer(2, 2, GL_FLOAT, false, 5*4, 3*4); //sizeof(float) = 4 : CHANGE 3*4 to 6*4 IF USING COLOURS
		glEnableVertexAttribArray(2);*/
		
		
		//Element Array Buffer
		/*glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);*/
		
		cube = new Cubef();
		
		shader.bind();
		
		
		//##################
		//##   Textures   ##
		//##################
		/*tex1 = new Texture("src\\main\\resources\\textures\\container.jpg", 0);
		tex2 = new Texture("src\\main\\resources\\textures\\awesomeface.png", 1);
		
		shader.bind();
		shader.setUniform("texture1", 0);
		shader.setUniform("texture2", 1);*/
		
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
		//model.rotate((float)Math.toRadians(-55), new Vector3f(1.0f, 0f, 0f));
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
		
		cam = new Camera(new Vector3f(0,0,5), new Vector3f(0,1,0), new Vector3f(0,0,-1));
		
		//Unbinding
		//glBindBuffer(GL_ARRAY_BUFFER, 0);
		//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		//glBindVertexArray(0);
		shader.unbind();
		
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); //Wireframe drawing mode
		
	}
	
	private void loop() {
		Matrix4f model = new Matrix4f();
		Matrix4f proj = new Matrix4f();
		
		while (!glfwWindowShouldClose(window)) {
			
			glClearColor(0.2f, 0.3f, 0.3f, 1.0f); //background color
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			shader.bind();
			
			//Timing
			float currentFrame = (float)glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;
			
			//Model
			model.identity();
			//model.rotate(currentFrame, new Vector3f(0.5f, 1.0f, 1.0f).normalize());
			//model.rotate(phi, new Vector3f(1.0f, 0.0f, 0.0f));
			shader.setUniform("model", model);
			
			//View
			shader.setUniform("view", cam.getViewMatrix());
			
			//Projection
			proj.identity();
			
			proj.perspective((float)Math.toRadians(cam.zoom), win.getAspectRatio(), 0.1f, 1000.0f);
			shader.setUniform("projection", proj);
			
			//Textures
			//tex1.bind();
			//tex2.bind();
			
			//glBindVertexArray(VAO);
			//glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			//glDrawArrays(GL_TRIANGLES, 0, 36);
			
			cube.bind();
			cube.move(deltaTime);
			shader.setUniform("model", cube.getModelMatrix());
			cube.draw();
			
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
			
			
			win.free();
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
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.FORWARD, deltaTime);
		}
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.BACK, deltaTime);
		}
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.LEFT, deltaTime);
		}
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.RIGHT, deltaTime);
		}
		if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.UP, deltaTime);
		}
		if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
			cam.processKeyboard(CameraDirection.DOWN, deltaTime);
		}
		
		if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
			cube.movementInput(Direction.FORWARD);
		}
		if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
			cube.movementInput(Direction.BACK);
		}
		if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
			cube.movementInput(Direction.LEFT);
		}
		if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
			cube.movementInput(Direction.RIGHT);
		}
		
		//cameraPos.y = 0;
		
	}
	
	public static void main(String[] args) {
		new project().run();

	}

}
