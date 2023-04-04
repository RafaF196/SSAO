package ssaoRenderer;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import render.Loader;
import toolbox.MatrixOps;

public class SSAORenderer {

	private final RawModel quad;
	private SSAOShader shader;
	
	public SSAORenderer(Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new SSAOShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadRandVectorsNN(10);
		shader.loadRandVectors2N(10);
		shader.stop();
	}
	
	public void render(Integer texId, Integer randTexId, Integer N, Float radius, Integer strat){
		shader.start();
		shader.loadSSAOparams(N, radius);
		shader.loadSSAOstrategy(strat);
		
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, randTexId);
		
		Matrix4f matrix = MatrixOps.createIdentityMatrix();
		shader.loadTransformation(matrix);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
}
