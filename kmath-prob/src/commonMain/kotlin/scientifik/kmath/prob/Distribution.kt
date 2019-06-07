package scientifik.kmath.prob

import scientifik.kmath.chains.Chain

/**
 * A distribution of typed objects
 */
interface Distribution<T : Any> {
    /**
     * A probability value for given argument [arg].
     * For continuous distributions returns PDF
     */
    fun probability(arg: T): Double

    /**
     * Create a chain of samples from this distribution.
     * The chain is not guaranteed to be stateless.
     */
    fun sample(generator: RandomGenerator): Chain<T>
    //TODO add sample bunch generator
}

interface UnivariateDistribution<T : Comparable<T>> : Distribution<T> {
    /**
     * Cumulative distribution for ordered parameter
     */
    fun cumulative(arg: T): Double
}