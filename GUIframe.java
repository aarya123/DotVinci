import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

public class GUIframe implements Engine.EngineClient {
	
	private JFrame window;
	
	private JPanel mainPanel;	//the main Panel will have sub panels
	private JPanel canvasPanel;
	private JPanel buttonsPanel;
	BufferedImage image;
	private MyCanvas canvas;
	String suffices[];
	private Engine engine;
	private JButton startFilter;

	public GUIframe(int width, int height) throws FileNotFoundException, IOException {
	
		window = new JFrame("Dot Vinci");
		window.setSize(width, height);
		
		//add canvasPanel objects
        canvas = new MyCanvas();
        //image = ImageIO.read(new FileInputStream("C:/Users/Pranav/Pictures/doge.jpeg"));
		canvas.setSize(width, height);
		canvas.setBounds(0, 0, 300, 300);
		canvas.setBackground(Color.WHITE);
		
		//intialize engine
		engine = new Engine();
		engine.setEngineClient(this);

		//add buttonsPanel objects
		
		// - add buttons
		JButton openImage = new JButton("Open Image");
		openImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(engine.isTimerRunning()) {
					JOptionPane.showMessageDialog(window, "Cannot open new image while timer is running");
					return;
				}
				//open a JFilesChooser when the open button is clicked
		        JFileChooser chooser = new JFileChooser();
		        
		        // Get array of available formats (only once)
		        if(suffices == null){	
		        	suffices = ImageIO.getReaderFileSuffixes();

		        	// Add a file filter for each one
		        	for (int i = 0; i < suffices.length; i++) {
		        		FileNameExtensionFilter filter = new FileNameExtensionFilter(suffices[i] + " files", suffices[i]);
		        		System.out.println(suffices[i]+"\n");
		        		chooser.addChoosableFileFilter(filter);
		        	}
		        }

		        //FileNameExtensionFilter filter = new FileNameExtensionFilter("c files", "c");
		       // chooser.addChoosableFileFilter(filter);
		        int ret = chooser.showDialog(null, "Open file");

		        if (ret == JFileChooser.APPROVE_OPTION) {
		        	
		          //add the selected file to the canvas
		          File file = chooser.getSelectedFile();
		          try {
					image = ImageIO.read(new FileInputStream(file.toString()));
		          	engine.loadImageFromFile(file);
					canvas.repaint();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		          System.out.println(file);
		        }

			}
		});
		
		JButton saveImage = new JButton("Save Image");
		
		// - add filters
		JLabel filterText = new JLabel("Filters:");
		JRadioButton noFilter = new JRadioButton("None");
		noFilter.setSelected(true);
		JRadioButton sepiaFilter = new JRadioButton("Sepia");
		JRadioButton grayscaleFilter = new JRadioButton("Gray Scale");
		JRadioButton negativeFilter = new JRadioButton("Negative");
		
		// - add slider
		JLabel renderSpeedText = new JLabel("Render Speed:");
		final JSlider renderSpeed_slider = new JSlider(1, 100);
		final JTextField renderSpeed_value = new JTextField(3);
		Dimension dim = new Dimension(40, 30);
		renderSpeed_value.setSize(20, 20);
		renderSpeed_value.setMaximumSize(dim);
		renderSpeed_value.setText("50%");
		renderSpeed_slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
            	renderSpeed_value.setText(String.valueOf(renderSpeed_slider.getValue() + "%"));
            }
        });
		
		
		//setup the panels
		mainPanel = new JPanel();
		canvasPanel = new JPanel();
		buttonsPanel = new JPanel();
		//buttonsPanel.setBounds(0, 0, 300, 300);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		canvas.setBounds(0, 0, 1024, 800);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		
		//setup the button panel
		Container contentPane = window.getContentPane();
		
		
		buttonsPanel.add(openImage);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,10)));
		buttonsPanel.add(saveImage);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,20)));
		buttonsPanel.add(filterText);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,10)));
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
		filterPanel.add(Box.createRigidArea(new Dimension(10,20)));
		filterPanel.add(filterText);
		filterPanel.add(Box.createRigidArea(new Dimension(10,20)));
		filterPanel.add(noFilter);
		filterPanel.add(sepiaFilter);
		filterPanel.add(grayscaleFilter);
		filterPanel.add(negativeFilter);
		
		buttonsPanel.add(filterPanel);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,10)));
		buttonsPanel.add(renderSpeedText);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,10)));
		buttonsPanel.add(renderSpeed_slider);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5,10)));
		buttonsPanel.add(renderSpeed_value);

		startFilter = new JButton("Start filter");
		buttonsPanel.add(startFilter);
		startFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(image == null) {
					JOptionPane.showMessageDialog(window, "Cannot start timer without an image open");
					return;
				}
				engine.startTimer(renderSpeed_slider.getValue());
			}
		});

		canvasPanel.add(canvas);
		mainPanel.add(buttonsPanel);
		mainPanel.add(canvasPanel);
		
		
		//add the main panel to the window
	    window.add(mainPanel);
	    
	    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    window.setResizable(true);
	    window.setVisible(true);
	}


    class MyCanvas extends Canvas {
    	 
        @Override
        public void paint(Graphics g) {

                if(engine.hasImage()) {
                	g.drawImage(engine.getImage(), 0, 0, this);
            	}
        }
    }

    @Override
    public void onTimerTick() {
    	canvas.repaint();
    }
	

}