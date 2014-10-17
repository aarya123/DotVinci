import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import Sharing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GUIframe implements Engine.EngineClient {


    BufferedImage image;
    String suffices[];
    private JFrame window;
    private JPanel canvas;
    private Engine engine;
    private JButton startFilter;
    private JButton pauseFilter;
    private boolean showImage;
    private BufferedImage dotImage;

    public GUIframe(int width, int height) throws IOException {

        showImage = true;
        window = new JFrame("Dot Vinci");
        window.setSize(width, height);

        // add canvasPanel objects
        canvas = new MyCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.WHITE);

        // intialize engine
        engine = new Engine();
        engine.setEngineClient(this);

        // add buttonsPanel objects

        // - add buttons
        JButton openImage = new JButton("Save Image");							/*Defect 001*/
        startFilter = new JButton("Draw");
        pauseFilter = new JButton("Pause");
        JButton resetFilter = new JButton("Re-draw");
        JButton immediateFilter = new JButton("Quick Draw");
        JButton saveImage = new JButton("Open Image");							/*Defect 002*/
        JButton shareImage = new JButton("Share Image");


        shareImage.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                BufferedImage bi = new BufferedImage(
                                canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                                canvas.paint(bi.getGraphics());
                                try{
                     ImageIO.write(bi,"png",new File(".temp.png"));

 String to_email = (String)JOptionPane.showInputDialog(
                 
                    "Email to share to( Works best with gmail to gmail):"
                   );
		   /*
		   System.out.println(to_email);

                     String from_email = (String)JOptionPane.showInputDialog(
                 
                    "Gmail Full Email (Ex: bill@gmail.com ):"
                   
                 );
		 System.out.println(from_email);
                    String from_pass = (String)JOptionPane.showInputDialog(
                    
                    "Gmail Password:"
               );
	       */
  JLabel from_email_label = new JLabel("Email ID:");
        JTextField from_email= new JTextField();
	        JLabel from_pass_label  = new JLabel("Password");
		        JTextField from_pass = new JPasswordField();
			        Object[] ob = {from_email_label, from_email, from_pass_label,from_pass };
				        int result = JOptionPane.showConfirmDialog(null, ob, "Gmail Sign In", JOptionPane.OK_CANCEL_OPTION);
					 
  if (result == JOptionPane.OK_OPTION) {
			              //Here is some validation code
                    GmailShare email = new GmailShare(to_email, from_email.getText(), from_pass.getText(), "Check out my drawing on DotVinci!", ".temp.png");
                    email.share();
		    }
                }
                catch(Exception e1)
                {
                    System.out.println(e.toString());
                }
            }
        });


        openImage.addActionListener(new ActionListener() {

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
                
                chooser.addChoosableFileFilter(new OpenImageFilter("tiff"));
                chooser.addChoosableFileFilter(new OpenImageFilter("tif"));
                chooser.addChoosableFileFilter(new OpenImageFilter("gif"));
                chooser.addChoosableFileFilter(new OpenImageFilter("jpeg"));
                chooser.addChoosableFileFilter(new OpenImageFilter("jpg"));
                chooser.addChoosableFileFilter(new OpenImageFilter("png"));
                chooser.setFileFilter(new OpenImageFilter("All Images"));
                																		/*Defect 004 (lack of required code)*/
                																
                
                
                int ret = chooser.showDialog(null, "Open file");

                if (ret == JFileChooser.APPROVE_OPTION) {

                    // add the selected file to the canvas
                    File file = chooser.getSelectedFile();
                    try {
                    	
                    	if(image == null){														/*Defect - 006*/
                    		image = ImageIO.read(new FileInputStream("sample.jpeg")); 			/* Defect - 003*/
                    		LoadImage(image);
                    	}
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (Main.DEBUG) {
                        System.out.println(file);
                    }
                }

            }
        });

        //Save action listener
        saveImage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            																	/* Defect 007 (lack of code)*/
               
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
                // open a JFilesChooser when the save button is clicked
                JFileChooser chooser = new JFileChooser();

                chooser.addChoosableFileFilter(new SaveImageFilter("BMP"));
                chooser.addChoosableFileFilter(new SaveImageFilter("JPG"));
                chooser.addChoosableFileFilter(new SaveImageFilter("WBMP"));
                chooser.addChoosableFileFilter(new SaveImageFilter("PNG"));
                chooser.addChoosableFileFilter(new SaveImageFilter("GIF"));
                chooser.setFileFilter(new SaveImageFilter("JPEG"));
                chooser.setAcceptAllFileFilterUsed(false);

                int ret = chooser.showDialog(null, "Save file");

                if (ret == JFileChooser.APPROVE_OPTION) {

                    // get selected file
                    File file = chooser.getSelectedFile();

                    //add extension
                    String ext="";

                    String extension=chooser.getFileFilter().getDescription();
                    if(extension.equals("JPG"))
                        ext=".jpg";
                    if(extension.equals("PNG"))
                        ext=".png";
                    if(extension.equals("GIF"))
                        ext=".gif";
                    if(extension.equals("WBMP"))
                        ext=".wbmp";
                    if(extension.equals("JPEG"))
                        ext=".jpeg";
                    if(extension.equals("BMP"))
                        ext=".bmp";
                    
                    
                    String fileName = file.toString() + ".jpeg";		/* Defect 009 && Defect 008*/
                    
                    //creating new file with modified file name
                    File newFile = new File(fileName);
                    System.out.println(fileName + "new: \t\t" + newFile.toString());

                    try {
                        // save image
                        BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);		/* Defect 10*/
                        canvas.paint(bi.getGraphics());
                        System.out.println("ext = " + ext);

                        String format = "jpeg";
                        System.out.println("format = " + format);
                        ImageIO.write(bi, format , newFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

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

            public void actionPerformed(ActionEvent arg0) {
                if (!noFilter.isSelected()) {
                    noFilter.setSelected(true);
                } 
            }
        });

        sepiaFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!sepiaFilter.isSelected()) {
                    sepiaFilter.setSelected(true);
                }
            }
        });

        negativeFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!negativeFilter.isSelected()) {
                    negativeFilter.setSelected(true);
                }
            }
        });

        grayscaleFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!grayscaleFilter.isSelected()) {
                    grayscaleFilter.setSelected(true);
                }
            }
        });

        //uncheck all other radio buttons when the user checks a radio button
        // bugbug
        sepiaFilter.addChangeListener(new ChangeListener() {


            public void stateChanged(ChangeEvent e) {
                if (sepiaFilter.isSelected()) {
                    noFilter.setSelected(false);
                    grayscaleFilter.setSelected(false);
                    negativeFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.SEPIA);
                }
                if (Main.DEBUG) {
                    System.out.println("SEPIA");
                    System.out.println(engine.getFilter());
                }
            }
        });
        negativeFilter.addChangeListener(new ChangeListener() {


            public void stateChanged(ChangeEvent e) {
                if (negativeFilter.isSelected()) {
                    sepiaFilter.setSelected(false);
                    grayscaleFilter.setSelected(false);
                    noFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.NEGATIVE);
                }
                if (Main.DEBUG) {
                    System.out.println("NEGATIVE");
                    System.out.println(engine.getFilter());
                }
            }
        });
        grayscaleFilter.addChangeListener(new ChangeListener() {


            public void stateChanged(ChangeEvent e) {
                if (grayscaleFilter.isSelected()) {
                    sepiaFilter.setSelected(false);
                    noFilter.setSelected(false);
                    negativeFilter.setSelected(false);
                    engine.setFilter(Engine.Filter.GRAYSCALE);
                }
                if (Main.DEBUG) {
                    System.out.println("GRAYSCALE");
                    System.out.println(engine.getFilter());
                }
            }
        });

        // - add render speed slider
        JLabel renderSpeedText = new JLabel("Render Speed:");
        final JSlider renderSpeed_slider = new JSlider(1, 100);
        final JTextField renderSpeed_value = new JTextField(3);
        Dimension dim = new Dimension(40, 30);
        renderSpeed_slider.setValue(100);
        renderSpeed_value.setSize(20, 20);
        renderSpeed_value.setMaximumSize(dim);
        renderSpeed_value.setText("100%");
        renderSpeed_slider.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent arg0) {
                renderSpeed_value.setText(String.valueOf(renderSpeed_slider
                        .getValue() + "%"));
                if (engine.isTimerRunning()) {
                    pauseFilter.doClick();
                    startFilter.doClick();
                }
            }


            public void mouseClicked(MouseEvent e) {
            }


            public void mouseEntered(MouseEvent e) {
            }


            public void mouseExited(MouseEvent e) {
            }


            public void mousePressed(MouseEvent e) {
            }
        });

        // - add dot size slider
        JLabel dotSizeText = new JLabel("Pixel Size:");
        final JSlider dotSize_slider = new JSlider(7, 50);
        final JTextField dotSize_value = new JTextField(3);
        Dimension dim2 = new Dimension(40, 30);
        dotSize_slider.setValue(100);
        dotSize_slider.setValue(6);
        dotSize_value.setSize(20, 20);
        dotSize_value.setMaximumSize(dim2);
        dotSize_value.setText("7 px");
        dotSize_slider.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent arg0) {
                dotSize_value.setText(String.valueOf(dotSize_slider
                        .getValue() + " px"));
                engine.setPixelSize(dotSize_slider.getValue());
                if (engine.isTimerRunning()) {
                    pauseFilter.doClick();
                    startFilter.doClick();
                }
            }


            public void mouseClicked(MouseEvent e) {
            }


            public void mouseEntered(MouseEvent e) {
            }


            public void mouseExited(MouseEvent e) {
            }


            public void mousePressed(MouseEvent e) {
            }
        });


        // - add dot shape options
        JLabel dotShapeText = new JLabel("Pixel Shape:");
        final JRadioButton circleShape = new JRadioButton("Circle");
        noFilter.setSelected(true);
        final JRadioButton squareShape = new JRadioButton("Square");

        //prevent user from unchecking a radio button
        circleShape.setSelected(true);
        squareShape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!squareShape.isSelected()) {
                    squareShape.setSelected(true);
                    circleShape.setSelected(false);
                }
            }
        });

        //uncheck all other radio buttons when the user checks a radio button
        //bugbug
        squareShape.addChangeListener(new ChangeListener() {


            public void stateChanged(ChangeEvent e) {
                if (squareShape.isSelected()) {
                    //bugbug
                    circleShape.setSelected(false);
                }
                if (Main.DEBUG) {
                    System.out.println("SQUARE SHAPE");
                }
            }
        });

        // setup the panels
        JPanel mainPanel = new JPanel();
        JPanel canvasPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JPanel tweakablesPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        tweakablesPanel.setLayout(new BoxLayout(tweakablesPanel, BoxLayout.X_AXIS));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // setup the button panel
        @SuppressWarnings("unused")
        Container contentPane = window.getContentPane();

        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(openImage);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(saveImage);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(shareImage);
        buttonsPanel.add(Box.createHorizontalGlue());

        JPanel filterPanel = new JPanel();
        filterPanel.add(filterText);
        filterPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        filterPanel.add(filterText);
        filterPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        filterPanel.add(noFilter);
        filterPanel.add(sepiaFilter);
        filterPanel.add(grayscaleFilter);
        filterPanel.add(negativeFilter);
        tweakablesPanel.add(filterPanel);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));

        tweakablesPanel.add(renderSpeedText);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        tweakablesPanel.add(renderSpeed_slider);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        // tweakablesPanel.add(renderSpeed_value);

        JPanel shapePanel = new JPanel();
        shapePanel.setLayout(new BoxLayout(shapePanel, BoxLayout.X_AXIS));
        shapePanel.add(Box.createRigidArea(new Dimension(10, 20)));
        shapePanel.add(dotShapeText);
        shapePanel.add(Box.createRigidArea(new Dimension(10, 20)));
        shapePanel.add(circleShape);
        shapePanel.add(squareShape);
        tweakablesPanel.add(shapePanel);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));

        tweakablesPanel.add(dotSizeText);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        tweakablesPanel.add(dotSize_slider);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        // tweakablesPanel.add(dotSize_value);

        // buttonsPanel.add(Box.createRigidArea(new Dimension(800, 10)));
        buttonsPanel.add(startFilter);
        //bugbug
        pauseFilter.addActionListener(new ActionListener() {

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
                pauseFilter.setVisible(false);
                startFilter.setVisible(true);
            }
        });
        buttonsPanel.add(pauseFilter);
        //bugbug
        pauseFilter.setVisible(true);
        startFilter.setVisible(false);
        startFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pauseFilter.setVisible(true);
                startFilter.setVisible(false);
                engine.stopTimer();
            }
        });
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(resetFilter);
        resetFilter.addActionListener(new ActionListener() {

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
                    if (Main.DEBUG) {
                        System.out.println("YES");
                    }
                    // Reset canvas to original image
                    engine.setImage(image);
                    showImage = false;
                    clearDotImage();
                    canvas.repaint();
                    if (interrupt) {
                        startFilter.doClick();
                    } else {
                        showImage = true;
                    }
                } else {
                    if (Main.DEBUG) {
                        System.out.println("NO");
                    }
                    if (interrupt) {
                        // Continue draw operation
                        startFilter.doClick();
                    }
                }
            }
        });
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonsPanel.add(immediateFilter);
        immediateFilter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                engine.stopTimer();
                //bugbug
                pauseFilter.doClick();
                if (!engine.isTimerRunning()) {
                    clearDotImage();
                    canvas.repaint();
                    showImage = false;
                    engine.drawOutputFast(dotImage.getGraphics());
                }
            }
        });
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        canvasPanel.add(canvas);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        mainPanel.add(buttonsPanel);
        mainPanel.add(tweakablesPanel);
        mainPanel.add(canvasPanel);

        // add the main panel to the window
        window.add(mainPanel);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //bugbug
        window.setResizable(false);
        window.setVisible(true);

        if (Main.DEBUG) {
            image = ImageIO.read(new FileInputStream("sample.jpg"));
            LoadImage(image);
            System.out.println(String.format("Size is width: %d height: %d", image.getWidth(), image.getHeight()));
        }
    }

    private void LoadImage(BufferedImage image) {
        canvas.setSize(image.getWidth(), image.getHeight());
        canvas.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        engine.setImage(image);
        dotImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        startFilter.doClick();
        pauseFilter.doClick();
        showImage = true;
        clearDotImage();
        canvas.repaint();
    }

    public void onTimerTick() {
        canvas.repaint();
    }

    public void forceRedraw() {
        canvas.repaint();
    }

    private void clearDotImage() {
        //mg bug
        dotImage.getGraphics().fillRect(40, 30, dotImage.getWidth(), dotImage.getHeight());
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
                //mg bug
                g.drawImage(dotImage, 50, 50, null);
            }
        }
    }

}
