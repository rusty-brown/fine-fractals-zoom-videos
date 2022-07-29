package fine.fractals.gpgpu;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.CALCULATION_BOUNDARY;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;

public abstract class GPUProgram {

    public static String programSourceCode() {
        return "__global int START;"
                + "\n"
                + "__kernel void calculateFractalValues("
                + "         __global double *originRe,"
                + "         __global double *originIm,"
                + "         __global int *iterator,"
                + "         __global int *length,"
                + "         __global int *from,"
                + "         __global int *to,"
                + "         __global double *pathRe,"
                + "         __global double *pathIm) {"
                + "     int gid = get_global_id(0);\n"
                + "     double re = originRe[gid];\n"
                + "     double im = originIm[gid];\n"
                + "     double ore = re;\n"
                + "     double oim = im;\n"
                + "     double temp;\n"
                + "     int ite = 0;\n"
                + "     int len = 0;\n"
                + "     while (" + GPUMath.quadrance + " < " + CALCULATION_BOUNDARY + " && ite < " + ITERATION_MAX + ") {\n"
                + "         \n"
                + GPUMath.square
                + GPUMath.plusOrigin
                + "         \n"
                + "         if (re > " + AreaFinebrot.borderLowRe + " && re < " + AreaFinebrot.borderHighRe + " && im > " + AreaFinebrot.borderLowIm + " && im < " + AreaFinebrot.borderHighIm + ") {\n"
                /*              Program will calculate path again if it was a good path
                 *              It isn't possible to create arbitrary amount of temp arrays. */
                + "             len++;\n"
                + "         }\n"
                + "         ite++;\n"
                + "     }\n"
                + "     iterator[gid] = ite;\n"
                + "     length[gid] = len;\n"
                + "     if (ite < " + ITERATION_MAX + " && len > " + ITERATION_min + ") {\n"
                + "         int f = atomic_add(&START, len);\n"
                + "         int t = f + len;\n"
                + "         from[gid] = f;\n"
                + "         to[gid] = t;\n"
                + "         len = 0;\n"
                + "         re = ore;\n"
                + "         im = oim;\n"
                + "         for (int i = 0; i < ite; i++) {\n"
                + "             \n"
                + GPUMath.square
                + GPUMath.plusOrigin
                + "             \n"
                + "             if (re > " + AreaFinebrot.borderLowRe + " && re < " + AreaFinebrot.borderHighRe + " && im > " + AreaFinebrot.borderLowIm + " && im < " + AreaFinebrot.borderHighIm + ") {\n"
                + "                 pathRe[f + len] = re;\n"
                + "                 pathIm[f + len] = im;\n"
                + "                 len++;\n"
                + "             }\n"
                + "         }\n"
                + "     } else {\n"
                + "         from[gid] = 0;\n"
                + "         to[gid] = 0;\n"
                + "     }\n"
                + "}\n";
    }
}
