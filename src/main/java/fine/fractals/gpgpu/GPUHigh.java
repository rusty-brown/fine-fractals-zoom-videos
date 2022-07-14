package fine.fractals.gpgpu;//package com.pieceofinfinity.fractal.engine;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clSetKernelArg;
import static org.jocl.Pointer.to;
import static org.jocl.Sizeof.cl_double;
import static org.jocl.Sizeof.cl_int;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
            final Pointer pointerOriginRe = to(originRe);
            final Pointer pointerOriginIm = to(originIm);

            final int memOriginsSize = cl_double * calculationSize;
            final cl_mem memOriginRe = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginRe, null);
            final cl_mem memOriginIm = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, memOriginsSize, pointerOriginIm, null);

            /*
             * initiate input data
             */

            int gid = 0;
            for (MandelbrotElement el : chunk) {
                originRe[gid] = el.originRe;
                originIm[gid] = el.originIm;
                gid++;
            }

            int kid = 0;
            clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, to(memOriginRe));
            clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, to(memOriginIm));

            final long[] global_work_size = new long[]{calculationSize};

            /*
             * Output data
             */


            final int[] iterator = new int[calculationSize]; /* amount of iteration before path diverged */
            final int[] length = new int[calculationSize];   /* calculation path length, only elements which hit Area Finebrot */
            final int[] from = new int[calculationSize];     /* path index from */
            final int[] to = new int[calculationSize];       /* path index to */

            /*
             * There was at most 9.48 x more elements in paths then total new elements calculated
             * Even for High ITERATION_MAX only few points hits Finebrot Area.
             * (That is why ITERATION_MAX was increased in the first place)
             */
            final int expectedCombinedPathsLength = calculationSize * 11;
            final double[] pathRe = new double[expectedCombinedPathsLength];
            final double[] pathIm = new double[expectedCombinedPathsLength];

            final int memIndexSize = cl_int * calculationSize;
            final int memPathsSize = cl_double * expectedCombinedPathsLength;

            final Pointer pointerIterator = to(iterator);
            final Pointer pointerLength = to(length);
            final Pointer pointerFrom = to(from);
            final Pointer pointerTo = to(to);
            final Pointer pointerPathRe = to(pathRe);
            final Pointer pointerPathIm = to(pathIm);

            final cl_mem memIterator = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memIndexSize, pointerIterator, null);
            final cl_mem memLength = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memIndexSize, pointerLength, null);
            final cl_mem memFrom = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memIndexSize, pointerFrom, null);
            final cl_mem memTo = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memIndexSize, pointerTo, null);
            final cl_mem memPathRe = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, pointerPathRe, null);
            final cl_mem memPathIm = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, memPathsSize, pointerPathIm, null);

            clEnqueueNDRangeKernel(commandQueue, KERNEL, 1, null, global_work_size, null, 0, null, null);

            clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, to(memIterator));
            clEnqueueReadBuffer(commandQueue, memIterator, CL_TRUE, 0, memIndexSize, pointerIterator, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memLength, CL_TRUE, 0, memIndexSize, pointerLength, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memFrom, CL_TRUE, 0, memIndexSize, pointerFrom, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memTo, CL_TRUE, 0, memIndexSize, pointerTo, 0, null, null);

            clSetKernelArg(KERNEL, kid++, Sizeof.cl_mem, to(memPathRe));
            clSetKernelArg(KERNEL, kid, Sizeof.cl_mem, to(memPathIm));
            clEnqueueReadBuffer(commandQueue, memPathRe, CL_TRUE, 0, memPathsSize, pointerPathRe, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memPathIm, CL_TRUE, 0, memPathsSize, pointerPathIm, 0, null, null);

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
                    int f = from[gid];
                    int t = to[gid];
                    assertNotEquals(0, t);
                    assertNotEquals(0, f);
                    assertEquals(t - f, l);
                    final ArrayList<double[]> path = new ArrayList<>();

                    for (int i = f; i <= t; i++) {
                        path.add(new double[]{pathRe[i], pathIm[i]});
                    }
                    paths.add(path);
                }

                gid++;
            }

            /*
             * Release memory objects
             */
            CL.clReleaseMemObject(memOriginRe);
            CL.clReleaseMemObject(memOriginIm);
            CL.clReleaseMemObject(memIterator);
            CL.clReleaseMemObject(memLength);
            CL.clReleaseMemObject(memFrom);
            CL.clReleaseMemObject(memTo);
            CL.clReleaseMemObject(memPathRe);
            CL.clReleaseMemObject(memPathIm);
        }

        /*
         * Release kernel objects
         */
        CL.clReleaseKernel(KERNEL);
        CL.clReleaseProgram(PROGRAM);
        CL.clReleaseCommandQueue(commandQueue);
        CL.clReleaseContext(context);
        CL.clReleaseDevice(device);
    }
}
