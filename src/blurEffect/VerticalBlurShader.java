package blurEffect;

import shaders.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/blurEffect/verticalBlurVertex.txt";
	private static final String FRAGMENT_FILE = "/blurEffect/blurFragment.txt";
	
	private int location_texture;
	
	protected VerticalBlurShader() {
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
