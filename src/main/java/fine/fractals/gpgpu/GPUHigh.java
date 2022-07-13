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

public class GPUHigh extends GPULow {


    public void compute(ArrayList<ArrayList<MandelbrotElement>> chunks, ArrayList<ArrayList<double[]>> paths) {

        init();

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

            final int[] iterator = new int[calculationSize];
            final int[] length = new int[calculationSize];
            /* these paths have to be double, because they will be remembered and their elements moved across the pixels with zoom */
            final ArrayList<double[]> pathsRe = new ArrayList<>();
            final ArrayList<double[]> pathsIm = new ArrayList<>();
            final ArrayList<cl_mem> memPathsRe = new ArrayList<>();
            final ArrayList<cl_mem> memPathsIm = new ArrayList<>();

            final int memPathsSize = Sizeof.cl_double * calculationSize;
            final int memStateSize = Sizeof.cl_int * calculationSize;
            final int memLengthSize = Sizeof.cl_int * calculationSize;

            final Pointer pointerIterator = Pointer.to(iterator);
            final Pointer pointerLength = Pointer.to(length);
            final cl_mem memIterator = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memStateSize, pointerIterator, null);
            final cl_mem memLength = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memLengthSize, pointerLength, null);

            CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memIterator));
            CL.clEnqueueReadBuffer(commandQueue, memIterator, CL_TRUE, 0, memStateSize, pointerIterator, 0, null, null);
            CL.clEnqueueReadBuffer(commandQueue, memLength, CL_TRUE, 0, memLengthSize, pointerLength, 0, null, null);

            for (int i = 0; i < calculationSize; i++) {
                final double[] pathRe = new double[ITERATION_MAX];
                final double[] pathIm = new double[ITERATION_MAX];

                final Pointer pointerPathRe = Pointer.to(pathRe);
                final Pointer pointerPathIm = Pointer.to(pathIm);

                final cl_mem memPathRe = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, pointerPathRe, null);
                final cl_mem memPathIm = CL.clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, pointerPathIm, null);

                pathsRe.add(pathRe);
                pathsIm.add(pathIm);

                memPathsRe.add(memPathRe);
                memPathsIm.add(memPathIm);

                CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memPathRe));
                CL.clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, Pointer.to(memPathIm));
                CL.clEnqueueReadBuffer(commandQueue, memPathRe, CL_TRUE, 0, memPathsSize, pointerPathRe, 0, null, null);
                CL.clEnqueueReadBuffer(commandQueue, memPathIm, CL_TRUE, 0, memPathsSize, pointerPathIm, 0, null, null);
            }

            /*
             * Read calculation result data
             */

            gid = 0;
            for (MandelbrotElement el : chunk) {

                int l = length[gid];
                el.setFinishedState(iterator[gid], l);

                /*
                 * read calculation path
                 */
                if (l >= ITERATION_min) {
                    final double[] pathRe = pathsRe.get(gid);
                    final double[] pathIm = pathsIm.get(gid);
                    final ArrayList<double[]> path = new ArrayList<>();

                    for (int i = 0; i < l; i++) {
                        double re = pathRe[i];
                        double im = pathIm[i];
                        path.add(new double[]{re, im});
                    }
                    paths.add(path);
                }

                gid++;
            }

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
