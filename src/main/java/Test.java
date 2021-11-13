import java.io.IOException;
import java.nio.ByteOrder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bridj.Pointer;
import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLPlatform.ContextProperties;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.IOUtils;


public class Test {

    private static final int BUFFER_SIZE = 100 * 1024 * 100 * 2;

    public static void main( String args[] )
    {
        CPUBench();

        GPUBench();

    }

    public static void GPUBench()
    {
        List<CLDevice> devices = new ArrayList<>();
        for (CLPlatform platform : JavaCL.listPlatforms())
            for (CLDevice device : platform.listAllDevices(true))
            {
                if ( platform.getName().toUpperCase().contains("AMD"))
                    devices.add( device );
            }

        CLDevice selectedDevice = devices.get(0);

        CLContext context = JavaCL.createContext( 
                                Collections.<ContextProperties, Object> emptyMap(),
                                selectedDevice );

        try {    
            GPUBench( context );
        } catch (Exception e) {
            e.printStackTrace();
        }    

    }

    public static void GPUBench( CLContext context ) throws IOException
    {
        long timeStart = System.currentTimeMillis();

        CLQueue queue = context.createDefaultQueue();
        ByteOrder byteOrder = context.getByteOrder();

        Pointer<Double> inputArray1 = Pointer.allocateDoubles( BUFFER_SIZE );
        Pointer<Double> inputArray2 = Pointer.allocateDoubles( BUFFER_SIZE );

        CLBuffer<Double> in1 = context.createBuffer( Usage.InputOutput, inputArray1);
        CLBuffer<Double> in2 = context.createBuffer( Usage.InputOutput, inputArray2);

        String src = IOUtils.readTextClose(Test.class.getResourceAsStream("copy.cl"));

        CLProgram program = context.createProgram( src).build();
        CLKernel kernel = program.createKernel("copy", in1, in2, BUFFER_SIZE);

        CLEvent event = kernel.enqueueNDRange(queue, new int[] { BUFFER_SIZE });

        inputArray1 = in1.read(queue, event).order(byteOrder);
        inputArray2 = in2.read(queue, event).order(byteOrder);

        Pointer<Double> outputArray = Pointer.allocateDoubles( BUFFER_SIZE );
        CLBuffer<Double> out = context.createBuffer( Usage.Output, outputArray);
        
        CLKernel sumKernel = program.createKernel("sum", in1, in2, out, BUFFER_SIZE);
        CLEvent sumEvent = sumKernel.enqueueNDRange(queue, new int[] { BUFFER_SIZE });

        outputArray = out.read(queue, sumEvent).order( byteOrder );

        //System.out.println( outputArray.get( 1000 ) ) ;

        long endTime = System.currentTimeMillis();

        System.out.println( String.format("GPU Bench -> Time elapsed: %d ms - %s - (Items Processed: %s)",  endTime-timeStart,
            context.getPlatform().getName(), 
            NumberFormat.getNumberInstance(Locale.US).format( BUFFER_SIZE) ));

    
    }

    
    public static void CPUBench()
    {
        long timeStart = System.currentTimeMillis();

        List<Double> inputArray1 = new ArrayList<Double>();
        List<Double> inputArray2 = new ArrayList<Double>();

        List<Double> outputArray = new ArrayList<Double>();

        for( long i=0; i<BUFFER_SIZE; i++)
        {
            inputArray1.add( (double) i );
            inputArray2.add( (double) i );
        }

        for( long i=0; i<BUFFER_SIZE; i++)
        {
            outputArray.add( inputArray1.get( (int) i) + inputArray2.get((int) i) );
        }

        //System.out.println( outputArray.get(1000) );

        long endTime = System.currentTimeMillis();

        System.out.println( String.format("CPU Bench -> Time elapsed: %d ms (Items Processed: %s)",  endTime-timeStart, 
        NumberFormat.getNumberInstance(Locale.US).format( BUFFER_SIZE) ));
    }

}
