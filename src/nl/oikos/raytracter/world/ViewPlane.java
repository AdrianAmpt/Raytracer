package nl.oikos.raytracter.world;

import nl.oikos.raytracter.sampler.Sampler;

/**
 * Created by Adrian on 31-7-2017.
 */
public class ViewPlane
{

	public int height;
	public int width;
	public double pixelSize;

	public double gamma;
	public double inverseGamma;
	public boolean showOutOfGamut;

	public Sampler sampler;
	public int maxDepth;

	public ViewPlane()
	{
		this.height = 400;
		this.width = 400;
		this.pixelSize = 1;
		this.setGamma(1);
		this.showOutOfGamut = false;
		this.sampler = null;
	}

	public ViewPlane(ViewPlane viewPlane)
	{
		this.height = viewPlane.height;
		this.width = viewPlane.width;
		this.pixelSize = viewPlane.pixelSize;
		this.gamma = viewPlane.gamma;
		this.inverseGamma = viewPlane.inverseGamma;
		this.showOutOfGamut = viewPlane.showOutOfGamut;
		this.sampler = viewPlane.sampler;
		this.maxDepth = viewPlane.maxDepth;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setPixelSize(double pixelSize)
	{
		this.pixelSize = pixelSize;
	}

	public void setGamma(double gamma)
	{
		this.gamma = gamma;
		this.inverseGamma = 1 / gamma;
	}

	public void setShowOutOfGamut(boolean showOutOfGamut)
	{
		this.showOutOfGamut = showOutOfGamut;
	}

	public void setSampler(Sampler sampler)
	{
		this.sampler = sampler;
	}

	public void setMaxDepth(int maxDepth)
	{
		this.maxDepth = maxDepth;
	}
}
