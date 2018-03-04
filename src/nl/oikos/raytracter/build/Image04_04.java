package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Orthographic;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.PointLight;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.Jittered;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 3-9-2017.
 */
public class Image04_04 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 1;

		// view plane
		viewPlane.setWidth(320);
		viewPlane.setHeight(320);
		viewPlane.setPixelSize(0.1);
		viewPlane.setSampler(new Jittered(numberOfSamples));

		backgroundColor = RGBColor.BLACK;
		tracer = new RayCast(this);

		// camera
		Orthographic orthographic = new Orthographic();
		orthographic.setEye(0, 0, 100);
		orthographic.setLookat(0, 0, 0);
		setCamera(orthographic);

		// lights
		Ambient ambient = new Ambient();
		//ambient.scaleRadiance(1);
		setAmbientLight(ambient);

		PointLight pointLight = new PointLight();
		pointLight.setLocation(new Point3D(100, 100, 200));
		pointLight.scaleRadiance(2);
		addLight(pointLight);

		Matte matte = new Matte();
		matte.setKa(0.2);
		matte.setKd(0.8);
		matte.setCd(new RGBColor(1, 1, 0));

		Sphere sphere = new Sphere(Point3D.O, 13);
		sphere.setMaterial(matte);
		addGeometricObject(sphere);
	}
}