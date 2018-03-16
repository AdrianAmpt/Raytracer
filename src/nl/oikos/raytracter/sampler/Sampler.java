package nl.oikos.raytracter.sampler;

import nl.oikos.raytracter.util.MathUtils;
import nl.oikos.raytracter.util.Point2D;
import nl.oikos.raytracter.util.Point3D;
import nl.oikos.raytracter.util.ShadeRec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Adrian on 1-8-2017.
 */
public abstract class Sampler
{

	protected List<Integer> shuffledIndices;
	protected List<Point2D> samples;
	protected List<Point2D> diskSamples;
	protected List<Point3D> hemisphereSamples;
	protected List<Point3D> sphereSamples;
	protected int numberOfSets;
	protected int numberOfSamples;
	protected int count;
	protected int jump;
	protected Random random;

	public Sampler()
	{
		this(1);
	}

	public Sampler(int numberOfSamples)
	{
		this(numberOfSamples, 83);
	}

	public Sampler(int numberOfSamples, int numberOfSets)
	{
		this.numberOfSamples = numberOfSamples;
		this.numberOfSets = numberOfSets;
		this.count = 0;
		this.jump = 0;
		this.random = new Random();
		initialize();
	}

	public void initialize()
	{
		this.samples = new ArrayList<>(this.numberOfSamples * this.numberOfSets);
		this.setupShuffledIndices();
		generateSamples();
	}

	public abstract void generateSamples();

	public void shuffleXcoordinates()
	{
		for (int p = 0; p < numberOfSets; p++)
		{
			for (int i = 0; i < numberOfSamples - 1; i++)
			{
				int target = randomInt(0, numberOfSamples - 1) + p * numberOfSamples;
				double temp = samples.get(i + p * numberOfSamples + 1).x;
				samples.get(i + p * numberOfSamples + 1).x = samples.get(target).x;
				samples.get(target).x = temp;
			}
		}
	}

	public void shuffleYcoordinates()
	{
		for (int p = 0; p < numberOfSets; p++)
		{
			for (int i = 0; i < numberOfSamples - 1; i++)
			{
				int target = randomInt(0, numberOfSamples - 1) + p * numberOfSamples;
				double temp = samples.get(i + p * numberOfSamples + 1).y;
				samples.get(i + p * numberOfSamples + 1).y = samples.get(target).y;
				samples.get(target).y = temp;
			}
		}
	}

	public void setupShuffledIndices()
	{
		this.shuffledIndices = new ArrayList<>(this.numberOfSamples * this.numberOfSets);
		List<Integer> indices = new ArrayList<>(numberOfSamples);

		for (int i = 0; i < this.numberOfSamples; i++)
			indices.add(i);

		for (int j = 0; j < this.numberOfSets; j++)
		{
			Collections.shuffle(indices);

			for (int i = 0; i < this.numberOfSamples; i++)
				this.shuffledIndices.add(indices.get(i));
		}
	}

	/**
	 * Maps the 2D sample points to a disk in the unit square [-1,1].
	 */
	public void mapSamplesToUnitDisk()
	{
		int size = this.samples.size();

		double r;
		double phi;

		this.diskSamples = new ArrayList<>(size);

		for (Point2D sample : this.samples)
		{
			Point2D sp = new Point2D(2 * sample.x - 1, 2 * sample.y - 1);

			if (sp.x > -sp.y)
			{
				if (sp.x > sp.y)
				{
					r = sp.x;
					phi = sp.y / sp.x;
				}
				else
				{
					r = sp.y;
					phi = 2.0 - sp.x / sp.y;
				}
			}
			else
			{
				if (sp.x < sp.y)
				{
					r = -sp.x;
					phi = 4.0 + sp.y / sp.x;
				}
				else
				{
					r = -sp.y;
					if (sp.y != 0.0)
						phi = 6.0 - sp.x / sp.y;
					else
						phi = 0.0;
				}
			}

			phi *= Math.PI / 4;
			diskSamples.add(new Point2D(r * Math.cos(phi), r * Math.sin(phi)));
		}

		samples.clear();
	}

	/**
	 * Maps the 2D sample points to 3D points on a unit hemisphere
	 * with a cosine power density distribution in the polar angle.
	 *
	 * @param exp
	 */
	public void mapSamplesToHemisphere(final double exp)
	{
		hemisphereSamples = new ArrayList<>(numberOfSamples * numberOfSets);

		for (Point2D sample : samples)
		{
			double cosPhi = Math.cos(MathUtils.TWO_PI * sample.x);
			double sinPhi = Math.sin(MathUtils.TWO_PI * sample.x);
			double cosTheta = Math.pow((1.0 - sample.y), 1.0 / (exp + 1.0));
			double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
			double pu = sinTheta * cosPhi;
			double pv = sinTheta * sinPhi;
			double pw = cosTheta;
			hemisphereSamples.add(new Point3D(pu, pv, pw));
		}
	}

	/**
	 * Maps the 2D sample points to 3D points on a unit sphere
	 * with a uniform density distribution over the surface.
	 */
	public void mapSamplesToSphere()
	{
		double r1, r2;
		double x, y, z;
		double r, phi;

		sphereSamples = new ArrayList<>(numberOfSamples * numberOfSets);

		for (int j = 0; j < numberOfSamples * numberOfSets; j++) {
			r1 	= samples.get(j).x;
			r2 	= samples.get(j).y;
			z 	= 1.0 - 2.0 * r1;
			r 	= Math.sqrt(1.0 - z * z);
			phi = MathUtils.TWO_PI * r2;
			x 	= r * Math.cos(phi);
			y 	= r * Math.sin(phi);
			sphereSamples.add(new Point3D(x, y, z));
		}
	}

	private <T> T sample(ShadeRec sr, List<T> samples)
	{
		// if this is the first call to sample for this pixel
		if(sr.sync == false)
		{
			sr.sync = true;
			if (sr.count % numberOfSamples == 0)
				sr.jump = randomInt(0, numberOfSets - 1) * numberOfSamples;

			return samples.get(sr.jump + shuffledIndices.get(sr.jump + sr.count++ % numberOfSamples));
		}
		else // not the first call to sample for this pixel
		{
			return samples.get(sr.jump + shuffledIndices.get(sr.jump + sr.count++ % numberOfSamples));
		}
	}

	public Point2D sampleUnitSquare(ShadeRec sr)
	{
		return sample(sr, samples);
	}

	public Point2D sampleUnitDisk(ShadeRec sr)
	{
		return sample(sr, diskSamples);
	}

	public Point3D sampleSphere(ShadeRec sr)
	{
		return sample(sr, sphereSamples);
	}

	public Point3D sampleHemisphere(ShadeRec sr)
	{
		return sample(sr, hemisphereSamples);
	}

	public Point2D sampleOneSet(ShadeRec sr)
	{
		return(samples.get((sr.count)++ % numberOfSamples));
	}

	public double randomDouble()
	{
		return random.nextDouble();
	}

	public double randomDouble(double low, double high)
	{
		return low + this.randomDouble() * (high - low);
	}

	public int randomInt()
	{
		return random.nextInt();
	}

	public int randomInt(int low, int high)
	{
		return low + random.nextInt(high - low + 1);
	}

	public int getNumberOfSets()
	{
		return numberOfSets;
	}

	public void setNumberOfSets(int numberOfSets)
	{
		this.numberOfSets = numberOfSets;
	}

	public int getNumberOfSamples()
	{
		return numberOfSamples;
	}

	public void setNumberOfSamples(int numberOfSamples)
	{
		this.numberOfSamples = numberOfSamples;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getJump()
	{
		return jump;
	}

	public void setJump(int jump)
	{
		this.jump = jump;
	}
}
