package Models.MetaData;


public class ProcessedMetaEntry
{
    String layerName;
    String imageName;
    String fullPath;
    int layerOrder;

    public ProcessedMetaEntry( String layerName,
                                String imageName,
                                    String fullPath,
                                        int layerOrder)
    {
        this.layerName = layerName;
        this.imageName = imageName;
        this.fullPath = fullPath;
        this.layerOrder = layerOrder;
    }

    public Integer GetOrder()
    {
        return this.layerOrder;
    }

    public String GetFullPath()
    {
        return this.fullPath;
    }
}