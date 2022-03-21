import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FractalExplorer {
    private int dispSize;
    private JImageDisplay img;
    private FractalGenerator fr_gen;
    private Rectangle2D.Double rect;

    public FractalExplorer(int dispSize){
        this.dispSize = dispSize;
        this.fr_gen=new Mandelbrot();
        this.rect=new Rectangle2D.Double();
        fr_gen.getInitialRange(this.rect);
    }

    public void createAndShowGUI(){
        JFrame frame=new JFrame();
        img=new JImageDisplay(dispSize, dispSize);
        JButton button=new JButton();

        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        button.addActionListener(aHandler);
        img.addMouseListener(mHandler);

        frame.setLayout(new BorderLayout());
        frame.add(img, BorderLayout.CENTER);
        frame.add(img, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        for(int i=0;i<dispSize;i++)
            for(int j=0;j<dispSize;j++) {
                double xCoord = FractalGenerator.getCoord(rect.x, rect.x + rect.width, dispSize, i);
                double yCoord = FractalGenerator.getCoord(rect.y, rect.y + rect.height,dispSize, j);
                double numIt = fr_gen.numIterations(xCoord, yCoord);

                if(numIt == -1)
                    img.drawPixel(i, j, 0);
                else{
                    float hue = 0.7f + (float) numIt / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    img.drawPixel(i, j, rgbColor);
                }
            }
        img.repaint();
    }

    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fr_gen.getInitialRange(rect);
            drawFractal();
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
}
