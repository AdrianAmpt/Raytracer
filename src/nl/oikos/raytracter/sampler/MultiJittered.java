package nl.oikos.raytracter.sampler;

import nl.oikos.raytracter.util.Point2D;

/**
 * Created by Adrian on 29-8-2017.
 */
public class MultiJittered extends Sampler
{

	public MultiJittered(int numberOfSamples)
	{
		super(numberOfSamples);
	}

	public MultiJittered(int numberOfSamples, int numberOfSets)
	{
		super(numberOfSamples, numberOfSets);
	}

	@Override
	public void generateSamples()
	{
		int n = (int) Math.sqrt(numberOfSamples);
		double subcellWidth = 1.0/ numberOfSamples;

		for (int i = 0; i < numberOfSamples * numberOfSets; i++)
			samples.add(new Point2D(0));

		// distribute points in the initial patterns
		for (int p = 0; p < numberOfSets; p++)
		{
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < n; j++)
				{
					samples.get(i * n + j + p * numberOfSamples).x = (i * n + j) * subcellWidth + randomDouble(0, subcellWidth);
					samples.get(i * n + j + p * numberOfSamples).y = (j * n + i) * subcellWidth + randomDouble(0, subcellWidth);
				}
			}
		}

		for (int p = 0; p < numberOfSets; p++)
		{
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < n; j++)
				{
					int k = randomInt(j, n - 1);
					double temp = samples.get(i * n + j + p * numberOfSamples).x;
					samples.get(i * n + j + p * numberOfSamples).x = samples.get(i * n + k + p * numberOfSamples).x;
					samples.get(i * n + k + p * numberOfSamples).x = temp;
				}
			}
		}

		for (int p = 0; p < numberOfSets; p++)
		{
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < n; j++)
				{
					int k = randomInt(j, n - 1);
					double temp = samples.get(i * n + j + p * numberOfSamples).y;
					samples.get(i * n + j + p * numberOfSamples).y = samples.get(i * n + k + p * numberOfSamples).y;
					samples.get(i * n + k + p * numberOfSamples).y = temp;
				}
			}
		}
	}
}
