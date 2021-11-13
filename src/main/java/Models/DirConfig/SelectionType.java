package Models.DirConfig;

import org.apache.commons.io.*;

import Models.Enums.NodeSelectionType;

public class SelectionType {
    String mode;
    String pattern;
    float index;

    private NodeSelectionType GetSelectionType()
    {
        switch( mode.toUpperCase() )
        {
            case "OR":
                return NodeSelectionType.OR;
            case "AND":
                return NodeSelectionType.AND;
            case "NONE":
                return NodeSelectionType.NONE;
        }

        return NodeSelectionType.NONE;
    }

    public NodeSelectionType GetSelectionMode( String fileName, NodeSelectionType selectionType )
    {
        if ( FilenameUtils.wildcardMatch( fileName, this.pattern, IOCase.INSENSITIVE ) )
            return GetSelectionType();

        return selectionType;
    }

    public float GetLayerPriority( String fileName, float priority )
    {
        if ( index != 0 && FilenameUtils.wildcardMatch( fileName, this.pattern, IOCase.INSENSITIVE ) )
            return index;

        return priority;
    }
}
