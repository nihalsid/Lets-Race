package com.letsrace.game;

import static com.letsrace.game.FRConstants.PIXELS_PER_UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class FRGameRenderer {
	FRGameWorld worldRef;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera cam;
	SpriteBatch batch;
	TiledMapRenderer tiledMapRenderer;
	int carNumber;
	
	public FRGameRenderer(int myCarNumber, FRGameWorld world){
		worldRef = world;
		carNumber = myCarNumber;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		this.cam = new OrthographicCamera(PIXELS_PER_UNIT*20,PIXELS_PER_UNIT*20*h/w);
		cam.position.set(cam.viewportWidth/2f,cam.viewportHeight/2f,0);
		this.batch = new SpriteBatch();
		debugRenderer=new Box2DDebugRenderer();
		//tiledMapRenderer = new OrthogonalTiledMapRenderer(world.mapHandler.tiledMap);
	}
	
	public void render(){
		cam.update();
		cam.rotate(worldRef.mapHandler.angleMon.getTurnAngle());
		cam.position.set(worldRef.carHandler.cars.get(carNumber).getWorldPosition().x*PIXELS_PER_UNIT,worldRef.carHandler.cars.get(carNumber).getWorldPosition().y*PIXELS_PER_UNIT,0);
		//tiledMapRenderer.setView(cam);
		//tiledMapRenderer.render();
		Matrix4 debugMat = new Matrix4(cam.combined);
		debugMat.scale(PIXELS_PER_UNIT, PIXELS_PER_UNIT, 1f);
		batch.setProjectionMatrix(cam.combined);
		batch.enableBlending();
		batch.begin();
		debugRenderer.render(worldRef.physicalWorld, debugMat);
		batch.end();
	}
}
