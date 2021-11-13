package Controllers;

import Models.MetaData.MetaData;
import Models.Nodes.Node;
import Models.Nodes.SelectionNode;

import java.util.*;


public class SelectionController {
    
    public static SelectionNode CreateSelection ( Node baseNode )
    {
        SelectionNode selectionNode = new SelectionNode( baseNode );

        selectionNode.branchNodes( baseNode );

        return selectionNode;
    }

    public static List<MetaData> CreateMetaData( SelectionNode baseNode )
    {
        List<MetaData> output = new ArrayList<MetaData>();

        baseNode.CreateMetaData( output );

        return output;
    }
}
