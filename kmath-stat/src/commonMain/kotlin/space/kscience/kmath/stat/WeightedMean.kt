/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.operations.*
import space.kscience.kmath.structures.*

/**
 * Weighted arithmetic mean
 */
public class WeightedMean<T >(
    private val field: Field<T>)
    :   ComposableWeightedStatistic<T,  Pair<T, T>, T>, BlockingWeightedStatistic<T, T> {

    override fun evaluateBlocking(data: Buffer<T>, weights: Buffer<T>): T = when
    {
        data.size != weights.size -> error("Can't compute weighted mean for not equal data and weights buffers sizes")
        else -> with(field){
            var res = zero
            var acc = zero

            for (i in data.indices) {
                res += data[i] * weights[i]
                acc += weights[i]
            }
            res / acc
        }
    }

    override suspend fun evaluate(data: Buffer<T>, weights: Buffer<T>): T = super<ComposableWeightedStatistic>.evaluate(data, weights)

    override suspend fun computeIntermediate(data: Buffer<T>, weights: Buffer<T>): Pair<T, T> = when
    {
        data.size != weights.size -> error("Can't compute weighted mean for not equal data and weights buffers sizes")
        else -> with(field){
            var res = zero
            var acc = zero

            for (i in data.indices) {
                res += data[i] * weights[i]
                acc += weights[i]
            }
            res to acc
        }
    }

    override suspend fun composeIntermediate(first: Pair<T, T>, second: Pair<T, T>): Pair<T, T> =
        with(field) { first.first + second.first to first.second + second.second }

    override suspend fun toResult(intermediate: Pair<T, T>): T = with(field) {
        intermediate.first / intermediate.second
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>, weights: Buffer<Double>): Double = Float64Field.weightedMean.evaluateBlocking(buffer, weights)
        public fun evaluate(buffer: Buffer<Int>, weights: Buffer<Int>): Int = Int32Ring.weightedMean.evaluateBlocking(buffer, weights)
        public fun evaluate(buffer: Buffer<Long>, weights: Buffer<Long>): Long = Int64Ring.weightedMean.evaluateBlocking(buffer, weights)
    }
}

public val Float64Field.weightedMean: WeightedMean<Double> get() = WeightedMean(Float64Field)
public val Int32Ring.weightedMean: WeightedMean<Int> get() = WeightedMean(Int32Field)
public val Int64Ring.weightedMean: WeightedMean<Long> get() = WeightedMean(Int64Field)