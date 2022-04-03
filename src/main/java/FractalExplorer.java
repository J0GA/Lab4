import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class FractalExplorer {
    private int dispSize;
    private JImageDisplay img;
    private FractalGenerator fr_gen;
    private Rectangle2D.Double rect;
    JComboBox<FractalGenerator> fractalCBox;
    int rowsRem;
    JButton s_button;
    JButton r_button;

    public FractalExplorer(int dispSize){
        this.dispSize = dispSize;
        this.fr_gen=new Mandelbrot();
        this.rect=new Rectangle2D.Double();
        fr_gen.getInitialRange(this.rect);
    }

    public void createAndShowGUI(){
        JFrame frame=new JFrame();
        img=new JImageDisplay(dispSize, dispSize);
        r_button=new JButton();
        r_button.setActionCommand("Reset");
        s_button=new JButton();
        s_button.setActionCommand("Save");
        JLabel label = new JLabel("Fractal: ");
        fractalCBox	= new JComboBox<FractalGenerator>();
        JPanel cbPanel = new JPanel();
        cbPanel.add(label);
        cbPanel.add(fractalCBox);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(s_button);
        buttonPanel.add(r_button);
        fractalCBox.addItem(new Mandelbrot());
        fractalCBox.addItem(new BurningShip());
        fractalCBox.addItem(new Tricorn());

        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        r_button.addActionListener(aHandler);
        s_button.addActionListener(aHandler);
        img.addMouseListener(mHandler);
        fractalCBox.addActionListener(aHandler);

        frame.setLayout(new BorderLayout());
        frame.add(img, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(cbPanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        enableUI(false);

        rowsRem = dispSize;
        for (int i = 0; i < dispSize; i++) {
            FractalWorker rowDrawer = new FractalWorker(i);
            rowDrawer.execute();
        }
    }

    public void enableUI(boolean val) {
        s_button.setEnabled(val);
        r_button.setEnabled(val);
        fractalCBox.setEnabled(val);
    }



    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "Reset") {
                fr_gen.getInitialRange(rect);
                drawFractal();
            }
            else if (e.getActionCommand() == "Save") {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int res = fileChooser.showSaveDialog(img);

                if (res == JFileChooser.APPROVE_OPTION) {
                    try {
                        javax.imageio.ImageIO.write(img.getBufferedImage(),
                                "png", fileChooser.getSelectedFile());
                    } catch (NullPointerException | IOException e1) {
                        javax.swing.JOptionPane.showMessageDialog(img, e1.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    return;
                }
            }
            else if (e.getSource() == (Object) fractalCBox) {
                fr_gen = (FractalGenerator) fractalCBox.getSelectedItem();
                fr_gen.getInitialRange(rect);
                drawFractal();
            }
        }
    }

    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = FractalGenerator.getCoord(rect.x, rect.x + rect.width, dispSize, e.getX());
            double yCoord = FractalGenerator.getCoord(rect.y, rect.y + rect.width, dispSize, e.getY());
            fr_gen.recenterAndZoomRange(rect, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args){
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }

    private class FractalWorker extends SwingWorker<Object, Object>{
        int y;
        int[] rgbVals;

        public FractalWorker(int yCoord) {
            y = yCoord;
        }

        public Object doInBackground(){
            rgbVals = new int[dispSize];
            double yCoord = FractalGenerator.getCoord(rect.y, rect.y + rect.height,dispSize, this.y);
            for(int i=0;i<dispSize;i++) {
                    double xCoord = FractalGenerator.getCoord(rect.x, rect.x + rect.width, dispSize, i);
                    double numIt = fr_gen.numIterations(xCoord, yCoord);

                    if(numIt == -1)
                        rgbVals[i] = 0;
                    else{
                        float hue = 0.7f + (float) numIt / 200f;
                        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                        rgbVals[i] = rgbColor;
                    }
                }
            return null;
        }

        public void done() {
            for (int i = 0; i < dispSize; i++) {
                img.drawPixel(i, y, rgbVals[i]);
            }
            img.repaint(0, 0, y, dispSize, 1);

            rowsRem -= 1;
            if (rowsRem == 0) {
                enableUI(true);
            }
        }
    }
}


