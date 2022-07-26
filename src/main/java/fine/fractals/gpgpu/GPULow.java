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

import static fine.fractals.gpgpu.GPU.GPU_COUNT;
import static fine.fractals.gpgpu.GPUProgram.PROGRAM_SOURCE_CODE;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_NAME;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
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

        for (int i = 0; i < numDevices; i++) {
            log.debug("Device " + (i + 1) + " of " + numDevices + ": " + getDeviceName(devices[i]));
        }

        context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
        commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);
        PROGRAM = clCreateProgramWithSource(context, 1, new String[]{PROGRAM_SOURCE_CODE}, null, null);
        clBuildProgram(PROGRAM, 0, null, null, null, null);
        KERNEL = clCreateKernel(PROGRAM, "calculateFractalValues", null);
    }

    private String getDeviceName(cl_device_id device) {
        long[] size = new long[1];
        clGetDeviceInfo(device, CL_DEVICE_NAME, 0, null, size);
        byte[] buffer = new byte[(int) size[0]];
        clGetDeviceInfo(device, CL_DEVICE_NAME, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }
}
