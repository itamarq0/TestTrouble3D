package org.testtrouble3d.game.engine;

import org.testtrouble3d.game.engine.window.Window;

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


public class GameEngine implements Runnable {
	
	public GameEngine(String windowTitle, int width, int height, boolean vsSync, IGameLogic gameLogic) throws Exception {
		//gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Window(windowTitle, width, height, vsSync);
		this.gameLogic = gameLogic;
	}
	
	public void start() {
		//gameLoopThread.start();
		run();
		
	}
	
	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception excp) {
			excp.printStackTrace();
		} finally {
			cleanup();
		}
	}

	protected void gameLoop() {
		window.show();

		float lastTimeStamp = 0;
		float currentTimeStamp =(float) glfwGetTime();
		float interval = 0;
		while (!window.shouldClose()) {
			lastTimeStamp = currentTimeStamp;
			currentTimeStamp = (float) glfwGetTime();
			interval = currentTimeStamp - lastTimeStamp;
		    input();
		    update(interval);
		    render();
		}
	}

	
	protected void render() {
		gameLogic.render(window);	
	}

	protected void init() throws Exception {
		//WINDOW NEEDS TO BE THE FIRST ONE INITIALIZED!
		window.init();
		gameLogic.init();
	}

	protected void cleanup(){
		window.cleanup();
		gameLogic.cleanup();
	}
	protected void input() {
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
		gameLogic.input(window);
	}
	protected void update(float interval) {
		gameLogic.update(interval);
	}

	//private final Thread gameLoopThread;
	private Window window;
	private IGameLogic gameLogic;
}