package org.testtrouble3d.game.engine;

import org.testtrouble3d.game.engine.window.MouseInput;
import org.testtrouble3d.game.engine.window.Window;

public interface IGameLogic {
	
	void init() throws Exception;
	void input(Window window ,MouseInput mouseInput);
	void update(float interval, MouseInput mouseInput);
	void render(Window window);
	void cleanup();
}
