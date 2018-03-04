package nl.oikos.raytracter.sampler;

import nl.oikos.raytracter.util.Point2D;

/**
 * Created by Adrian on 29-8-2017.
 */
public class Jittered extends Sampler
{

	public Jittered(int numberOfSamples)
	{
		super(numberOfSamples);
	}

	public Jittered(int numberOfSamples, int numberOfSets)
	{
		super(numberOfSamples, numberOfSets);
	}

	@Override
	public void generateSamples()
	{
		int n = (int) Math.sqrt(numberOfSamples);

		for (int j = 0; j < numberOfSets; j++)
			for (int p = 0; p < n; p++)
				for (int q = 0; q < n; q++)
					samples.add(new Point2D((q + randomDouble()) / n, (p + randomDouble()) / n));
	}
}
