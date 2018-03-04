package nl.oikos.raytracter.sampler;

import nl.oikos.raytracter.util.Point2D;

/**
 * Created by Adrian on 29-8-2017.
 */
public class PureRandom extends Sampler
{

	public PureRandom(int numberOfSamples)
	{
		super(numberOfSamples);
	}


	public PureRandom(int numberOfSamples, int numberOfSets)
	{
		super(numberOfSamples, numberOfSets);
	}

	@Override
	public void generateSamples()
	{
		for (int i = 0; i < numberOfSets; i++)
			for (int j = 0; j < numberOfSamples; j++)
				samples.add(new Point2D(randomDouble(), randomDouble()));
	}
}