package fine.fractals.math;

import fine.fractals.data.mem.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.ITERATION_MAX;

@SuppressWarnings(value = "unused")
public class MathematicianImpl {

	private static HashSet<Integer> PRIMES;
	private static HashSet<Integer> FIBONACCI;
	private static HashSet<Integer> PERFECT;
	private static HashSet<Integer> SQUARE;

	private static final Logger log = LogManager.getLogger(MathematicianImpl.class);

	public static final MathematicianImpl Mathematician;

	static {
		log.info("init");
		Mathematician = new MathematicianImpl();
	}

	private MathematicianImpl() {
	}

	public void initPrimes() {
		log.info("initPrimes()");
		PRIMES = new HashSet<>();
		for (int i = 0; i < ITERATION_MAX; i++) {
			if (isPrimeInit(i)) {
				PRIMES.add(i);
			}
		}
	}

	public void initFibonacci() {
		log.info("initFibonacci()");
		FIBONACCI = new HashSet<>();
		int a = 0;
		int b = 1;
		int sum;
		while (b <= ITERATION_MAX) {
			sum = a + b;
			FIBONACCI.add(sum);
			a = b;
			b = sum;
		}
	}

	public void initPerfectNumbers() {
		log.info("initPerfectNumbers()");
		PERFECT = new HashSet<>();
		for (int i = 0; i <= ITERATION_MAX; i++) {
			if (isPerfectInit(i)) {
				PERFECT.add(i);
			}
		}
	}

	public void initSquares() {
		log.info("initSquares()");
		SQUARE = new HashSet<>();
		int sq;
		for (int i = 0; i * i <= ITERATION_MAX; i++) {
			sq = i * i;
			SQUARE.add(sq);
		}
	}

	private boolean isPrimeInit(int n) {
		if (n % 2 == 0) {
			return false;
		}
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	private boolean isPerfectInit(int number) {
		int temp = 0;
		for (int i = 1; i <= number / 2; i++) {
			if (number % i == 0) {
				temp += i;
			}
		}
		return temp == number;
	}

	public boolean isPrime(int n) {
		return PRIMES.contains(n);
	}

	public boolean isFibonacci(int n) {
		return FIBONACCI.contains(n);
	}

	public boolean isPerfect(int n) {
		return PERFECT.contains(n);
	}

	public boolean isSquare(int n) {
		return SQUARE.contains(n);
	}

	/**
	 * (t^2 + x^2 - 2at)^2 = 4a^2 (t^2 + x^2)
	 */
	public static boolean isOutsideCardioid(double re, double im) {
		final double a = 0.25;
		final double t = re - 0.25;
		final double t2 = t * t;
		final double x2 = im * im;
		final double leftSide = t2 + x2 + 2 * a * t;
		return leftSide * leftSide > 4 * a * a * (t2 + x2);
	}

	/**
	 * circle with center at re=-1,im=0 and radius 1/4
	 */
	public static boolean isOutsideCircle(double re, double im) {
		return ((re + 1) * (re + 1)) + (im * im) > 0.0625;
	}

	public void multiplyBy(Mem m, double re, double im) {
		double temp = (m.re * re) - (m.im * im);
		m.im = (m.re * im) + (re * m.im);
		m.re = temp;
	}

	public void plusInvert(Mem m) {
		double a = m.re;
		double b = m.im;
		double quadrance = (a * a) + (b * b);
		m.re = m.re + (a / quadrance);
		m.im = m.im - (b / quadrance);
	}

	public void minusInvert(Mem m) {
		double a = m.re;
		double b = m.im;
		double quadrance = (a * a) + (b * b);
		m.re = m.re - (a / quadrance);
		m.im = m.im + (b / quadrance);
	}

	public void innerProduct(Mem m, double re, double im) {
		m.re = m.re * re;
		m.im = m.im * im;
	}

	public void inverse(Mem m) {
		double q = m.quadrance();
		m.conjugation();
		m.re /= q;
		m.im /= q;
	}

	public void rote(Mem m, Mem center) {
		center.conjugation();
		multiplyBy(m, center.re, center.im);
		multiplyBy(m, m.re, m.im);
		double q = center.quadrance();
		m.re /= q;
		m.im /= q;
	}

	/* (a + ib)^3 */
	public void binomial3(Mem m) {
		double temp = (m.re * m.re * m.re) - (3 * m.re * m.im * m.im);
		m.im = (3 * m.re * m.re * m.im) - (m.im * m.im * m.im);
		m.re = temp;
	}

	/* (a + ib)^4 */
	public void binomial4(Mem m) {
		double temp = ((m.re * m.re * m.re * m.re)
				- (6 * m.re * m.re * m.re * m.im)
				+ (m.im * m.re * m.im * m.im));
		m.im = ((4 * m.re * m.re * m.re * m.im)
				- (4 * m.re * m.im * m.im * m.im));
		m.re = temp;
	}

	/* (a + ib)^5 */
	public void binomial5(Mem m) {
		double temp = ((m.re * m.re * m.re * m.re * m.re)
				- (10 * m.re * m.re * m.re * m.im * m.im)
				+ (5 * m.re * m.im * m.im * m.im * m.im));
		m.im = ((5 * m.re * m.re * m.re * m.re * m.im)
				- (10 * m.re * m.re * m.im * m.im * m.im)
				+ (m.im * m.im * m.im * m.im * m.im));
		m.re = temp;
	}


	public void reciprocal(Mem m) {
		double scale = m.re * m.re + m.im * m.im;
		m.re = m.re / scale;
		m.im = -m.im / scale;
	}

	public void circleInversion(Mem m, double re, double im) {
		double d = (re * re) + (im * im);
		m.re = re / d;
		m.im = im / d;
	}

	public static void rot(Mem m, double t) {
		double temp = (1 - t * t) / (1 + t * t);
		m.im = (2 * t) / (1 + t * t);
		m.re = temp;
	}
}
