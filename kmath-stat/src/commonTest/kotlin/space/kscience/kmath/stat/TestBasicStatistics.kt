/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.UnstableKMathAPI
import space.kscience.kmath.misc.sorted
import space.kscience.kmath.operations.Float64Field
import space.kscience.kmath.random.RandomGenerator
import space.kscience.kmath.structures.MutableBuffer
import space.kscience.kmath.structures.slice
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(UnstableKMathAPI::class)
class TestBasicStatistics {
    companion object {
        val size = 100
        val float64Sample = RandomGenerator.default(123).nextDoubleBuffer(size)
        val float64Weights = RandomGenerator.default(123).nextDoubleBuffer(size)
        val onesWeights = MutableBuffer(100) { 1.0 }
    }

    @Test
    fun medianFloat64() {
        assertEquals(0.508, Float64Field.median(float64Sample), 0.0005)
        assertEquals(0.5055, Float64Field.median(float64Sample.slice { 0..<last }), 0.0005)
    }

    @Test
    fun meanFloat64() {
        assertEquals(0.488, Float64Field.mean(float64Sample), 0.0002)
    }

    @Test
    fun squaredMeanFloat64() {
        assertEquals(0.321, Float64Field.squaredMean(float64Sample), 0.0002)
    }

    @Test
    fun varianceFloat64() {
        assertEquals(0.083, Float64Field.variance(float64Sample), 0.0002)
    }

    @Test
    fun standardDeviationFloat64() {
        assertEquals(0.288, Float64Field.standardDeviation(float64Sample), 0.0002)
    }

    @Test
    fun trimmedMeanFloat64() {
        assertEquals(0.4917, Float64Field.trimmedMean(0.1)(float64Sample), 0.0002)
    }

    @Test
    fun weightedMeanFloat64() {
        assertEquals(0.6580, Float64Field.weightedMean(float64Sample, float64Weights), 0.0005)
        assertEquals(Float64Field.mean(float64Sample), Float64Field.weightedMean(float64Sample, onesWeights), 0.0005)
    }

    @Test
    fun zScoreFloat64() {
        assertEquals(0.2378, Float64Field.zScore(4)(float64Sample), 0.0005)
    }
}