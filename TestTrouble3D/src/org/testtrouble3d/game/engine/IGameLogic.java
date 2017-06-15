package org.testtrouble3d.game.engine;

import org.testtrouble3d.game.engine.window.Window;

public interface IGameLogic {
	
	void init() throws Exception;
	void input(Window window);
	void update(float interval);
	void render(Window window);
	void cleanup();
}
