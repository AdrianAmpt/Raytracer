package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.geometricobject.Sphere;
import nl.oikos.raytracter.light.Ambient;
import nl.oikos.raytracter.light.Directional;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.Jittered;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.sampler.NRooks;
import nl.oikos.raytracter.sampler.Regular;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.Vector3D;
import nl.oikos.raytracter.world.World;

/**
 * Created b y Adrian on 20-8-2017.
 */
public class ExampleImage1 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 16; // use 9 or 16 samples for production image

		// view plane  

		viewPlane.setWidth(600);
		viewPlane.setHeight(600);
		viewPlane.setPixelSize(0.5f);
		//viewPlane.set_samples(numberOfSamples);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		//viewPlane.setLensSampler(new Jittered(numberOfSamples));
		//viewPlane.setLensSampler(new NRooks(numberOfSamples));
		//viewPlane.setLensSampler(new Regular(numberOfSamples));
		viewPlane.setShowOutOfGamut(true);

		backgroundColor = RGBColor.BLACK;

		tracer = new RayCast(this);


		// camera
		Pinhole pinhole = new Pinhole();
		pinhole.setEye(0.0, 0.0, 1500.0);
		pinhole.setLookat(0.0, -5.0, 0.0);
		pinhole.setViewDistance(2200.0);
		pinhole.computeUVW();
		setCamera(pinhole);


		// lights

		Ambient ambient = new Ambient();
		ambient.scaleRadiance(1.0);
		setAmbientLight(ambient);

		Directional directional = new Directional();
		directional.setDirection(new Vector3D(100.0, 100.0, 200.0));
		directional.scaleRadiance(3.0);
		addLight(directional);
		
		// colors

		float a = 0.75f;  // scaling factor for orange, light green, and light purple

		RGBColor cyan = new RGBColor(0.2f, 0.8f, 0.8f);									// cyan  
		RGBColor brown = new RGBColor(0.71f, 0.40f, 0.16f);								// brown
		RGBColor dark_green = new RGBColor(0.0f, 0.41f, 0.41f);							// dark green
		RGBColor orange = new RGBColor(a * 1.0f, a * 0.75f, 0.0f);						// orange
		RGBColor green = new RGBColor(0.0f, 0.6f, 0.3f);									// green
		RGBColor light_green = new RGBColor(a * 0.65f, a * 1.0f, a * 0.30f);				// light green
		RGBColor dark_yellow = new RGBColor(0.61f, 0.61f, 0.0f);							// dark yellow
		RGBColor light_purple = new RGBColor(a * 0.65f, a * 0.3f, a * 1.0f);				// light purple
		RGBColor dark_purple = new RGBColor(0.45f, 0.0f, 0.9f);							// dark purple
		RGBColor gray = new RGBColor(0.6f);												// gray




		// Matte material reflection coefficients - common to all sphere materials

		float ka = 0.25f;
		float kd = 0.75f;


		// spheres

		Matte matte1 = new Matte();
		matte1.setKa(ka);
		matte1.setKd(kd);
		matte1.setCd(cyan);

		Sphere sphere1 = new Sphere(new Point3D(5.0, 3.0, 20.0), 25.0);
		sphere1.setMaterial(matte1);	   							// cyan
		addGeometricObject(sphere1);


		Matte matte2 = new Matte();
		matte2.setKa(ka);
		matte2.setKd(kd);
		matte2.setCd(brown);

		Sphere sphere2 = new Sphere(new Point3D(45.0, -7.0, -60.0), 20.0);
		sphere2.setMaterial(matte2);								// brown
		addGeometricObject(sphere2);


		Matte matte3 = new Matte();
		matte3.setKa(ka);
		matte3.setKd(kd);
		matte3.setCd(dark_green);

		Sphere sphere3 = new Sphere(new Point3D(40.0, 43.0, -100.0), 17.0);
		sphere3.setMaterial(matte3);								// dark green
		addGeometricObject(sphere3);


		Matte matte4 = new Matte();
		matte4.setKa(ka);
		matte4.setKd(kd);
		matte4.setCd(orange);

		Sphere sphere4 = new Sphere(new Point3D(-20.0, 28.0, -15.0), 20.0);
		sphere4.setMaterial(matte4);								// orange
		addGeometricObject(sphere4);


		Matte matte5 = new Matte();
		matte5.setKa(ka);
		matte5.setKd(kd);
		matte5.setCd(green);

		Sphere sphere5 = new Sphere(new Point3D(-25.0, -7.0, -35.0), 27.0);
		sphere5.setMaterial(matte5);								// green
		addGeometricObject(sphere5);


		Matte matte6 = new Matte();
		matte6.setKa(ka);
		matte6.setKd(kd);
		matte6.setCd(light_green);

		Sphere sphere6 = new Sphere(new Point3D(20.0, -27.0, -25.0), 25.0);
		sphere6.setMaterial(matte6);								// light green
		addGeometricObject(sphere6);


		Matte matte7 = new Matte();
		matte7.setKa(ka);
		matte7.setKd(kd);
		matte7.setCd(green);

		Sphere sphere7 = new Sphere(new Point3D(35.0, 18.0, -35.0), 22.0);
		sphere7.setMaterial(matte7);   								// green
		addGeometricObject(sphere7);


		Matte matte8 = new Matte();
		matte8.setKa(ka);
		matte8.setKd(kd);
		matte8.setCd(brown);

		Sphere sphere8 = new Sphere(new Point3D(-57.0, -17.0, -50.0), 15.0);
		sphere8.setMaterial(matte8);								// brown
		addGeometricObject(sphere8);

		Matte matte9 = new Matte();
		matte9.setKa(ka);
		matte9.setKd(kd);
		matte9.setCd(light_green);

		Sphere sphere9 = new Sphere(new Point3D(-47.0, 16.0, -80.0), 23.0);
		sphere9.setMaterial(matte9);								// light green
		addGeometricObject(sphere9);


		Matte matte10 = new Matte();
		matte10.setKa(ka);
		matte10.setKd(kd);
		matte10.setCd(dark_green);

		Sphere sphere10 = new Sphere(new Point3D(-15.0, -32.0, -60.0), 22.0);
		sphere10.setMaterial(matte10);     							// dark green
		addGeometricObject(sphere10);


		Matte matte11 = new Matte();
		matte11.setKa(ka);
		matte11.setKd(kd);
		matte11.setCd(dark_yellow);

		Sphere sphere11 = new Sphere(new Point3D(-35.0, -37.0, -80.0), 22.0);
		sphere11.setMaterial(matte11);								// dark yellow
		addGeometricObject(sphere11);


		Matte matte12 = new Matte();
		matte12.setKa(ka);
		matte12.setKd(kd);
		matte12.setCd(dark_yellow);

		Sphere sphere12 = new Sphere(new Point3D(10.0, 43.0, -80.0), 22.0);
		sphere12.setMaterial(matte12);								// dark yellow
		addGeometricObject(sphere12);


		Matte matte13 = new Matte();
		matte13.setKa(ka);
		matte13.setKd(kd);
		matte13.setCd(dark_yellow);

		Sphere sphere13 = new Sphere(new Point3D(30.0, -7.0, -80.0), 10.0); 	// hidden
		sphere13.setMaterial(matte13);								// dark yellow
		addGeometricObject(sphere13);


		Matte matte14 = new Matte();
		matte14.setKa(ka);
		matte14.setKd(kd);
		matte14.setCd(dark_green);

		Sphere sphere14 = new Sphere(new Point3D(-40.0, 48.0, -110.0), 18.0);
		sphere14.setMaterial(matte14); 								// dark green
		addGeometricObject(sphere14);


		Matte matte15 = new Matte();
		matte15.setKa(ka);
		matte15.setKd(kd);
		matte15.setCd(brown);

		Sphere sphere15 = new Sphere(new Point3D(-10.0, 53.0, -120.0), 18.0);
		sphere15.setMaterial(matte15); 								// brown
		addGeometricObject(sphere15);


		Matte matte16 = new Matte();
		matte16.setKa(ka);
		matte16.setKd(kd);
		matte16.setCd(light_purple);

		Sphere sphere16 = new Sphere(new Point3D(-55.0, -52.0, -100.0), 10.0);
		sphere16.setMaterial(matte16);								// light purple
		addGeometricObject(sphere16);


		Matte matte17 = new Matte();
		matte17.setKa(ka);
		matte17.setKd(kd);
		matte17.setCd(brown);

		Sphere sphere17 = new Sphere(new Point3D(5.0, -52.0, -100.0), 15.0);
		sphere17.setMaterial(matte17);								// browm
		addGeometricObject(sphere17);


		Matte matte18 = new Matte();
		matte18.setKa(ka);
		matte18.setKd(kd);
		matte18.setCd(dark_purple);

		Sphere sphere18 = new Sphere(new Point3D(-20.0, -57.0, -120.0), 15.0);
		sphere18.setMaterial(matte18);								// dark purple
		addGeometricObject(sphere18);


		Matte matte19 = new Matte();
		matte19.setKa(ka);
		matte19.setKd(kd);
		matte19.setCd(dark_green);

		Sphere sphere19 = new Sphere(new Point3D(55.0, -27.0, -100.0), 17.0);
		sphere19.setMaterial(matte19);								// dark green
		addGeometricObject(sphere19);


		Matte matte20 = new Matte();
		matte20.setKa(ka);
		matte20.setKd(kd);
		matte20.setCd(brown);

		Sphere sphere20 = new Sphere(new Point3D(50.0, -47.0, -120.0), 15.0);
		sphere20.setMaterial(matte20);								// browm
		addGeometricObject(sphere20);


		Matte matte21 = new Matte();
		matte21.setKa(ka);
		matte21.setKd(kd);
		matte21.setCd(light_purple);

		Sphere sphere21 = new Sphere(new Point3D(70.0, -42.0, -150.0), 10.0);
		sphere21.setMaterial(matte21);								// light purple
		addGeometricObject(sphere21);


		Matte matte22 = new Matte();
		matte22.setKa(ka);
		matte22.setKd(kd);
		matte22.setCd(light_purple);

		Sphere sphere22 = new Sphere(new Point3D(5.0, 73.0, -130.0), 12.0);
		sphere22.setMaterial(matte22);								// light purple
		addGeometricObject(sphere22);


		Matte matte23 = new Matte();
		matte23.setKa(ka);
		matte23.setKd(kd);
		matte23.setCd(dark_purple);

		Sphere sphere23 = new Sphere(new Point3D(66.0, 21.0, -130.0), 13.0);
		sphere23.setMaterial(matte23);								// dark purple
		addGeometricObject(sphere23);


		Matte matte24 = new Matte();
		matte24.setKa(ka);
		matte24.setKd(kd);
		matte24.setCd(light_purple);

		Sphere sphere24 = new Sphere(new Point3D(72.0, -12.0, -140.0), 12.0);
		sphere24.setMaterial(matte24);								// light purple
		addGeometricObject(sphere24);


		Matte matte25 = new Matte();
		matte25.setKa(ka);
		matte25.setKd(kd);
		matte25.setCd(green);

		Sphere sphere25 = new Sphere(new Point3D(64.0, 5.0, -160.0), 11.0);
		sphere25.setMaterial(matte25);					 			// green
		addGeometricObject(sphere25);


		Matte matte26 = new Matte();
		matte26.setKa(ka);
		matte26.setKd(kd);
		matte26.setCd(light_purple);

		Sphere sphere26 = new Sphere(new Point3D(55.0, 38.0, -160.0), 12.0);
		sphere26.setMaterial(matte26);								// light purple
		addGeometricObject(sphere26);


		Matte matte27 = new Matte();
		matte27.setKa(ka);
		matte27.setKd(kd);
		matte27.setCd(light_purple);

		Sphere sphere27 = new Sphere(new Point3D(-73.0, -2.0, -160.0), 12.0);
		sphere27.setMaterial(matte27);								// light purple
		addGeometricObject(sphere27);


		Matte matte28 = new Matte();
		matte28.setKa(ka);
		matte28.setKd(kd);
		matte28.setCd(dark_purple);

		Sphere sphere28 = new Sphere(new Point3D(30.0, -62.0, -140.0), 15.0);
		sphere28.setMaterial(matte28); 								// dark purple
		addGeometricObject(sphere28);


		Matte matte29 = new Matte();
		matte29.setKa(ka);
		matte29.setKd(kd);
		matte29.setCd(dark_purple);

		Sphere sphere29 = new Sphere(new Point3D(25.0, 63.0, -140.0), 15.0);
		sphere29.setMaterial(matte29);								// dark purple
		addGeometricObject(sphere29);


		Matte matte30 = new Matte();
		matte30.setKa(ka);
		matte30.setKd(kd);
		matte30.setCd(dark_purple);

		Sphere sphere30 = new Sphere(new Point3D(-60.0, 46.0, -140.0), 15.0);
		sphere30.setMaterial(matte30); 								// dark purple
		addGeometricObject(sphere30);


		Matte matte31 = new Matte();
		matte31.setKa(ka);
		matte31.setKd(kd);
		matte31.setCd(light_purple);

		Sphere sphere31 = new Sphere(new Point3D(-30.0, 68.0, -130.0), 12.0);
		sphere31.setMaterial(matte31); 								// light purple
		addGeometricObject(sphere31);


		Matte matte32 = new Matte();
		matte32.setKa(ka);
		matte32.setKd(kd);
		matte32.setCd(green);

		Sphere sphere32 = new Sphere(new Point3D(58.0, 56.0, -180.0), 11.0);
		sphere32.setMaterial(matte32);								//  green
		addGeometricObject(sphere32);


		Matte matte33 = new Matte();
		matte33.setKa(ka);
		matte33.setKd(kd);
		matte33.setCd(green);

		Sphere sphere33 = new Sphere(new Point3D(-63.0, -39.0, -180.0), 11.0);
		sphere33.setMaterial(matte33);								// green 
		addGeometricObject(sphere33);


		Matte matte34 = new Matte();
		matte34.setKa(ka);
		matte34.setKd(kd);
		matte34.setCd(light_purple);

		Sphere sphere34 = new Sphere(new Point3D(46.0, 68.0, -200.0), 10.0);
		sphere34.setMaterial(matte34);								// light purple
		addGeometricObject(sphere34);


		Matte matte35 = new Matte();
		matte35.setKa(ka);
		matte35.setKd(kd);
		matte35.setCd(light_purple);

		Sphere sphere35 = new Sphere(new Point3D(-3.0, -72.0, -130.0), 12.0);
		sphere35.setMaterial(matte35);								// light purple
		addGeometricObject(sphere35);


		// vertical plane - this goes through some spheres

		Matte matte36 = new Matte();
		matte36.setKa(0.25f);
		matte36.setKd(0.65f);
		matte36.setCd(gray);

		Plane plane = new Plane(new Point3D(0.0, 0.0, -160.0), new Normal3D(0.0, 0.0, 1.0));
		plane.setMaterial(matte36);
		addGeometricObject(plane);
	}
}
