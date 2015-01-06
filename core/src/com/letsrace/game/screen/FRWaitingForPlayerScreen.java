package com.letsrace.game.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.letsrace.game.Animation;
import com.letsrace.game.LetsRace;
import com.letsrace.game.unused.FRAssets;
import com.letsrace.game.unused.OverlapTester;

public class FRWaitingForPlayerScreen extends ScreenAdapter {
	public LetsRace gameRef;
	SpriteBatch batch;

	Sprite car;
	Sprite firedMissile;
	Sprite cannistor;
	Animation explosion;
	Sprite background;
	BitmapFont font;

	Camera camera;
	int score;

	enum State {
		COMPLETED, RUNNING
	}

	State state;

	class Box implements Poolable {
		Rectangle bound;
		boolean isHit;

		public Box() {
			bound = new Rectangle();
			bound.setSize(50, 20);
			isHit = false;
		}

		public void init() {
			bound.setPosition(0, 0);
			bound.setSize(50, 20);
			isHit = false;
		}

		@Override
		public void reset() {
			bound.setPosition(0, 0);
			bound.setSize(50, 20);
			isHit = false;
		}

		public void update(float delta) {
			bound.setPosition(bound.getX(), bound.getY() - 10 * delta);
		}

	}

	class Missile implements Poolable {
		Rectangle bound;
		boolean isHit;

		int width = 20;
		int height = 75;

		public Missile() {
			bound = new Rectangle();
			bound.setSize(width, height);
			isHit = false;
		}

		public void init() {
			bound.setPosition(0, 0);
			bound.setSize(width, height);
			isHit = false;
		}

		@Override
		public void reset() {
			bound.setPosition(0, 0);
			bound.setSize(width, height);
			isHit = false;
		}

		public void update(float delta) {
			bound.setPosition(bound.getX(), bound.getY() + 180 * delta);
		}

	}

	class Car {
		Rectangle bound;

		public Car() {
			bound = new Rectangle(150, 10, 75, 90);
		}
	}

	Array<Box> cannisters = new Array<FRWaitingForPlayerScreen.Box>();

	// bullet pool.
	private final Pool<Box> cannisterPool = new Pool<Box>() {
		@Override
		protected Box newObject() {
			return new Box();
		}
	};

	Array<Missile> missiles = new Array<FRWaitingForPlayerScreen.Missile>();

	// bullet pool.
	private final Pool<Missile> missilePool = new Pool<Missile>() {
		@Override
		protected Missile newObject() {
			return new Missile();
		}
	};

	Random random;
	Car vaderCar;
	private final float TIME_BETWEEN_SPAWN = 1.4f;

	float timeElapsed;

	public FRWaitingForPlayerScreen(LetsRace letsRace) {
		this.gameRef = letsRace;
		this.car = FRAssets.vaderFullCar;
		this.cannistor = FRAssets.cannister;
		this.firedMissile = FRAssets.firedMissile;
		this.explosion = FRAssets.explosion;
		gameRef.stage.clear();
		camera = new OrthographicCamera(300, 500);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0);
		camera.update();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		random = new Random();

		FRAssets.load();
		car = FRAssets.vaderFullCar;
		cannistor = FRAssets.cannister;
		firedMissile = FRAssets.firedMissile;

		background = FRAssets.background;
		vaderCar = new Car();
		touchPoint = new Vector3();

		font = new BitmapFont();

		state = State.RUNNING;
	}

	private static final float TIME_BETWEEN_FIRE = 0.6f;
	float timeSinceLastFire;
	private Vector3 touchPoint;

	@Override
	public void render(float delta) {
		switch (state) {
		case COMPLETED:
			updateComplete();
			renderComplete();
			break;
		case RUNNING:
			updateRunning(delta);
			renderRunning(delta);
			break;
		}
	}

	private void updateComplete() {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			resetGame();
			state = State.RUNNING;
		}
	}

	private void renderComplete() {
		batch.begin();
		background.draw(batch);
		background.setPosition(0, 0);
		background.setSize(300, 500);
		background.draw(batch);
		font.draw(batch, "TAP TO RESTART", 100, 250);
		batch.end();
	}

	private void renderRunning(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		background.setPosition(0, 0);
		background.setSize(300, 500);
		background.draw(batch);

		Box item;
		int len = cannisters.size;
		for (int i = len; --i >= 0;) {
			item = cannisters.get(i);
			if (item.isHit)
				continue;
			cannistor.setSize(item.bound.getWidth(), item.bound.getHeight());
			cannistor.setPosition(item.bound.getX(), item.bound.getY());
			cannistor.draw(batch);
		}

		Missile mitem;
		len = missiles.size;
		for (int i = len; --i >= 0;) {
			mitem = missiles.get(i);
			if (mitem.isHit)
				continue;
			firedMissile.setSize(mitem.bound.getWidth(),
					mitem.bound.getHeight());
			firedMissile.setPosition(mitem.bound.getX(), mitem.bound.getY());
			firedMissile.draw(batch);
		}

		car.setSize(vaderCar.bound.getWidth(), vaderCar.bound.getHeight());
		car.setPosition(vaderCar.bound.getX(), vaderCar.bound.getY());
		car.draw(batch);
		// explosion.getCurrentSprite().draw(batch);
		font.setColor(Color.BLACK);
		font.draw(batch, "Score : " + score, 220, 490);
		batch.end();

	}

	private void updateRunning(float delta) {
		timeElapsed += delta;
		timeSinceLastFire += delta;

		Box item;
		int l = cannisters.size;
		for (int i = l; --i >= 0;) {
			item = cannisters.get(i);
			if (item.bound.getY() < 0) {
				state = State.COMPLETED;
				return;
			}
		}

		if (Gdx.input.isTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (OverlapTester.pointInRectangle(vaderCar.bound, touchPoint.x,
					touchPoint.y)) {
				fireMissile();
			} else {
				if (touchPoint.x < 100) {
					moveCarLeft(delta);
				}
				if (touchPoint.x > 200) {
					moveCarRight(delta);
				}
			}
		}

		Missile mitem;
		int len = missiles.size;
		for (int i = len; --i >= 0;) {
			mitem = missiles.get(i);
			mitem.update(delta);
		}

		len = cannisters.size;
		for (int i = len; --i >= 0;) {
			item = cannisters.get(i);
			item.update(delta);
		}

		len = cannisters.size;
		for (int i = len; --i >= 0;) {
			item = cannisters.get(i);
			int mlen = missiles.size;
			for (int j = mlen; --j >= 0;) {
				mitem = missiles.get(j);
				if (item.isHit || mitem.isHit) {
					continue;
				}
				if (OverlapTester.overlapRectangles(mitem.bound, item.bound)) {
					item.isHit = true;
					mitem.isHit = true;
					score += 1;
				}
			}
		}

		len = missiles.size;
		for (int i = len; --i >= 0;) {
			mitem = missiles.get(i);
			if (mitem.bound.getY() > 500) {
				mitem.isHit = true;
			}
			if (mitem.isHit == true) {
				missiles.removeIndex(i);
				missilePool.free(mitem);
			}
		}

		if (timeElapsed > TIME_BETWEEN_SPAWN) {
			timeElapsed = 0.0f;
			Box box = cannisterPool.obtain();
			int x = random.nextInt(6);
			box.bound.setPosition(x * 50, 490);
			cannisters.add(box);
			len = cannisters.size;
			for (int i = len; --i >= 0;) {
				item = cannisters.get(i);
				if (item.bound.getY() < 0) {
					item.isHit = true;
				}
				if (item.isHit == true) {
					cannisters.removeIndex(i);
					cannisterPool.free(item);
				}
			}
		}

		explosion.update(delta);
	}

	public void fireMissile() {
		if (timeSinceLastFire > TIME_BETWEEN_FIRE) {
			timeSinceLastFire = 0.0f;
			Missile missile = missilePool.obtain();
			missile.bound.setPosition(
					vaderCar.bound.getX() + vaderCar.bound.getWidth() / 2
							- missile.bound.getWidth() / 2,
					vaderCar.bound.getY() + vaderCar.bound.getHeight());
			missiles.add(missile);
		}
	}

	public void moveCarLeft(float delta) {
		vaderCar.bound.setPosition(
				Math.max(0, vaderCar.bound.getX() - 50 * delta),
				vaderCar.bound.getY());
	}

	public void moveCarRight(float delta) {
		vaderCar.bound.setPosition(
				Math.min(300 - vaderCar.bound.getWidth(), vaderCar.bound.getX()
						+ 50 * delta), vaderCar.bound.getY());
	}

	public void resetGame() {
		Missile mitem;
		int len = missiles.size;
		for (int i = len; --i >= 0;) {
			mitem = missiles.get(i);
			mitem.isHit = true;
			if (mitem.isHit == true) {
				missiles.removeIndex(i);
				missilePool.free(mitem);
			}
		}

		Box item;
		len = cannisters.size;
		for (int i = len; --i >= 0;) {
			item = cannisters.get(i);
			item.isHit = true;
			if (item.isHit == true) {
				cannisters.removeIndex(i);
				cannisterPool.free(item);
			}
		}

		score = 0;
		vaderCar.bound.setPosition(150, 10);
	}

}
