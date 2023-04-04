package ssaoRenderer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class SSAOShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/ssaoRenderer/ssaoVertexShader.txt";
	private static final String FRAGMENT_FILE = "/ssaoRenderer/ssaoFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_modelTexture;
	private int location_randomTexture;
	private int location_n;
	private int location_radius;
	private int location_strat;
	private int location_randvecs[];
	private int location_randvecssep[];

	public SSAOShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_randomTexture, 1);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_randomTexture = super.getUniformLocation("randTexture");
		location_n = super.getUniformLocation("N");
		location_radius = super.getUniformLocation("radius");
		location_strat = super.getUniformLocation("strat");
		
		location_randvecs = new int[100];
		for(int i = 0; i < 100; i++){
			location_randvecs[i] = super.getUniformLocation("randvecs[" + i + "]");
		}
		
		location_randvecssep = new int[20];
		for(int i = 0; i < 20; i++){
			location_randvecssep[i] = super.getUniformLocation("randvecssep[" + i + "]");
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadSSAOparams(int N, float radius) {
		super.loadInt(location_n, N);
		super.loadFloat(location_radius, radius);
	}
	
	public void loadSSAOstrategy(int strat) {
		super.loadInt(location_strat, strat);
	}
	
	// Generated N*N samples of random vectors
	public void loadRandVectorsNN(int N) {
		float x, y, z, min = -1, max = 1;
		for (int i = 0; i < N*N; i++) {
			x = (float) (Math.random() * (max - min + 1) + min);
			y = (float) (Math.random() * (max - min + 1) + min);
			z = (float) (Math.random() * (max - min + 1) + min);
			super.loadVector(location_randvecs[i], new Vector3f(x,y,z));
		}
	}
	
	// Generated 2*N samples of random vectors in two orthogonal directions
	public void loadRandVectors2N(int N) {
		float x, y, min = -1, max = 1;
		for (int i = 0; i < 2*N; i+=2) {
			x = (float) (Math.random() * (max - min + 1) + min);
			y = (float) (Math.random() * (max - min + 1) + min);
			super.loadVector(location_randvecssep[i], new Vector3f(x,0,0));
			super.loadVector(location_randvecssep[i+1], new Vector3f(0,y,0));
		}
	}

}
