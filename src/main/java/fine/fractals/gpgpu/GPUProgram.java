package fine.fractals.gpgpu;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.*;

public class GPUProgram {

    public static final String PROGRAM_SOURCE_CODE = "__global int START;"
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
            /*              Will calculate path again if it was a good path */
            + "             len++;\n"
            + "         }\n"
            + "         ite++;\n"
            + "     }\n"
            + "     \n"
            + "     iterator[gid] = ite;\n"
            + "     \n"
            + "     if (ite < " + ITERATION_MAX + " && len >= " + ITERATION_min + ") {\n"
            + "         int f = START;\n"
            + "         START = START + len;\n"
            + "         int t = f + len;\n"
            + "         \n"
            + "         length[gid] = len;\n"
            + "         from[gid] = f;\n"
            + "         to[gid] = t;\n"
            + "         \n"
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
            + "         \n"
            + "         length[gid] = 0;\n"
            + "         from[gid] = 0;\n"
            + "         to[gid] = 0;\n"
            + "         \n"
            + "     }\n"
            + "}\n";
}
