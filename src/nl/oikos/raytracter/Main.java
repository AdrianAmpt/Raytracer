package nl.oikos.raytracter;

import nl.oikos.raytracter.build.*;
import nl.oikos.raytracter.thread.PausableThreadPoolExecutor;
import nl.oikos.raytracter.util.*;
import nl.oikos.raytracter.world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main {

	public static Supplier<? extends World> BuildConstructor = Image10_10::new;

	public static int STANDARD_DIMENSION = 400;

	private JMenu menuFile;
	private JMenu menuRender;
	private JMenu menuBuild;
	private JMenu menuThread;
	private JMenu menuMode;
	private JMenu menuDivision;
	private JMenu menuDisplay;
	private JMenu menuSampling;
	private JLabel statusMessageLabel;
	private JLabel etrLabel;
	private Canvas canvas;

	public void setElapsed(long millis)
	{
		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) (millis / (1000 * 60) % 60);
		int seconds = (int) (millis / 1000) % 60;

		String message = etrLabel.getText();
		message = message.replaceAll("Elapsed Time: \\d+:\\d+:\\d+", String.format("Elapsed Time: %02d:%02d:%02d", hours, minutes, seconds));

		etrLabel.setText(message);
	}

	public void setEtr(long millis)
	{
		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) (millis / (1000 * 60) % 60);
		int seconds = (int) (millis / 1000) % 60;

		String message = etrLabel.getText();
		message = message.replaceAll("ETR: \\d+:\\d+:\\d+", String.format("ETR: %02d:%02d:%02d", hours, minutes, seconds));

		etrLabel.setText(message);
	}

	public void setStatusMessage(String message)
	{
		statusMessageLabel.setText(message);
	}

	public void onRenderStart()
	{
		canvas.render();

		// start
		menuRender.getItem(0).setEnabled(false);
		// pause
		menuRender.getItem(1).setEnabled(true);
		// resume
		menuRender.getItem(2).setEnabled(false);

		// open
		menuFile.getItem(0).setEnabled(false);
		// save
		menuFile.getItem(1).setEnabled(true);

		for(int i = 0; i < menuBuild.getItemCount(); i++)
		{
			menuBuild.getItem(i).setEnabled(false);
		}

		for(int i = 0; i < menuThread.getItemCount(); i++)
		{
			menuThread.getItem(i).setEnabled(false);
		}

		for(int i = 0; i < menuMode.getItemCount(); i++)
		{
			menuMode.getItem(i).setEnabled(false);
		}

		for(int i = 0; i < menuDivision.getItemCount(); i++)
		{
			menuDivision.getItem(i).setEnabled(false);
		}

		for(int i = 0; i < menuDisplay.getItemCount(); i++)
		{
			menuDisplay.getItem(i).setEnabled(true);
		}

		if (canvas.numberOfSamples != canvas.world.viewPlane.sampler.getNumberOfSamples())
		{
			switch (canvas.world.viewPlane.sampler.getNumberOfSamples())
			{
				case 1:
					menuSampling.getItem(1).setSelected(true);
					break;
				case 4:
					menuSampling.getItem(2).setSelected(true);
					break;
				case 9:
					menuSampling.getItem(3).setSelected(true);
					break;
				case 16:
					menuSampling.getItem(4).setSelected(true);
					break;
				case 25:
					menuSampling.getItem(5).setSelected(true);
					break;
				case 36:
					menuSampling.getItem(6).setSelected(true);
					break;
				case 64:
					menuSampling.getItem(7).setSelected(true);
					break;
				case 144:
					menuSampling.getItem(8).setSelected(true);
					break;
				case 256:
					menuSampling.getItem(9).setSelected(true);
					break;
				default:
					menuSampling.getItem(0).setSelected(true);
			}
		}

		for(int i = 0; i < menuSampling.getItemCount(); i++)
		{
			menuSampling.getItem(i).setEnabled(false);
		}
	}

	public void onRenderCompleted()
	{
		// start
		menuRender.getItem(0).setEnabled(true);
		// pause
		menuRender.getItem(1).setEnabled(false);
		// resume
		menuRender.getItem(2).setEnabled(false);

		// open
		menuRender.getItem(0).setEnabled(true);

		for(int i = 0; i < menuBuild.getItemCount(); i++)
		{
			menuBuild.getItem(i).setEnabled(true);
		}

		for(int i = 0; i < menuThread.getItemCount(); i++)
		{
			menuThread.getItem(i).setEnabled(true);
		}

		for(int i = 0; i < menuMode.getItemCount(); i++)
		{
			menuMode.getItem(i).setEnabled(true);
		}

		for(int i = 0; i < menuDivision.getItemCount(); i++)
		{
			menuDivision.getItem(i).setEnabled(true);
		}

		for(int i = 0; i < menuDisplay.getItemCount(); i++)
		{
			menuDisplay.getItem(i).setEnabled(true);
		}

		for(int i = 0; i < menuSampling.getItemCount(); i++)
		{
			menuSampling.getItem(i).setEnabled(true);
		}

		this.setStatusMessage("Rendering complete");
	}

	public void onRenderPause()
	{
		// start
		menuRender.getItem(0).setEnabled(false);
		// pause
		menuRender.getItem(1).setEnabled(false);
		// resume
		menuRender.getItem(2).setEnabled(true);

		canvas.renderPause();

		setStatusMessage("Pausing rendering");
	}

	public void onRenderResume()
	{
		// start
		menuRender.getItem(0).setEnabled(false);
		// pause
		menuRender.getItem(1).setEnabled(true);
		// resume
		menuRender.getItem(2).setEnabled(false);

		canvas.renderResume();

		setStatusMessage("Rendering...");
	}

	public static String getBuildFunctionName(Class<? extends World> clazz)
	{
		return clazz.getSimpleName().replace('_', ' ');
	}

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
    	Main main = new Main();

    	Map<String, Class<? extends World>> buildFunctions = new HashMap<>();

		try
		{
			FileUtils.getClassesInSamePackage(BuildConstructor.get().getClass()).forEach(c ->
			{
				Class<?> superClass = c.getSuperclass();

				while (superClass != null)
				{
					if (superClass.equals(World.class))
					{
						buildFunctions.put(getBuildFunctionName((Class<? extends World>) c), (Class<? extends World>) c);
						break;
					}
					superClass = superClass.getSuperclass();
				}
			});
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

        SwingUtilities.invokeLater(() ->
        {
			JFrame mainFrame = new JFrame("RayCast: " + getBuildFunctionName(Main.BuildConstructor.get().getClass()));
			mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			JMenu menuFile = new JMenu("File");
            JMenuItem menuItemFileOpen = new JMenuItem("Open...");
			JMenuItem menuItemFileSave = new JMenuItem("Save As...");
			menuItemFileSave.setEnabled(false);
			JMenuItem menuItemFileExit = new JMenuItem("Exit");
			menuFile.add(menuItemFileOpen);
			menuFile.add(menuItemFileSave);
			menuFile.addSeparator();
			menuFile.add(menuItemFileExit);

			JMenu menuBuild = new JMenu("BuildFunctions");

			buildFunctions.forEach((key, function) ->
			{
				JMenuItem menuItem = new JMenuItem(key);
				menuItem.setEnabled(true);
				menuItem.addActionListener(e ->
				{
					mainFrame.setTitle("RayCast: " + key);
					Main.BuildConstructor = () ->
					{
						try
						{
							return function.newInstance();
						}
						catch (InstantiationException | IllegalAccessException e1)
						{
							e1.printStackTrace();
						}
						return Main.BuildConstructor.get();
					};

					main.onRenderStart();
				});

				menuBuild.add(menuItem);
			});

			JMenu menuRender = new JMenu("Render");
			JMenuItem menuItemRenderStart = new JMenuItem("Start");
			JMenuItem menuItemRenderPause = new JMenuItem("Pause job submission");
			menuItemRenderPause.setEnabled(false);
			JMenuItem menuItemRenderResume = new JMenuItem("Resume job submission");
			menuItemRenderResume.setEnabled(false);
			menuRender.add(menuItemRenderStart);
			menuRender.add(menuItemRenderPause);
			menuRender.add(menuItemRenderResume);

			JMenu menuThread = new JMenu("Multi-Threading");
			JRadioButtonMenuItem menuItemThreadDefault = new JRadioButtonMenuItem("One Thread per System Core");
			JRadioButtonMenuItem menuItem1Thread = new JRadioButtonMenuItem("Single Thread");
			JRadioButtonMenuItem menuItem2Thread = new JRadioButtonMenuItem("Dual Threads");
			JRadioButtonMenuItem menuItem4Thread = new JRadioButtonMenuItem("Quad Threads");
			JRadioButtonMenuItem menuItemJobThread = new JRadioButtonMenuItem("One Thread per Job");
			ButtonGroup threadGroup = new ButtonGroup();
			threadGroup.add(menuItemThreadDefault);
			threadGroup.add(menuItem1Thread);
			threadGroup.add(menuItem2Thread);
			threadGroup.add(menuItem4Thread);
			threadGroup.add(menuItemJobThread);
			menuThread.add(menuItemThreadDefault);
			menuThread.add(menuItem1Thread);
			menuThread.add(menuItem2Thread);
			menuThread.add(menuItem4Thread);
			menuThread.add(menuItemJobThread);

			JMenu menuMode = new JMenu("RenderMode");
			JRadioButtonMenuItem menuItemModeSequenceV2 = new JRadioButtonMenuItem("Sequence v2");
			JRadioButtonMenuItem menuItemModeSpiralInOutV2 = new JRadioButtonMenuItem("Spiral In and Out v2");
			JRadioButtonMenuItem menuItemModeSpiralInOut = new JRadioButtonMenuItem("Spiral In and Out");
			JRadioButtonMenuItem menuItemModeSpiralOut = new JRadioButtonMenuItem("Spiral Out");
			JRadioButtonMenuItem menuItemModeSpiralIn = new JRadioButtonMenuItem("Spiral In");
			JRadioButtonMenuItem menuItemModeSequence = new JRadioButtonMenuItem("Sequence");
			JRadioButtonMenuItem menuItemModeRandom = new JRadioButtonMenuItem("Random");
			JRadioButtonMenuItem menuItemModeGrid = new JRadioButtonMenuItem("Grid");
			ButtonGroup modeGroup = new ButtonGroup();
			modeGroup.add(menuItemModeSequenceV2);
			modeGroup.add(menuItemModeSpiralInOutV2);
			modeGroup.add(menuItemModeSpiralInOut);
			modeGroup.add(menuItemModeSpiralOut);
			modeGroup.add(menuItemModeSpiralIn);
			modeGroup.add(menuItemModeSequence);
			modeGroup.add(menuItemModeRandom);
			modeGroup.add(menuItemModeGrid);
			menuMode.add(menuItemModeSequenceV2);
			menuMode.add(menuItemModeSpiralInOutV2);
			menuMode.add(menuItemModeSpiralInOut);
			menuMode.add(menuItemModeSpiralOut);
			menuMode.add(menuItemModeSpiralIn);
			menuMode.add(menuItemModeSequence);
			menuMode.add(menuItemModeRandom);
			menuMode.add(menuItemModeGrid);

			JMenu menuDivision = new JMenu("Divisions");
			JRadioButtonMenuItem menuItemDivision8 = new JRadioButtonMenuItem("64 Jobs (8 x 8)");
			JRadioButtonMenuItem menuItemDivision1 = new JRadioButtonMenuItem("1 Job (1 x 1)");
			JRadioButtonMenuItem menuItemDivision2 = new JRadioButtonMenuItem("4 Jobs (2 x 2)");
			JRadioButtonMenuItem menuItemDivision4 = new JRadioButtonMenuItem("16 Jobs (4 x 4)");
			JRadioButtonMenuItem menuItemDivision16 = new JRadioButtonMenuItem("256 Jobs (16 x 16)");
			JRadioButtonMenuItem menuItemDivision32 = new JRadioButtonMenuItem("1024 Jobs (32 x 32)");
			JRadioButtonMenuItem menuItemDivision64 = new JRadioButtonMenuItem("4096 Jobs (64 x 64)");
			ButtonGroup divisionGroup = new ButtonGroup();
			divisionGroup.add(menuItemDivision8);
			divisionGroup.add(menuItemDivision1);
			divisionGroup.add(menuItemDivision2);
			divisionGroup.add(menuItemDivision4);
			divisionGroup.add(menuItemDivision16);
			divisionGroup.add(menuItemDivision32);
			divisionGroup.add(menuItemDivision64);
			menuDivision.add(menuItemDivision8);
			menuDivision.add(menuItemDivision1);
			menuDivision.add(menuItemDivision2);
			menuDivision.add(menuItemDivision4);
			menuDivision.add(menuItemDivision16);
			menuDivision.add(menuItemDivision32);
			menuDivision.add(menuItemDivision64);

			JMenu menuDisplay = new JMenu("Display");
			JRadioButtonMenuItem menuItemDisplayPixel = new JRadioButtonMenuItem("Every Pixel");
			JRadioButtonMenuItem menuItemDisplayRow = new JRadioButtonMenuItem("Every Row");
			JRadioButtonMenuItem menuItemDisplayJob = new JRadioButtonMenuItem("End of Job");
			ButtonGroup displayGroup = new ButtonGroup();
			displayGroup.add(menuItemDisplayPixel);
			displayGroup.add(menuItemDisplayRow);
			displayGroup.add(menuItemDisplayJob);
			menuDisplay.add(menuItemDisplayPixel);
			menuDisplay.add(menuItemDisplayRow);
			menuDisplay.add(menuItemDisplayJob);

			JMenu menuSampling = new JMenu("Anti-Aliasing");
			JRadioButtonMenuItem menuItemSamplingDefault = new JRadioButtonMenuItem("Set by Build");
			JRadioButtonMenuItem menuItemSampling1 = new JRadioButtonMenuItem("1");
			JRadioButtonMenuItem menuItemSampling4 = new JRadioButtonMenuItem("4");
			JRadioButtonMenuItem menuItemSampling9 = new JRadioButtonMenuItem("9");
			JRadioButtonMenuItem menuItemSampling16 = new JRadioButtonMenuItem("16");
			JRadioButtonMenuItem menuItemSampling25 = new JRadioButtonMenuItem("25");
			JRadioButtonMenuItem menuItemSampling36 = new JRadioButtonMenuItem("36");
			JRadioButtonMenuItem menuItemSampling64 = new JRadioButtonMenuItem("64");
			JRadioButtonMenuItem menuItemSampling144 = new JRadioButtonMenuItem("144");
			JRadioButtonMenuItem menuItemSampling256 = new JRadioButtonMenuItem("256");
			ButtonGroup samplingGroup = new ButtonGroup();
			samplingGroup.add(menuItemSamplingDefault);
			samplingGroup.add(menuItemSampling1);
			samplingGroup.add(menuItemSampling4);
			samplingGroup.add(menuItemSampling9);
			samplingGroup.add(menuItemSampling16);
			samplingGroup.add(menuItemSampling25);
			samplingGroup.add(menuItemSampling36);
			samplingGroup.add(menuItemSampling64);
			samplingGroup.add(menuItemSampling144);
			samplingGroup.add(menuItemSampling256);
			menuSampling.add(menuItemSamplingDefault);
			menuSampling.add(menuItemSampling1);
			menuSampling.add(menuItemSampling4);
			menuSampling.add(menuItemSampling9);
			menuSampling.add(menuItemSampling16);
			menuSampling.add(menuItemSampling25);
			menuSampling.add(menuItemSampling36);
			menuSampling.add(menuItemSampling64);
			menuSampling.add(menuItemSampling144);
			menuSampling.add(menuItemSampling256);

			JFileChooser fileChooser = new JFileChooser();
			// Makes sure only .jpg and jpeg files can be selected
			// Other directories are also allowed
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileFilter()
			{
				public boolean accept(File f)
				{
					return (f.isDirectory() || f.getName().toLowerCase()
												.endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg"));
				}

				public String getDescription()
				{
					return "JPEG Image (*.jpg, *.jpeg)";
				}
			});

			// menu item handlers
			// file
			menuItemFileOpen.addActionListener(e ->
			{
				int action = fileChooser.showOpenDialog(mainFrame);
				// if open button is pressed, open the file.
				if (action == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					try
					{
						main.canvas.setImage(ImageIO.read(file));
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}

				menuItemFileSave.setEnabled(true);
			});
			menuItemFileSave.addActionListener(e ->
			{
				fileChooser.setSelectedFile(new File(mainFrame.getTitle().replace("RayCast: ", "") + ".jpg"));
				int action = fileChooser.showSaveDialog(mainFrame);
				// if open save is pressed, save the file.
				if (action == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					try
					{
						ImageIO.write(main.canvas.getImage(), "jpg", file);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			});
			menuItemFileExit.addActionListener(e -> System.exit(0));
			// render
			menuItemRenderStart.addActionListener(e -> main.onRenderStart());
			menuItemRenderPause.addActionListener(e -> main.onRenderPause());
			menuItemRenderResume.addActionListener(e -> main.onRenderResume());
			// thread
			menuItemThreadDefault.addActionListener(e -> main.canvas.numberOfThreads = Runtime.getRuntime().availableProcessors());
			menuItem1Thread.addActionListener(e -> main.canvas.numberOfThreads = 1);
			menuItem2Thread.addActionListener(e -> main.canvas.numberOfThreads = 2);
			menuItem4Thread.addActionListener(e -> main.canvas.numberOfThreads = 4);
			menuItemJobThread.addActionListener(e -> main.canvas.numberOfThreads = 0);
			// mode
			menuItemModeSequenceV2.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SEQUENCE2);
			menuItemModeSpiralInOutV2.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SPIRAL_IN_AND_OUT2);
			menuItemModeSpiralInOut.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SPIRAL_IN_AND_OUT);
			menuItemModeSpiralOut.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SPIRAL_OUT);
			menuItemModeSpiralIn.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SPIRAL_IN);
			menuItemModeSequence.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.SEQUENCE);
			menuItemModeRandom.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.RANDOM);
			menuItemModeGrid.addActionListener(e -> main.canvas.renderMode = Canvas.RenderMode.GRID);
			// division
			menuItemDivision8.addActionListener(e -> main.canvas.numberOfDivisions = 8);
			menuItemDivision1.addActionListener(e -> main.canvas.numberOfDivisions = 1);
			menuItemDivision2.addActionListener(e -> main.canvas.numberOfDivisions = 2);
			menuItemDivision4.addActionListener(e -> main.canvas.numberOfDivisions = 4);
			menuItemDivision16.addActionListener(e -> main.canvas.numberOfDivisions = 16);
			menuItemDivision32.addActionListener(e -> main.canvas.numberOfDivisions = 32);
			menuItemDivision64.addActionListener(e -> main.canvas.numberOfDivisions = 64);
			// display
			menuItemDisplayPixel.addActionListener(e -> main.canvas.renderDisplay = Canvas.RenderDisplay.EVERY_PIXEL);
			menuItemDisplayRow.addActionListener(e -> main.canvas.renderDisplay = Canvas.RenderDisplay.EVERY_ROW);
			menuItemDisplayJob.addActionListener(e -> main.canvas.renderDisplay = Canvas.RenderDisplay.EVERY_JOB);
			// samples
			menuItemSamplingDefault.addActionListener(e -> main.canvas.numberOfSamples = 0);
			menuItemSampling1.addActionListener(e -> main.canvas.numberOfSamples = 1);
			menuItemSampling4.addActionListener(e -> main.canvas.numberOfSamples = 4);
			menuItemSampling9.addActionListener(e -> main.canvas.numberOfSamples = 9);
			menuItemSampling16.addActionListener(e -> main.canvas.numberOfSamples = 16);
			menuItemSampling25.addActionListener(e -> main.canvas.numberOfSamples = 25);
			menuItemSampling36.addActionListener(e -> main.canvas.numberOfSamples = 36);
			menuItemSampling64.addActionListener(e -> main.canvas.numberOfSamples = 64);
			menuItemSampling144.addActionListener(e -> main.canvas.numberOfSamples = 144);
			menuItemSampling256.addActionListener(e -> main.canvas.numberOfSamples = 256);

            JMenuBar menu = new JMenuBar();
            main.menuFile = menuFile;
            main.menuRender = menuRender;
            main.menuBuild = menuBuild;
            main.menuThread = menuThread;
            main.menuMode = menuMode;
            main.menuDivision = menuDivision;
            main.menuDisplay = menuDisplay;
            main.menuSampling = menuSampling;
			menu.add(menuFile);
			menu.add(menuBuild);
			menu.add(menuRender);
			menu.add(menuThread);
			menu.add(menuMode);
			menu.add(menuDivision);
			menu.add(menuDisplay);
			menu.add(menuSampling);

			JPanel panel = new JPanel();

			panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			main.canvas = new Canvas(main);
			panel.add(main.canvas);

			JPanel statusPanel = new JPanel();
			statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
			main.statusMessageLabel = new JLabel();
			main.statusMessageLabel.setText("Ready");
			main.statusMessageLabel.setPreferredSize(new Dimension(150, 20));
			main.etrLabel = new JLabel();
			main.etrLabel.setText("Elapsed Time: 00:00:00 / ETR: 00:00:00");
			statusPanel.add(main.statusMessageLabel);
			statusPanel.add(main.etrLabel);

            mainFrame.setJMenuBar(menu);
            mainFrame.setMinimumSize(new Dimension(400, 100));

			JScrollPane pane = new JScrollPane(panel);
			pane.setPreferredSize(new Dimension(STANDARD_DIMENSION + 2, STANDARD_DIMENSION + 2));
			pane.setBackground(Color.lightGray);

			menuItemThreadDefault.doClick();
			menuItemModeSpiralInOutV2.doClick();
			menuItemDivision8.doClick();
			menuItemDisplayPixel.doClick();
			menuItemSamplingDefault.doClick();

            mainFrame.add(pane, BorderLayout.CENTER);
            mainFrame.add(statusPanel, BorderLayout.SOUTH);
			mainFrame.pack();
			mainFrame.setIconImage(new ImageIcon(Main.class.getResource("../../../icon.png")).getImage());
            mainFrame.setVisible(true);
		});
    }

}

class Canvas extends JPanel
{

	private enum Direction{UP, RIGHT, DOWN, LEFT}
	public enum RenderDisplay{EVERY_PIXEL, EVERY_ROW, EVERY_JOB}
	public enum RenderMode{GRID, RANDOM, SPIRAL_IN, SPIRAL_OUT, SPIRAL_IN_AND_OUT, SPIRAL_IN_AND_OUT2, SEQUENCE, SEQUENCE2}

	Main main;
	World world;
	int numberOfJobs;

	private BufferedImage image;
	PausableThreadPoolExecutor executorService;

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

	public Canvas(Main main)
	{
		super();
		this.main = main;

		this.image = new BufferedImage(Main.STANDARD_DIMENSION, Main.STANDARD_DIMENSION, BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(Main.STANDARD_DIMENSION, Main.STANDARD_DIMENSION));
	}

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
			this.main.setStatusMessage("Rendering paused");
		}
	}

	public void render()
	{
		main.setStatusMessage("Building world...");

		this.world = Main.BuildConstructor.get();
		this.world.build();

		main.setStatusMessage("Preparing Environment...");

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
			world.camera.updateNumberSamples(numberOfSamples);
		}

		this.numberOfJobs = 0;
		this.startTime = System.currentTimeMillis();
		pixelsRendered = 0;
		pixelsToRender = width * height;

		this.setImage(width, height);

		if (this.numberOfThreads == 0)
			this.numberOfThreads = numberOfDivisions * numberOfDivisions;

		main.setStatusMessage("Setting up Rendering Queue...");

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

		executorService = new PausableThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

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
					renderJobs.add(new RenderJob(this, current));

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
				renderJobs.add(new RenderJob(this, current));

				current.clear();
			}
		}
		toRender.clear();

		for (RenderJob job : renderJobs)
			executorService.execute(job);

		executorService.shutdown();

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
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
							Canvas.this.main.setStatusMessage(String.format("Rendering...%d%%", (int) (completed * 100)));
						Canvas.this.main.setElapsed(interval);
						if (completed != 0)
							Canvas.this.main.setEtr((long) ((interval / completed) * remaining));
					}
				});
			}
		}, 0, 500);
	}

	Timer timer;

	private List<Pixel> getPixelList(int width, int height, int jobWidth, int jobHeight)
	{
		List<Pixel> toRender = null;

		switch (renderMode)
		{
			case SPIRAL_IN:
			case SPIRAL_OUT:
			case SPIRAL_IN_AND_OUT:
			case SPIRAL_IN_AND_OUT2:
				toRender = spiral(0,0, width - 1, height - 1, width, height, Direction.RIGHT);

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

	private List<Pixel> spiral(int x, int y, int width, int height, int viewPlaneWidth, int viewPlaneHeight, Direction direction)
	{
		List<Pixel> currentList = new ArrayList<>();
		Direction newDirection = Direction.RIGHT;
		int value;

		switch (direction)
		{
			case RIGHT:
				value = width;
				while (x <= value)
				{
					currentList.add(new Pixel(x,y));
					++x;
				}
				--x;
				++y;
				newDirection = Direction.UP;
				break;
			case UP:
				value = height;
				while (y <= value)
				{
					currentList.add(new Pixel(x,y));
					++y;
				}
				--y;
				newDirection = Direction.LEFT;
				break;
			case LEFT:
				value = viewPlaneWidth - (width + 1);
				while (x > value)
				{
					--x;
					currentList.add(new Pixel(x,y));
				}
				if(width >= 1)
					width -= 1;

				newDirection = Direction.DOWN;
				break;
			case DOWN:
				if (height >= 1)
				{
					height -= 1;
				}

				value = viewPlaneHeight - (height + 1);
				while(y > value)
				{
					--y;
					currentList.add(new Pixel(x,y));
				}
				newDirection = Direction.RIGHT;
				++x;
				break;
		}

		if (!(width <= 0 && height <= 0) && currentList.size() != 0)
		{
			List<Pixel> addList = this.spiral(x, y, width, height, viewPlaneWidth, viewPlaneHeight, newDirection);
			currentList.addAll(addList);
		}

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

class RenderJob extends SwingWorker<Void, RenderedPixel>
{

	private Canvas canvas;
	private List<Pixel> pixels;

	RenderJob(Canvas canvas, List<Pixel> pixels)
	{
		this.canvas = canvas;
		this.pixels = new ArrayList<>(pixels);
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		List<RenderedPixel> renderedPixels = new ArrayList<>(pixels.size());
		for (Pixel pixel : this.pixels)
		{
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

		if (canvas.executorService.isPaused())
		{
			canvas.threadPaused();
		}

		if (--canvas.numberOfJobs == 0)
		{
			canvas.timer.cancel();
			canvas.main.onRenderCompleted();
		}
	}
}