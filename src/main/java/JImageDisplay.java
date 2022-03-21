import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;

public class JImageDisplay extends JComponent{
    private BufferedImage img;

    public JImageDisplay(int width, int height)
    {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    public void clearImage(){
        for(int i=0;i<img.getWidth();i++)
            for(int j=0;j<img.getHeight();j++)
                img.setRGB(i, j, 0);
    }

    public void drawPixel(int x, int y, int rgbColor){
        img.setRGB(x, y, rgbColor);
    }
}