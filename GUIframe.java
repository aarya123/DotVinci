import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GUIframe implements Engine.EngineClient {
    boolean DEBUG = true;
    BufferedImage image;
    String suffices[];
    private JFrame window;
    private JPanel mainPanel; // the main Panel will have sub panels
    private JPanel canvasPanel;
    private JPanel buttonsPanel;
    private JPanel canvas;
    private Engine engine;
    private JButton startFilter;
    private JButton pauseFilter;
    private JButton resetFilter;
    private boolean showImage;
    private BufferedImage dotImage;

    public GUIframe(int width, int height) throws IOException {

        showImage = true;
        window = new JFrame("Dot Vinci");
        window.setSize(width, height);
        dotImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        clearDotImage();
        // add canvasPanel objects
        canvas = new MyCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.WHITE);

        // intialize engine
        engine = new Engine();
        engine.setEngineClient(this);
        if (DEBUG) {
            image = ImageIO.read(new FileInputStream("sample.jpg"));
            engine.setImage(image);
            System.out.println(String.format("Size is width: %d height: %d", image.getWidth(), image.getHeight()));
        }

        // add buttonsPanel objects

        // - add buttons
        JButton openImage = new JButton("Open Image");
        startFilter = new JButton("Start filter");
        pauseFilter = new JButton("Pause filter");
        resetFilter = new JButton("Restart filter");
        
        openImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (engine.isTimerRunning()) {
                    // Pause drawing on canvas to load image
                    for (ActionListener a : pauseFilter.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                        });
                    }
                }
                // open a JFilesChooser when the open button is clicked
                JFileChooser chooser = new JFileChooser();

                // Get array of available formats (only once)
                if (suffices == null) {
                    suffices = ImageIO.getReaderFileSuffixes();

                    // Add a file filter for each one
                    for (int i = 0; i < suffices.length; i++) {
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                suffices[i] + " files", suffices[i]);
                        chooser.addChoosableFileFilter(filter);
                    }
                }
                chooser.setFileFilter(new ImageFilter());
                chooser.setAcceptAllFileFilterUsed(false);
                int ret = chooser.showDialog(null, "Open file");

                if (ret == JFileChooser.APPROVE_OPTION) {

                    // add the selected file to the canvas
                    File file = chooser.getSelectedFile();
                    try {
                        image = ImageIO.read(new FileInputStream(file
                                .toString()));
                        engine.loadImageFromFile(file);
                        showImage = true;
                        clearDotImage();
                        canvas.repaint();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(file);
                }

            }
        });

        JButton saveImage = new JButton("Save Image");
        openImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (engine.isTimerRunning()) {
                    // Pause drawing on canvas to save image
                    for (ActionListener a : pauseFilter.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                        });
                    }
                }
                /*
                 * TODO: Add save code here
                 */
            }
        });

        // - add filters
        JLabel filterText = new JLabel("Filters:");
        final JRadioButton noFilter = new JRadioButton("None");
        noFilter.setSelected(true);

        final JRadioButton sepiaFilter = new JRadioButton("Sepia");
        final JRadioButton grayscaleFilter = new JRadioButton("Gray Scale");
        final JRadioButton negativeFilter = new JRadioButton("Negative");

        //prevent user from unchecking a radio button
        noFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!noFilter.isSelected()) {
                    noFilter.setSelected(true);
                }
            }
        });
        
        sepiaFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!sepiaFilter.isSelected()) {
                    sepiaFilter.setSelected(true);
                }
            }
        });
        
        negativeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!negativeFilter.isSelected()) {
                    negativeFilter.setSelected(true);
                }
            }
        });
        
        grayscaleFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!grayscaleFilter.isSelected()) {
                    grayscaleFilter.setSelected(true);
                }
            }
        });

        //uncheck all other radio buttons when the user checks a radio button
        noFilter.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (noFilter.isSelected()) {
                    sepiaFilter.setSelected(false);
                    grayscaleFilter.setSelected(false);
                    negativeFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.NORMAL);
                }
                if (DEBUG) { 
            		System.out.println("NORMAL");
            		System.out.println(engine.getFilter()); 
            	}
            }
        });
        sepiaFilter.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (sepiaFilter.isSelected()) {
                    noFilter.setSelected(false);
                    grayscaleFilter.setSelected(false);
                    negativeFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.SEPIA);
                }
                if (DEBUG) { 
            		System.out.println("SEPIA");
            		System.out.println(engine.getFilter()); 
                }
            }
        });
        negativeFilter.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (negativeFilter.isSelected()) {
                    sepiaFilter.setSelected(false);
                    grayscaleFilter.setSelected(false);
                    noFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.NEGATIVE);
                }
                if (DEBUG) { 
            		System.out.println("NEGATIVE");
            		System.out.println(engine.getFilter()); 
            	}
            }
        });
        grayscaleFilter.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (grayscaleFilter.isSelected()) {
                    sepiaFilter.setSelected(false);
                    noFilter.setSelected(false);
                    negativeFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.GRAYSCALE);
                }
                if (DEBUG) { 
            		System.out.println("GRAYSCALE");
            		System.out.println(engine.getFilter()); 
            	}
            }
        });

        // - add slider
        JLabel renderSpeedText = new JLabel("Render Speed:");
        final JSlider renderSpeed_slider = new JSlider(1, 100);
        final JTextField renderSpeed_value = new JTextField(3);
        Dimension dim = new Dimension(40, 30);
        renderSpeed_slider.setValue(100);
        renderSpeed_value.setSize(20, 20);
        renderSpeed_value.setMaximumSize(dim);
        renderSpeed_value.setText("100%");
        renderSpeed_slider.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				renderSpeed_value.setText(String.valueOf(renderSpeed_slider
                        .getValue() + "%"));
                if (engine.isTimerRunning()) {
	                pauseFilter.doClick();
	                startFilter.doClick();
                }
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
        });

        // setup the panels
        mainPanel = new JPanel();
        canvasPanel = new JPanel();
        buttonsPanel = new JPanel();
        // buttonsPanel.setBounds(0, 0, 300, 300);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // setup the button panel
        @SuppressWarnings("unused")
		Container contentPane = window.getContentPane();

        buttonsPanel.add(openImage);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(saveImage);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 20)));
        buttonsPanel.add(filterText);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        filterPanel.add(filterText);
        filterPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        filterPanel.add(noFilter);
        filterPanel.add(sepiaFilter);
        filterPanel.add(grayscaleFilter);
        filterPanel.add(negativeFilter);

        buttonsPanel.add(filterPanel);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(renderSpeedText);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(renderSpeed_slider);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        //buttonsPanel.add(renderSpeed_value);
        
        buttonsPanel.add(startFilter);
        startFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image == null) {
                    JOptionPane.showMessageDialog(window,
                            "Cannot start timer without an image open");
                    return;
                }
                engine.startTimer(renderSpeed_slider.getValue());
                if (showImage) {
                    clearDotImage();
                }
                showImage = false;
                pauseFilter.setVisible(true);
                startFilter.setVisible(false);
            }
        });

        buttonsPanel.add(pauseFilter);
        pauseFilter.setVisible(false);
        startFilter.setVisible(true);
        pauseFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseFilter.setVisible(false);
                startFilter.setVisible(true);
                engine.stopTimer();
            }
        });
        
        buttonsPanel.add(resetFilter);
        resetFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean interrupt = false;
		        if (engine.isTimerRunning()) {
		        	// Pause drawing
		            pauseFilter.doClick();
		            if (!showImage) {
		            	interrupt = true;
		            }
		        }
		        int confirmation = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to reset to the starting image?", 
                        "Choose", 
                        JOptionPane.YES_NO_OPTION
                ); 
				if (confirmation == JOptionPane.YES_OPTION) {
					if (DEBUG) {
						System.out.println("YES");
					}
					// Reset canvas to original image
					engine.setImage(image);
					showImage = true;
					clearDotImage();
					canvas.repaint();
					engine.startTimer(renderSpeed_slider.getValue());  
							// I'm not sure why this works, but it does?
				}
				else {
					if (DEBUG) {
						System.out.println("NO");
					}
					if (interrupt) {
						// Continue draw operation
						startFilter.doClick();
					}
				}
            }
        });
        
        canvasPanel.add(canvas);
        mainPanel.add(buttonsPanel);
        mainPanel.add(canvasPanel);

        // add the main panel to the window
        window.add(mainPanel);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setVisible(true);
    }

    public void onTimerTick() {
        canvas.repaint();
    }

    private void clearDotImage() {
        dotImage.getGraphics().setColor(Color.WHITE);
        dotImage.getGraphics().fillRect(0, 0, dotImage.getWidth(), dotImage.getHeight());
    }

    class MyCanvas extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (dotImage != null) {
                Graphics gImg = dotImage.getGraphics();
                if (showImage) {
                    engine.drawImage(gImg);
                } else {
                    engine.updateOutput(gImg);
                }
                g.drawImage(dotImage, 0, 0, null);
            }
        }
    }

}