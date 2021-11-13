package Models.Nodes;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import java.nio.file.Path;
import java.nio.file.Paths;


import Controllers.FileSystemController;
import Models.DirConfig.DirectoryConfig;
import Models.Enums.FileType;
import Models.Enums.NodeSelectionType;

public class Node extends BaseNode {

    FileType fileType;
    NodeSelectionType selectionType;
    float layerPriority;
    float weight;
    int depth;
    int childId;
    boolean isParentLayer;
    String layerName;

    public Node( String nodeName )
    {
        super(nodeName);

        this.layerPriority = 1.0f;
        this.weight = 1.0f;
        this.depth = 0;
        this.childId = 0;
        this.isParentLayer = false;
        this.parentNode = null;
        this.layerName = "";
    }

    public void SetFileType( FileType fileType )
    {
        this.fileType = fileType;
    }

    public FileType GetFileType()
    {
        return this.fileType;
    }

    public void SetSelectionType( NodeSelectionType selectionType )
    {
        this.selectionType = selectionType;
    }

    public void SetLayerPriority( float layerPriority )
    {
        this.layerPriority = layerPriority;
    }

    public NodeSelectionType GetSelectionType()
    {
        return this.selectionType;
    }

    public float GetLayerPriority()
    {
        return this.layerPriority;
    }

    public void SetDepth( int depth )
    {
        this.depth = depth;
    }

    public int GetDepth()
    {
        return this.depth;
    }

    public void SetWeight( float weight )
    {
        this.weight = weight;
    }

    public float GetWeight()
    {
        return this.weight;
    }

    public int GetChildId()
    {
        return this.childId;
    }

    public void SetChildId( int childId )
    {
        this.childId = childId;
    }

    public boolean IsParentLayer()
    {
        return this.isParentLayer;
    }

    public void SetIsParentLayer( boolean isParentLayer )
    {
        this.isParentLayer = isParentLayer;
    }

    public void SetLayerName( String layerName )
    {
        this.layerName = layerName;
    }

    public String GetLayerName()
    {
        return this.layerName;
    }

    @Override
    public void branchNodes()
    {
        
        Path myPath = Paths.get( this.getFullpath() );

        String config = FileSystemController.readFile( this.getFullpath(), "config.json");

        Gson gson = new Gson();
        DirectoryConfig dirConfig = gson.fromJson( config, DirectoryConfig.class);

        this.SetIsParentLayer( dirConfig.IsParentLayer() );
        this.SetLayerName(  dirConfig.GetLayerName() );


        ArrayList<String> folders = FileSystemController.GetFolders( myPath.toString() );
        for ( int i=0; i<folders.size(); i++)
        {
            String folderName = folders.get( i );

            Node newNode = new Node( folderName );
            newNode.SetParentNode( this );

            if ( dirConfig != null )
            {
                
                newNode.SetSelectionType( dirConfig.GetSelectionMode( folderName ) );
                newNode.SetLayerPriority( dirConfig.GetLayerPriority( folderName ) );

                if ( newNode.GetSelectionType() == NodeSelectionType.NONE )
                    return;
            }

            
            newNode.SetFileType( FileType.FOLDER );
            newNode.setFullPath( myPath.resolve( folderName ).toString() );
            newNode.SetDepth( this.GetDepth() + 1 );
            newNode.SetWeight( Node.GetWeightFromName( folderName ) );
            newNode.SetChildId( this.children.size() );
            

            // System.out.println( folderName + " -> " + newNode.GetSelectionType() + " -> " + newNode.GetLayerPriority() );
            // System.out.println( folderName + " -> " + newNode.GetDepth() + " -> " + newNode.GetWeight() );

            newNode.branchNodes();


            this.AddChild( newNode );
        }

        ArrayList<String> files = FileSystemController.GetFiles( myPath.toString() );
        for ( int i=0; i<files.size(); i++)
        {
            String fileName = files.get( i );
            Node newNode = new Node( fileName );
            newNode.SetParentNode( this );

            newNode.SetFileType( FileType.FILE );
            newNode.setFullPath( myPath.resolve( fileName ).toString() );
            newNode.SetDepth( this.GetDepth() + 1 );
            newNode.SetWeight( Node.GetWeightFromName( fileName ) );
            newNode.SetChildId( this.children.size() );

            //System.out.println( fileName );

            if ( dirConfig != null )
            {
                newNode.SetSelectionType( dirConfig.GetSelectionMode( fileName ) );
                newNode.SetLayerPriority( dirConfig.GetLayerPriority( fileName ) );
            }

            // System.out.println( fileName + " -> " + newNode.GetSelectionType() + " -> " + newNode.GetLayerPriority() );
            // System.out.println( fileName + " -> " + newNode.GetDepth() + " -> " + newNode.GetWeight()  );

            this.AddChild( newNode );
        }


        super.branchNodes(); //! to set branched to true
    }

    public static float GetWeightFromName( String folderName )
    {
        float weight = 1.0f;

        String[] fileParts = folderName.split("#");

        if ( fileParts.length <= 1 ) return weight;

        String weightString = fileParts[1];

        fileParts = weightString.split( Pattern.quote(".") );

        List<String> numParts = new ArrayList<String>();

        for ( int i=0; i<fileParts.length; i++)
        {
            String part = fileParts[i];

            Integer num;

            try
            {
                num = Integer.parseInt( part );
                numParts.add( Integer.toString(num) );
            }
            catch (NumberFormatException e)
            {
            }

        }

        String weightResultString = numParts.stream().collect( Collectors.joining(".") );
        float weightResult = 1.0f;

        try
        {
            weightResult = Float.parseFloat( weightResultString );
        } catch (NumberFormatException e)
        {
            weightResult = weight;
        }

        return weightResult;
    }
}
