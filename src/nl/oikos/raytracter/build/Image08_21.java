package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 1-9-2017.
 */
public class Image08_21 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 1;

		// view plane
		viewPlane.setWidth(500);
		viewPlane.setHeight(500);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		viewPlane.setPixelSize(1);

		backgroundColor = RGBColor.BLACK;

		tracer = new RayCast(this);

		// camera
		Pinhole pinhole = new Pinhole();
//		pinhole.setEye(0, 15, 0);
//		pinhole.setViewDistance(755);
		pinhole.setEye(0, 5, 0);
		pinhole.setViewDistance(235);
//		pinhole.setEye(0, 2, 0);
//		pinhole.setViewDistance(73);
		pinhole.setLookat(Point3D.O);
		pinhole.computeUVW();
		setCamera(pinhole);


		// lights
		Directional directional = new Directional();
		directional.setDirection(new Vector3D(0, 1, 1));
		directional.scaleRadiance(3.0);
		addLight(directional);

		double d = 2.0; 		// sphere center spacing
		double r = 0.75; 	// sphere radius
		double xc, yc; 		// sphere center coordinates
		int num_rows = 5;
		int num_columns = 5;

		Checker3D checker = new Checker3D();
		checker.setSquareSize(0.5);
		checker.setColor1(0, 0.4, 0.8);
		checker.setColor2(RGBColor.WHITE);

		Matte matte = new Matte();
		matte.setKa(0.2);
		matte.setKd(0.8);
		matte.setCd(checker);

		for (int k = 0; k < num_columns; k++)
		{ // up
			for (int j = 0; j < num_rows; j++)
			{ // across
				Sphere sphere = new Sphere();
				xc = d * (j - (num_columns - 1) / 2.0);
				yc = d * (k - (num_rows - 1) / 2.0);
				sphere.setCenter(xc, 0, yc);
				sphere.setRadius(r);
				sphere.setMaterial(matte);
				addGeometricObject(sphere);
			}
		}
}
}
