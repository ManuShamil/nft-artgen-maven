package Models.DirConfig;

import java.util.List;

import Models.Enums.NodeSelectionType;

import java.util.ArrayList;


public class DirectoryConfig {
    String layerName;

    List<SelectionType> selectionType;

    public DirectoryConfig()
    {
        selectionType = new ArrayList<SelectionType>();
    }

    public boolean IsParentLayer()
    {
        if ( layerName == null)
            return false;

        return layerName != "";
    }

    public String GetLayerName()
    {
        return this.layerName;
    }

    public NodeSelectionType GetSelectionMode( String fileName )
    {
        NodeSelectionType type = NodeSelectionType.NONE;

        for ( int i=0; i<this.selectionType.size(); i++)
            type = this.selectionType.get(i).GetSelectionMode( fileName, type );
        
        return type;
    }

    public float GetLayerPriority( String fileName )
    {
        float priority = 1;

        for ( int i=0; i<this.selectionType.size(); i++)
            priority = this.selectionType.get(i).GetLayerPriority( fileName, priority );


        return priority;
    }
}
