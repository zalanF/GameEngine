package zfEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.Version;

public class project2d {
	
	Window window;
	Shader shader;
	
	float x = 0;
	float y = 0;
	Vector2i position;
	Vector2i dir;
	
	int VAO;
	
	float vertices[] = {
			-0.50f, -0.50f,
			-0.50f,  0.50f,
			 0.50f, -0.50f,
			 0.50f,  0.50f,
		};
	
	int[] indices = {
		0, 1, 2,
		1, 3, 2
	};
	
	Vector3f[] colours = {
		new Vector3f(1f,0f,0f),		//RED
		new Vector3f(0f,1f,0f),		//GREEN
		new Vector3f(0f,0f,1f),		//BLUE
		new Vector3f(1f,1f,0f),		//YELLOW
		//new Vector3f(1f,0f,1f),		//PURPLE
		//new Vector3f(0f,1f,1f),		//TEAL
	};
	
	Vector2i[] worm;
	
	Vector2i apple;
	
	//########################
	//##   Initialization   ##
	//########################
	private void init() {
		System.out.println("LWJGL Version" + Version.getVersion());

		
		window = new Window(1000, 1000, "2D Game", false);
		//window.setVsync(true);
		
		shader = new Shader("src/main/resources/shaders/2dvert.glsl", "src/main/resources/shaders/2dfrag.glsl");
		
		shader.bind();
		
		VAO = glGenVertexArrays();
		int VBO = glGenBuffers();
		int EBO = glGenBuffers();
		
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 2*4, 0);
		glEnableVertexAttribArray(0);
		
		//Make the square smaller
		//Matrix2f transform = new Matrix2f();
		//transform.scale(0.5f);
		//shader.setUniform("transform", transform);
		
		
		//position = new Vector2i();
		worm = new Vector2i[1];
		worm[0] = new Vector2i(0,0);
		
		dir = new Vector2i(1, 0);
		
		apple = new Vector2i(3,3);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	private void loop() {	
		
		Timer timer = new Timer();
		
		timer.createIntervalTimer(1);
		
		Matrix2f transform = new Matrix2f();
		
//		Matrix3f translate = new Matrix3f();
//		for (int i = 0; i < 10; ++i) {
//		
//		translate.identity();
//		translate.scale((window.getHeight()*1.0f)/(window.getWidth()*1.0f), 1, 1);
//		translate.scale(1/10f);
//		
//		translate.mul(translate( (float)i+0.1f*i-1*10f , +1*10f));
//		
//		shader.setUniform("translate", translate);
//		
//		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
//		
//	}
		while(!window.shouldWindowClose()) {
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //background color
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			timer.update();
			
			float time = (float)glfwGetTime();
			
			shader.bind();
//			float scaleX, scaleY;
//			
//			scaleX = (window.getWidth() > window.getHeight()) ? 1f/window.getAspectRatio() : 1f;
//			scaleY = (window.getWidth() < window.getHeight()) ? window.getAspectRatio() : 1f;
//						
//			transform.identity();
//			transform.scale(scaleX, scaleY);
//			shader.setUniform("u_transform", transform);
//			shader.setUniform("u_translate", x, y);
//			shader.setUniform("u_colour", 1.0f, 0.0f, 0.0f);

			
			glBindVertexArray(VAO);
//			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			
			float SCALE = 9f/2f;
			
			transform.identity();
			transform.scale(1f/SCALE);
			shader.setUniform("u_transform", transform);
			
			float SHIFT = 0f;
			float INITIAL_SIZE = 0.5f;
			SHIFT = INITIAL_SIZE;
			
			processInput(timer.getDeltaTime());
			
			
			if (timer.hasIntervalPassed()) {
				if (worm[0].x == apple.x && worm[0].y == apple.y) {
					
					worm = increaseArray(worm);
					apple.x = (int) (Math.random() * 9);
					apple.y = (int) (Math.random() * 9);
				}
				worm = shiftArray(worm);					
				
				worm[0].add(dir);
				System.out.println("--------");
				for (int i = 0; i < worm.length; ++i) {
					
					System.out.println(worm[i].toString());
					
				}
				//shader.setUniform("u_translate", -1f*SCALE+SHIFT+worm[0].x, (1f)*SCALE-SHIFT-worm[0].y);
				
			}
			
			
			shader.setUniform("u_colour", 0.0f, 1.0f, 0.0f);
			for (int i = 0; i < worm.length; i++) {				
				shader.setUniform("u_translate", -1f*SCALE+SHIFT+worm[i].x, (1f)*SCALE-SHIFT-worm[i].y);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			}
			
			
			shader.setUniform("u_colour", 1.0f, 0.0f, 0.0f);
			shader.setUniform("u_translate", -1f*SCALE+SHIFT+apple.x, (1f)*SCALE-SHIFT-apple.y);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			
			
			
			int colourIndex = 0;
			
			//This loop fills in a grid of squares starting from the 
			//top corner to the bottom corner
//			for (int y_shift = 0; y_shift < SCALE*2; ++y_shift) {
//				for (int x_shift = 0; x_shift < SCALE*2; ++x_shift) {
//					processInput(timer.getDeltaTime());
//					if (timer.hasIntervalPassed()) {
//						position.add(dir);
//					}
//					
//					shader.setUniform("u_translate", -1f*SCALE+SHIFT+x_shift, (1f)*SCALE-SHIFT-y_shift);					
//					//colour
//					shader.setUniform("u_colour", colours[colourIndex]);
//					colourIndex = colourSelector(colourIndex);
//					
//					if (x_shift == position.x && y_shift == position.y) {
//						shader.setUniform("u_colour", 0f, 0f, 0f);
//						//shader.setUniform("u_translate", -1f*SCALE+SHIFT+x_shift, (1f)*SCALE-SHIFT-y_shift);					
//
//						
//					}
//					
//					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
//				}
//			}
//			
			window.swapBuffer();
			glfwPollEvents();
		}
		
	}
	
	int colourSelector(int colourIndex) {
		final int MAX_LENGTH = colours.length-1;
		colourIndex = (colourIndex == MAX_LENGTH) ? colourIndex -= MAX_LENGTH: ++colourIndex;
		
		return colourIndex;
	}
	
	
	Vector2i[] shiftArray(Vector2i[] array) {
		
		for (int i = array.length-1; i > 0; --i) {
			
			array[i] = new Vector2i(array[i-1]);
		}
		return array;
	}
	
	Vector2i[] increaseArray(Vector2i[] array) {
		Vector2i[] longArray = new Vector2i[array.length+1];
				
		for (int i = 0; i < array.length; ++i) {
			longArray[i] = new Vector2i(array[i]);
		}
		
		return longArray;
		
	}
	
	private void run() {
		try {
			init();
			loop();
			
			window.free();
		}
		catch (Throwable t) {
			t.printStackTrace(System.err);
		} finally {
			
			window.destory();
			shader.delete();
			glfwTerminate();
		}
	}
	
	Matrix3f translate(float x, float y) {
		Matrix3f translate = new Matrix3f();
		
		translate.m20 = x;
		translate.m21 = y;
		
		return translate;
	}
	
	public void processInput(float deltaTime) {
		final float speed = 1f;
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			y += speed*deltaTime;
			System.out.println("("+x+","+y+")");
		}
		if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			y -= speed*deltaTime;
			System.out.println("("+x+","+y+")");
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			x -= speed*deltaTime;
			System.out.println("("+x+","+y+")");
		}
		if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			x += speed*deltaTime;
			System.out.println("("+x+","+y+")");
		}
		
		if (window.isKeyPressed(GLFW_KEY_W)) {
			dir.zero();
			dir.y = -1;
		}
		if (window.isKeyPressed(GLFW_KEY_S)) {
			dir.zero();
			dir.y = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_D)) {
			dir.zero();
			dir.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			dir.zero();
			dir.x = -1;
		}
		
//		if (glfwGetMouseButton(window.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
//			glBindVertexArray(VAO);
//			shader.setUniform("u_translate", 0, 0);
//			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
//			
//		}
	}
	
	public static void main(String[] args) {
		new project2d().run();
	}

}
