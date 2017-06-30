package org.testtrouble3d.game.engine.heightmaps;

import java.nio.ByteBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.testtrouble3d.game.engine.renderer.renderables.IRenderable;
import org.testtrouble3d.game.engine.renderer.renderables.Mesh;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.renderer.textures.Texture;
import org.testtrouble3d.game.engine.utils.ImageData;
import org.testtrouble3d.game.engine.utils.MatrixUtils;

public class HeightMap implements IRenderable{
	private final float maxY;
	private final float minY;
	private final int maxColor;
	private ImageData heightMapData;
	private Mesh heightMesh;
	
	
	public HeightMap(String heightmapPath, String texturePath){
		maxY = HeightMapGenerator.getMaxY();
		minY = HeightMapGenerator.getMinY();
		maxColor = HeightMapGenerator.getMaxColor();
		heightMapData = new ImageData(heightmapPath);
		Texture texture = new Texture(texturePath);
		heightMesh = HeightMapGenerator.generateHeightMapMesh(heightMapData, texture);
		
	}
	
	public float getHeight(int x, int z, int width){
		ByteBuffer buffer = heightMapData.getImageBuffer();
		byte r = buffer.get(x * 4 + 0 + z * 4 * width);
		byte g = buffer.get(x * 4 + 1 + z * 4 * width);
		byte b = buffer.get(x * 4 + 2 + z * 4 * width);
		byte a = buffer.get(x * 4 + 3 + z * 4 * width);
		int argb = ((0xFF & a) << 24) | ((0xFF & r) << 16)
		| ((0xFF & g) << 8) | (0xFF & b);
		return minY + Math.abs(maxY - minY) * ((float) argb / (float) maxColor);
	}

	@Override
	public void render(ShaderProgram shader) {
		shader.bind();
        Matrix4f worldMatrix = MatrixUtils.getWorldMatrix(getPosition(), getRotation(), getScale());
        shader.setUniform("worldMatrix", worldMatrix);
		heightMesh.render(shader);
        shader.unbind();
	}

	private float getScale() {
		return 1;
	}

	private Vector3f getRotation() {
		return new Vector3f(0, 0, 0);
	}

	private Vector3f getPosition() {
		return new Vector3f(0, 0, 0);
	}

	@Override
	public void init() {
		heightMesh.init();
	}

	@Override
	public void cleanUp() {
		heightMesh.cleanUp();
	}
	
	


	
}
