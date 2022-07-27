package fine.fractals.gpgpu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.*;

import java.nio.ByteBuffer;

import static fine.fractals.gpgpu.GPU.GPU_COUNT;
import static fine.fractals.gpgpu.GPUProgram.PROGRAM_SOURCE_CODE;
import static org.jocl.CL.*;

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

        // Obtain the number of platforms
        int[] numPlatformsArray = new int[GPU_COUNT];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        log.info("numPlatforms " + numPlatforms);

        // Obtain a platform ID
        cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];
        log.info("platform " + platform);

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int[] numDevicesArray = new int[1];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id[] devices = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, numDevices, devices, null);
        this.device = devices[deviceIndex];

        if (first) {
            first = false;
            for (int i = 0; i < numDevices; i++) {
                log.info("Device " + (i + 1) + " of " + numDevices + ": " + getDeviceString(devices[i], CL_DEVICE_NAME));
                log.info("DEVICE_VERSION: " + getDeviceString(devices[i], CL_DEVICE_VERSION));
                log.info("MAX_CONSTANT_BUFFER_SIZE: " + kb(getDeviceLong(devices[i], CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE)));
            }
            log.info(PROGRAM_SOURCE_CODE);
        }

        context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
        commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);
        PROGRAM = clCreateProgramWithSource(context, 1, new String[]{PROGRAM_SOURCE_CODE}, null, null);
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

    protected String kb(long bytes) {
        return ((long) Math.floor(bytes / (double) (1024 * 1024))) + " MB";
    }
}
