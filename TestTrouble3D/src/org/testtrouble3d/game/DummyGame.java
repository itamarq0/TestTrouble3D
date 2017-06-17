package org.testtrouble3d.game;


import org.testtrouble3d.game.engine.IGameLogic;
import org.testtrouble3d.game.engine.renderer.Renderer;
import org.testtrouble3d.game.engine.renderer.renderables.Mesh;
import org.testtrouble3d.game.engine.renderer.renderables.Renderable;
import org.testtrouble3d.game.engine.renderer.renderables.Triangle;
import org.testtrouble3d.game.engine.window.Window;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DummyGame implements IGameLogic {
	private int direction = 0;
	private float color = 0.0f;
	private final Renderer renderer;
	private List<Renderable> renderables;
	public DummyGame() {
		renderer = new Renderer();
		renderables = new ArrayList<Renderable>();
	}
	@Override
	public void init() throws Exception {
		renderer.init();
	    Renderable tri = new Triangle(
	    	0.0f,  0.5f, 0.0f,
	    	-0.5f, -0.5f, 0.0f,
	    	0.5f, -0.5f, 0.0f
		);
	    //renderables.add(tri);
	    float[] positions = new float[]{
			-0.5f,  0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
	    };
	    Mesh mesh = new Mesh(positions);
	    renderables.add(mesh);
	}
	@Override
	public void input(Window window) {
		if ( window.isKeyPressed(GLFW_KEY_UP) ) {
			direction = 1;
		} else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
			direction = -1;
		} else {
			direction = 0;
		}
	}
	@Override
	public void update(float interval) {
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		} else if ( color < 0 ) {
			color = 0.0f;
		}
	}
	@Override
	public void render(Window window) {
		if ( window.isResized() ) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		window.setClearColor(color, color, color, 0.0f);
		for(Renderable entity: renderables){
			renderer.render(window,entity);
		}
		glfwSwapBuffers(window.getHandle());
	}
	@Override
	public void cleanup() {
		renderer.cleanup();
	    for(Renderable entity: renderables){
	    	entity.cleanUp();
	    }
	}
}
