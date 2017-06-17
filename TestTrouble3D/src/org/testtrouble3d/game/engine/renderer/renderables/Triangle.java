package org.testtrouble3d.game.engine.renderer.renderables;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.window.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;



public class Triangle extends Mesh {
	public Triangle(float point1X , float point1Y,float point1Z,float point2X,float point2Y,float point2Z,float point3X,float point3Y,float point3Z) {
		super(
			new float[]{
				point1X,point1Y,point1Z,
				point2X,point2Y,point2Z,
				point3X,point3Y,point3Z
			}
		);
	}
}
