package blurEffect;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import models.RawModel;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import render.Loader;

public class VerticalBlur {

	private final RawModel quad;
	private VerticalBlurShader shader;
	
	public VerticalBlur(Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new VerticalBlurShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(Integer texId){
		shader.start();
		
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
}
