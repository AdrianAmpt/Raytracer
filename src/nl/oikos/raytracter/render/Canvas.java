package nl.oikos.raytracter.render;

import nl.oikos.raytracter.Main;
import nl.oikos.raytracter.util.Pixel;
import nl.oikos.raytracter.util.RGBColor;
import nl.oikos.raytracter.util.RGBIntColor;
import nl.oikos.raytracter.util.RenderedPixel;
import nl.oikos.raytracter.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Canvas extends JPanel
{

	private enum Direction{UP, RIGHT, DOWN, LEFT}
	public enum RenderDisplay{EVERY_PIXEL, EVERY_ROW, EVERY_JOB}
	public enum RenderMode{GRID, RANDOM, SPIRAL_IN, SPIRAL_OUT, SPIRAL_IN_AND_OUT, SPIRAL_IN_AND_OUT2, SEQUENCE, SEQUENCE2}

	/***********************************
	 **          Properties           **
	 ***********************************/

	public World world;

	protected StatusIndicator statusIndicator;
	protected int numberOfJobs;

	private BufferedImage image;
	public RenderJobThreadPoolExecutor executorService;

	private int threadsPaused;
	private long pausedTime;
	private long startTime;
	private int pixelsRendered;
	private int pixelsToRender;

	public int numberOfSamples;
	public int numberOfThreads;
	public int numberOfDivisions;
	public RenderDisplay renderDisplay;
	public RenderMode renderMode;

	protected Timer timer;

	/***********************************
	 **          Constructors         **
	 ***********************************/

	public Canvas(StatusIndicator statusIndicator)
	{
		super();
		this.statusIndicator = statusIndicator;

		this.image = new BufferedImage(Main.STANDARD_DIMENSION, Main.STANDARD_DIMENSION, BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(Main.STANDARD_DIMENSION, Main.STANDARD_DIMENSION));
	}

	/***********************************
	 **         Business logic        **
	 ***********************************/

	private void setImage(int width, int height)
	{
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(width, height));

		switch (this.renderMode)
		{
			case GRID:
			case SPIRAL_IN:
			case SPIRAL_OUT:
			case SPIRAL_IN_AND_OUT:
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						if ((x % 16 < 8) ^ (y % 16 < 8))
							image.setRGB(x, y, new RGBIntColor(102).getRGB());
						else
							image.setRGB(x, y, new RGBIntColor(153).getRGB());
					}
				}
				break;
			default:
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						image.setRGB(x, y, RGBColor.BLACK.getRGB());
					}
				}
				break;
		}

		this.repaint();
		this.revalidate();
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		this.repaint();
		this.revalidate();
	}

	public BufferedImage getImage()
	{
		return this.image;
	}

	public void renderPause()
	{
		this.executorService.pause();
	}

	public void renderResume()
	{
		if (pausedTime != 0)
		{
			this.startTime += System.currentTimeMillis() - pausedTime;
		}
		pausedTime = 0;
		threadsPaused = 0;
		this.executorService.resume();
	}

	public void threadPaused()
	{
		this.threadsPaused++;
		if (threadsPaused == this.numberOfThreads)
		{
			pausedTime = System.currentTimeMillis();
			this.statusIndicator.setStatusMessage("Rendering paused");
		}
	}

	public void render()
	{
		this.statusIndicator.setStatusMessage("Building world...");

		this.world = Main.BuildConstructor.get();
		this.world.build();

		this.statusIndicator.setStatusMessage("Preparing Environment...");

		int width = world.viewPlane.width;
		int height = world.viewPlane.height;
		if (world.camera.isStereo())
		{
			width = width * 2 + world.camera.getOffset();
		}

		int currentSamples = world.viewPlane.sampler.getNumberOfSamples();
		if (numberOfSamples != currentSamples && numberOfSamples != 0)
		{
			world.viewPlane.sampler.setNumberOfSamples(numberOfSamples);
			world.viewPlane.sampler.initialize();
			world.camera.updateNumberOfSamples(numberOfSamples);
			world.ambientLight.updateNumberOfSamples(numberOfSamples);
			world.lights.forEach(l -> l.updateNumberOfSamples(numberOfSamples));
		}

		this.numberOfJobs = 0;
		this.startTime = System.currentTimeMillis();
		pixelsRendered = 0;
		pixelsToRender = width * height;

		this.setImage(width, height);

		if (this.numberOfThreads == 0)
			this.numberOfThreads = numberOfDivisions * numberOfDivisions;

		this.statusIndicator.setStatusMessage("Setting up Rendering Queue...");

		int jobWidth;
		int jobHeight;

		if (width < numberOfDivisions)
			jobWidth = width;
		else
			jobWidth = width / numberOfDivisions;

		if (world.viewPlane.height < numberOfDivisions)
			jobHeight = world.viewPlane.height;
		else
			jobHeight = world.viewPlane.height / numberOfDivisions;

		List<Pixel> toRender = this.getPixelList(width, height, jobWidth, jobHeight);

		int jobSize = jobWidth * jobHeight;

		List<Pixel> current = new ArrayList<>(jobSize);
		int id = 0;

		executorService = new RenderJobThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());

		List<RenderJob> renderJobs = new ArrayList<>();

		for(int y = 0; y < numberOfDivisions; y++)
		{
			for(int x = 0; x < numberOfDivisions; x++)
			{
				if (id < toRender.size())
				{
					for (int i = 0; i < jobSize; i++)
					{
						if (id >= toRender.size())
							break;
						current.add(toRender.get(id));
						id++;
					}

					this.numberOfJobs++;

					// create swing worker for current list
					renderJobs.add(new RenderJob(this, current, this.numberOfJobs));

					current.clear();
				}
			}
		}

		if(id < pixelsToRender)
		{
			while(id < pixelsToRender)
			{
				for(int i = 0; i < jobSize; i++)
				{
					current.add(toRender.get(id));
					id++;
					if(id == pixelsToRender)
						i = jobSize;
				}

				this.numberOfJobs++;

				// create swing worker for current list
				renderJobs.add(new RenderJob(this, current, this.numberOfJobs));

				current.clear();
			}
		}
		toRender.clear();

		for (RenderJob job : renderJobs)
			executorService.execute(job);

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run() {
				SwingUtilities.invokeLater(() ->
				{
					if (threadsPaused != numberOfThreads)
					{
						long interval = System.currentTimeMillis() - startTime;
						double completed = ((double) pixelsRendered) / pixelsToRender;
						double remaining = 1 - completed;

						if (!Canvas.this.executorService.isPaused())
							Canvas.this.statusIndicator.setStatusMessage(String.format("Rendering...%d%%", (int) (completed * 100)));
						Canvas.this.statusIndicator.setElapsed(interval);
						if (completed != 0)
							Canvas.this.statusIndicator.setEtr((long) ((interval / completed) * remaining));
					}
				});
			}
		}, 0, 500);
	}

	private List<Pixel> getPixelList(int width, int height, int jobWidth, int jobHeight)
	{
		List<Pixel> toRender = null;

		switch (renderMode)
		{
			case SPIRAL_IN:
			case SPIRAL_OUT:
			case SPIRAL_IN_AND_OUT:
			case SPIRAL_IN_AND_OUT2:
				toRender = spiral(width - 1, height - 1, width, height);

				if (renderMode == RenderMode.SPIRAL_OUT)
					Collections.reverse(toRender);
				else if (renderMode == RenderMode.SPIRAL_IN_AND_OUT || renderMode == RenderMode.SPIRAL_IN_AND_OUT2)
				{
					List<Pixel> bounds = new ArrayList<>(toRender.subList(0, toRender.size() / 2));
					List<Pixel> center = new ArrayList<>(toRender.subList(toRender.size() / 2, toRender.size()));
					toRender.clear();
					if (renderMode == RenderMode.SPIRAL_IN_AND_OUT)
					{
						for (int i = 0; i < center.size(); i++)
						{
							toRender.add(bounds.get(i));
							toRender.add(center.get(center.size() - i - 1));
						}
					}
					else if (renderMode == RenderMode.SPIRAL_IN_AND_OUT2)
					{
						for (int i = 0; i < center.size(); i += 2)
						{
							toRender.add(bounds.get(i));
							toRender.add(center.get(center.size() - i - 1));
						}
						for (int i = center.size() - 1; i > 0; i -= 2)
						{
							toRender.add(bounds.get(i));
							toRender.add(center.get(center.size() - i - 1));
						}
					}
				}
				break;
			case GRID:
				toRender = new ArrayList<>(pixelsToRender);

				int xRemainder = width % numberOfDivisions;
				int yRemainder = height % numberOfDivisions;

				int currentX;
				int currentY;
				for (int y = 0; y < numberOfDivisions; y++)
				{
					currentY = y * jobHeight;
					for (int x = 0; x < numberOfDivisions; x++)
					{
						currentX = x * jobWidth;
						int xEnd = currentX + jobWidth;
						int yEnd = currentY + jobHeight;
						for (int iy = currentY; iy < yEnd; iy++)
						{
							for (int ix = currentX; ix < xEnd; ix++)
							{
								toRender.add(new Pixel(ix, iy));
							}
						}
					}

					if (xRemainder != 0)
					{
						int yStart = currentY;
						int xStart = width - xRemainder;
						int yEnd = currentY + jobHeight;
						int xEnd = width;
						for (int ix = xStart; ix < xEnd; ix++)
						{
							for(int iy = yStart; iy < yEnd; iy++)
							{
								toRender.add(new Pixel(ix, iy));
							}
						}
					}

				}
				if (yRemainder != 0)
				{
					int yStart = height - yRemainder;
					int xStart = 0;
					int yEnd = height;
					int xEnd = width;

					for(int iy = yStart; iy < yEnd; iy++)
					{
						for(int ix = xStart; ix < xEnd; ix++)
						{
							toRender.add(new Pixel(ix, iy));
						}
					}
				}
				break;
			case SEQUENCE2:
				toRender = new ArrayList<>(pixelsToRender);
				List<Pixel> toRenderHalf = new ArrayList<>(pixelsToRender / 2);

				for (int y = 0; y < height; y++)
				{
					for (int x = 0; x < width; x++)
					{
						if (((x + y) % 2) == 0)
							toRenderHalf.add(new Pixel(x, y));
					}
				}

				List<Pixel> bounds = new ArrayList<>(toRenderHalf.subList(0, toRenderHalf.size() / 2));
				List<Pixel> center = new ArrayList<>(toRenderHalf.subList(toRenderHalf.size() / 2, toRenderHalf.size()));
				toRenderHalf.clear();

				for (int i = 0; i < center.size(); i++)
				{
					toRender.add(bounds.get(i));
					toRender.add(center.get(center.size() - i - 1));
				}

				for (int y = 0; y < height; y++)
				{
					for (int x = 0; x < width; x++)
					{
						if (((x + y) % 2) == 1)
							toRenderHalf.add(new Pixel(x, y));
					}
				}

				bounds = new ArrayList<>(toRenderHalf.subList(0, toRenderHalf.size() / 2));
				center = new ArrayList<>(toRenderHalf.subList(toRenderHalf.size() / 2, toRenderHalf.size()));

				for (int i = center.size() - 1; i >= 0; i--)
				{
					toRender.add(bounds.get(i));
					toRender.add(center.get(center.size() - i - 1));
				}

				break;
			case SEQUENCE:
			case RANDOM:
				toRender = new ArrayList<>(pixelsToRender);

				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						toRender.add(new Pixel(x, y));

				if (renderMode == RenderMode.RANDOM)
					Collections.shuffle(toRender);
				break;
		}

		return toRender;
	}

	private List<Pixel> spiral(int width, int height, int viewPlaneWidth, int viewPlaneHeight)
	{
		int x = 0;
		int y = 0;
		List<Pixel> currentList = new ArrayList<>(pixelsToRender);
		int value;
		Direction direction = Direction.RIGHT;

		do
		{
			switch (direction)
			{
				case RIGHT:
					value = width;
					while (x <= value)
					{
						currentList.add(new Pixel(x, y));
						++x;
					}
					--x;
					++y;
					direction = Direction.UP;
					break;
				case UP:
					value = height;
					while (y <= value)
					{
						currentList.add(new Pixel(x, y));
						++y;
					}
					--y;
					direction = Direction.LEFT;
					break;
				case LEFT:
					value = viewPlaneWidth - (width + 1);
					while (x > value)
					{
						--x;
						currentList.add(new Pixel(x, y));
					}
					if (width >= 1)
						width -= 1;

					direction = Direction.DOWN;
					break;
				case DOWN:
					if (height >= 1)
					{
						height -= 1;
					}

					value = viewPlaneHeight - (height + 1);
					while (y > value)
					{
						--y;
						currentList.add(new Pixel(x, y));
					}
					direction = Direction.RIGHT;
					++x;
					break;
			}
		}
		while (!(width <= 0 && height <= 0) && currentList.size() != 0);

		return currentList;
	}

	public void renderPixel(RenderedPixel pixel)
	{
		this.setRGB(pixel.x, pixel.y, pixel.color);

		this.pixelsRendered ++;
	}

	public void setRGB(int x, int y, RGBColor color)
	{
		image.setRGB(x, y, color.getRGB());
		this.repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.image, null, null);
	}
}
