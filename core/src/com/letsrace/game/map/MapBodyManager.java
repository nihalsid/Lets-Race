package com.letsrace.game.map;

import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author David Saltares Márquez david.saltares at gmail.com
 * @brief Populates box2D world with static bodies using data from a map object
 * 
 *        It uses a JSON formatted materials file to assign properties to the
 *        static bodies it creates. To assign a material to a shape add a
 *        "material" custom property to the shape in question using your editor
 *        of choice (Tiled, Gleed, Tide...). Such file uses the following
 *        structure:
 * @code [ { "name" : "ice", "density" : 1.0, "restitution" : 0.0, "friction" :
 *       0.1 }, { "name" : "elastic", "density" : 1.0, "restitution" : 0.8,
 *       "friction" : 0.8 } ]
 * @endcode
 * 
 *          In case no material property is found, it'll get a default one.
 * 
 */
public class MapBodyManager {
	private World world;
	private float units;
	private Array<Body> bodies = new Array<Body>();
	private ObjectMap<String, FixtureDef> materials = new ObjectMap<String, FixtureDef>();
	public Array<MarkerBody> markBodies = new Array<MarkerBody>();

	/**
	 * @param world
	 *            box2D world to work with.
	 * @param unitsPerPixel
	 *            conversion ratio from pixel units to box2D metres.
	 * @param materialsFile
	 *            json file with specific physics properties to be assigned to
	 *            newly created bodies.
	 * @param loggingLevel
	 *            verbosity of the embedded logger.
	 */
	public MapBodyManager(World world, float unitsPerPixel,
			FileHandle materialsFile, int loggingLevel) {

		this.world = world;
		this.units = unitsPerPixel;

		FixtureDef defaultFixture = new FixtureDef();
		defaultFixture.density = 1.0f;
		defaultFixture.friction = 0.8f;
		defaultFixture.restitution = 0.0f;

		materials.put("default", defaultFixture);

		if (materialsFile != null) {
			loadMaterialsFile(materialsFile);
		}
	}

	/**
	 * @param map
	 *            will use the "physics" layer of this map to look for shapes in
	 *            order to create the static bodies.
	 */
	public void createPhysics(Map map) {
		createPhysics(map, "Collision", false);
		createPhysics(map, "MapMarks", true);
	}

	/**
	 * @param map
	 *            map to be used to create the static bodies.
	 * @param layerName
	 *            name of the layer that contains the shapes.
	 */
	public void createPhysics(Map map, String layerName, boolean markerLayer) {
		MapLayer layer = map.getLayers().get(layerName);

		if (layer == null) {
			return;
		}

		MapObjects objects = layer.getObjects();
		Iterator<MapObject> objectIt = objects.iterator();

		while (objectIt.hasNext()) {
			MapObject object = objectIt.next();

			if (object instanceof TextureMapObject) {
				continue;
			}

			Shape shape;
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;

			if (object instanceof RectangleMapObject) {
				RectangleMapObject rectangle = (RectangleMapObject) object;
				shape = getRectangle(rectangle, bodyDef);
			} else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject) object);
			} else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject) object);
			} else if (object instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject) object);
			} else {
				continue;
			}

			MapProperties properties = object.getProperties();
			String material = properties.get("material", "default",
					String.class);
			FixtureDef fixtureDef = materials.get(material);
			if (fixtureDef == null) {
				fixtureDef = materials.get("default");
			}
			if(markerLayer)
				fixtureDef.isSensor = true;
			fixtureDef.shape = shape;
			// fixtureDef.filter.categoryBits =
			// game.getCategoryBitsManager().getCategoryBits("level");

			Body body = world.createBody(bodyDef);
			body.createFixture(fixtureDef);

			body.setUserData(object.getName());
			if (markerLayer) {
				Polyline pline = ((PolylineMapObject) object).getPolyline();
				float[] transformedV = pline.getTransformedVertices();
				Vector2 v = new Vector2(transformedV[2] - transformedV[0],
						transformedV[3] - transformedV[1]).nor();
				markBodies.add(new MarkerBody(body, v));
			} else {
				bodies.add(body);
			}
			fixtureDef.shape = null;
			shape.dispose();
		}
	}

	/**
	 * Destroys every static body that has been created using the manager.
	 */
	public void destroyPhysics() {
		for (Body body : bodies) {
			world.destroyBody(body);
		}

		bodies.clear();
	}

	private void loadMaterialsFile(FileHandle materialsFile) {

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.0f;
		materials.put("default", fixtureDef);

		try {
			JsonReader reader = new JsonReader();
			JsonValue root = reader.parse(materialsFile);
			JsonIterator materialIt = root.iterator();

			while (materialIt.hasNext()) {
				JsonValue materialValue = materialIt.next();

				if (!materialValue.has("name")) {
					continue;
				}

				String name = materialValue.getString("name");

				fixtureDef = new FixtureDef();
				fixtureDef.density = materialValue.getFloat("density", 1.0f);
				fixtureDef.friction = materialValue.getFloat("friction", 1.0f);
				fixtureDef.restitution = materialValue.getFloat("restitution",
						0.0f);
				materials.put(name, fixtureDef);
			}

		} catch (Exception e) {

		}
	}

	private Shape getRectangle(RectangleMapObject rectangleObject, BodyDef def) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f)
				/ units, (rectangle.y + rectangle.height * 0.5f) / units);
		polygon.setAsBox(rectangle.width * 0.5f / units, rectangle.height
				* 0.5f / units, size, 0.0f);

		def.position.set(size.x, size.y);
		return polygon;
	}

	private Shape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / units);
		circleShape
				.setPosition(new Vector2(circle.x / units, circle.y / units));
		return circleShape;
	}

	private Shape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			worldVertices[i] = vertices[i] / units;
		}

		polygon.set(worldVertices);
		return polygon;
	}

	private Shape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline()
				.getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] / units;
			worldVertices[i].y = vertices[i * 2 + 1] / units;
		}

		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}

	public Vector2 getInitialPosition(TiledMap map){
		MapLayer layer = map.getLayers().get("Positions");

		if (layer == null) {
			return null;
		}

		MapObjects objects = layer.getObjects();
		Rectangle r = ((RectangleMapObject)objects.get("CarSpawn")).getRectangle();
		return new Vector2(r.x/units,r.y/units);
	}
	
	public MarkerBody fetchMarkerBody(Body b){
		for (MarkerBody m : markBodies ){
			if(m.body == b){
				return m;
			}
		}
		return null;
	}
	
}
class MarkerBody {
	public Body body;
	public Vector2 direction;

	public MarkerBody(Body b, Vector2 v) {
		body = b;
		direction = v;
	}
}