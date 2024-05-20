/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.operations.*
import space.kscience.kmath.stat.Rank.Companion
import space.kscience.kmath.structures.Buffer
import space.kscience.kmath.structures.indices
import kotlin.math.sqrt
import kotlin.reflect.typeOf

/**
 * Standard deviation
 */
public class StandardDeviation<T> (
        private val field: Field<T>
    ) : BlockingStatistic<T, Double> {

    override fun evaluateBlocking(data: Buffer<T>): Double = with(field) {
        val variance = Variance(field).evaluateBlocking(data).toString().toDoubleOrNull()
        if (variance != null) sqrt(variance) else error("Deviations should be parsed to Double for sqrt() call")
    }
}

//TODO replace with optimized version which respects overflow
public val Float64Field.standardDeviation : StandardDeviation<Double> get() = StandardDeviation(Float64Field)
public val Int32Ring.standardDeviation : StandardDeviation<Int> get() = StandardDeviation(Int32Field)
public val Int64Ring.standardDeviation : StandardDeviation<Long> get() = StandardDeviation(Int64Field)