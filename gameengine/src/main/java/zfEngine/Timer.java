package zfEngine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
	
	private double currentFrame;
	private double lastFrame;
	private double deltaTime;
	
	private double lastInterval;
	private double interval;
	
	private boolean intervalTimerOn;
	
	public Timer() {
		currentFrame = 0;
		lastFrame = 0;
		deltaTime = 0;
	}
	
	public double update() {
		currentFrame = glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;
		
		return deltaTime;
	}
	
	public float getDeltaTime() {
		return (float)deltaTime;
	}
	
	public void createIntervalTimer(double interval) {
		lastInterval = currentFrame;
		intervalTimerOn = true;
		this.interval = interval;
	}
	
	public void createIntervalTimer(double interval, boolean hertz) {
		lastInterval = currentFrame;
		intervalTimerOn = true;
		this.interval = (hertz) ? 1.0/interval : interval;
	}
	
	public void startIntervalTimer() {
		intervalTimerOn = true;
	}
	
	public void stopIntervalTimer() {
		intervalTimerOn = false;
	}
	
	public boolean hasIntervalPassed() {
		
		if (intervalTimerOn && currentFrame >= lastInterval+interval) {
			lastInterval = currentFrame;
			return true;
		}
		
		return false;
	}
}
