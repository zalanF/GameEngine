package demo;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.*;

import java.util.Arrays;

import org.joml.Matrix2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.Version;

import zfEngine.*;

public class Snake {
	
	Window window;
	Shader shader;
	
	float x = 0;
	float y = 0;
	Vector2i position;
	Vector2i dir, lastDir;
	
	int VAO;
	
	float vertices[] = {
			-1.00f, -1.00f,
			-1.00f,  1.00f,
			 1.00f, -1.00f,
			 1.00f,  1.00f,
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
	
	final float GRID_SIZE = 9f;
	final float INITIAL_SIZE = 1f;
	
	Vector2i[] snake;
	
	Vector2i food;
	
	//########################
	//##   Initialization   ##
	//########################
	private void init() {
		System.out.println("LWJGL Version" + Version.getVersion());

		
		window = new Window(1000, 1000, "Snake", false);
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
		
		
		snake = new Vector2i[1];
		snake[0] = new Vector2i(0,0);
		
		dir = new Vector2i(1, 0);
		lastDir = new Vector2i();
		
		food = new Vector2i(3,3);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	//TODO: Find all empty slots and find a good algo to spawn in the food
	
	private void loop() {	
		
		Timer timer = new Timer();
		
		timer.createIntervalTimer(5, true);
		
		Matrix2f transform = new Matrix2f();
		transform.scale(1f/GRID_SIZE);
		shader.setUniform("u_transform", transform);
		
		boolean isOutOfBounds = false;
		boolean gameOver = false;
		
		while(!window.shouldWindowClose()) {			
			timer.update();
			processInput();
			
			glfwPollEvents();
			
			if (timer.hasIntervalPassed()) {
				if (snake[0].x == food.x && snake[0].y == food.y) {
					
					snake = extendArray(snake);
					
					int tempX = randomInt((int)GRID_SIZE);
					int tempY = randomInt((int)GRID_SIZE);
					while(isOccupied(tempX, tempY) && !gameOver) {
						tempX = randomInt((int)GRID_SIZE);
						tempY = randomInt((int)GRID_SIZE);
						System.out.println("Retry");
					}
					
					food.x = tempX;
					food.y = tempY;
				}
				snake = shiftArrayDown(snake);					
				
				snake[0].add(dir);
				lastDir.set(dir);
				
				isOutOfBounds = snake[0].x < 0 || snake[0].x >= GRID_SIZE || snake[0].y < 0 || snake[0].y >= GRID_SIZE;
				
				gameOver = checkCollision(snake) || isOutOfBounds;
				
				//RENDERING
				glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //background color
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				shader.bind();
				glBindVertexArray(VAO);
				
				if(gameOver) {
					
					timer.stopIntervalTimer();
					//shader.setUniform("u_colour", 1.0f, 1.0f, 1.0f);
					//translateOnGrid(snake[0].x, snake[0].y);
					//glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
					
					System.out.println(snake.length);
				} 
				
				//SNAKE
				shader.setUniform("u_colour", 0.0f, 1.0f, 0.0f);
				for (int i = 0; i < snake.length; i++) {
					translateOnGrid(snake[i].x, snake[i].y);
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				}
				
				//FOOD
				shader.setUniform("u_colour", 1.0f, 0.0f, 0.0f);
				translateOnGrid(food.x, food.y);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				
				
				window.swapBuffer();
			}		
		}		
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
	
	void translateOnGrid(float x, float y) {
		shader.setUniform("u_translate", -1f*GRID_SIZE+INITIAL_SIZE+x*INITIAL_SIZE*2, 1f*GRID_SIZE-INITIAL_SIZE-y*INITIAL_SIZE*2);	
	}
	
	int colourSelector(int colourIndex) {
		final int MAX_LENGTH = colours.length-1;
		colourIndex = (colourIndex == MAX_LENGTH) ? colourIndex -= MAX_LENGTH: ++colourIndex;
		
		return colourIndex;
	}
	
	int randomInt(int ceiling) {
		return (int) (Math.random() * ceiling);
	}
	
	Vector2i[] shiftArrayDown(Vector2i[] array) {
		
		for (int i = array.length-1; i > 0; --i) {
			
			array[i] = new Vector2i(array[i-1]);
		}
		return array;
	}
	
	Vector2i[] extendArray(Vector2i[] array) {
		Vector2i[] longArray = new Vector2i[array.length+1];
		
		for (int i = 0; i < array.length; ++i) {
			longArray[i] = array[i];
		}
		
		return longArray;
	}
	
	boolean checkCollision(Vector2i[] array) {
		for (int i = 1; i < array.length; ++i) {	
			if (array[0].x == array[i].x && array[0].y == array[i].y) {
				return true;
			}
		}
		
		return false;
	}
	
	boolean isOccupied(int x, int y) {
		
		for (int i = 0; i < snake.length-1; ++i) {
			if (x == snake[i].x || y == snake[i].y) {
				return true;
			}
		}
		
		return false;
	}
	
	public void processInput() {
		if ((window.isKeyPressed(GLFW_KEY_W) || window.isKeyPressed(GLFW_KEY_UP)) && lastDir.y != 1) {
			dir.zero();
			dir.y = -1;
		}
		if ((window.isKeyPressed(GLFW_KEY_S) || window.isKeyPressed(GLFW_KEY_DOWN)) && lastDir.y != -1) {
			dir.zero();
			dir.y = 1;
		}
		if ((window.isKeyPressed(GLFW_KEY_A) || window.isKeyPressed(GLFW_KEY_LEFT)) && lastDir.x != 1) {
			dir.zero();
			dir.x = -1;
		}
		if ((window.isKeyPressed(GLFW_KEY_D) || window.isKeyPressed(GLFW_KEY_RIGHT)) && lastDir.x != -1) {
			dir.zero();
			dir.x = 1;
		}

	}
	
	public static void main(String[] args) {
		new Snake().run();
	}

}
