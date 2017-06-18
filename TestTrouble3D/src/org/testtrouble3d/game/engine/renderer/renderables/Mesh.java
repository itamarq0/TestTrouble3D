package org.testtrouble3d.game.engine.renderer.renderables;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;


public class Mesh implements Renderable  {

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int colorVboId;
    private final int vertexCount;

/*    public Mesh(float[] positions) {
        FloatBuffer verticesBuffer = null;
        try {
            vertexCount = positions.length / 3;
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            
            

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);            
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);         
        } finally {
            if (verticesBuffer  != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }*/
    
    public Mesh(float[] positions, float[] colors, int[] indices) {
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer colourBuffer = null;
        try {
        	vertexCount = indices.length;
            
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            
        	colourBuffer = MemoryUtil.memAllocFloat(colors.length);
        	colourBuffer.put(colors).flip();
            
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            
            // Indecies VBO
            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Position VBO
            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);   
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            
        	// Color VBO
        	colorVboId = glGenBuffers();
        	glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        	glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
        	glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        	
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            glBindVertexArray(0);         
        } finally {
            if (verticesBuffer  != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null){
            	MemoryUtil.memFree(indicesBuffer);
            }
            if (colourBuffer != null){
            	MemoryUtil.memFree(colourBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
    
	@Override
    public void cleanUp() {
        
        glDisableVertexAttribArray(0);
	    glDisableVertexAttribArray(1);
	    
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

	@Override
	public void render(ShaderProgram shader) {
		// Bind to the VAO
		shader.bind();
	    glBindVertexArray(vaoId);
	    glEnableVertexAttribArray(0);
	    glEnableVertexAttribArray(1);

	    // Draw the vertices
	    glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

	    // Restore state
	    glDisableVertexAttribArray(1);
	    glDisableVertexAttribArray(0);
	    glBindVertexArray(0);
	    shader.unbind();
	}

	@Override
	public void init() {
	
	}

}