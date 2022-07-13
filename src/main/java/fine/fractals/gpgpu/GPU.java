//package fine.fractals.gpgpu;//package com.pieceofinfinity.fractal.engine;
//
//import fine.fractals.data.mandelbrot.MandelbrotElement;
//import org.jocl.CL;
//import org.jocl.Pointer;
//import org.jocl.Sizeof;
//import org.jocl.cl_mem;
//
//import java.util.ArrayList;
//
//import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;
//import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;
//import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
//import static org.jocl.CL.CL_MEM_READ_ONLY;
//import static org.jocl.CL.CL_MEM_READ_WRITE;
//import static org.jocl.CL.CL_TRUE;
//
//public class GPU extends GPUAbstract {
//
//    /* Maximum allowed GPU execution time 60s */
//    private final long EXECUTION_TIME_MAX = 60 * 1000;
//    private int SPLIT_TO = 1;
//
//
//    public void filter(ArrayList<ArrayList<MandelbrotElement>> chunks) {
//        for (ArrayList<MandelbrotElement> chunk : chunks) {
//            /* Size of work unit is decided in Mandelbrot */
//            final int calculationSize = chunk.size();
//            /* input values */
//            final double[] originRe = new double[calculationSize];
//            final double[] originIm = new double[calculationSize];
//            /* output values */
//            final ArrayList<int[]> pathRe = new ArrayList<>();
//            final ArrayList<int[]> pathIm = new ArrayList<>();
//
//            int gid = 0;
//            for (MandelbrotElement el : chunk) {
//                originRe[gid] = el.originRe;
//                originIm[gid] = el.originIm;
//                gid++;
//            }
//
//            final Pointer pointerOriginRe = Pointer.to(originRe);
//            final Pointer pointerOriginIm = Pointer.to(originIm);
//            final ArrayList<Pointer> pointerPathsRe = new ArrayList<>();
//            final ArrayList<Pointer> pointerPathsIm = new ArrayList<>();
//
//            for (int i = 0; i < originRe.length; i++) {
//                int[] pr = new int[ITERATION_MAX - ITERATION_min];
//                int[] pi = new int[ITERATION_MAX - ITERATION_min];
//                pointerPathsRe.add(Pointer.to(pr));
//                pointerPathsIm.add(Pointer.to(pi));
//            }
//
//            /* Allocate the memory objects for the input- and output data */
//            /* Origin used as calculation input */
//            int memOriginsSize = Sizeof.cl_double * calculationSize;
//
//            /* Origin used as calculation input */
//            final cl_mem memOriginReT = CL.clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginRe, null);
//            final cl_mem memOriginImX = CL.clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginIm, null);
//
//
//            /* Element used as calculation input for continuation of calculation */
//            /* And to return elements for continuation of calculation next time */
//            int memLastVisitedSize = Sizeof.cl_double * calculationSize;
//
//            final cl_mem memLastVisitedReT = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memLastVisitedSize, pointerLastVisitedRe, null);
//            /* Element used as calculation input for continuation of calculation */
//            /* And to return elements for continuation of calculation next time */
//            final cl_mem memLastVisitedImX = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memLastVisitedSize, pointerLastVisitedIm, null);
//
//            /* Iteration of calculation to run */
//            /* And to return when element diverged */
//            int memIterationsToRunSize = Sizeof.cl_int * calculationSize;
//            final cl_mem memIterationsToRun = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memIterationsToRunSize, pointerIterationsToRun, null);
//            // long totalMemory = memOriginReSize + memOriginImSize + memLastVisitedReSize + memLastVisitedImSize + memOriginDivergedInSize;
//
//            // Set the arguments for the kernel
//            CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memOriginReT));
//            CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memOriginImX));
//            CL.clSetKernelArg(kernel, 4, Sizeof.cl_mem, Pointer.to(memLastVisitedReT));
//            CL.clSetKernelArg(kernel, 5, Sizeof.cl_mem, Pointer.to(memLastVisitedImX));
//
//            CL.clSetKernelArg(kernel, 8, Sizeof.cl_mem, Pointer.to(memIterationsToRun));
//
//            final long[] global_work_size = new long[]{calculationSize};
//
//            // Execute the kernel
//            CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size, null, 0, null, null);
//            CL.clEnqueueReadBuffer(commandQueue, memLastVisitedReT, CL_TRUE, 0, memLastVisitedSize, pointerLastVisitedRe, 0, null, null);
//            CL.clEnqueueReadBuffer(commandQueue, memLastVisitedImX, CL_TRUE, 0, memLastVisitedSize, pointerLastVisitedIm, 0, null, null);
//            CL.clEnqueueReadBuffer(commandQueue, memIterationsToRun, CL_TRUE, 0, memIterationsToRunSize, pointerIterationsToRun, 0, null, null);
//
//
//            gid = 0;
//            int divergedIn;
//            int shouldDivergeIn;
//            for (Element el : part) {
//                divergedIn = iterationsToRun[gid];
//                shouldDivergeIn = Application.iteration_Max_Full;
//                /* Elements with paths for Design */
//                if (divergedIn < shouldDivergeIn) {
//                    if (divergedIn > Application.iterationMinGet()) {
//                        cd.domainGPUFiltered.add(el);
//                    }
//                    /* Element diverged */
//                    el.setValue(divergedIn);
//                    /* Don't set last iteration; I will need test if it was 0 bellow. It is set in last else */
//                    /* This state may latter change to hibernatedFinishedInside */
//                    el.setHibernatedFinished();
//                    cd.newDiverged++;
//                    cd.newDivergedNow++;
//                    if (divergedIn > cd.newMax) {
//                        cd.newMax = divergedIn;
//                    }
//                    if (divergedIn < cd.newMin) {
//                        cd.newMin = divergedIn;
//                    }
//                } else {
//                    /* Element didn't diverge yet */
//                    el.setLastVisited(lastVisitedRe[gid], lastVisitedIm[gid]);
//                    /* Don't set Value. It would mess up black elements inside */
//                    el.setLastIteration(divergedIn);
//                    el.setBlack();
//                }
//                gid++;
//            }
//            // Release memory objects
//            CL.clReleaseMemObject(memOriginReT);
//            CL.clReleaseMemObject(memOriginImX);
//            CL.clReleaseMemObject(memLastVisitedReT);
//            CL.clReleaseMemObject(memLastVisitedImX);
//
//            CL.clReleaseMemObject(memIterationsToRun);
//
//            /* Don't release these if GPU use is repeated */
//            // CL.clReleaseKernel(kernel);
//            // CL.clReleaseProgram(program);
//            // CL.clReleaseCommandQueue(commandQueue);
//            // CL.clReleaseContext(context);
//            // CL.clReleaseDevice(device);
//            return cd;
//        }
//    }
//}
