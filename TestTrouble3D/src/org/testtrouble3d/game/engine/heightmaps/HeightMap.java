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
	
	
	private Vector3f position;
	private final float incX;
	private final float incZ;
	private final float incTex;
	private final float maxY;
	private final float minY;
	private final int maxColor;
	private ImageData heightMapData;
	private Mesh heightMesh;
	
	
	public HeightMap(String heightmapPath, String texturePath){
		position = new Vector3f(HeightMapGenerator.getStartX(),HeightMapGenerator.getMinY(),HeightMapGenerator.getStartZ());
		maxY = HeightMapGenerator.getMaxY();
		minY = HeightMapGenerator.getMinY();
		maxColor = HeightMapGenerator.getMaxColor();
		incX = HeightMapGenerator.getIncX();
		incZ = HeightMapGenerator.getIncZ();
		incTex = HeightMapGenerator.getIncTex();
		heightMapData = new ImageData(heightmapPath);
		Texture texture = new Texture(texturePath);
		heightMesh = HeightMapGenerator.generateHeightMapMesh(heightMapData, texture);
	}
	
	public float getHeight(float x, float z){

		int indexX = (int) (Math.floor(x-position.x())/incX);
		//System.out.println(indexX);
		int indexZ = (int) (Math.floor(z-position.z())/incZ);
		//System.out.println(indexZ);
		if(indexX < 0 || indexZ < 0 || indexX >= heightMapData.getWidth()-1 || indexZ >= heightMapData.getHeight()-1){
			return 0.0f;
		}
		float posX = position.x()+(indexX + 1)*incX;
		float posY = getVertexHeight(indexX + 1, indexZ);
		float posZ = position.z()+indexZ*incZ;
		Vector3f v1 = new Vector3f(posX,posY,posZ);
		posX = position.x()+(indexX)*incX;
		posY = getVertexHeight(indexX, indexZ+1);
		posZ = position.z()+(indexZ+1)*incZ;
		Vector3f v2 = new Vector3f(posX,posY,posZ);
		if(x-position.x>z-position.z){
			indexX+= 1;
			indexZ+= 1;
		}
		posX = position.x()+(indexX)*incX;
		posY = getVertexHeight(indexX, indexZ);
		posZ = position.z()+(indexZ)*incZ;
		Vector3f v3 = new Vector3f(posX,posY,posZ);
		//System.out.println(v3);
		return interpolateHeight(x, z, v1, v2, v3)+position.y();
	}
	
	private float interpolateHeight(float x, float z,Vector3f v1,Vector3f v2,Vector3f v3){
		float a = (v2.y - v1.y) * (v3.z - v1.z) - (v3.y - v1.y) * (v2.z - v1.z);
		float b = (v2.z - v1.z) * (v3.x - v1.x) - (v3.z - v1.z) * (v2.x - v1.x);
		float c = (v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y);
		float d = -(a * v1.x + b * v1.y + c * v1.z);
		float y = (-d - a * x - c * z) / b;
		return y;
	}
	
	private float getVertexHeight(int x, int z){
		int width = heightMapData.getWidth();
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
		return position;
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
