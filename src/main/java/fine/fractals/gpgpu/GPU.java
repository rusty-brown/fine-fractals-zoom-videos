package fine.fractals.gpgpu;//package com.pieceofinfinity.fractal.engine;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;

public class GPU extends GPUAbstract {

    public void filter(ArrayList<ArrayList<MandelbrotElement>> chunks) {
        for (ArrayList<MandelbrotElement> chunk : chunks) {

            final int calculationSize = chunk.size();

            /*
             * Input data
             */

            final double[] originRe = new double[calculationSize];
            final double[] originIm = new double[calculationSize];
            final Pointer pointerOriginRe = Pointer.to(originRe);
            final Pointer pointerOriginIm = Pointer.to(originIm);

            final int memOriginsSize = Sizeof.cl_double * calculationSize;
            final cl_mem memOriginRe = CL.clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginRe, null);
            final cl_mem memOriginIm = CL.clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginIm, null);

            int gid = 0;
            for (MandelbrotElement el : chunk) {
                originRe[gid] = el.originRe;
                originIm[gid] = el.originIm;
                gid++;
            }

            int kid = 0;
            CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memOriginRe));
            CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memOriginIm));

            final long[] global_work_size = new long[]{calculationSize};

            /*
             * Kernel program
             */

            CL.clEnqueueNDRangeKernel(commandQueue, KERNEL, 1, null, global_work_size, null, 0, null, null);

            /*
             * Output data
             */

            final ArrayList<int[]> pathsRe = new ArrayList<>();
            final ArrayList<int[]> pathsIm = new ArrayList<>();
            final ArrayList<Pointer> pointerPathsRe = new ArrayList<>();
            final ArrayList<Pointer> pointerPathsIm = new ArrayList<>();
            final ArrayList<cl_mem> memPathsRe = new ArrayList<>();
            final ArrayList<cl_mem> memPathsIm = new ArrayList<>();

            final int memPathsSize = Sizeof.cl_int * calculationSize;

            for (int i = 0; i < originRe.length; i++) {
                final int[] pathRe = new int[ITERATION_MAX - ITERATION_min];
                final int[] pathIm = new int[ITERATION_MAX - ITERATION_min];
                final Pointer pointerPathRe = Pointer.to(pathRe);
                final Pointer pointerPathIm = Pointer.to(pathIm);
                final cl_mem memPathRe = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, Pointer.to(pathRe), null);
                final cl_mem memPathIm = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, Pointer.to(pathIm), null);

                memPathsRe.add(memPathRe);
                memPathsIm.add(memPathIm);

                CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memPathRe));
                CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memPathIm));

                CL.clEnqueueReadBuffer(commandQueue, memPathRe, CL_TRUE, 0, memPathsSize, pointerPathRe, 0, null, null);
                CL.clEnqueueReadBuffer(commandQueue, memPathIm, CL_TRUE, 0, memPathsSize, pointerPathIm, 0, null, null);
            }

            /*
             * Execute calculation with data
             */

            gid = 0;
            int divergedIn;
            int shouldDivergeIn;

//            for (MandelbrotElement el : chunk) {
//                divergedIn = pathRe[gid];
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
//                    el.setFinishedState(divergedIn);
//                    el.setBlack();
//                }
//                gid++;
//            }

            // Release memory objects
            CL.clReleaseMemObject(memOriginRe);
            CL.clReleaseMemObject(memOriginIm);
            for (cl_mem memPathRe : memPathsRe) {
                CL.clReleaseMemObject(memPathRe);
            }
            for (cl_mem memPathIm : memPathsIm) {
                CL.clReleaseMemObject(memPathIm);
            }

            /* TODO Don't release these if GPU use is repeated */
            CL.clReleaseKernel(KERNEL);
            CL.clReleaseProgram(PROGRAM);
            CL.clReleaseCommandQueue(commandQueue);
            CL.clReleaseContext(context);
            CL.clReleaseDevice(device);
        }
    }
}
