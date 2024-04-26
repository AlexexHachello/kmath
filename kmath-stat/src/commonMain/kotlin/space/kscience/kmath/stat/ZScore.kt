/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.operations.*
import space.kscience.kmath.structures.Buffer

/**
 * Z-Score function
 */
public class ZScore<T>(
    private val field: Field<T>)
    : DottyStatistic<T, T>{
     public override fun evaluate(data: Buffer<T>, index: Int): T = when (data.size) {
        0 -> error("Can't compute z-score of an empty buffer")
        1 -> with(field){
            zero
        }
        else -> with(field){
           if (StandardDeviation(field).evaluateBlocking(data) == zero)
           {
               (data[index] - Mean(field).evaluateBlocking(data)) / StandardDeviation(field).evaluateBlocking(data)
           }
           else {
               throw Exception("Zero division!")
           }
        }
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>, index: Int): Double = Float64Field.zScore.evaluate(buffer, index)
        public fun evaluate(buffer: Buffer<Int>, index: Int): Int = Int32Ring.zScore.evaluate(buffer, index)
        public fun evaluate(buffer: Buffer<Long>, index: Int): Long = Int64Ring.zScore.evaluate(buffer, index)
    }
}

public val Float64Field.zScore: ZScore<Double> get() = ZScore(Float64Field)
public val Int32Ring.zScore: ZScore<Int> get() = ZScore(Int32Field)
public val Int64Ring.zScore: ZScore<Long> get() = ZScore(Int64Field)