package org.testtrouble3d.game.engine.renderer.renderables;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;
import org.testtrouble3d.game.engine.renderer.shader.ShaderProgram;
import org.testtrouble3d.game.engine.renderer.textures.Texture;


public class Mesh implements IRenderable  {
	
	private enum MeshVBOAttachment{
		TEXTURE_COORDS,
		INDICIES,
		TEXTURE
	}

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int textVboId;
    private Set<MeshVBOAttachment> attachments;
    private final int vertexCount;
    private Texture texture;
    
    public Mesh(float[] positions,  float[] textCoords, int[] indices , Texture texture) {
    	this.texture = texture;
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer textCoordBuffer = null;
        try {
        	vertexCount = indices.length;
            
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            
        	textCoordBuffer = MemoryUtil.memAllocFloat(textCoords.length);
        	textCoordBuffer.put(textCoords).flip();
            
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            
            // Indices VBO
            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Position VBO
            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);   
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            
        	// Color VBO
            textVboId = glGenBuffers();
        	glBindBuffer(GL_ARRAY_BUFFER, textVboId);
        	glBufferData(GL_ARRAY_BUFFER, textCoordBuffer, GL_STATIC_DRAW);
        	glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        	
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            glBindVertexArray(0);         
        } finally {
            if (verticesBuffer  != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null){
            	MemoryUtil.memFree(indicesBuffer);
            }
            if (textCoordBuffer != null){
            	MemoryUtil.memFree(textCoordBuffer);
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
        glDeleteBuffers(textVboId);
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
	    // Activate first texture unit
	    glActiveTexture(GL_TEXTURE0);
	    // Bind the texture
	    glBindTexture(GL_TEXTURE_2D, texture.getId());

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