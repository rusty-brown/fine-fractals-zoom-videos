package fine.fractals.gpgpu;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.CALCULATION_BOUNDARY;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;

public class GPUProgram {

    public static final String PROGRAM_SOURCE_CODE = "inline int contains("
            + "         __private double re"
            + "         __private double im) {\n"
            + "     return re > " + AreaFinebrot.borderLowRe + "\n"
            + "         && re < " + AreaFinebrot.borderHighRe + "\n"
            + "         && im > " + AreaFinebrot.borderLowIm + "\n"
            + "         && im < " + AreaFinebrot.borderHighIm + ";\n"
            + "}\n"
            + "__kernel void calculateFractalValues(\n"
            + "         __global double *originRe,\n"
            + "         __global double *originIm,\n"
            + "         __global int *iterator,\n"
            + "         __global int *length,\n"
            + "         __global double *pathRe,\n"
            + "         __global double *pathIm) {\n"
            + "     int gid = get_global_id(0);\n"
            + "     double re = originRe[gid];\n"
            + "     double im = originIm[gid];\n"
            + "     double ore = re;\n"
            + "     double oim = im;\n"
            + "     double temp;\n"
            + "     int ite = 0;\n"
            + "     int len = 0;\n"
            //@Formatter:off
                    + "     while ((" + GPUMath.quadrance + ") < " + CALCULATION_BOUNDARY + " && iterator < + " + ITERATION_MAX + ") {\n"
                    + 					GPUMath.square
                    + 					GPUMath.plusOrigin
                    //@Formatter:on
            + "         if (contains(re, im)) {"
            //              TODO
            + "             pathRe[gid] = re;\n"
            + "             pathIm[gid] = im;\n"
            + "             len++;\n"
            + "             }\n"
            + "         ite++;\n"
            + "         }\n"
            + "     }\n"
            + "     iterator[gid] = ite;\n"
            + "     length[gid] = len;\n"
            + "}\n";
}
