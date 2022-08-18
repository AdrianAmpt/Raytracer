package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.geometricobject.Torus;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.material.Phong;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 10-3-2018.
 */
public class Image19_16 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 16;

		// view plane

		Sampler sampler = new MultiJittered(numberOfSamples);

		viewPlane.setWidth(400);
		viewPlane.setHeight(400);
		viewPlane.setSampler(sampler);

		backgroundColor = RGBColor.BLACK;

		tracer = new RayCast(this);

		// camera
		Pinhole pinHole = new Pinhole();
		pinHole.setEye(new Point3D(5, 25, 20));
		pinHole.setLookat(new Point3D(0, 0, 0));
		pinHole.setViewDistance(1500);
		pinHole.computeUVW();
		setCamera(pinHole);

		Directional light = new Directional();
		light.setDirection(new Vector3D(200, 75, 100));
		light.scaleRadiance(4);
		light.setShadows(true);
		addLight(light);

		Phong phong = new Phong();
		phong.setKa(0.25);
		phong.setKd(0.5);
		phong.setCd(new RGBColor(1, 1, 0.45));
		phong.setKs(0.05);
		phong.setExp(5);

		double a = 2.0;	 	// for all parts
//		double b = 0.15;	// for Figure 19.16(a)
		double b = 0.5;	   	// for Figure 19.16(b)  default torus
//		double b = 2;      	// for Figure 19.16(c)

		Torus torus = new Torus(a, b);
		torus.setMaterial(phong);
		addGeometricObject(torus);

		// ground plane with checker:

		Checker3D checker = new Checker3D();
		checker.setSquareSize(1.0);
		checker.setColor1(0.8);
		checker.setColor2(1);

		Matte matte = new Matte();
		matte.setKa(0.30);
		matte.setKd(0.6);
		matte.setCd(checker);

		Plane plane = new Plane(new Point3D(0, -2, 0), new Normal3D(0, 1, 0));
		plane.setMaterial(matte);
		addGeometricObject(plane);
	}
}
