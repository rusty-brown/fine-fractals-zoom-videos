package fine.fractals.gpgpu;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.CALCULATION_BOUNDARY;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;

public class GPUProgram {

    public static final String PROGRAM_SOURCE_CODE = "inline int contains("
            + "         __private double re,"
            + "         __private double im) {\n"
            + "     return re > " + AreaFinebrot.borderLowRe + "\n"
            + "         && re < " + AreaFinebrot.borderHighRe + "\n"
            + "         && im > " + AreaFinebrot.borderLowIm + "\n"
            + "         && im < " + AreaFinebrot.borderHighIm + ";\n"
            + "}\n"
            + "__global int start;"
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
            /* temp paths */
            + "     double *pRe;\n"
            + "     double *pIm;\n"
            + "     int ite = 0;\n"
            + "     int len = 0;\n"
            //@Formatter:off
            + "     while (" + GPUMath.quadrance + " < " + CALCULATION_BOUNDARY + " && ite < " + ITERATION_MAX + ") {\n"
            +                  GPUMath.square
            +                  GPUMath.plusOrigin
            //@Formatter:on
            + "         if (contains(re, im)) {\n"
            + "             pRe[len] = re;\n"
            + "             pIm[len] = im;\n"
            + "             len++;\n"
            + "         }\n"
            + "         ite++;\n"
            + "     }\n"
            + "     if (ite < " + ITERATION_MAX + ") {\n"
            + "         length[gid] = len;\n"

            + "         int f = start;\n"
            + "         start = start + len;\n"
            + "         int t = f + len;\n"
            + "         for (int i = f; i < t; i++) {\n"
            + "             pathRe[i] = pRe[i];\n"
            + "             pathIm[i] = pIm[i];\n"
            + "         };\n"

            + "         from[gid] = f;\n"
            + "         to[gid] = t;\n"
            + "     } else {\n"
            + "         length[gid] = 0;\n"
            + "         from[gid] = 0;\n"
            + "         to[gid] = 0;\n"
            + "     }\n"
            + "     iterator[gid] = ite;\n"
            + "}\n";
}
