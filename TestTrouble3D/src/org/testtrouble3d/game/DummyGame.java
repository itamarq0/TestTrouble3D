package org.testtrouble3d.game;


import org.testtrouble3d.game.engine.IGameLogic;
import org.testtrouble3d.game.engine.heightmaps.HeightMap;
import org.testtrouble3d.game.engine.heightmaps.HeightMapGenerator;
import org.testtrouble3d.game.engine.renderer.Renderer;
import org.testtrouble3d.game.engine.renderer.camera.Camera;
import org.testtrouble3d.game.engine.renderer.renderables.GameItem;
import org.testtrouble3d.game.engine.renderer.renderables.Mesh;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.renderer.renderables.IRenderable;
import org.testtrouble3d.game.engine.renderer.textures.Texture;
import org.testtrouble3d.game.engine.window.KeyboardInput;
import org.testtrouble3d.game.engine.window.MouseInput;
import org.testtrouble3d.game.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DummyGame implements IGameLogic {
	static {
		final String dir = System.getProperty("user.dir");
		texturesPath = dir + "/src/org/testtrouble3d/game/engine/renderer/textures/";
		heightMapsPath = dir + "/src/org/testtrouble3d/game/engine/heightmaps/";
	}
	private static final String texturesPath;
	private static final String heightMapsPath;
	private static final float CAMERA_POS_STEP = 4.0f;
	private static final int MOUSE_SENSITIVITY = 45;
	private int direction = 0;
	private float color = 0.0f;
	private final Renderer renderer;
	private Map<String,IRenderable> renderables;
	private Vector3f cameraInc;
	
	public DummyGame() {
		renderer = new Renderer();
		renderables = new HashMap<String,IRenderable>();
		cameraInc = new Vector3f();
	}
	
	@Override
	public void init() throws Exception {
		System.out.println(texturesPath);
		renderer.init();
        // Create the Mesh
        float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,
            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,
            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,};
        float[] textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,
            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,
            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,};
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,};
        Texture texture = new Texture(texturesPath + "cube_texture.png");
	    Mesh mesh = new Mesh(positions,textCoords,indices,texture);
	    GameItem item = new GameItem(mesh);
	    item.init();
	    renderables.put("cube",item);
	    HeightMap map = new HeightMap(heightMapsPath + "heightmap.png", texturesPath + "grass.jpg");
	    map.init();
	    renderables.put("map",map);
	}
	
	@Override
	public void input(Window window, MouseInput mouseInput,KeyboardInput keyboardInput) {

		cameraInc.set(0, 0, 0);
	    if (keyboardInput.isKeyPressed(GLFW_KEY_W)) {
	        cameraInc.z = -1;
	    } else if (keyboardInput.isKeyPressed(GLFW_KEY_S)) {
	        cameraInc.z = 1;
	    }
	    if (keyboardInput.isKeyPressed(GLFW_KEY_A)) {
	        cameraInc.x = -1;
	    } else if (keyboardInput.isKeyPressed(GLFW_KEY_D)) {
	        cameraInc.x = 1;
	    }
	    if (keyboardInput.isKeyPressed(GLFW_KEY_Z)) {
	        cameraInc.y = -1;
	    } else if (keyboardInput.isKeyPressed(GLFW_KEY_X)) {
	        cameraInc.y = 1;
	    }
	    if(keyboardInput.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
	    	cameraInc.x *=4;
	    	cameraInc.y *=4;
	    	cameraInc.z *=4;
	    }
	}
	
	@Override
	public void update(float interval, MouseInput mouseInput,KeyboardInput keyboardInput) {
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		} else if ( color < 0 ) {
			color = 0.0f;
		}
		// Update rotation angle
		GameItem item = (GameItem)renderables.get("cube");
		float rotation = item.getRotation().x + 1.5f;
		if ( rotation > 360 ) {
		    rotation = 0;
		}
		item.setPosition(0.0f, 0.0f, -2.0f);
		item.setRotation(rotation, rotation, rotation);
		Camera camera = renderer.getCamera();
		HeightMap floor = (HeightMap)renderables.get("map");
	    // Update camera position
		System.out.println(floor.getHeight(camera.getPosition().x, camera.getPosition().z)+1.0f);
		camera.setPosition(camera.getPosition().x,floor.getHeight(camera.getPosition().x, camera.getPosition().z) +0.5f, camera.getPosition().z);
	    camera.movePosition(cameraInc.x * CAMERA_POS_STEP * interval,
	    		0,
	        cameraInc.z * CAMERA_POS_STEP * interval);
	    // Update camera based on mouse            
	    if (mouseInput.isRightButtonPressed()) {
	        Vector2f rotVec = mouseInput.getDisplVec();
	        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY * interval, rotVec.y * MOUSE_SENSITIVITY * interval, 0);
	    }
	}
	
	@Override
	public void render(Window window) {
		if ( window.isResized() ) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		window.setClearColor(color, color, color, 0.0f);
		renderer.clear();
		for(Entry<String, IRenderable> entry : renderables.entrySet()) {
		    String name = entry.getKey();
		    IRenderable entity = entry.getValue();
		    renderer.render(window, entity);
		}
		glfwSwapBuffers(window.getHandle());
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
		for(Entry<String, IRenderable> entry : renderables.entrySet()) {
		    IRenderable entity = entry.getValue();
		    entity.cleanUp();
		}
	}
}
