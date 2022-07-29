package fine.fractals.gpgpu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.Pointer;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import java.nio.ByteBuffer;

import static fine.fractals.gpgpu.GPU.GPU_COUNT;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_NAME;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_DEVICE_VERSION;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateCommandQueueWithProperties;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.setExceptionsEnabled;

abstract sealed class GPULow permits GPUHigh {

    private static final Logger log = LogManager.getLogger(GPULow.class);
    private static boolean first = true;
    final protected cl_context context;
    final protected cl_command_queue commandQueue;
    final protected cl_program PROGRAM;
    final protected cl_kernel KERNEL;
    final protected cl_device_id device;

    protected GPULow() {

        final int platformIndex = 0;
        final int deviceIndex = 0;

        setExceptionsEnabled(true);

        /* Obtain the number of platforms */
        int[] numPlatformsArray = new int[GPU_COUNT];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        log.debug("numPlatforms " + numPlatforms);

        /* Obtain a platform ID */
        cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        /* Initialize the context properties */
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        /* Obtain the number of devices for the platform */
        int[] numDevicesArray = new int[1];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        /* Obtain a org.bytedeco.librealsense.device ID */
        cl_device_id[] devices = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, numDevices, devices, null);
        this.device = devices[deviceIndex];

        if (first) {
            first = false;
            for (int i = 0; i < numDevices; i++) {
                log.debug("Device " + (i + 1) + " of " + numDevices + ": " + getDeviceString(devices[i], CL_DEVICE_NAME));
                log.debug("DEVICE_VERSION: " + getDeviceString(devices[i], CL_DEVICE_VERSION));
            }
        }

        final String CODE = GPUProgram.programSourceCode();
        log.debug(CODE);

        context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
        commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);
        PROGRAM = clCreateProgramWithSource(context, 1, new String[]{CODE}, null, null);
        clBuildProgram(PROGRAM, 0, null, null, null, null);
        KERNEL = clCreateKernel(PROGRAM, "calculateFractalValues", null);
    }

    private byte[] getDeviceInfo(cl_device_id device, int paramName) {
        long[] size = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);
        byte[] buffer = new byte[(int) size[0]];
        clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return buffer;
    }

    private String getDeviceString(cl_device_id device, int paramName) {
        byte[] buffer = getDeviceInfo(device, paramName);
        return new String(buffer, 0, buffer.length - 1);
    }

    private long getDeviceLong(cl_device_id device, int paramName) {
        byte[] buffer = getDeviceInfo(device, paramName);
        return ByteBuffer.wrap(buffer, 0, buffer.length).getLong();
    }

    protected String mb(long bytes) {
        return ((long) Math.floor(bytes / (double) (1024 * 1024))) + " MB";
    }
}
