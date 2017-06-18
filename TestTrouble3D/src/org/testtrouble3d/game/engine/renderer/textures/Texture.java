package org.testtrouble3d.game.engine.renderer.textures;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Texture {
	private final int id;
	public Texture(String path){
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			ByteBuffer image = stbi_load(path, w, h, comp, 4);
			if (image == null) {
			    throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason());
			}
			int width = w.get();
			int height = h.get();
			
			// Create a new OpenGL texture 
			id = glGenTextures();
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, id);
			
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width,
				    height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			
			glGenerateMipmap(GL_TEXTURE_2D);
		}
	}

	public int getId() {
		return id;
	}

}
