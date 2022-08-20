package nl.oikos.raytracter;

import nl.oikos.raytracter.build.ExampleImage1;
import nl.oikos.raytracter.render.Canvas;
import nl.oikos.raytracter.render.StatusIndicator;
import nl.oikos.raytracter.util.FileUtils;
import nl.oikos.raytracter.world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Main implements StatusIndicator
{

	public static Supplier<? extends World> BuildConstructor = ExampleImage1::new;

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
	private CompletableFuture<Void> renderFuture;

	@Override
	public void setElapsed(long millis)
	{
		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) (millis / (1000 * 60) % 60);
		int seconds = (int) (millis / 1000) % 60;

		String message = etrLabel.getText();
		message = message.replaceAll("Elapsed Time: \\d+:\\d+:\\d+", String.format("Elapsed Time: %02d:%02d:%02d", hours, minutes, seconds));

		etrLabel.setText(message);
	}

	@Override
	public void setEtr(long millis)
	{
		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) (millis / (1000 * 60) % 60);
		int seconds = (int) (millis / 1000) % 60;

		String message = etrLabel.getText();
		message = message.replaceAll("ETR: \\d+:\\d+:\\d+", String.format("ETR: %02d:%02d:%02d", hours, minutes, seconds));

		etrLabel.setText(message);
	}

	@Override
	public void setStatusMessage(String message)
	{
		statusMessageLabel.setText(message);
	}

	public void onRenderStart()
	{
		renderFuture = CompletableFuture.runAsync(() ->
		{
			canvas.render();

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
		});

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

		for(int i = 0; i < menuSampling.getItemCount(); i++)
		{
			menuSampling.getItem(i).setEnabled(false);
		}
	}

	@Override
	public void onRenderCompleted()
	{
		canvas.executorService.shutdown();

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

		renderFuture.exceptionally((e) ->
		{
			e.printStackTrace();
			return null;
		});

		if (!renderFuture.isDone())
		{
			renderFuture.cancel(true);
		}
		else if (!renderFuture.isCompletedExceptionally())
		{
			canvas.renderPause();
		}

		setStatusMessage("Paused rendering");
	}

	public void onRenderResume()
	{
		// start
		menuRender.getItem(0).setEnabled(false);
		// pause
		menuRender.getItem(1).setEnabled(true);
		// resume
		menuRender.getItem(2).setEnabled(false);

		if (!renderFuture.isDone() || renderFuture.isCompletedExceptionally())
		{
			this.onRenderStart();
		}
		else
		{
			canvas.renderResume();

			setStatusMessage("Rendering...");
		}
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
				if (World.class.isAssignableFrom(c))
				{
					buildFunctions.put(getBuildFunctionName((Class<? extends World>) c), (Class<? extends World>) c);
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

			buildFunctions.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.forEach(entry ->
			{
				String key = entry.getKey();
				Class<? extends World> function = entry.getValue();

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
			JMenuItem menuItemRenderPause = new JMenuItem("Pause");
			menuItemRenderPause.setEnabled(false);
			JMenuItem menuItemRenderResume = new JMenuItem("Resume");
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