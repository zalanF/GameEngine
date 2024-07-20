package zfEngine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Cubef {
	public float x, y, z;
	private float velocityX = 1.5f;
	private float accelerationX = 0f;
	private float friction = 2f;
	
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f acceleration;
	
	private int VAO;
	
	Cubef() {
		x = 0;
		y = 0;
		y = 0;
		position = new Vector3f();
		velocity = new Vector3f();
		acceleration = new Vector3f();
		
		float[] vertices = {
				//Positions
				0.0f,	0.0f,	0.0f,
				1.0f,	0.0f,	0.0f,
				0.0f,	1.0f,	0.0f,
				1.0f,	1.0f,	0.0f,
				
				0.0f,	0.0f,	1.0f,
				1.0f,	0.0f,	1.0f,
				0.0f,	1.0f,	1.0f,
				1.0f,	1.0f,	1.0f
		};
		
		int[] indices = {
				0,	3,	1,	0,	2,	3,	//Back
				1,	3,	7,	1,	7,	5,	//Right
				5,	4,	7,	4,	6,	7,	//Front
				4,	6,	0,	6,	2,	0,	//Left
				2,	3,	7,	2,	7,	6,	//Top
				0,	1,	5,	0,	5,	4 	//Bottom
		};
		
		VAO = glGenVertexArrays();
		int VBO = glGenBuffers();
		int EBO = glGenBuffers();
		
		
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*4, 0);
		glEnableVertexAttribArray(0);
		
	}
	
	public void bind() {
		glBindVertexArray(VAO);
	}
	
	public void draw() {
		glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
	}
	
	public void resetPosition() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Matrix4f getModelMatrix() {
		Matrix4f model = new Matrix4f();
		model.translate(position);
		return model;
	}
	
	public void move(float deltaTime) {
//		float lastX, displacementX;
//		lastX = x;
//		
//		float speed = velocityX * deltaTime;
//		x = x + speed;
//		
//		displacementX = x - lastX;
//		velocityX = velocityX - friction*displacementX + accelerationX*deltaTime;
//		
//		accelerationX = 0;
		
		//VECTORIZED!!!
		Vector3f displacement = new Vector3f();
		Vector3f deltaVelocity = new Vector3f();
		
		Vector3f previousPosition = position;
		
		velocity.mul(deltaTime, deltaVelocity);
		position.add(deltaVelocity);
		
		position.sub(previousPosition, displacement);
		
		displacement.mul(friction);
		displacement.mul(deltaTime);
		velocity.sub(displacement);
		velocity.add(acceleration.mul(deltaTime));
		
		acceleration.zero();
		
		
	}
	
	public void movementInput(Direction dir) {
		final float accelAmount = 2f;
		switch (dir) {
		
		case FORWARD:
			acceleration.x = accelAmount;
			break;
			
		case BACK:
			acceleration.x = -accelAmount;
			break;
			
		case LEFT:
			acceleration.z = -accelAmount;
			break;
			
		case RIGHT:
			acceleration.z = accelAmount;
			break;
			
		default:
			break;
		}
		
	}
}

enum Direction {
	FORWARD,
	BACK,
	LEFT,
	RIGHT,
	JUMP
}
