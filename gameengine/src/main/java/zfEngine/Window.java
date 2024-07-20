package zfEngine;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
	/*
	 * window pointer (long)
	 * GLFW init
	 * vsync
	 * viewport
	 * fullscreen!
	 * GET window
	 */
	
	private long windowId;
	private int width, height;
	private boolean vsync;
	private boolean isFullscreen;
	private boolean isWireframe;
	private HashMap<String, Integer> windowKeys = new HashMap<String, Integer>();

	
	public Window(int width, int height, String name, boolean fullscreen) {
		this.width = width;
		this.height = height;
		vsync = false;
		isWireframe = false;
		
		if (!glfwInit()) {
			System.err.println("Unable to intialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		createWindow(name, fullscreen);
		
		glfwMakeContextCurrent(windowId);
		GL.createCapabilities();
		
		glfwSwapInterval(0);
		
		glViewport(0, 0, width, height);
		
		//READ KEYS FROM FILES
		//PUT IN HASHMAP
		windowKeys.put("Fullscreen", GLFW_KEY_F11);
		
		//###################
		//##   Callbacks   ##
		//###################
		glfwSetFramebufferSizeCallback(windowId, (window, w, h) -> {
			glViewport(0, 0, w, h);
			this.width = w;
			this.height = h;
		});
		
		glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) ->{
			//Close window on escape
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
			//Fullscreen toggle
			if (key == windowKeys.get("Fullscreen") && action == GLFW_PRESS) {
				long monitor = glfwGetPrimaryMonitor();
				if (isFullscreen) {
					this.width = 1000;
					this.height = 1000;
					
					glfwSetWindowMonitor(window, NULL, 50, 50, this.width, this.height, 0);
					glViewport(0, 0, this.width, this.height);
					
				} else {
					GLFWVidMode mode = glfwGetVideoMode(monitor);
					glfwSetWindowMonitor(window, monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
					
					this.width = mode.width();
					this.height = mode.height();
					
					glViewport(0, 0, this.width, this.height);
				}
				
				isFullscreen = (isFullscreen) ? false : true;
			}
			//Wireframe toggle
			if (key == GLFW_KEY_F1 && action == GLFW_PRESS) {
				
				if (isWireframe) {
					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				} else {
					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); //Wireframe drawing mode					
				}
				
				isWireframe = (isWireframe) ? false : true;

			}
			
			
		} );
		
	}
	
	private void createWindow(String name, boolean fullscreen) {
		
		long fullscreenHandle = (fullscreen) ? glfwGetPrimaryMonitor() : NULL; 
		
		windowId = glfwCreateWindow(width, height, name, fullscreenHandle, NULL);
		
		if (windowId == NULL) {
			System.err.println("Failed to create GLFW window");
			glfwTerminate();
		}
		
	}
	
	public void bind() {
		glfwMakeContextCurrent(windowId);
	}
	public long getWindow() {
		return windowId;
	}
	
	public void setVsync(boolean vsync) {
		this.vsync = vsync;
		if (this.vsync) 	{glfwSwapInterval(1);}
		else 				{glfwSwapInterval(0);}
	}
	
	public boolean isVsyncOn() {
		return this.vsync;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public float getAspectRatio() {
		return (float)width / (float)height;
	}
	
	public boolean isKeyPressed(int key) {
		return glfwGetKey(windowId, key) == GLFW_PRESS;
	}
	
	public boolean shouldWindowClose() {
		return glfwWindowShouldClose(windowId);
	}
	
	
	public void swapBuffer() {
		glfwSwapBuffers(windowId);
	}
	
	public void destory() {
		glfwDestroyWindow(windowId);
	}
	
	/**
	 * Free all callbacks to <i><code>this</code></i> window
	 */
	public void free() {
		Callbacks.glfwFreeCallbacks(windowId);
	}

}
