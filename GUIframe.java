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
        JButton openImage = new JButton("Open Image");
        startFilter = new JButton("Draw");
        pauseFilter = new JButton("Pause");
        JButton resetFilter = new JButton("Re-draw");
        JButton immediateFilter = new JButton("Quick Draw");
        JButton saveImage = new JButton("Save Image");
        JButton shareImage = new JButton("Share Image");

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
                    for (String suffice : suffices) {
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                suffice.toUpperCase(), suffice);
                        chooser.addChoosableFileFilter(filter);
                    }
                }
                chooser.setFileFilter(new AllImagesFilter());
                chooser.setAcceptAllFileFilterUsed(false);
                int ret = chooser.showDialog(null, "Open file");

                if (ret == JFileChooser.APPROVE_OPTION) {

                    // add the selected file to the canvas
                    File file = chooser.getSelectedFile();
                    try {
                        image = ImageIO.read(new FileInputStream(file
                                .toString()));
                        LoadImage(image);
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
            @Override
            public void actionPerformed(ActionEvent e) {

                if (image == null) {
                    JOptionPane.showMessageDialog(window,
                            "No image to save, please load one first");
                    return;
                }
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
                    String ext = "";

                    String extension = chooser.getFileFilter().getDescription();
                    if (extension.equals("JPG"))
                        ext = ".jpg";
                    if (extension.equals("PNG"))
                        ext = ".png";
                    if (extension.equals("GIF"))
                        ext = ".gif";
                    if (extension.equals("WBMP"))
                        ext = ".wbmp";
                    if (extension.equals("JPEG"))
                        ext = ".jpeg";
                    if (extension.equals("BMP"))
                        ext = ".bmp";

                    String fileName = file.toString() + ext;

                    //creating new file with modified file name
                    File newFile = new File(fileName);
                    System.out.println(fileName + "\t\t" + newFile.toString());

                    try {
                        // save image
                        BufferedImage bi = new BufferedImage(
                                canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                        canvas.paint(bi.getGraphics());
                        System.out.println("ext = " + ext);
                        String format = ext.substring(1);
                        System.out.println("format = " + format);
                        ImageIO.write(bi, format, newFile);    //why doesn't it save the file???
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
                if (Main.DEBUG) {
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
                if (Main.DEBUG) {
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
                if (Main.DEBUG) {
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
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
        });

        // - add dot size slider
        JLabel dotSizeText = new JLabel("Pixel Size:");
        final JSlider dotSize_slider = new JSlider(1, 50);
        final JTextField dotSize_value = new JTextField(3);
        Dimension dim2 = new Dimension(40, 30);
        dotSize_slider.setValue(100);
        dotSize_slider.setValue(6);
        dotSize_value.setSize(20, 20);
        dotSize_value.setMaximumSize(dim2);
        dotSize_value.setText("6 px");
        dotSize_slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                dotSize_value.setText(String.valueOf(dotSize_slider
                        .getValue() + " px"));
                engine.setPixelSize(dotSize_slider.getValue());
                if (engine.isTimerRunning()) {
                    pauseFilter.doClick();
                    startFilter.doClick();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
        });


        // - add dot shape options
        JLabel dotShapeText = new JLabel("Pixel Shape:");
        final JRadioButton circleShape = new JRadioButton("Circle");
        noFilter.setSelected(true);
        final JRadioButton squareShape = new JRadioButton("Square");
        final JRadioButton triangleShape = new JRadioButton("Triangle");

        //prevent user from unchecking a radio button
        circleShape.setSelected(true);
        circleShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!circleShape.isSelected()) {
                    circleShape.setSelected(true);
                    squareShape.setSelected(false);
                    triangleShape.setSelected(false);
                }
            }
        });

        squareShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!squareShape.isSelected()) {
                    squareShape.setSelected(true);
                    circleShape.setSelected(false);
                    triangleShape.setSelected(false);
                }
            }
        });

        triangleShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!triangleShape.isSelected()) {
                    triangleShape.setSelected(true);
                    circleShape.setSelected(false);
                    squareShape.setSelected(false);
                }
            }
        });


        //uncheck all other radio buttons when the user checks a radio button
        circleShape.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (circleShape.isSelected()) {
                    engine.setShape(Engine.Shape.Circle);
                    squareShape.setSelected(false);
                    triangleShape.setSelected(false);
                    //engine.setFilter(Engine.Filter.NORMAL);
                }
                if (Main.DEBUG) {
                    System.out.println("CIRCLE SHAPE");
                    //System.out.println(engine.getFilter());
                }
            }
        });
        squareShape.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (squareShape.isSelected()) {
                    engine.setShape(Engine.Shape.Square);
                    circleShape.setSelected(false);
                    triangleShape.setSelected(false);
                    //engine.setFilter(Engine.Filter.SEPIA);
                }
                if (Main.DEBUG) {
                    System.out.println("SQUARE SHAPE");
                    //System.out.println(engine.getFilter());
                }
            }
        });
        triangleShape.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (triangleShape.isSelected()) {
                    engine.setShape(Engine.Shape.Triangle);
                    squareShape.setSelected(false);
                    circleShape.setSelected(false);
                    //engine.setFilter(Engine.Filter.NEGATIVE);
                }
                if (Main.DEBUG) {
                    System.out.println("TRIANGLE SHAPE");
                    //System.out.println(engine.getFilter());
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
        shapePanel.add(triangleShape);
        tweakablesPanel.add(shapePanel);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));

        tweakablesPanel.add(dotSizeText);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        tweakablesPanel.add(dotSize_slider);
        tweakablesPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        // tweakablesPanel.add(dotSize_value);

        // buttonsPanel.add(Box.createRigidArea(new Dimension(800, 10)));
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
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
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
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.stopTimer();
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
        window.setResizable(true);
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