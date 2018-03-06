package nl.oikos.raytracter.camera;

import nl.oikos.raytracter.util.MathUtils;
import nl.oikos.raytracter.util.Pixel;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.RenderedPixel;
import nl.oikos.raytracter.world.World;

public class StereoCamera<C extends	Camera> extends Camera
{

	public enum ViewType
	{
		parallel, transverse
	}

	private ViewType viewType;
	private int pixelGap;
	/* The angle beta. */
	private double stereoAngle;
	private C leftCamera;
	private C rightCamera;

	public StereoCamera()
	{
		super();
	}

	@Override
	public boolean isStereo()
	{
		return true;
	}

	@Override
	public int getOffset()
	{
		return this.pixelGap;
	}

	public void setupCameras()
	{
		double r = this.eye.distance(lookat);
		double x = r * Math.tan(0.5 * stereoAngle * MathUtils.PI_ON_180);

		leftCamera.setEye(eye.subtract(u.multiply(x)));
		leftCamera.setLookat(lookat.subtract(u.multiply(x)));
		leftCamera.computeUVW();

		rightCamera.setEye(eye.add(u.multiply(x)));
		rightCamera.setLookat(lookat.add(u.multiply(x)));
		rightCamera.computeUVW();
	}

	@Override
	public RenderedPixel renderScene(World world, Pixel pixel)
	{
		RGBColor L= RGBColor.BLACK;

		double r = this.eye.distance(lookat);
		double x = r * Math.tan(0.5 * stereoAngle * MathUtils.PI_ON_180);

		Camera leftImageCamera;
		Camera rightImageCamera;

		if (viewType == ViewType.parallel)
		{
			leftImageCamera = this.leftCamera;
			rightImageCamera = this.rightCamera;
		}
		else
		{
			leftImageCamera = this.rightCamera;
			rightImageCamera = this.leftCamera;
			x = -x;
		}

		if (pixel.x < world.viewPlane.width)
		{
			return leftImageCamera.renderStereo(world, pixel, x);
		}
		else if (pixel.x >= world.viewPlane.width + pixelGap)
		{
			pixel = new Pixel(pixel.x - world.viewPlane.width - pixelGap, pixel.y);

			RenderedPixel renderedPixel = rightImageCamera.renderStereo(world, pixel, -x);

			return new RenderedPixel(renderedPixel.x + world.viewPlane.width + pixelGap, renderedPixel.y, renderedPixel.color);
		}
		else
		{
			return world.displayPixel(pixel, L);
		}
	}

	@Override
	public RenderedPixel renderStereo(World world, Pixel pixel, double xOffset)
	{
		throw new UnsupportedOperationException("This method is irrelevant for this camera type.");
	}

	public void setViewType(ViewType viewType)
	{
		this.viewType = viewType;
	}

	public void setPixelGap(int pixelGap)
	{
		this.pixelGap = pixelGap;
	}

	public void setStereoAngle(double stereoAngle)
	{
		this.stereoAngle = stereoAngle;
	}

	public void setLeftCamera(C leftCamera)
	{
		this.leftCamera = leftCamera;
	}

	public void setRightCamera(C rightCamera)
	{
		this.rightCamera = rightCamera;
	}
}