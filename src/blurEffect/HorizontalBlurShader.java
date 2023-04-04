package blurEffect;

import shaders.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/blurEffect/horizontalBlurVertex.txt";
	private static final String FRAGMENT_FILE = "/blurEffect/blurFragment.txt";
	
	private int location_texture;
	
	protected HorizontalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void loadTargetWidth(float width){ }
	
	@Override
	protected void getAllUniformLocations() {
		location_texture = super.getUniformLocation("originalTexture");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_texture, 0);
	}
	
}
