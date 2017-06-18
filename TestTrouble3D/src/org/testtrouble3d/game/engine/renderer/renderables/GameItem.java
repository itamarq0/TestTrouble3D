package org.testtrouble3d.game.engine.renderer.renderables;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.utils.MatrixUtils;
public class GameItem implements Renderable {

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

	@Override
	public void render(ShaderProgram shader) {
		shader.bind();
        Matrix4f worldMatrix = MatrixUtils.getWorldMatrix(getPosition(), getRotation(), getScale());
        shader.setUniform("worldMatrix", worldMatrix);
        mesh.render(shader);
        shader.unbind();
	}

	@Override
	public void init() {
		mesh.init();
	}

	@Override
	public void cleanUp() {
		mesh.cleanUp();
	}
}
