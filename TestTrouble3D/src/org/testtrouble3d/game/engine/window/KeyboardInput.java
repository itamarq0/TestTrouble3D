package org.testtrouble3d.game.engine.window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class KeyboardInput {

	//TODO: Use Enum instead of boolean?
	private boolean[] keys;
	public final int NUM_OF_KEYS = 1024;
	
	public KeyboardInput(){
		keys = new boolean[NUM_OF_KEYS];
		// Set all keys to not pressed
		for(int i=0; i< NUM_OF_KEYS ; i++){
			keys[i] = false;
		}
	}
	
	public void init(Window window){
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window.getHandle(), (windowHandle, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(windowHandle, true); // We will detect this in the rendering loop
			if(action == GLFW_RELEASE ){
				keys[key] = false;
			}
			if(action == GLFW_PRESS){
				keys[key] = true;
			}
		});
	}
	
	// Mutators
	public void setKeyState(int keyCode,boolean state){
		keys[keyCode] = state;
	}
	public boolean isKeyPressed(int keyCode) {
		return this.keys[keyCode];
	}
	
	
	
}
