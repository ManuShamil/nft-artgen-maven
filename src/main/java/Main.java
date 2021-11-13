import java.nio.file.Paths;
import java.util.*;

import Controllers.MetaDataController;
import Controllers.PrinterController;
import Controllers.TreeController;
import Models.MetaData.ProcessedMetaData;
import Models.Nodes.*;



public class Main 
{
	public static void main( String args[] )
	{
		try
		{
			String layersPath = args[0];
			String exportPath = args[1];
			String exportsDirectory = args[3];

			Integer expectedCount = 10;

			try {
				if ( args.length >= 3) expectedCount = Integer.parseInt( args[2] );
			} catch (Exception e) {}


			layersPath = Paths.get( layersPath ).toAbsolutePath().normalize().toString();
			exportPath = Paths.get( exportPath ).toAbsolutePath().normalize().toString();
			exportsDirectory = Paths.get( exportsDirectory ).toAbsolutePath().normalize().toString();

	
			Node tree = TreeController.CreateTree( layersPath );
	
			List<ProcessedMetaData> metaDataOutput = MetaDataController.GenerateMetaData(tree, expectedCount);
			//MetaDataController.ExportOutput( metaDataOutput, exportPath );
			PrinterController.PrintImages( metaDataOutput, exportsDirectory );

		} catch ( IndexOutOfBoundsException e )
		{
			System.out.println("Not enough parameters");
		}

	}
}
