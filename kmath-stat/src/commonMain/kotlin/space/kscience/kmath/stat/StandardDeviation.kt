/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.operations.*
import space.kscience.kmath.structures.Buffer
import kotlin.math.sqrt

/**
 * Standard deviation
 */
public class StandardDeviation<T>(
    private val field: Field<T>,
) : BlockingStatistic<T, T> {

    override fun evaluateBlocking(data: Buffer<T>): T = with(field) {
        val res = sqrt(Variance(field).evaluateBlocking(data) as Double)
        res as T
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>): Double = Float64Field.standardDeviation.evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Int>): Int = Int32Ring.standardDeviation.evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Long>): Long = Int64Ring.standardDeviation.evaluateBlocking(buffer)
    }
}

//TODO replace with optimized version which respects overflow
public val Float64Field.standardDeviation: StandardDeviation<Double> get() = StandardDeviation(Float64Field)
public val Int32Ring.standardDeviation: StandardDeviation<Int> get() = StandardDeviation(Int32Field)
public val Int64Ring.standardDeviation: StandardDeviation<Long> get() = StandardDeviation(Int64Field)