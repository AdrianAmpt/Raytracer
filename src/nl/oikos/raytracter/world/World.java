package nl.oikos.raytracter.world;

import nl.oikos.raytracter.camera.Camera;
import nl.oikos.raytracter.geometricobject.GeometricObject;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.Light;
import nl.oikos.raytracter.tracer.Tracer;
import nl.oikos.raytracter.util.*;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Adrian on 31-7-2017.
 */
public abstract class World
{

	public ViewPlane viewPlane;
	public RGBColor backgroundColor;
	public Tracer tracer;
	public Light ambientLight;
	public Camera camera;

	public List<GeometricObject> objects;
	public List<Light> lights;

	public World()
	{
		this.backgroundColor = RGBColor.BLACK;
		this.tracer = null;
		this.ambientLight = new Ambient();
		this.camera = null;
		this.viewPlane = new ViewPlane();
		this.objects = new ArrayList<>();
		this.lights = new ArrayList<>();
	}

	public abstract void build();

	public final RenderedPixel displayPixel(Pixel pixel, RGBColor rawColor)
	{
		RGBColor mappedColor = rawColor;

		if (viewPlane.showOutOfGamut)
			mappedColor = clampToColor(mappedColor);
		else
			mappedColor = maxToOne(mappedColor);

		if (viewPlane.gamma != 1.0)
			mappedColor = mappedColor.pow(viewPlane.inverseGamma);

		return new RenderedPixel(pixel.x, viewPlane.height - pixel.y - 1, mappedColor);
	}

	private final RGBColor maxToOne(final RGBColor c)
	{
		double maxValue = Math.max(c.r, Math.max(c.g, c.b));

		if (maxValue > 1.0)
		{
			return c.divide(maxValue);
		}
		else
			return c;
	}

	/**
	 * Set color to red if any component is greater than one
	 * @param rawColor
	 * @return
	 */
	private final RGBColor clampToColor(final RGBColor rawColor)
	{
		if (rawColor.r > 1.0 || rawColor.g > 1.0 || rawColor.b > 1.0)
			return new RGBColor(1f, 0f, 0f);
		else
			return rawColor;
	}

	public final ShadeRec hitObjects(final Ray ray)
	{
		ShadeRec sr = new ShadeRec(this);
		Reference<Double> t = new Reference<>(0d);
		Normal3D normal = null;
		Point3D localHitPoint = null;
		double tmin = Double.MAX_VALUE;

		for (GeometricObject object : this.objects)
		{
			if (object.hit(ray, t, sr) && (t.get() < tmin))
			{
				sr.hitAnObject = true;
				tmin = t.get();
				if (object.getMaterial() != null)
					sr.material = object.getMaterial();

				sr.hitPoint = ray.o.add(ray.d.multiply(t.get()));
				normal = sr.normal;
				localHitPoint = sr.localHitPoint;
			}
		}

		if(sr.hitAnObject)
		{
			sr.t = tmin;
			sr.normal = normal;
			sr.localHitPoint = localHitPoint;
		}

		return sr;
	}

	public void setViewPlane(ViewPlane viewPlane)
	{
		this.viewPlane = viewPlane;
	}

	public void setBackgroundColor(RGBColor backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public void setTracer(Tracer tracer)
	{
		this.tracer = tracer;
	}

	public void setAmbientLight(Light ambientLight)
	{
		this.ambientLight = ambientLight;
	}

	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	public void addGeometricObject(GeometricObject geometricObject)
	{
		if (this.objects == null)
			this.objects = new ArrayList<>();

		this.objects.add(geometricObject);
	}

	public void addLight(Light light)
	{
		if (this.lights == null)
			this.lights = new ArrayList<>();

		this.lights.add(light);
	}

	private static Field[] getallFields(Class<?> clazz)
	{
		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = clazz.getDeclaredFields();

		Class<?> parentClass = clazz.getSuperclass();

		Field[] parentFields;

		if (parentClass != null)
			parentFields = World.getallFields(parentClass);
		else
			parentFields = new Field[0];

		Field[] result = new Field[fields.length + parentFields.length];

		for (int i = 0; i < parentFields.length; i++)
		{
			result[i] = parentFields[i];
		}

		for (int i = 0; i < fields.length; i++)
		{
			result[parentFields.length + i] = fields[i];
		}

		return result;
	}

	public static void getBuilder(Object object, StringBuilder result, int level)
	{
		String newLine = System.getProperty("line.separator");

		if (object == null)
		{
			result.append("null");
			result.append(newLine);
			return;
		}

		result.append( object.getClass().getSimpleName() );
		result.append( " Object {" );
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = World.getallFields(object.getClass());

		//print field names paired with their values
		for ( Field field : fields ) {

			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			for (int i = 0; i <= level; i++)
				result.append("  ");
			try {
				result.append( field.getName() );
				result.append(": ");
				//requires access to private field:
				field.setAccessible(true);

				Object subObject = field.get(object);
				if (subObject instanceof Number
						|| subObject instanceof String
						|| subObject instanceof Boolean
						|| subObject instanceof Object[]
						|| subObject instanceof Collection
						|| subObject instanceof Random)
				{
					result.append( field.get(object) );
				}
				else if (subObject instanceof World && level != 0)
				{
					result.append( "WORLD" );
				}
				else
				{
					getBuilder(subObject, result, ++level);
				}
			} catch ( IllegalAccessException ex ) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");
		result.append(newLine);
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		/*String newLine = System.getProperty("line.separator");

		result.append( this.getClass().getSimpleName() );
		result.append( " Object {" );
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = World.getallFields(this.getClass());

		//print field names paired with their values
		for ( Field field : fields ) {
			result.append("  ");
			try {
				result.append( field.getName() );
				result.append(": ");
				//requires access to private field:
				result.append( field.get(this) );
			} catch ( IllegalAccessException ex ) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();*/

		getBuilder(this, result, 0);

		return result.toString();
	}
}
