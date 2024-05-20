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
    : BlockingStatistic<T, Double>{

    public override fun evaluateBlocking(data: Buffer<T>): Double = when (data.size) {
        0 -> error("Can't compute z-score of an empty buffer")
        1 -> {
            if (index ==  1) 0.0 else error("Index is out of range")
        }
        else -> with(field){
            if (index >= data.size){
                error("Index is out of range")
            }
            else {
                val std = StandardDeviation(field).evaluateBlocking(data)

                if (std != 0.0) {
                    val mean = Mean(field).evaluateBlocking(data).toString().toDoubleOrNull()
                    val elem = data[index].toString().toDoubleOrNull()

                    if (mean != null && elem != null) {
                        (elem - mean) / std
                    } else error("ZScore should be parsed to Double")
                } else {
                    error("Zero division!")
                }
            }
        }
    }
}

//TODO replace with optimized version which respects overflow
public fun Float64Field.zScore(index: Int): ZScore<Double> = ZScore(Float64Field, index)
public fun Int32Ring.zScore(index: Int): ZScore<Int> = ZScore(Int32Field, index)
public fun Int64Ring.zScore(index: Int): ZScore<Long> = ZScore(Int64Field, index)