package org.testtrouble3d.game.engine.utils;



import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.lwjgl.system.MemoryStack;




public class Utils {
	private Utils(){
		
	}
	public static String loatTextFile(String path){
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			System.out.println("Utils::Cannot find resource at: " + path);
			e.printStackTrace();
		}
		return new String(encoded, Charset.defaultCharset());	
	}
	
	public static float[] listToArray(List<Float> numbers){
		float[] arr = new float[numbers.size()];
		int i = 0;
		for(Float num : numbers){
			arr[i] = num;
			i++;
		}
		return arr;
	}
	
	
	

}
