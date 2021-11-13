package Models.Nodes;

import java.util.*;

public class BaseNode {

    String nodeName;
    String fullPath;

    boolean isBranched;

    ArrayList<BaseNode> children;
    BaseNode parentNode;

    public BaseNode( String nodeName )
    {
        this.nodeName = nodeName;
        this.isBranched = false;
        this.children = new ArrayList<BaseNode>();
        this.parentNode = null;
    }

    public String GetNodeName()
    {
        return nodeName;
    }

    public String getFullpath()
    {
        return fullPath;
    }

    public void setFullPath( String fullPath )
    {
        this.fullPath = fullPath;

        //System.out.println( fullPath );
    }

    public boolean isBranched()
    {
        return this.isBranched;
    }

    public void branchNodes()
    {
        this.isBranched = true;
    }

    public void AddChild( BaseNode node )
    {
        this.children.add( node );
    }

    public void SetParentNode( BaseNode node )
    {
        this.parentNode = node;
    }

    public BaseNode GetParentNode()
    {
        return this.parentNode;
    }

}