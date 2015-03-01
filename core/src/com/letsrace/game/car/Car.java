package com.letsrace.game.car;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.letsrace.game.car.Wheel.WheelType;

public class Car {
	public Body body;
	public float width, length, angle, maxSteerAngle, maxSpeed, maxRevSpeed,
			power;
	public float wheelAngle;
	public Steer steer;
	public Accel accelerate;
	public List<Wheel> wheels;
	Vector2 origin;
//	private FRInterpolationModel interpolator;

	public enum Steer {
		NONE, LEFT, RIGHT
	};

	public enum Accel {
		NONE, ACCELERATE, BRAKE
	};

	public Car(World world, float width, float length, Vector2 position,
			float angle, float power, float maxSteerAngle, float maxSpeed,
			String jsonFilePrefix) {
		super();
		this.steer = Steer.NONE;
		this.accelerate = Accel.ACCELERATE;
		this.width = width;
		this.length = length;
		this.angle = angle;
		this.maxSteerAngle = maxSteerAngle;
		this.maxSpeed = maxSpeed;
		this.maxRevSpeed = maxSpeed / 2;
		this.power = power;
		this.wheelAngle = 0;
//		this.interpolator = new FRInterpolationModel();
		BodyEditorLoader loader = new BodyEditorLoader(
				Gdx.files.internal(jsonFilePrefix + "-normal.json"));
		// init body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = angle;
		this.body = world.createBody(bodyDef);
		origin = loader.getOrigin("Name", width);
		// init shape
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.6f; // friction when rubbing against other
									// shapes
		fixtureDef.restitution = 0.4f; // amount of force feedback when hitting
										// something. >0 makes the car bounce
										// off, it's fun!
		loader.attachFixture(this.body, "Name", fixtureDef, this.width);
		// initialize wheels
		this.wheels = new ArrayList<Wheel>();
		this.wheels.add(new Wheel(world, this, this.width, false, false,
				jsonFilePrefix, WheelType.BOTTOM_LEFT)); // top left

		this.wheels.add(new Wheel(world, this, this.width, false, false,
				jsonFilePrefix, WheelType.BOTTOM_RIGHT)); // top right

		this.wheels.add(new Wheel(world, this, this.width, true, true,
				jsonFilePrefix, WheelType.TOP_LEFT)); // back left
		this.wheels.add(new Wheel(world, this, this.width, true, true,
				jsonFilePrefix, WheelType.TOP_RIGHT)); // back right
	}

	public List<Wheel> getPoweredWheels() {
		List<Wheel> poweredWheels = new ArrayList<Wheel>();
		for (Wheel wheel : this.wheels) {
			if (wheel.powered)
				poweredWheels.add(wheel);
		}
		return poweredWheels;
	}

	public Vector2 getLocalVelocity() {
		/*
		 * returns car's velocity vector relative to the car
		 */
		return this.body.getLocalVector(this.body
				.getLinearVelocityFromLocalPoint(new Vector2(0, 0)));
	}

	public Vector2 getWorldPosition() {
		return this.body.getPosition();
	}

	public float getRotation() {
		return this.body.getAngle();
	}

	public List<Wheel> getRevolvingWheels() {
		List<Wheel> revolvingWheels = new ArrayList<Wheel>();
		for (Wheel wheel : this.wheels) {
			if (wheel.revolving)
				revolvingWheels.add(wheel);
		}
		return revolvingWheels;
	}

	public float getSpeedKMH() {
		Vector2 velocity = this.body.getLinearVelocity();
		float len = velocity.len();
		return (len / 1000) * 3600;
	}

	public float getBodyAngle() {
		return this.body.getAngle();
	}

	public void setSpeed(float speed) {
		/*
		 * speed - speed in kilometers per hour
		 */
		Vector2 velocity = this.body.getLinearVelocity();
		velocity = velocity.nor();
		velocity = new Vector2(velocity.x * ((speed * 1000.0f) / 3600.0f),
				velocity.y * ((speed * 1000.0f) / 3600.0f));
		this.body.setLinearVelocity(velocity);
	}

	public void update(float deltaTime) {
		// 1. KILL SIDEWAYS VELOCITY

		for (Wheel wheel : wheels) {
			wheel.killSidewaysVelocity();
		}

		// 2. SET WHEEL ANGLE

		// calculate the change in wheel's angle for this update
		float incr = (this.maxSteerAngle) * deltaTime * 5;

		if (this.steer == Steer.LEFT) {
			this.wheelAngle = Math.min(Math.max(this.wheelAngle, 0) + incr,
					this.maxSteerAngle); // increment angle without going over
											// max steer
		} else if (this.steer == Steer.RIGHT) {
			this.wheelAngle = Math.max(Math.min(this.wheelAngle, 0) - incr,
					-this.maxSteerAngle); // decrement angle without going over
											// max steer
		} else {
			this.wheelAngle = 0;
		}

		// update revolving wheels
		for (Wheel wheel : this.getRevolvingWheels()) {
			wheel.setAngle(this.wheelAngle);
		}

		// 3. APPLY FORCE TO WHEELS
		Vector2 baseVector = new Vector2(0, 0); // vector pointing in the
												// direction force will be
		// applied to a wheel ; relative to the wheel.

		// if accelerator is pressed down and speed limit has not been reached,
		// go forwards
		if ((this.accelerate == Accel.ACCELERATE)
				&& (this.getSpeedKMH() < this.maxSpeed)) {
			baseVector = new Vector2(0, 1);
		} else if (this.accelerate == Accel.BRAKE) {
			// braking, but still moving forwards - increased force
			if (this.getLocalVelocity().y > 0) {
				baseVector = new Vector2(0f, -1.3f);
			}
			// going in reverse - less force
			else if ((this.getSpeedKMH() < this.maxRevSpeed))
				baseVector = new Vector2(0f, -0.7f);
		} else if (this.accelerate == Accel.NONE) {
			// slow down if not accelerating
			baseVector = new Vector2(0, 0);
			if (this.getSpeedKMH() < 7)
				this.setSpeed(0);
			else if (this.getLocalVelocity().y > 0)
				baseVector = new Vector2(0, -0.7f);
			else if (this.getLocalVelocity().y < 0)
				baseVector = new Vector2(0, +0.7f);
		} else
			baseVector = new Vector2(0, 0);
		// multiply by engine power, which gives us a force vector relative to
		// the wheel
		Vector2 forceVector = new Vector2(this.power * baseVector.x, this.power
				* baseVector.y);

		// apply force to each wheel
		for (Wheel wheel : this.getPoweredWheels()) {
			Vector2 position = wheel.body.getWorldCenter();
			wheel.body.applyForce(wheel.body.getWorldVector(new Vector2(
					forceVector.x, forceVector.y)), position, true);
		}

	}

	public void setTransform(float posX, float posY, float cBodyAngle) {
		this.body.setTransform(posX, posY, cBodyAngle);
	}

//	public void adjustTransformTo(float posX, float posY, float cBodyAngle) {
//		interpolator.setupModel(this.body.getPosition().x,
//				this.body.getPosition().y, posX, posY, this.body.getAngle(),
//				cBodyAngle);
//	}

}

//class FRInterpolationModel {
//
//	private float slope = 0f;
//	private float a, b;
//	private float m, n;
//	private boolean horizantalSteps;
//	private float scaledTimeFraction;
//	private float timeElapsed;
//	private float ith, fth;
//	private boolean initialized = false;
//
//	void setupModel(float initX, float initY, float finX, float finY,
//			float initAngle, float finAngle) {
//		m = initX;
//		n = initY;
//		a = finX;
//		b = finY;
//		ith = initAngle;
//		fth = finAngle;
//		timeElapsed = 0;
//		if (Math.abs(a - m) > Math.abs(b - n)) {
//			horizantalSteps = true;
//			if (a - m == 0)
//				return;
//			else
//				slope = ((float) b - n) / (a - m);
//			scaledTimeFraction = (a - m) * FRConstants.SYNC_PACKETS_PER_SEC;
//		} else {
//			horizantalSteps = false;
//			if (b - n == 0)
//				return;
//			else
//				slope = ((float) a - m) / (b - n);
//			scaledTimeFraction = (b - n) * FRConstants.SYNC_PACKETS_PER_SEC;
//		}
//		initialized = true;
//	}
//
//	public float getAngle() {
//		return (fth - ith) * timeElapsed * FRConstants.SYNC_PACKETS_PER_SEC
//				+ ith;
//	}
//
//	Vector2 getPosition(float delta) {
//		timeElapsed+= delta;
//		
//		if (!initialized ) {
//			return null;
//		}
//		if (timeElapsed > (1f / FRConstants.SYNC_PACKETS_PER_SEC)){
//			timeElapsed = 1f / FRConstants.SYNC_PACKETS_PER_SEC;
//			initialized = false;
//		}
//		float x, y;
//		if (horizantalSteps) {
//			x = scaledTimeFraction * timeElapsed + m;
//			y = (x - m) * slope + n;
//		} else {
//			y = scaledTimeFraction * timeElapsed + n;
//			x = (y - n) * slope + m;
//		}
//		return new Vector2(x, y);
//	}
//
//}