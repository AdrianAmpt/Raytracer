package nl.oikos.raytracter.build;

import nl.oikos.raytracter.camera.FishEye;
import nl.oikos.raytracter.camera.Pinhole;
import nl.oikos.raytracter.camera.Spherical;
import nl.oikos.raytracter.geometricobject.AABox;
import nl.oikos.raytracter.geometricobject.Plane;
import nl.oikos.raytracter.light.PointLight;
import nl.oikos.raytracter.material.Matte;
import nl.oikos.raytracter.sampler.MultiJittered;
import nl.oikos.raytracter.texture.Checker3D;
import nl.oikos.raytracter.tracer.RayCast;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.world.World;

import java.util.Random;

/**
 * Created by Adrian on 4-3-2018.
 */
public class Image11_11 extends World
{

	@Override
	public void build()
	{
		final int numberOfSamples = 100; // use 9 or 16 samples for production image

		// view plane

		viewPlane.setWidth(600);
		viewPlane.setHeight(400);
		viewPlane.setSampler(new MultiJittered(numberOfSamples));
		//viewPlane.setShowOutOfGamut(true);
		viewPlane.setMaxDepth(0);

		backgroundColor = RGBColor.WHITE;

		tracer = new RayCast(this);

		// camera
		Spherical spherical = new Spherical();
		spherical.setEye(new Point3D(10, 15, 13));
		spherical.setLookat(new Point3D(34, 15, 0));
		spherical.setFovHorizontal(180);
		spherical.setFovVertical(120);
		spherical.computeUVW();
		setCamera(spherical);


		// lights
		PointLight light1 = new PointLight();
		light1.setLocation(new Point3D(150, 500, 300));
		light1.scaleRadiance(3.75);
		light1.setShadows(true);
		addLight(light1);

		// city parameters

		double 	a					= 10;   // city block width:  xw extent
		double 	b   				= 12;	// city block length:  yw extent
		int 	numRows				= 10;  	// number of blocks in the xw direction
		int 	numColumns			= 12; 	// number of blocks in the zw direction
		double	width				= 7;	// building width: xw extent in range [min, a - offset]
		double 	length				= 7;	// building length: zw extent in range [min, b - offset]
		double 	minSize				= 6;	// mininum building extent in xw and yw directions
		double 	offset				= 1.0;	// half the minimum distance between buildings
		double 	minHeight			= 0.0; 	// minimum building height
		double 	maxHeight			= 30; 	// maximum bulding height
		double 	height;						// the building height in range [minHeight, maxHeight]
		int		numParkRows			= 4;  	// number of blocks of park in xw direction
		int		numParkColumns		= 6;  	// number of blocks of park in xw direction
		int 	rowTest;					// there are no buildings in the park
		int 	columnTest;					// there are no buildings in the park
		double 	minColor			= 0.1;  // prevents black buildings
		double 	maxColor			= 0.9;	// prevents white buildings

		Random rand = new Random(15); 	// as the buildings' dimensions and colors are random, it's necessary to
											// seed rand to keep these quantities the same at each run
											// if you leave this out, and change the number of samples per pixel,
											// these will change


		for (int r = 0; r < numRows; r++)  		// xw direction
		{
			for (int c = 0; c < numColumns; c++) 	// zw direction
			{
				// determine if the block is in the park

				if ((r - numRows / 2) >= 0)
					rowTest = r - numRows / 2;
				else
					rowTest = r - numRows / 2 + 1;

				if ((c - numColumns / 2) >= 0)
					columnTest = c - numColumns / 2;
				else
					columnTest = c - numColumns / 2 + 1;

				if (Math.abs(rowTest) >= (numParkRows / 2) || Math.abs(columnTest) >= (numParkColumns / 2))
				{

					Matte matte = new Matte();
					matte.setKa(0.4);
					matte.setKd(0.6);
					matte.setCd(new RGBColor(minColor + rand.nextDouble() * (maxColor - minColor),
							minColor + rand.nextDouble() * (maxColor - minColor),
							minColor + rand.nextDouble() * (maxColor - minColor)));

					// block center coordinates

					double xc = a * (r - numRows / 2.0 + 0.5);
					double zc = b * (c - numColumns / 2.0 + 0.5);

					width = minSize + rand.nextDouble() * (a - 2 * offset - minSize);
					length = minSize + rand.nextDouble() * (b - 2 * offset - minSize);

					// minimum building coordinates

					double xmin = xc - width / 2.0;
					double ymin = 0.0;
					double zmin = zc - length / 2.0;

					// maximum building coordinates

					height = minHeight + rand.nextDouble() * (maxHeight - minHeight);

					// The following is a hack to make the middle row and column of buildings higher
					// on average than the other buildings.
					// This only works properly when there are three rows and columns of buildings

					if (r == 1 || r == numRows - 2 || c == 1 || c == numColumns - 2)
						height *= 1.5;

					double xmax = xc + width / 2.0;
					double ymax = height;
					double zmax = zc + length / 2.0;

					AABox building = new AABox(new Point3D(xmin, ymin, zmin), new Point3D(xmax, ymax, zmax));
					building.setMaterial(matte);
					addGeometricObject(building);
				}
			}
		}

		// park
		Checker3D checker1 = new Checker3D();
		checker1.setSquareSize(5);
		checker1.setColor1(new RGBColor(0.35, 0.75, 0.35));
		checker1.setColor2(new RGBColor(0.3, 0.5, 0.3));

		Matte svMatte1 = new Matte();
		svMatte1.setKa(0.3);
		svMatte1.setKd(0.5);
		svMatte1.setCd(checker1);

		AABox park = new AABox(new Point3D(-a * numParkRows / 2, 0, -b * numParkColumns / 2),
							   new Point3D(a * numParkRows / 2, 0.1, b * numParkColumns / 2));
		park.setMaterial(svMatte1);
		addGeometricObject(park);

		// ground plane
		Checker3D checker2 = new Checker3D();
		checker2.setSquareSize(50);
		checker2.setColor1(new RGBColor(0.7));
		checker2.setColor2(RGBColor.WHITE);

		Matte svMatte2 = new Matte();
		svMatte2.setKa(0.3);
		svMatte2.setKd(0.4);
		svMatte2.setCd(checker2);

		Plane plane = new Plane(new Point3D(0, 0.01, 0), Normal3D.Y);
		plane.setMaterial(svMatte2);
		addGeometricObject(plane);
	}
}
