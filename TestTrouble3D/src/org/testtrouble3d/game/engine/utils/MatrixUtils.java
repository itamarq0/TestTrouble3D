package org.testtrouble3d.game.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixUtils {
    public static final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;  
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public static final Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
        Matrix4f worldMatrix = new Matrix4f();
    	worldMatrix.identity().translate(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }
    
    public static final Matrix4f getViewMatrix(Vector3f position, Vector3f rotation){
    	Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }
}
