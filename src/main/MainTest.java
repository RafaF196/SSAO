package main;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRender.TextMaster;
import models.RawModel;
import objFileLoader.ObjFileLoader;
import render.DisplayManager;
import render.FrameBufferObject;
import render.Loader;
import render.MasterRenderer;

public class MainTest {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		TextMaster.init(loader);
		
		Camera camera = new Camera();
		List<Entity> entities = new ArrayList<Entity>();
		
		entities.add(new Entity(loadModel("house", loader), new Vector3f(0, -1, 0), 180, 0.4f));
		
		FontType font = new FontType(loader.loadTexture("candara"), new File("res/candara.fnt"));
		GUIText fps_text = new GUIText("FPS: ", 1.8f, font, new Vector2f(0.008f, 0.008f), 1f, false);
		fps_text.setColour(1, 1, 1);
		GUIText N_text = new GUIText("N: 5", 1.8f, font, new Vector2f(0.008f, 0.068f), 1f, false);
		N_text.setColour(1, 0, 0);
		GUIText R_text = new GUIText("R: 10,0", 1.8f, font, new Vector2f(0.008f, 0.128f), 1f, false);
		R_text.setColour(1, 0, 0);
		GUIText Strategy_text = new GUIText("Strategy 1", 1.8f, font, new Vector2f(0.008f, 0.188f), 1f, false);
		Strategy_text.setColour(0, 0.3f, 1);
		GUIText Blur_text = new GUIText("Blur off", 1.8f, font, new Vector2f(0.008f, 0.248f), 1f, false);
		Blur_text.setColour(0, 0.3f, 1);
		
		Integer randomTex = loader.loadTexture("randcolor");
		
		Integer N = 5, strat = 0, blur = 0;
		Integer counter = 1;
		float delta, deltacount = 0, R = 10;
		
		Boolean NchangePressed = false, blurPressed = false, stratPressed = false;
		
		// A total of 3 FBOs are needed before the rendering to the screen (only if blurring is applied)
		FrameBufferObject fbo = new FrameBufferObject();
		FrameBufferObject fbo2 = new FrameBufferObject();
		FrameBufferObject fbo3 = new FrameBufferObject();
		
		while(!Display.isCloseRequested()) {
			
			// Change N
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (!NchangePressed) {
					if (N < 10) N++;
					NchangePressed = true;
					DecimalFormat decimalFormat = new DecimalFormat("0");
			        String numberAsString = decimalFormat.format(N);
			        N_text.remove();
			        N_text = new GUIText("N: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.068f), 1f, false);
			        N_text.setColour(1, 0, 0);
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (!NchangePressed) {
					if (N > 1) N--;
					NchangePressed = true;
					DecimalFormat decimalFormat = new DecimalFormat("0");
			        String numberAsString = decimalFormat.format(N);
			        N_text.remove();
			        N_text = new GUIText("N: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.068f), 1f, false);
			        N_text.setColour(1, 0, 0);
				}
			} else {
				NchangePressed = false;
			}
			
			// Change R
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				if (R < 29.95) R += 0.2;
				DecimalFormat decimalFormat = new DecimalFormat("0.0");
		        String numberAsString = decimalFormat.format(R);
		        R_text.remove();
		        R_text = new GUIText("R: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.128f), 1f, false);
		        R_text.setColour(1, 0, 0);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				if (R > 1) R -= 0.2;
				DecimalFormat decimalFormat = new DecimalFormat("0.0");
		        String numberAsString = decimalFormat.format(R);
		        R_text.remove();
		        R_text = new GUIText("R: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.128f), 1f, false);
		        R_text.setColour(1, 0, 0);
			}
			
			// Change strat
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				if (!stratPressed) {
					strat = (strat + 1)%3;
					stratPressed = true;
					DecimalFormat decimalFormat = new DecimalFormat("0");
			        String numberAsString = decimalFormat.format(strat+1);
			        Strategy_text.remove();
			        Strategy_text = new GUIText("Strategy " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.188f), 1f, false);
			        Strategy_text.setColour(0, 0.3f, 1);
				}
			} else {
				stratPressed = false;
			}
			
			// Change blur
			if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
				if (!blurPressed) {
					blur = (blur + 1)%2;
					blurPressed = true;
					Blur_text.remove();
			        if (blur == 0) Blur_text = new GUIText("Blur off", 1.8f, font, new Vector2f(0.008f, 0.248f), 1f, false);
			        else Blur_text = new GUIText("Blur on", 1.8f, font, new Vector2f(0.008f, 0.248f), 1f, false);
			        Blur_text.setColour(0, 0.3f, 1);
				}
			} else {
				blurPressed = false;
			}
			
			delta = DisplayManager.getFrameTimeSeconds();
			
	        if (deltacount > 0.2f) { // update every 200 ms or so
	        	float fps = (float) (counter/deltacount);
		        DecimalFormat decimalFormat = new DecimalFormat("00");
		        String numberAsString = decimalFormat.format(fps);
		        fps_text.remove();
		        fps_text = new GUIText("FPS: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.008f), 1f, false);
		        fps_text.setColour(1, 1, 1);
				counter = 1;
				deltacount = 0;
	        } else {
	        	counter++;
	        	deltacount += delta;
	        }

	        fbo.bindFrameBuffer();
	        renderer.renderScene(entities.subList(0, 1), camera, N);
	        fbo.unbindCurrentFrameBuffer();
	        if (blur == 1) {
		        fbo2.bindFrameBuffer();
		        renderer.renderScene2(fbo.getTexture(), randomTex, N, R, strat);
		        fbo2.unbindCurrentFrameBuffer();
		        fbo3.bindFrameBuffer();
		        renderer.renderHorBlur(fbo2.getTexture());
		        fbo3.unbindCurrentFrameBuffer();
				renderer.renderVerBlur(fbo3.getTexture());
	        } else {
	        	renderer.renderScene2(fbo.getTexture(), randomTex, N, R, strat);
	        }
			
			TextMaster.render();
			camera.move();
			DisplayManager.updateDisplay();
			
		}
		
		fbo.cleanUp();
		fbo2.cleanUp();
		TextMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	private static RawModel loadModel(String modelName, Loader loader){
		RawModel model = ObjFileLoader.loadOBJ(modelName, loader);
		return model;
	}

}
