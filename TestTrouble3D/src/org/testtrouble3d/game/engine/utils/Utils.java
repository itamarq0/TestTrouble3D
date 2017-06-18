package org.testtrouble3d.game.engine.utils;



import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;




public class Utils {
	private Utils(){
		
	}
	public static String loadResource(String path){
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			System.out.println("Utils::Cannot find resource at: " + path);
			e.printStackTrace();
		}
		return new String(encoded, Charset.defaultCharset());	
	}
	

}
