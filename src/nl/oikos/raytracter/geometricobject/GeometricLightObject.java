package nl.oikos.raytracter.geometricobject;

import nl.oikos.raytracter.sampler.Sampler;
import nl.oikos.raytracter.util.Normal3D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.ShadeRec;

/**
 * Created by Adrian on 10-3-2018.
 */
public interface GeometricLightObject
{

	void setSampler(Sampler sampler);

	void updateNumberOfSamples(int numberOfSamples);

	Point3D sample(ShadeRec shadeRec);

	double pdf(ShadeRec shadeRec);

	Normal3D getNormal(Point3D p);

}
