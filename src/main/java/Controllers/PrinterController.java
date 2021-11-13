package Controllers;

//import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import Models.MetaData.MetaData;
import Models.MetaData.ProcessedMetaData;
import Models.MetaData.ProcessedMetaEntry;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;

import net.haesleinhuepf.clij.CLIJ;


public class PrinterController {
    
    public static void PrintImages( List<ProcessedMetaData> data, String exportsDir )
    {

        FileSystemController.DeleteDirectory( exportsDir );
        FileSystemController.CreateDirectory( exportsDir );
        
        Integer printerHeader = 0;
        for ( ProcessedMetaData metaData : data )
        {
            PrintImage( metaData, printerHeader, exportsDir);

            printerHeader++;
        }

    }

    private static void PrintImage( ProcessedMetaData imageMeta, int printerHeader, String exportsDir)
    {
        
        long startTime = System.currentTimeMillis();

        int width = 500;
        int height = 500;

        BufferedImage canvas = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = canvas.getGraphics();

        List<ProcessedMetaEntry> sortedLayers = imageMeta.GetSortedLayers();

        for ( ProcessedMetaEntry metaData : sortedLayers )
        {
            try {
                BufferedImage layer = ImageIO.read( new File( metaData.GetFullPath() ) );

                if ( layer.getWidth() != width || layer.getHeight() != height)
                    layer = Scalr.resize( layer, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, height, Scalr.OP_ANTIALIAS);

                graphics.drawImage( layer, 0, 0, null);

            } catch (Exception e) {
                
            }
        }

        graphics.dispose();

        //! save image
        SaveImage( canvas, String.format("%s/%d.png", exportsDir, printerHeader), startTime );
    }

    public static void SaveImage( BufferedImage image, String path, long startTime )
    {

        try {

            ImageIO.write( image, "PNG", new File( path ));  
            
            long endTime = System.currentTimeMillis();


            System.out.println( String.format("Saved %s -> %f(s)", path, (endTime - startTime)/1000f ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
