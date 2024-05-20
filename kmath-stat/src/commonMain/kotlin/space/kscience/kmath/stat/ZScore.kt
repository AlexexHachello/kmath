/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.operations.*
import space.kscience.kmath.structures.Buffer

/**
 * Variance
 */
public class Variance<T>(
    private val field: Field<T>,
) : BlockingStatistic<T, T> {

    override fun evaluateBlocking(data: Buffer<T>): T = with(field) {
        val mean = Mean(field).evaluateBlocking(data)
        SquaredMean(field).evaluateBlocking(data) - mean * mean
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>): Double = Float64Field.variance.evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Int>): Int = Int32Ring.variance.evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Long>): Long = Int64Ring.variance.evaluateBlocking(buffer)
    }
}

//TODO replace with optimized version which respects overflow
public val Float64Field.variance: Variance<Double> get() = Variance(Float64Field)
public val Int32Ring.variance: Variance<Int> get() = Variance(Int32Field)
public val Int64Ring.variance: Variance<Long> get() = Variance(Int64Field)