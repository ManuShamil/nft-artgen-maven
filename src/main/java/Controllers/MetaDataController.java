package Controllers;

import Models.Nodes.Node;
import Models.Nodes.SelectionNode;

import java.util.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Models.MetaData.MetaData;
import Models.MetaData.ProcessedMetaData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetaDataController {

    public static final Integer FAIL_COUNT_MAX = 100;
    
    public static List<ProcessedMetaData> GenerateMetaData( Node tree, int count )
    {
        List<List<MetaData>> output = new ArrayList<List<MetaData>>();

        HashSet<String> existingUniqueIds = new HashSet<String>();

        Integer processedCount = 0;

        Integer failCount = 0;

        while ( processedCount < count )
        {
            List<MetaData> generatedDNA = MetaDataController.GenerateMetaData(tree);
            String uniqueId = GetUniqueId( generatedDNA );

            if ( failCount > FAIL_COUNT_MAX)
            {
                System.out.println( String.format("Not enough data sets available to generate %d meta datas", count) );
                break;
            }

            if ( existingUniqueIds.contains( uniqueId ) )
            {
                System.out.println( String.format("UniqueId: %s already exists", uniqueId) );
                failCount++;
                continue;
            }

            failCount = 0; //! reset attempts

            output.add( generatedDNA );
            existingUniqueIds.add( uniqueId );

            processedCount++;

            System.out.println( String.format("Generated %d/%d meta", processedCount, count));
        }

        List<ProcessedMetaData> processedMetaDataOutput = new ArrayList<ProcessedMetaData>();

        output.forEach( metaDatas -> {

            ProcessedMetaData processedMetaData = new ProcessedMetaData( metaDatas );
            processedMetaDataOutput.add( processedMetaData );
        });

        return processedMetaDataOutput;
    }

    private static List<MetaData> GenerateMetaData( Node tree )
    {
        SelectionNode selectionTree = SelectionController.CreateSelection( tree );

        List<MetaData> metaData = SelectionController.CreateMetaData( selectionTree );

        return metaData;
    }

    private static String GetUniqueId( List<MetaData> metaDatas )
    {
        String uniqueId = "";

        for ( MetaData metaData : metaDatas ) uniqueId += metaData.GetUniqueId();

        return uniqueId;
    }

    public static void ExportOutput( List<ProcessedMetaData> data, String outputPath )
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String metaDataJson = gson.toJson( data );

        try
        {
            File file = new File( outputPath );

            file.delete();
        } catch ( Exception e) { e.printStackTrace(); };

        try
        {
            FileWriter exporter = new FileWriter( outputPath );

            exporter.write( metaDataJson );
            exporter.close();

        } catch ( IOException e)
        {
            e.printStackTrace();
        }
    }
}
