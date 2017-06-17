package org.testtrouble3d.game.engine.renderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.testtrouble3d.game.engine.renderer.renderables.Renderable;
import org.testtrouble3d.game.engine.renderer.renderables.Triangle;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.utils.Utils;
import org.testtrouble3d.game.engine.window.Window;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {
	static{
		final String dir = System.getProperty("user.dir");
		shadersPath = dir + "/src/org/testtrouble3d/game/engine/renderer/shader/";
	}
	
	public Renderer(){

	}
	
	public void render(Window window, Renderable entity) {
		clear();
		shaderProgram.bind();
		
		entity.render(shaderProgram);

		shaderProgram.unbind();
		

	}
	
	public void init() throws Exception {
	    shaderProgram = new ShaderProgram();
	    shaderProgram.createVertexShader(Utils.loadResource(shadersPath + "vertexshaders/vertex1.vs"));
	    shaderProgram.createFragmentShader(Utils.loadResource(shadersPath + "fragmentshaders/fragment1.fs"));
	    shaderProgram.link();
	}

	
	public void cleanup(){
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}

	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	

	private final static String shadersPath;
	private ShaderProgram shaderProgram;
}
