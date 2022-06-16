package org.brokenarrow.library.menusettings.utillity;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public final class RandomUntility extends Random {

	private static Random random;
	private static final AtomicLong seedUniquifier
			= new AtomicLong(8682522807148012L);

	public RandomUntility() {
		random = new Random(seedUniquifier() ^ System.nanoTime());
	}

	private static long seedUniquifier() {
		// L'Ecuyer, "Tables of Linear Congruential Generators of
		// Different Sizes and Good Lattice Structure", 1999
		for (; ; ) {
			long current = seedUniquifier.get();
			long next = current * 1181783497276652981L;
			if (seedUniquifier.compareAndSet(current, next))
				return next;
		}
	}

	public RandomUntility newRandomsSeed() {
		random = new Random(seedUniquifier() ^ System.nanoTime());
		return this;
	}

	public int randomIntNumber(int origin, int bound) {
		return randomIntNumber(origin, bound, false);
	}

	public int randomIntNumber(int origin, int bound, boolean newRandomsSeed) {
		if (newRandomsSeed)
			this.newRandomsSeed();
		return origin + this.nextInt(bound - origin + 1);
	}

	/**
	 * Get random duble, converted number from 0.0 to 100.0
	 *
	 * @return percent with decimals..
	 */

	public double randomDouble() {
		return randomDouble(false);
	}

	/**
	 * Get random duble, converted number from 0.0 to 100.0
	 *
	 * @param newRandomsSeed if it shall create new random seed before get next double.
	 * @return percent with decimals..
	 */
	public double randomDouble(final boolean newRandomsSeed) {
		if (newRandomsSeed)
			this.newRandomsSeed();
		return nextDouble() * 100D;
	}

	public boolean chance(final double percent) {
		return chance(percent, false);
	}

	public boolean chance(final double percent, final boolean newRandomsSeed) {
		if (newRandomsSeed)
			this.newRandomsSeed();
		return this.randomDouble() < percent;
	}

	@Override
	public double nextDouble() {
		return random.nextDouble();
	}

	@Override
	public int nextInt(int bound) {
		if (bound < 0)
			bound = 1;
		return random.nextInt(bound);
	}
}
