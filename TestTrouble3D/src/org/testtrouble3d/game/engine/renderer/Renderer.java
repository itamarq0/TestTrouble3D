package org.testtrouble3d.game.engine.renderer;
import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.testtrouble3d.game.engine.renderer.camera.Camera;
import org.testtrouble3d.game.engine.renderer.renderables.IRenderable;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.utils.MatrixUtils;
import org.testtrouble3d.game.engine.utils.Transformation;
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
	static {
		final String dir = System.getProperty("user.dir");
		shadersPath = dir + "/src/org/testtrouble3d/game/engine/renderer/shader/";
	}
	
	public Renderer(){
		camera = new Camera();
	}
	
	public void init() throws Exception {
	    shaderProgram = new ShaderProgram();
	    shaderProgram.createVertexShader(Utils.loatTextFile(shadersPath + "vertexshaders/vertex6.vs"));
	    shaderProgram.createFragmentShader(Utils.loatTextFile(shadersPath + "fragmentshaders/fragment3.fs"));
	    shaderProgram.link();
	    shaderProgram.createUniform("projectionMatrix");
	    shaderProgram.createUniform("worldMatrix");
	    shaderProgram.createUniform("viewMatrix");
	    shaderProgram.createUniform("texture_sampler");
	}
	
	public void render(Window window, IRenderable entity) {
	    shaderProgram.bind();
	    // Update projection Matrix
	    Matrix4f projectionMatrix = MatrixUtils.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
	    shaderProgram.setUniform("projectionMatrix", projectionMatrix);
	    Matrix4f viewMatrix = MatrixUtils.getViewMatrix(camera.getPosition(), camera.getRotation());
	    shaderProgram.setUniform("viewMatrix", viewMatrix);
	    shaderProgram.setUniform("texture_sampler", 0);
		entity.render(shaderProgram);
		shaderProgram.unbind();
	}

	public void cleanup(){
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}
	public Camera getCamera(){
		return camera;
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Camera camera;
	private final static String shadersPath;
	private ShaderProgram shaderProgram;
}
