import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.nativelibs4java.util.Pair;

import Controllers.PrinterController;
import net.imagej.ops.Ops.Image;
import java.io.File;
import java.awt.image.BufferedImage;

public class ImageScale {
    public static void main( String args[] )
    {
        PrintImageSet();


        // String imagePath = "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/BLACK/Position none skin black #1.png";
        // int width = 6000;
        // int height = 6000;

        // try
        // {
        //     BufferedImage image = OpenImage( imagePath );
        //     BoxScaleImage( image, width, height);
        // } catch ( Exception e)
        // {
        //     System.out.println("Could not open Image");
        //     e.printStackTrace();
        // }  
    }

    public static void PrintImageSet()
    {
        long startTime = System.currentTimeMillis();

        String[] imagePaths = {
            "C:/Users/admin/Desktop/nft-artgen/layers/BACKGROUNDS/Background plain blue#7.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/Position none skin tan #1.PNG",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/TATTOOS/Tattoo beast iron.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/SCARS/None#40.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/WARPAINT/No warpaint#65.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/EXPRESSIONS/Face evil eye green skin tan#1.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/HAIR/GREEN/EYE/Eyebrow bored green#1.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/HAIR/GREEN/HEAD/hair lizard green#0.5.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/HAIR/GREEN/BEARD/Beard star green#5.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/ARMS/SINGLE ARM/WEAPON/Weapon laser gun green#10.png",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/ARMS/SINGLE ARM/Arm single arm tan.PNG",
            "C:/Users/admin/Desktop/nft-artgen/layers/BODY/NO ARMOR/TAN/EXTRAS/No extra#85.png"
        };

        List<BufferedImage> loadedImages = new ArrayList<BufferedImage>();

        for ( int i=0; i<imagePaths.length; i++)
        {
            try {
                System.out.println("Loading " + imagePaths[i] );
                loadedImages.add( OpenImage( imagePaths[i] ) );
            } catch (Exception e) {
                System.out.println("Could not load Image");
            }
            
        }

        int newWidth = 512;
        int newHeight = 512;

        int i = 0;
        for( BufferedImage loadedImage: loadedImages)
        {
            System.out.println("Resizing " + imagePaths[i]);
            loadedImage = NNIScaleImage( loadedImage, newWidth, newHeight);
            i++;
        }

        long endTime = System.currentTimeMillis();

        System.out.println( String.format( "Time elapsed: %d ms", ( endTime - startTime )) );
    }

    public static BufferedImage OpenImage( String imagePath ) throws IOException
    {

        BufferedImage image = ImageIO.read( new File( imagePath ) );

        return image;
    }

    private static double GetMeanValueFromBox( BufferedImage image, int xStart, int yStart, int xEnd, int yEnd)
    {
        List<Integer> boxValues = new ArrayList<Integer>();
        double meanValue;
        
        for ( int i=xStart; i<xEnd; i++)
        {
            for ( int j=yStart; j<yEnd; j++)
            {
                boxValues.add( image.getRGB(i, j) );
            }
        }

        meanValue = (double) boxValues.stream().mapToInt(a -> a).average().orElse(0.0);

        return meanValue;
    }

    public static void BoxScaleImage( BufferedImage image, int newWidth, int newHeight)
    {
        long startTime = System.currentTimeMillis();

        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();

        
        double scaleX = (double) newWidth/oldWidth;
        double scaleY = (double) newHeight/oldHeight;

        int boxWidth = (int) Math.ceil( 1/ scaleX );
        int boxHeight = (int) Math.ceil( 1/ scaleY );

        BufferedImage canvas = new BufferedImage( newWidth, newHeight, BufferedImage.TYPE_INT_ARGB );
        
        for( int i=0; i<newWidth; i++)
        {
            for ( int j=0; j<newHeight; j++)
            {

                int _xStart = (int) Math.floor( i / scaleX);
                int _yStart = (int) Math.floor( j / scaleY);

                int _xEnd = (int) Math.min( _xStart + boxWidth, oldWidth - 1);
                int _yEnd = (int) Math.min( _yStart + boxHeight, oldHeight - 1);

                int meanOfBox = (int) GetMeanValueFromBox(image, _xStart, _yStart, _xEnd, _yEnd);
                canvas.setRGB(i, j, meanOfBox);

           }
        }

        PrinterController.SaveImage( canvas, "C:/Users/admin/Desktop/hashlips_art_engine-main/test-scaled.png", startTime);
    }

    public static BufferedImage NNIScaleImage( BufferedImage image, int newWidth, int newHeight)
    {
        long startTime = System.currentTimeMillis();

        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();

        double scaleX = (double) newWidth/oldWidth;
        double scaleY = (double) newHeight/oldHeight;

        BufferedImage canvas = new BufferedImage( newWidth, newHeight, BufferedImage.TYPE_INT_ARGB );
        
        for( int i=0; i<newWidth; i++)
        {
            for ( int j=0; j<newHeight; j++)
            {
                int x = (int) Math.round( i / scaleX);
                int y = (int) Math.round( j / scaleY);

                //System.out.println( x + " " + y);

                int newRGB = image.getRGB(x, y);

                canvas.setRGB(i, j, newRGB);
            }
        }

        return canvas;

        //PrinterController.SaveImage( canvas, "C:/Users/admin/Desktop/hashlips_art_engine-main/test-scaled.png", startTime);
    }

}
