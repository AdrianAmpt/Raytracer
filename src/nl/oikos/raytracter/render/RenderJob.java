package nl.oikos.raytracter.render;

import nl.oikos.raytracter.util.Pixel;
import nl.oikos.raytracter.util.RenderedPixel;

import javax.swing.*;
import java.util.*;

public class RenderJob extends SwingWorker<Void, RenderedPixel> implements Comparable<RenderJob>
{

	/***********************************
	 **          Properties           **
	 ***********************************/

	private final Canvas canvas;
	private final Queue<Pixel> pixels;
	private final int index;

	/***********************************
	 **          Constructors         **
	 ***********************************/

	public RenderJob(Canvas canvas, Collection<Pixel> pixels, int index)
	{
		this.index = index;
		this.canvas = canvas;
		this.pixels = new LinkedList<>(pixels);
	}

	public RenderJob(RenderJob renderJob)
	{
		this.index = renderJob.index;
		this.canvas = renderJob.canvas;
		++this.canvas.numberOfJobs;
		this.pixels = renderJob.pixels;
	}

	/***********************************
	 **         Business logic        **
	 ***********************************/

	@Override
	protected Void doInBackground()
	{
		List<RenderedPixel> renderedPixels = new ArrayList<>(pixels.size());

		while (!this.pixels.isEmpty())
		{
			if (canvas.executorService.isPaused())
			{
				break;
			}

			Pixel pixel = this.pixels.poll();

			RenderedPixel renderedPixel;

			try
			{
				renderedPixel = canvas.world.camera.renderScene(canvas.world, pixel);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			renderedPixels.add(renderedPixel);

			if (canvas.renderDisplay == Canvas.RenderDisplay.EVERY_PIXEL || (canvas.renderDisplay == Canvas.RenderDisplay.EVERY_ROW && renderedPixels.size() % 10 == 0))
			{
				publish(renderedPixels.toArray(new RenderedPixel[0]));
				renderedPixels.clear();
			}
		}

		// EVERY_ROW and EVERY_JOB
		if (renderedPixels.size() > 0)
		{
			publish(renderedPixels.toArray(new RenderedPixel[0]));
			renderedPixels.clear();
		}

		return null;
	}

	@Override
	protected void process(List<RenderedPixel> chunks)
	{
		for (RenderedPixel pixel : chunks)
		{
			canvas.renderPixel(pixel);
		}
	}

	@Override
	protected void done()
	{
		super.done();

		--canvas.numberOfJobs;

		if (canvas.executorService.isPaused())
		{
			canvas.threadPaused();
		}
		else if (canvas.numberOfJobs == 0)
		{
			canvas.timer.cancel();
			canvas.statusIndicator.onRenderCompleted();
		}
	}

	@Override
	public int compareTo(RenderJob o)
	{
		return Integer.compare(this.index, o.index);
	}
}
