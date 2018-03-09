package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created by Adrian on 7-3-2018.
 */
public class Image14_23 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 16; // use 9 or 16 samples for production image

		// view plane

		viewPlane.setWidth(600);
		viewPlane.setHeight(600);
		viewPlane.setPixelSize(0.5);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		viewPlane.setShowOutOfGamut(false);

		backgroundColor = RGBColor.BLACK;
		tracer = new RayCast(this);

		// camera
		Pinhole pinhole = new Pinhole();
		pinhole.setEye(0.0, 0.0, 10000);
		pinhole.setLookat(0.0, 0.0, 0.0);
		pinhole.setViewDistance(15000);
		pinhole.computeUVW();
		setCamera(pinhole);


		// lights
		/*Ambient ambient = new Ambient();
		ambient.scaleRadiance(1.0);
		setAmbientLight(ambient);*/

		Directional directional = new Directional();
		directional.setDirection(new Vector3D(100.0, 100.0, 200.0));
		directional.setColor(new RGBColor(1.0, 1.0, 1.0));
		directional.scaleRadiance(4.5);
		addLight(directional);

		// colors
		RGBColor yellow = RGBColor.YELLOW;
		RGBColor brown = new RGBColor(0.71f, 0.40f, 0.16f);
		RGBColor dark_green = new RGBColor(0.0f, 0.41f, 0.41f);
		RGBColor orange = new RGBColor(1.0f, 0.75f, 0.0f);
		RGBColor green = new RGBColor(0.0f, 0.6f, 0.3f);
		RGBColor light_green = new RGBColor(0.65f, 1.0f, 0.30f);
		RGBColor dark_yellow = new RGBColor(0.61f, 0.61f, 0.0f);
		RGBColor light_purple = new RGBColor(0.65f, 0.3f, 1.0f);
		RGBColor dark_purple = new RGBColor(0.5f, 0.0f, 1.0f);
		

		// Matte material reflection coefficients - common to all sphere materials

		float ka = 0.25f;
		float kd = 0.75f;


		// spheres

		Matte matte1 = new Matte();
		matte1.setKa(ka);
		matte1.setKd(kd);
		matte1.setCd(yellow);
		Sphere	sphere1 = new Sphere(new Point3D(5, 3, 0), 30);
		sphere1.setMaterial(matte1);	   							// yellow
		addGeometricObject(sphere1);

		Matte matte2 = new Matte();
		matte2.setKa(ka);
		matte2.setKd(kd);
		matte2.setCd(brown);
		Sphere	sphere2 = new Sphere(new Point3D(45, -7, -60), 20);
		sphere2.setMaterial(matte2);								// brown
		addGeometricObject(sphere2);


		Matte matte3 = new Matte();
		matte3.setKa(ka);
		matte3.setKd(kd);
		matte3.setCd(dark_green);
		Sphere	sphere3 = new Sphere(new Point3D(40, 43, -100), 17);
		sphere3.setMaterial(matte3);								// dark green
		addGeometricObject(sphere3);

		Matte matte4 = new Matte();
		matte4.setKa(ka);
		matte4.setKd(kd);
		matte4.setCd(orange);
		Sphere	sphere4 = new Sphere(new Point3D(-20, 28, -15), 20);
		sphere4.setMaterial(matte4);								// orange
		addGeometricObject(sphere4);

		Matte matte5 = new Matte();
		matte5.setKa(ka);
		matte5.setKd(kd);
		matte5.setCd(green);
		Sphere	sphere5 = new Sphere(new Point3D(-25, -7, -35), 27);
		sphere5.setMaterial(matte5);								// green
		addGeometricObject(sphere5);

		Matte matte6 = new Matte();
		matte6.setKa(ka);
		matte6.setKd(kd);
		matte6.setCd(light_green);
		Sphere	sphere6 = new Sphere(new Point3D(20, -27, -35), 25);
		sphere6.setMaterial(matte6);								// light green
		addGeometricObject(sphere6);

		Matte matte7 = new Matte();
		matte7.setKa(ka);
		matte7.setKd(kd);
		matte7.setCd(green);
		Sphere	sphere7 = new Sphere(new Point3D(35, 18, -35), 22);
		sphere7.setMaterial(matte7);   							// green
		addGeometricObject(sphere7);

		Matte matte8 = new Matte();
		matte8.setKa(ka);
		matte8.setKd(kd);
		matte8.setCd(brown);
		Sphere	sphere8 = new Sphere(new Point3D(-57, -17, -50), 15);
		sphere8.setMaterial(matte8);								// brown
		addGeometricObject(sphere8);

		Matte matte9 = new Matte();
		matte9.setKa(ka);
		matte9.setKd(kd);
		matte9.setCd(light_green);
		Sphere	sphere9 = new Sphere(new Point3D(-47, 16, -80), 23);
		sphere9.setMaterial(matte9);								// light green
		addGeometricObject(sphere9);

		Matte matte10 = new Matte();
		matte10.setKa(ka);
		matte10.setKd(kd);
		matte10.setCd(dark_green);
		Sphere	sphere10 = new Sphere(new Point3D(-15, -32, -60), 22);
		sphere10.setMaterial(matte10);     						// dark green
		addGeometricObject(sphere10);

		Matte matte11 = new Matte();
		matte11.setKa(ka);
		matte11.setKd(kd);
		matte11.setCd(dark_yellow);
		Sphere	sphere11 = new Sphere(new Point3D(-35, -37, -80), 22);
		sphere11.setMaterial(matte11);							// dark yellow
		addGeometricObject(sphere11);

		Matte matte12 = new Matte();
		matte12.setKa(ka);
		matte12.setKd(kd);
		matte12.setCd(dark_yellow);
		Sphere	sphere12 = new Sphere(new Point3D(10, 43, -80), 22);
		sphere12.setMaterial(matte12);							// dark yellow
		addGeometricObject(sphere12);

		Matte matte13 = new Matte();
		matte13.setKa(ka);
		matte13.setKd(kd);
		matte13.setCd(dark_yellow);
		Sphere	sphere13 = new Sphere(new Point3D(30, -7, -80), 10);
		sphere13.setMaterial(matte13);
		addGeometricObject(sphere13);											// dark yellow (hidden)

		Matte matte14 = new Matte();
		matte14.setKa(ka);
		matte14.setKd(kd);
		matte14.setCd(dark_green);
		Sphere	sphere14 = new Sphere(new Point3D(-40, 48, -110), 18);
		sphere14.setMaterial(matte14); 							// dark green
		addGeometricObject(sphere14);

		Matte matte15 = new Matte();
		matte15.setKa(ka);
		matte15.setKd(kd);
		matte15.setCd(brown);
		Sphere	sphere15 = new Sphere(new Point3D(-10, 53, -120), 18);
		sphere15.setMaterial(matte15); 							// brown
		addGeometricObject(sphere15);

		Matte matte16 = new Matte();
		matte16.setKa(ka);
		matte16.setKd(kd);
		matte16.setCd(light_purple);
		Sphere	sphere16 = new Sphere(new Point3D(-55, -52, -100), 10);
		sphere16.setMaterial(matte16);							// light purple
		addGeometricObject(sphere16);

		Matte matte17 = new Matte();
		matte17.setKa(ka);
		matte17.setKd(kd);
		matte17.setCd(brown);
		Sphere	sphere17 = new Sphere(new Point3D(5, -52, -100), 15);
		sphere17.setMaterial(matte17);							// browm
		addGeometricObject(sphere17);

		Matte matte18 = new Matte();
		matte18.setKa(ka);
		matte18.setKd(kd);
		matte18.setCd(dark_purple);
		Sphere	sphere18 = new Sphere(new Point3D(-20, -57, -120), 15);
		sphere18.setMaterial(matte18);							// dark purple
		addGeometricObject(sphere18);

		Matte matte19 = new Matte();
		matte19.setKa(ka);
		matte19.setKd(kd);
		matte19.setCd(dark_green);
		Sphere	sphere19 = new Sphere(new Point3D(55, -27, -100), 17);
		sphere19.setMaterial(matte19);							// dark green
		addGeometricObject(sphere19);

		Matte matte20 = new Matte();
		matte20.setKa(ka);
		matte20.setKd(kd);
		matte20.setCd(brown);
		Sphere	sphere20 = new Sphere(new Point3D(50, -47, -120), 15);
		sphere20.setMaterial(matte20);							// browm
		addGeometricObject(sphere20);

		Matte matte21 = new Matte();
		matte21.setKa(ka);
		matte21.setKd(kd);
		matte21.setCd(light_purple);
		Sphere	sphere21 = new Sphere(new Point3D(70, -42, -150), 10);
		sphere21.setMaterial(matte21);							// light purple
		addGeometricObject(sphere21);

		Matte matte22 = new Matte();
		matte22.setKa(ka);
		matte22.setKd(kd);
		matte22.setCd(light_purple);
		Sphere	sphere22 = new Sphere(new Point3D(5, 73, -130), 12);
		sphere22.setMaterial(matte22);							// light purple
		addGeometricObject(sphere22);

		Matte matte23 = new Matte();
		matte23.setKa(ka);
		matte23.setKd(kd);
		matte23.setCd(dark_purple);
		Sphere	sphere23 = new Sphere(new Point3D(66, 21, -130), 13);
		sphere23.setMaterial(matte23);							// dark purple
		addGeometricObject(sphere23);

		Matte matte24 = new Matte();
		matte24.setKa(ka);
		matte24.setKd(kd);
		matte24.setCd(light_purple);
		Sphere	sphere24 = new Sphere(new Point3D(72, -12, -140), 12);
		sphere24.setMaterial(matte24);							// light purple
		addGeometricObject(sphere24);

		Matte matte25 = new Matte();
		matte25.setKa(ka);
		matte25.setKd(kd);
		matte25.setCd(green);
		Sphere	sphere25 = new Sphere(new Point3D(64, 5, -160), 11);
		sphere25.setMaterial(matte25);					 		// green
		addGeometricObject(sphere25);

		Matte matte26 = new Matte();
		matte26.setKa(ka);
		matte26.setKd(kd);
		matte26.setCd(light_purple);
		Sphere	sphere26 = new Sphere(new Point3D(55, 38, -160), 12);
		sphere26.setMaterial(matte26);							// light purple
		addGeometricObject(sphere26);

		Matte matte27 = new Matte();
		matte27.setKa(ka);
		matte27.setKd(kd);
		matte27.setCd(light_purple);
		Sphere	sphere27 = new Sphere(new Point3D(-73, -2, -160), 12);
		sphere27.setMaterial(matte27);							// light purple
		addGeometricObject(sphere27);

		Matte matte28 = new Matte();
		matte28.setKa(ka);
		matte28.setKd(kd);
		matte28.setCd(dark_purple);
		Sphere	sphere28 = new Sphere(new Point3D(30, -62, -140), 15);
		sphere28.setMaterial(matte28); 							// dark purple
		addGeometricObject(sphere28);

		Matte matte29 = new Matte();
		matte29.setKa(ka);
		matte29.setKd(kd);
		matte29.setCd(dark_purple);
		Sphere	sphere29 = new Sphere(new Point3D(25, 63, -140), 15);
		sphere29.setMaterial(matte29);							// dark purple
		addGeometricObject(sphere29);

		Matte matte30 = new Matte();
		matte30.setKa(ka);
		matte30.setKd(kd);
		matte30.setCd(dark_purple);
		Sphere	sphere30 = new Sphere(new Point3D(-60, 46, -140), 15);
		sphere30.setMaterial(matte30); 							// dark purple
		addGeometricObject(sphere30);

		Matte matte31 = new Matte();
		matte31.setKa(ka);
		matte31.setKd(kd);
		matte31.setCd(light_purple);
		Sphere	sphere31 = new Sphere(new Point3D(-30, 68, -130), 12);
		sphere31.setMaterial(matte31); 							// light purple
		addGeometricObject(sphere31);

		Matte matte32 = new Matte();
		matte32.setKa(ka);
		matte32.setKd(kd);
		matte32.setCd(green);
		Sphere	sphere32 = new Sphere(new Point3D(58, 56, -180), 11);
		sphere32.setMaterial(matte32);							//  green
		addGeometricObject(sphere32);

		Matte matte33 = new Matte();
		matte33.setKa(ka);
		matte33.setKd(kd);
		matte33.setCd(green);
		Sphere	sphere33 = new Sphere(new Point3D(-63, -39, -180), 11);
		sphere33.setMaterial(matte33);							// green 
		addGeometricObject(sphere33);

		Matte matte34 = new Matte();
		matte34.setKa(ka);
		matte34.setKd(kd);
		matte34.setCd(light_purple);
		Sphere	sphere34 = new Sphere(new Point3D(46, 68, -200), 10);
		sphere34.setMaterial(matte34);							// light purple
		addGeometricObject(sphere34);

		Matte matte35 = new Matte();
		matte35.setKa(ka);
		matte35.setKd(kd);
		matte35.setCd(light_purple);
		Sphere	sphere35 = new Sphere(new Point3D(-3, -72, -130), 12);
		sphere35.setMaterial(matte35);							// light purple
		addGeometricObject(sphere35);
	}
}
