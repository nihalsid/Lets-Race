package com.letsrace.game;

import static com.letsrace.game.FRConstants.PIXELS_PER_UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.letsrace.game.car.Car;
import com.letsrace.game.map.OrthogonalTiledMapRenderer;

public class GameRenderer {
	GameWorld worldRef;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera cam;
	SpriteBatch batch;
	TiledMapRenderer tiledMapRenderer;
	Car myCar;

	public GameRenderer(GameWorld world, Batch batch, Car myCar) {
		worldRef = world;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		this.cam = new OrthographicCamera(PIXELS_PER_UNIT * 20, PIXELS_PER_UNIT
				* 20 * h / w);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		debugRenderer = new Box2DDebugRenderer();
		tiledMapRenderer = new OrthogonalTiledMapRenderer(world.mapHandler.tiledMap, batch);
		this.myCar = myCar;
	}

	public void render() {
		cam.update();
		cam.rotate(worldRef.mapHandler.angleMon.getTurnAngle());
		cam.position.set(myCar.getWorldPosition().x * PIXELS_PER_UNIT,myCar.getWorldPosition().y * PIXELS_PER_UNIT, 0);
		tiledMapRenderer.setView(cam);
		tiledMapRenderer.render();
		Matrix4 debugMat = new Matrix4(cam.combined);
		debugMat.scale(PIXELS_PER_UNIT, PIXELS_PER_UNIT, 1f);
		debugRenderer.render(worldRef.physicalWorld, debugMat);
	}
}
