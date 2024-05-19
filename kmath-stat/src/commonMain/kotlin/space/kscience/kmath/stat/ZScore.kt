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
    private val field: Field<T>,
    private val index: Int)
    : BlockingStatistic<T, T>{

     public override fun evaluateBlocking(data: Buffer<T>): T = when (data.size) {
        0 -> error("Can't compute z-score of an empty buffer")
        1 -> with(field){
            if (index ==  1) zero else error("Index is out of range")
        }
        else -> with(field){
            if (index >= data.size){
                error("Index is out of range")
            }
            else {
                val std = StandardDeviation(field).evaluateBlocking(data)

                if (std != zero) {
                    val mean = Mean(field).evaluateBlocking(data)

                    (data[index] - mean) / std
                } else {
                    error("Zero division!")
                }
            }
        }
    }

    public companion object {
        public fun evaluate(buffer: Buffer<Double>, index: Int): Double = Float64Field.zScore(index).evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Int>, index: Int): Int = Int32Ring.zScore(index).evaluateBlocking(buffer)
        public fun evaluate(buffer: Buffer<Long>, index: Int): Long = Int64Ring.zScore(index).evaluateBlocking(buffer)
    }
}

//TODO replace with optimized version which respects overflow
public fun Float64Field.zScore(index: Int): ZScore<Double> = ZScore(Float64Field, index)
public fun Int32Ring.zScore(index: Int): ZScore<Int> = ZScore(Int32Field, index)
public fun Int64Ring.zScore(index: Int): ZScore<Long> = ZScore(Int64Field, index)