package fine.fractals.gpgpu;//package com.pieceofinfinity.fractal.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;

class GPUAbstract {

	private static final Logger log = LogManager.getLogger(GPUAbstract.class);

	protected cl_context context;
	protected cl_command_queue commandQueue;

	protected cl_program PROGRAM;
	protected cl_kernel KERNEL;
	protected cl_device_id device;

	protected void init() {

		final int platformIndex = 0;
		final int deviceIndex = 0;

		CL.setExceptionsEnabled(true);

		// Obtain the number of platforms
		int[] numPlatformsArray = new int[3];
		CL.clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Obtain a platform ID
		cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
		CL.clGetPlatformIDs(platforms.length, platforms, null);
		cl_platform_id platform = platforms[platformIndex];

		// Initialize the context properties
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);

		// Obtain the number of devices for the platform
		int[] numDevicesArray = new int[1];
		CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Obtain a device ID
		cl_device_id[] devices = new cl_device_id[numDevices];
		CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
		this.device = devices[deviceIndex];

		for (int i = 0; i < numDevices; i++) {
			String deviceName = getString(devices[i], CL.CL_DEVICE_NAME);
			log.debug("Device " + (i + 1) + " of " + numDevices + ": " + deviceName);
		}

		context = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);

		commandQueue = CL.clCreateCommandQueueWithProperties(context, device, null, null);

		 /*
		 GIGABYTE GTX 960 MINI Gaming 2GB
         GIGABYTE GT 730 Ultra Durable 2 2GB
         GIGABYTE GT 640 NVIDIA GeForce GT640
         */

		// final int cudaCount = 1024 + 384 + 384; // 1792


		final String PROGRAM_SOURCE_CODE =
				"inline int domainToScreenRe(__private double re) {\n"
						+ "     int x = (int) floor((" + RESOLUTION_WIDTH + " * (re - " + AreaMandelbrot.centerRe + ") / " + RESOLUTION_WIDTH + ") + " + (RESOLUTION_WIDTH / 2) + ");\n"
						+ "     if (x >= " + RESOLUTION_WIDTH + " ) {\n"
						+ "        x = " + (RESOLUTION_WIDTH - 1) + ";\n"
						+ "     }\n"
						+ "     if (x < 0) {\n"
						+ "        x = 0;\n"
						+ "     }\n"
						+ "     return x;\n"
						+ "}\n"
						+ "inline int domainToScreenIm(__private double im) {\n"
						+ "     int y = (int) floor((" + RESOLUTION_HEIGHT + " * (" + AreaMandelbrot.centerIm + " - im) / " + RESOLUTION_WIDTH + ") + " + (RESOLUTION_HEIGHT / 2) + ");\n"
						+ "     if (y >= " + RESOLUTION_HEIGHT + " ) {\n"
						+ "         y = " + (RESOLUTION_HEIGHT - 1) + ";\n"
						+ "     }\n"
						+ "     if (y < 0) {\n"
						+ "         y = 0;\n"
						+ "     }\n"
						+ "     return y;\n"
						+ "}\n"
						+ "__kernel void calculateFractalValues(\n"
						+ "             __global double *originre,\n"
						+ "             __global double *originim,\n"
						+ "             __global double *lastVisitedre,\n"
						+ "             __global double *lastVisitedim,\n"
						+ "             __global int *iterationsToRun) {\n"
						+ "     int gid = get_global_id(0);\n"
						+ "     double re = lastVisitedre[gid];\n"
						+ "     double im = lastVisitedim[gid];\n"
						+ "     int iterator = 0;\n"
						+ "     double reNew;\n"
						+ "     double ore = originre[gid];\n"
						+ "     double oim = originim[gid];\n"
						+ "     int itr = iterationsToRun[gid];\n"
						+ "     double tTemp;\n"
						+ "     double xTemp;\n"
						+ "     double re2 = re * re;\n"
						+ "     double im2 = im * im;\n"
						//@Formatter:off
						+ "     while ((" + GPUMath.quadrance + ") < 4  && iterator < itr) {\n"
						+ 					GPUMath.square
						+ 					GPUMath.plusOrigin
						+ "         iterator++;\n"
						+ "     }\n"
						//@Formatter:on
						//		Use iterationsToRun to return DivergedIn
						+ "     iterationsToRun[gid] = iterator;\n"
						//      To continue with calculation from here next time
						+ "     lastVisitedre[gid] = re;\n"
						+ "     lastVisitedim[gid] = im;\n"
						+ "}\n";

		log.debug("1");
		PROGRAM = CL.clCreateProgramWithSource(context, 1, new String[]{PROGRAM_SOURCE_CODE}, null, null);
		log.debug("2");
		CL.clBuildProgram(PROGRAM, 0, null, null, null, null);
		// int[] errorKernel = new int[100];
		log.debug("3");
		KERNEL = CL.clCreateKernel(PROGRAM, "calculateFractalValues", null);
		log.debug("4");
	}

	protected String kb(long bytes) {
		return ((long) Math.floor(bytes / 1024)) + " Kb";
	}

	/**
	 * Returns the value of the device info parameter with the given name
	 *
	 * @param device    The device
	 * @param paramName The parameter name
	 * @return The value
	 */
	private String getString(cl_device_id device, int paramName) {
		long[] size = new long[1];
		CL.clGetDeviceInfo(device, paramName, 0, null, size);
		byte[] buffer = new byte[(int) size[0]];
		CL.clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
		return new String(buffer, 0, buffer.length - 1);
	}
}
