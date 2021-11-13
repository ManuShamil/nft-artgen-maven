package Controllers;

import java.util.*;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;


public class FileSystemController {

    static final String[] ignoreFiles = { "*.json", "*.bat" };

    public static ArrayList<String> GetFolders( String directoryName )
    {
    	ArrayList<String> myList = new ArrayList<String>();

        File directory = new File( directoryName );

        String[] myFiles = directory.list( new FilenameFilter() {
            public boolean accept( File file, String fileName )
            {
                return new File( file, fileName ).isDirectory();
            }
        });

        myList = new ArrayList<String>(Arrays.asList( myFiles ));
    	
    	return myList;
    }

    public static ArrayList<String> GetFiles( String directoryName )
    {
    	ArrayList<String> myList = new ArrayList<String>();

        File directory = new File( directoryName );

        String[] myFiles = directory.list( new FilenameFilter() {
            public boolean accept( File file, String fileName )
            {
                if ( regexMatch( fileName ) )
                    return false;

                return new File( file, fileName ).isFile();
            }
        });

        myList = new ArrayList<String>(Arrays.asList( myFiles ));
    	
    	return myList;
    }

    private static boolean regexMatch( String fileName )
    {
   
        for( int i=0; i<ignoreFiles.length; i++ )
        {
            String wildCard = ignoreFiles[i];
            String regex = ("\\Q" + wildCard + "\\E").replace("*", "\\E.*\\Q");

            if ( fileName.matches( regex ) )
                return true;
        }

        return false;
    }

    public static String readFile( String directory, String fileName )
    {
        String contents = "";

        File myFile = GetFile( directory, fileName );

        try {
            Scanner scanner = new Scanner( myFile );

            while( scanner.hasNext() )
                contents += scanner.next();
            
            scanner.close();
        } catch (Exception e) {
            contents = "";
        }


        return contents;
    }

    public static File GetFile( String directory, String fileName )
    {

        File myFile = new File( directory, fileName );

        return myFile;
    }

    public static boolean DeleteDirectory( String directory )
    {

        try
        {
            File file = new File( directory );
            FileUtils.deleteDirectory( file );
        } catch ( Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean CreateDirectory( String directory )
    {
        try
        {
            new File( directory ).mkdirs();
            
        } catch ( Exception e )
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
