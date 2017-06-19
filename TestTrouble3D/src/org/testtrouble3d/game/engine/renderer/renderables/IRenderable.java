package org.testtrouble3d.game.engine.renderer.renderables;

import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;

public interface IRenderable {
	void render(ShaderProgram shader);
	void init();
	void cleanUp();
}
