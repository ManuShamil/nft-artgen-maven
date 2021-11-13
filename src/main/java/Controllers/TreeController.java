package Controllers;

import java.nio.file.Paths;
import java.nio.file.Path;

import Models.Enums.FileType;
import Models.Nodes.*;

public class TreeController {
    
    public static Node CreateTree( String directoryPath )
    {

        Path myPath = Paths.get( directoryPath );
        String baseName = myPath.getFileName().toString();

        Node baseNode = new Node( baseName );
        baseNode.SetFileType( FileType.FOLDER );
        baseNode.setFullPath( directoryPath );

        baseNode.branchNodes();



        return baseNode;
    }
}
