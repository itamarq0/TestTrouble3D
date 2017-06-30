package org.testtrouble3d.game.engine.utils;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class ImageData{
	
	public ImageData(String path){
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			imageData = stbi_load(path, w, h, comp, 4);
			
			if (imageData == null) {
			    throw new RuntimeException("LoadImageFile::Failed to load a image file!" + System.lineSeparator() + stbi_failure_reason());
			}
			
			width = w.get();
			height = h.get();
		}
	}
	
	public ImageData(ByteBuffer imageData,int width,int height){
		this.imageData = imageData;
		this.width = width;
		this.height = height;
	}
	
	public ByteBuffer getImageBuffer(){
		return this.imageData;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	

	private ByteBuffer imageData;
	private final int width;
	private final int height;
	
}
