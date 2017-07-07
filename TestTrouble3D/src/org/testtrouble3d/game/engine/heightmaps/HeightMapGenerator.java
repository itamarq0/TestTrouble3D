package org.testtrouble3d.game.engine.heightmaps;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.testtrouble3d.game.engine.renderer.renderables.Mesh;
import org.testtrouble3d.game.engine.renderer.textures.Texture;
import org.testtrouble3d.game.engine.utils.ImageData;
import org.testtrouble3d.game.engine.utils.Utils;

public class HeightMapGenerator {


	
	private static float startX = 0;
	private static float maxY = 10.0f;
	private static float minY = 0.0f;
	private static float startZ = 0;
	private static final int MAX_COLOR = 255 * 255 * 255;
	private static float incX = 1.0f;
	private static float incZ = 1.0f;
	private static float incTex = 30.0f;
	

	private HeightMapGenerator(){
		
	}
	
	private static float getHeight(int x, int z, int width, ByteBuffer heightMapData){
		byte r = heightMapData.get(x * 4 + 0 + z * 4 * width);
		byte g = heightMapData.get(x * 4 + 1 + z * 4 * width);
		byte b = heightMapData.get(x * 4 + 2 + z * 4 * width);
		byte a = heightMapData.get(x * 4 + 3 + z * 4 * width);
		int argb = ((0xFF & a) << 24) | ((0xFF & r) << 16)
		| ((0xFF & g) << 8) | (0xFF & b);
		return minY + Math.abs(maxY - minY) * ((float) argb / (float) MAX_COLOR);
	}
	
	public static void init(){
		
	}
	
	public static void setStartX(float startX){
		HeightMapGenerator.startX = startX;
	}
	
	public static void setStartZ(float startZ){
		HeightMapGenerator.startZ = startZ;
	}
	
	public static void setMaxY(float maxY){
		HeightMapGenerator.maxY = maxY;
	}
	
	public static void setMinY(float minY){
		HeightMapGenerator.minY = minY;
	}
	
	public static void setIncX(float incX){
		HeightMapGenerator.incX = incX;
	}
	
	public static void setIncZ(float incZ){
		HeightMapGenerator.incZ = incZ;
	}
	
	public static void setIncTex(float incTex){
		HeightMapGenerator.incTex = incTex;
	}
	
	public static float getStartX(){
		return HeightMapGenerator.startX;
	}
	
	public static float getStartZ(){
		return HeightMapGenerator.startZ;
	}
	
	public static float getMaxY(){
		return HeightMapGenerator.maxY;
	}
	
	public static float getMinY(){
		return HeightMapGenerator.minY;
	}
	
	public static float getIncX(){
		return HeightMapGenerator.incX;
	}
	
	public static float getIncZ(){
		return HeightMapGenerator.incZ;
	}
	
	public static float getIncTex(){
		return HeightMapGenerator.incTex;
	}
	
	public static int getMaxColor(){
		return HeightMapGenerator.MAX_COLOR;
	}
	
/*	public static Mesh generateHeightMapMesh(String heightmapPath, String texturePath){
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			ByteBuffer heightMapData = stbi_load(heightmapPath, w, h, comp, 4);
			if (heightMapData == null) {
			    throw new RuntimeException("HeightMapGenerator::Failed to load a height map file!" + System.lineSeparator() + stbi_failure_reason());
			}
			int width = w.get();
			int height = h.get();
			List<Float> positions = new ArrayList<>();
			List<Float> textCoords = new ArrayList<>();
			List<Integer> indices = new ArrayList<>();
			List<Float> normals = new ArrayList<>();
			
			int incx = 1;
			int incz = 1;
			int textInc = 30;
			
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					// Create vertex for current position
					positions.add(startX + col * incx); // x
					positions.add(getHeight(col, row, width, heightMapData)); //y
					positions.add(startZ + row * incz); //z
					// Init normal entries
					normals.add(0.0f);
					normals.add(0.0f);
					normals.add(0.0f);
					// Set texture coordinates
					textCoords.add((float) textInc * (float) col / (float) width) ;
					textCoords.add((float) textInc * (float) row / (float) height);
					// Create indices
					if (col < width - 1 && row < height - 1) {
						int leftTop = row * width + col;
						int leftBottom = (row + 1) * width + col;
						int rightBottom = (row + 1) * width + col + 1;
						int rightTop = row * width + col + 1;
						indices.add(rightTop);
						indices.add(leftBottom);
						indices.add(leftTop);
						indices.add(rightBottom);
						indices.add(leftBottom);
						indices.add(rightTop);
					}
				}
			}
			Iterator<Integer> ite = indices.iterator();
			Vector3f vertex1 = new Vector3f(0,0,0);
			Vector3f vertex2 = new Vector3f(0,0,0);
			Vector3f vertex3 = new Vector3f(0,0,0);
			Vector3f edge1 = new Vector3f(0,0,0);
			Vector3f edge2 = new Vector3f(0,0,0);
			Vector3f normal = new Vector3f(0,0,0);
			Float num = 0.0f;
			while(ite.hasNext()){
				int triIndex1 = ite.next();
				vertex1.x = positions.get(3*triIndex1);
				vertex1.y = positions.get(3*triIndex1+1);
				vertex1.z = positions.get(3*triIndex1+2);
				int triIndex2 = ite.next();
				vertex2.x = positions.get(3*triIndex2);
				vertex2.y = positions.get(3*triIndex2+1);
				vertex2.z = positions.get(3*triIndex2+2);
				int triIndex3 = ite.next();
				vertex3.x = positions.get(3*triIndex3);
				vertex3.y = positions.get(3*triIndex3+1);
				vertex3.z = positions.get(3*triIndex3+2);
				edge1 = vertex3.add(vertex1.negate());
				edge2 = vertex3.add(vertex2.negate());
				normal = edge1.cross(edge2).normalize();
				num = normals.get(3*triIndex1);
				num += normal.x;
				num = normals.get(3*triIndex1+1);
				num += normal.y;
				num = normals.get(3*triIndex1+1);
				num += normal.z;
				num = normals.get(3*triIndex2);
				num += normal.x;
				num = normals.get(3*triIndex2+1);
				num += normal.y;
				num = normals.get(3*triIndex2+1);
				num += normal.z;
				num = normals.get(3*triIndex3);
				num += normal.x;
				num = normals.get(3*triIndex3+1);
				num += normal.y;
				num = normals.get(3*triIndex3+1);
				num += normal.z;
			}
			Iterator<Float> itePos = normals.iterator();
			Float verX = 0.0f;
			Float verY = 0.0f;
			Float verZ = 0.0f;
			while(itePos.hasNext()){
				verX = itePos.next();
				verY = itePos.next();
				verZ = itePos.next();
				normal.x = verX;
				normal.y = verY;
				normal.z = verZ;
				normal.normalize();
				verX = normal.x;
				verY = normal.y;
				verZ = normal.z;
			}
			
			float[] posArr = Utils.listToArray(positions);
			float[] textCoordsArr = Utils.listToArray(textCoords);
			int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
			float[] normalsArr = Utils.listToArray(normals);
			Texture texture = new Texture(texturePath);
			// TODO: Add normal field to constructor
			return new Mesh(posArr,textCoordsArr,indicesArr,texture);
		}	
	}*/
	
	public static Mesh generateHeightMapMesh(ImageData heightMapImageData, Texture texture){
		ByteBuffer heightMapData = heightMapImageData.getImageBuffer();
		int width = heightMapImageData.getWidth();
		int height = heightMapImageData.getHeight();
		List<Float> positions = new ArrayList<>();
		List<Float> textCoords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				// Create vertex for current position
				positions.add(startX + col * incX); // x
				positions.add(getHeight(col, row, width, heightMapData)); //y
				positions.add(startZ + row * incZ); //z
				// Set texture coordinates
				textCoords.add((float) incTex * (float) col / (float) width) ;
				textCoords.add((float) incTex * (float) row / (float) height);
				// Create indices
				if (col < width - 1 && row < height - 1) {
					int leftTop = row * width + col;
					int leftBottom = (row + 1) * width + col;
					int rightBottom = (row + 1) * width + col + 1;
					int rightTop = row * width + col + 1;
					indices.add(rightTop);
					indices.add(leftBottom);
					indices.add(leftTop);
					indices.add(rightBottom);
					indices.add(leftBottom);
					indices.add(rightTop);
				}
			}
		}
		
		float[] posArr = Utils.listToArray(positions);
		float[] textCoordsArr = Utils.listToArray(textCoords);
		int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
		// TODO: Add normal field to constructor
		return new Mesh(posArr,textCoordsArr,indicesArr,texture);

	}
	
}
