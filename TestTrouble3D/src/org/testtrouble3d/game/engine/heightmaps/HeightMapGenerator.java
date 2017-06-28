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
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.system.MemoryStack;
import org.testtrouble3d.game.engine.renderer.renderables.Mesh;
import org.testtrouble3d.game.engine.renderer.textures.Texture;
import org.testtrouble3d.game.engine.utils.Utils;

public class HeightMapGenerator {


	
	private static final float START_X = -0.5f;
	private static float maxY = 3.0f;
	private static float minY = 0.0f;
	private static final float START_Z = -0.5f;
	private static final int MAX_COLOR = 255 * 255 * 255;

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


	
	public static Mesh generateHeightMap(String heightmapPath, String texturePath){
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
			
			int incx = 3;
			int incz = 3;
			int textInc = 3;
			
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					// Create vertex for current position
					positions.add(START_X + col * incx); // x
					positions.add(getHeight(col, row, width, heightMapData)); //y
					positions.add(START_Z + row * incz); //z
					// Set texture coordinates
					textCoords.add((float) textInc * (float) col / (float) width);
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
			
			float[] posArr = Utils.listToArray(positions);
			float[] textCoordsArr = Utils.listToArray(textCoords);
			int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
			Texture texture = new Texture(texturePath);
			return Mesh();
		}	
	}
	
}
