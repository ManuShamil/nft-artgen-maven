package Models.MetaData;

import java.util.*;
import java.util.Map.Entry;

public class ProcessedMetaData {
    
    String uniqueId;
    Map<String, List<ProcessedMetaEntry>> layers;

    transient List<MetaData> metaDatas;
    transient List<Integer> metaDataLayerOrders;

    public ProcessedMetaData( List<MetaData> metaDatas )
    {
        this.metaDatas = metaDatas;
        this.layers = new HashMap<String, List<ProcessedMetaEntry>>();
        this.metaDataLayerOrders = new ArrayList<Integer>();

        this.GenerateUniqueId();
        this.ProcessMetaDatas();
    }

    private void GenerateUniqueId()
    {
        this.uniqueId = "";

        metaDatas.forEach( ( metaData ) -> {
            this.uniqueId += metaData.GetUniqueId();
        });
    }

    private void ProcessMetaDatas()
    {
        List<Float> priorityArray = new ArrayList< Float >();

        this.metaDatas.forEach( metaData -> {
            priorityArray.add( metaData.priority );
        });

        /**
         * sort layers using priority
         */
        for( int i=0; i<priorityArray.size(); i++) metaDataLayerOrders.add(0);

        Map<Integer, Float> indexMap = new HashMap< Integer, Float>();
        
        for( int i=0; i<priorityArray.size(); i++) indexMap.put( i, priorityArray.get(i) );

        Map<Integer, Float> sortedIndexMap = ProcessedMetaData.sortByValue( indexMap );

        int i = 0;
        for( Entry<Integer, Float> entry: sortedIndexMap.entrySet() )
        {
            Integer index = entry.getKey();
            
            metaDataLayerOrders.set(index, i++);
        }

        /**
         * convert metadatas to map
         */
        for ( int j=0; j<this.metaDatas.size(); j++ )
        {
            MetaData metaData = this.metaDatas.get( j );
            String layerName = metaData.layerName;
            Integer layerOrder = this.metaDataLayerOrders.get( j );

            List<ProcessedMetaEntry> curLayers = this.layers.get( layerName );

            if ( curLayers == null ) curLayers = new ArrayList<ProcessedMetaEntry>();
                
            curLayers.add( new ProcessedMetaEntry( layerName, metaData.fileName, metaData.fullPath, layerOrder) );

            this.layers.put( layerName, curLayers);
        }

    }

    private static ProcessedMetaEntry GetLayerbyOrder( List<ProcessedMetaEntry> metaEntries, Integer order )
    {
        for ( ProcessedMetaEntry entry : metaEntries )
        {
            if ( entry.GetOrder() == order)
                return entry;
        }

        return null;
    }

    public List<ProcessedMetaEntry> GetSortedLayers()
    {
        List<ProcessedMetaEntry> unsortedLayers = new ArrayList<ProcessedMetaEntry>();
        List<ProcessedMetaEntry> sortedLayers = new ArrayList<ProcessedMetaEntry>();

        for ( Entry<String, List<ProcessedMetaEntry>> entry : this.layers.entrySet() )
            for ( ProcessedMetaEntry meta : entry.getValue() ) unsortedLayers.add( meta );

        for ( int i=0; i<unsortedLayers.size(); i++)
        {
            ProcessedMetaEntry entry = GetLayerbyOrder( unsortedLayers, i);
            sortedLayers.add(i, entry);
        }

        return sortedLayers;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
