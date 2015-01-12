package com.letsrace.game.map;

import static com.letsrace.game.FRConstants.PIXELS_PER_UNIT;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class FRMapHandler{
	public MapBodyManager manager;
	public TiledMap tiledMap;
	public World physicalWorldRef;
	public Vector2[] initialPositionMarkers;
	public FRAngleMonitor angleMon=new FRAngleMonitor(0.5f);
	public Body mainCarBody;
	
	public FRMapHandler(World physicalWorld,String arenaName){
		this.physicalWorldRef = physicalWorld;
		tiledMap = new TmxMapLoader().load(arenaName);
		manager = new MapBodyManager(physicalWorld, PIXELS_PER_UNIT, null, 0);
		manager.createPhysics(tiledMap);
		initialPositionMarkers = manager.getInitialPosition(tiledMap);
	}
	
	public void setupContactListener(Body mainCarBody){
		this.mainCarBody = mainCarBody;
		physicalWorldRef.setContactListener(new ContactListener() {
			public Vector2 lastBodyVector= new Vector2(0,0);
			public void preSolve(Contact contact, Manifold oldManifold) {}
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			public void endContact(Contact contact) {}
			public void beginContact(Contact contact) {
				if(contact.getFixtureA().getBody() == FRMapHandler.this.mainCarBody||contact.getFixtureB().getBody() == FRMapHandler.this.mainCarBody){
					MarkerBody mb = manager.fetchMarkerBody(contact.getFixtureB().getBody());
					if(mb != null || (mb=manager.fetchMarkerBody(contact.getFixtureA().getBody()))!=null){
						angleMon.setTurnAngle(mb.direction.angle(lastBodyVector));
						lastBodyVector = mb.direction;
					}
				}
			
			}
			
		});
	}
	
	public Vector2 getInitialPositionForNumber(int index){
		return initialPositionMarkers[index];
	}
}
