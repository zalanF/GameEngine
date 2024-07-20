package zfEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.*;

enum CameraDirection {
	FORWARD,
	BACK,
	LEFT,
	RIGHT,
	UP,
	DOWN,
}

public class Camera {
	
	//DEFAULTS
	static final float YAW 				= -90.0f;
	static final float PITCH 			= 0.0f;
	static final float SPEED 			= 2.5f;
	static final float SENSITIVITY 		= 0.1f;
	static final float ZOOM 			= 45.0f;
	
	Vector3f position;
	Vector3f front;
	Vector3f up;
	
	Vector3f right;
	Vector3f worldUp;
	
	float yaw;
	float pitch;	
	float speed;
	float sensitivity;
	float zoom;
	
	public Camera() {
		position = new Vector3f();
		worldUp = new Vector3f(0f,1f,0f);
		front = new Vector3f(0f,0f,-1f);
		right = new Vector3f();
		up = new Vector3f();
		
		yaw = YAW;
		pitch = PITCH;
		speed = SPEED;
		sensitivity = SENSITIVITY;
		zoom = ZOOM;
		
		updateCameraVectors();

	}
	
	public Camera(Vector3f position, Vector3f up, Vector3f front, float yaw, float pitch, float speed, float sensitivity, float zoom) {
		
		this.position = position;
		worldUp = up;
		this.front = front;
		right = new Vector3f();
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.speed = speed;
		this.sensitivity = sensitivity;
		this.zoom = zoom;
		
		
		updateCameraVectors();
		
	}
	
	public Camera(Vector3f position, Vector3f up, Vector3f front) {
		
		this.position = position;
		worldUp = up;
		this.front = front;
		right = new Vector3f();
		this.up = new Vector3f();
		
		yaw = YAW;
		pitch = PITCH;
		speed = SPEED;
		sensitivity = SENSITIVITY;
		zoom = ZOOM;
		
		
		updateCameraVectors();
		
	}
	
	
	public Matrix4f getViewMatrix() {
		Matrix4f view = new Matrix4f();
		Vector3f lookDirection = new Vector3f();
		position.add(front, lookDirection);
		return view.lookAt(position, lookDirection, up);
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void processKeyboard(CameraDirection direction, float deltaTime) {
		float velocity = speed * deltaTime;
		Vector3f changeInPos = new Vector3f();
		switch (direction) {
		case FORWARD:
			front.mul(velocity, changeInPos);
			position.add(changeInPos);
			break;
			
		case BACK:
			front.mul(velocity, changeInPos);
			position.sub(changeInPos);
			break;
			
		case LEFT:
			right.mul(velocity, changeInPos);
			position.sub(changeInPos);
			break;
			
		case RIGHT:
			right.mul(velocity, changeInPos);
			position.add(changeInPos);
			break;
		
		case UP:
			up.mul(velocity, changeInPos);
			position.add(changeInPos);
			break;
			
		case DOWN:
			up.mul(velocity, changeInPos);
			position.sub(changeInPos);
			break;
			
		default:
			break;
			
		}
		
	}
	
	public float processMouseScroll(double yoffset) {
		 zoom -= (float) yoffset;
		if (zoom < 1.0f) {
			zoom = 1.0f;
		}
		if (zoom > 45.0f) {
			zoom = 45.0f;
		}
		return zoom;
	}
	
	public void processMouseMovement(float xoffset, float yoffset, boolean pitchLock) {
		
		xoffset *= sensitivity;
		yoffset *= sensitivity;
		
		yaw += xoffset;
		pitch += yoffset;
		
		if (pitchLock) {
			
			if (pitch > 89.0f) {
				pitch = 89.0f;
			}
			if (pitch < -89.0f) {
				pitch = -89.0f;
			}
		}
		
		updateCameraVectors();
	}
	
	public void updateCameraVectors() {
		
		//Front vector
		Vector3f direction = new Vector3f();
		direction.x = (float) (cos(toRadians(yaw)) * cos(toRadians(pitch)));
		direction.y = (float)  sin(toRadians(pitch));
		direction.z = (float) (sin(toRadians(yaw)) * cos(toRadians(pitch)));
		front = direction.normalize();
		
		//Right
		front.cross(worldUp, right);
		right.normalize();
		
		//Up
		right.cross(front, up);
		up.normalize();
		
	}
	
	

}
