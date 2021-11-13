package Models.MetaData;

public class MetaData {

    public String layerName;
    public String fileName;

    public float priority;
    public int depth;
    public int childId;

    public String fullPath;

    public float weight;

    public String uniqueId;

    public MetaData( String layerName, 
                        String fileName, 
                            float priority,
                                int depth,
                                    int childId,
                                        String fullPath,
                                            float weight )
    {
        this.layerName = layerName;
        this.fileName = fileName;
        this.priority = priority;
        this.depth = depth;
        this.childId = childId;
        this.fullPath = fullPath;
        this.weight = weight;

        this.uniqueId = String.format("%d%d", this.depth, this.childId);
    }

    public String GetUniqueId()
    {
        return this.uniqueId;
    }
}
