package nl.oikos.raytracter.sampler;

import nl.oikos.raytracter.util.Point2D;

/**
 * Created by Adrian on 20-8-2017.
 */
public class Regular extends Sampler
{

	public Regular(int numberOfSamples)
	{
		super(numberOfSamples);
	}

	public Regular(int numberOfSamples, int numberOfSets)
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
					samples.add(new Point2D((q + 0.5) / n, (p + 0.5) / n));
	}
}
