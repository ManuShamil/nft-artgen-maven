package Models.Nodes;

import java.util.*;

import Models.Enums.FileType;
import Models.Enums.NodeSelectionType;
import Models.MetaData.MetaData;

public class SelectionNode {

    Node referenceNode;
    List<SelectionNode> children;
    
    public SelectionNode( Node referenceNode )
    {
        this.referenceNode = referenceNode;
        this.children = new ArrayList<SelectionNode>();
    }

    public Node GetReferenceNode()
    {
        return referenceNode;
    }

    public void branchNodes( Node baseNode )
    {
        List<Node> requiredChildren = new ArrayList<Node>();
        List<Node> randomChildren = new ArrayList<Node>();

        baseNode.children.forEach(
            (node) -> {

                if ( ( (Node) node).GetSelectionType() == NodeSelectionType.AND ) 
                    requiredChildren.add( (Node) node );
                else if ( ( (Node) node).GetSelectionType() == NodeSelectionType.OR )
                    randomChildren.add( (Node) node );
            }
        );

        List<SelectionNode> chosenChildren = new ArrayList<SelectionNode>();

        requiredChildren.forEach( (node) -> { chosenChildren.add( new SelectionNode( node ) ); });

        if ( randomChildren.size() > 0)
            chosenChildren.add( GetRandomChildWithWeight( randomChildren ) );

        this.children = chosenChildren;

        chosenChildren.forEach( (node) -> {
            if ( node.GetReferenceNode().GetFileType() == FileType.FOLDER )
                node.branchNodes( node.GetReferenceNode() );
        });

 
    }

    private SelectionNode GetRandomChildWithWeight( List<Node> randomChildren )
    {

        float totalWeight = 0f;

        for ( int i=0; i<randomChildren.size(); i++)
            totalWeight += randomChildren.get(i).GetWeight();
        
        double randomWeight = Math.floor( Math.random() * totalWeight );

        for ( int i=0; i<randomChildren.size(); i++ )
        {
            float nodeWeight = randomChildren.get(i).GetWeight();

            randomWeight -= nodeWeight;

            if ( randomWeight < 0 ) 
            {
                //System.out.println(" GetRandomChildWithWeight " + new SelectionNode(randomChildren.get(i)) );

                return new SelectionNode(randomChildren.get(i));
            }
        }




        return new SelectionNode(
            randomChildren.get( 
                (int) Math.floor( Math.random() * randomChildren.size() ) 
            )
        );
    }

    public void CreateMetaData( List<MetaData> metaOutput )
    {
        if ( this.referenceNode.GetFileType() == FileType.FILE 
                && this.referenceNode.GetParentNode() != null 
                    && ( (Node) this.referenceNode.GetParentNode()).IsParentLayer() 
                    )
        {


            String layerName = ((Node) this.referenceNode.GetParentNode()).GetLayerName();

            MetaData data = new MetaData(
                layerName, 
                this.referenceNode.GetNodeName(), 
                    this.referenceNode.GetLayerPriority(),
                        this.referenceNode.GetDepth(), 
                            this.referenceNode.GetChildId(), 
                                this.referenceNode.getFullpath(), 
                                    this.referenceNode.GetWeight()  );

            metaOutput.add( data ); 
            
            
        }

        this.children.forEach(
            (node) -> { node.CreateMetaData( metaOutput); }
        );
        
    }
}
