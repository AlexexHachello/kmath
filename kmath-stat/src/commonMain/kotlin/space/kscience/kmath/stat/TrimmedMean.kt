/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.misc.sortedWith
import space.kscience.kmath.operations.*
import space.kscience.kmath.structures.Buffer
import space.kscience.kmath.structures.slice

/**
 * Trimmed mean
 */
public class TrimmedMean<T>(
    private val field: Field<T>,
    private val percentage: Double,
    private val comparator: Comparator<T>,
) : BlockingStatistic<T, T> {

init {
    require(percentage in 0.0..0.5){"The percentage value should be between 0.0 and 0.5"}
}

 override fun evaluateBlocking(data: Buffer<T>): T = with(field) {
        val sortedData = data.sortedWith(comparator)
        val trimmedNum = (0.5 * percentage * data.size).toInt()
        val trimmedData = sortedData.slice { trimmedNum..data.size-trimmedNum }
        Mean(field).evaluateBlocking(trimmedData)
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>, percentage: Double): Double {
            return Float64Field.trimmedMean(percentage).evaluateBlocking(buffer)
        }
        public fun evaluate(buffer: Buffer<Int>, percentage: Double): Int {
            return Int32Ring.trimmedMean(percentage).evaluateBlocking(buffer)
        }
        public fun evaluate(buffer: Buffer<Long>, percentage: Double): Long {
            return Int64Ring.trimmedMean(percentage).evaluateBlocking(buffer)
        }
    }
}

//TODO replace with optimized version which respects overflow
public fun Float64Field.trimmedMean(percentage: Double): TrimmedMean<Double> {
    return TrimmedMean(Float64Field, percentage) { a, b -> a.compareTo(b) }
}
public fun Int32Ring.trimmedMean(percentage: Double): TrimmedMean<Int> {
    return TrimmedMean(Int32Field, percentage) { a, b -> a.compareTo(b) }
}
public fun Int64Ring.trimmedMean(percentage: Double): TrimmedMean<Long> {
    return TrimmedMean(Int64Field, percentage) { a, b -> a.compareTo(b) }
}