package org.testtrouble3d.game.engine.window;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	// Constructors
	public Window(String windowName,int width,int height,boolean vsync){
		// Init fields
		this.width = width;
		this.height = height;
		this.vsync = vsync;
		this.resized = false;
		this.windowName = windowName;
	}
	// Helpers
	public void init(){
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

		// Create the window
		this.handle = glfwCreateWindow(this.width, this.height, windowName, NULL, NULL);
		if ( this.handle == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(this.handle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				this.handle,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
		// Make the OpenGL context current
		glfwMakeContextCurrent(handle);
		
		// Enable v-sync
		if(this.vsync){
			glfwSwapInterval(1);
		}
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
	}

	public void show(){
		// Make the window visible
		glfwShowWindow(handle);
	}
	public void cleanup(){
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(handle);
		glfwDestroyWindow(handle);
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	// Getters
	public long getHandle(){
		return this.handle;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public boolean shouldClose(){
		return glfwWindowShouldClose(handle);
	}
	public boolean isResized() {
		return this.resized;
	}
	public void setResized(boolean b) {
		this.resized = b;
	}
	public void setClearColor(float color, float color2 , float color3, float f) {
		// Set the clear color
		glClearColor(color, color2, color3, f);
		
	}
	// Fields
	private long handle;
	private int width;
	private int height;
	private boolean vsync;
	private boolean resized;
	private String windowName;






}
