/*
 * Copyright 2018-2024 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.stat

import space.kscience.kmath.UnstableKMathAPI
import space.kscience.kmath.operations.Float64Field
import space.kscience.kmath.operations.Int32Ring
import space.kscience.kmath.operations.Int64Ring
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
        val floatOnesWeights = MutableBuffer<Double>(100) { 1.0 }

        val int32SampleBuffer = RandomGenerator.default(123).nextIntBuffer(100)
        val int32Sample: MutableBuffer<Int> = MutableBuffer(size) { index -> int32SampleBuffer[index] / 1000000 }
        val int32WeightsBuffer = RandomGenerator.default(123).nextIntBuffer(100)
        val int32Weights: MutableBuffer<Int> = MutableBuffer(size) { index -> int32WeightsBuffer[index] / 1000000 }
        val int32OnesWeights = MutableBuffer<Int>(100) { 1 }

        val int64SampleBuffer = RandomGenerator.default(123).nextLongBuffer(100)
        val int64Sample: MutableBuffer<Long> = MutableBuffer(size) { index -> int64SampleBuffer[index] / 1000000000000 }
        val int64WeightsBuffer = RandomGenerator.default(123).nextLongBuffer(100)
        val int64Weights: MutableBuffer<Long> = MutableBuffer(size) { index -> int64WeightsBuffer[index] / 1000000000000 }
        val int64OnesWeights = MutableBuffer<Long>(100) { 1 }
    }

    @Test
    fun medianFloat64() {
        assertEquals(0.508, Float64Field.median(float64Sample), 0.0005)
        assertEquals(0.5055, Float64Field.median(float64Sample.slice { 0..<last }), 0.0005)
    }

    @Test
    fun medianInt32() {
        assertEquals(250, Int32Ring.median(int32Sample))
        assertEquals(246, Int32Ring.median(int32Sample.slice { 0..<last }))
    }

    @Test
    fun medianInt64() {
        assertEquals(-215305, Int64Ring.median(int64Sample))
        assertEquals(-142651, Int64Ring.median(int64Sample.slice { 0..<last }))
    }

    @Test
    fun meanFloat64() {
        assertEquals(0.488, Float64Field.mean(float64Sample), 0.0002)
    }

    @Test
    fun meanInt32() {
        assertEquals(151, Int32Ring.mean(int32Sample))
    }

    @Test
    fun meanInt64() {
        assertEquals(-407698, Int64Ring.mean(int64Sample))
    }

    @Test
    fun squaredMeanFloat64() {
        assertEquals(0.321, Float64Field.squaredMean(float64Sample), 0.0002)
    }

    @Test
    fun squaredMeanInt32() {
        assertEquals(1620602, Int32Ring.squaredMean(int32Sample))
    }

    @Test
    fun squaredMeanInt64() {
        assertEquals(29304396232343, Int64Ring.squaredMean(int64Sample))
    }

    @Test
    fun varianceFloat64() {
        assertEquals(0.083, Float64Field.variance(float64Sample), 0.0002)
    }

    @Test
    fun varianceInt32() {
        assertEquals(1597801, Int32Ring.variance(int32Sample))
    }

    @Test
    fun varianceInt64() {
        assertEquals(29138178573139, Int64Ring.variance(int64Sample))
    }

    @Test
    fun standardDeviationFloat64() {
        assertEquals(0.288, Float64Field.standardDeviation(float64Sample), 0.0002)
    }

    @Test
    fun standardDeviationInt32() {
        assertEquals(1264.04, Int32Ring.standardDeviation(int32Sample), 0.01)
    }

    @Test
    fun standardDeviationInt64() {
        assertEquals(5397979.11, Int64Ring.standardDeviation(int64Sample), 10.0)
    }

    @Test
    fun trimmedMeanFloat64() {
        assertEquals(0.4917, Float64Field.trimmedMean(0.1)(float64Sample), 0.0002)
        assertEquals(0.4892, Float64Field.trimmedMean(0.25)(float64Sample), 0.0002)
        assertEquals(0.4884, Float64Field.trimmedMean(0.5)(float64Sample), 0.0002)
    }

    @Test
    fun trimmedMeanInt32() {
        assertEquals(189, Int32Ring.trimmedMean(0.1)(int32Sample))
        assertEquals(225, Int32Ring.trimmedMean(0.25)(int32Sample))
        assertEquals(265, Int32Ring.trimmedMean(0.5)(int32Sample))
    }

    @Test
    fun trimmedMeanInt64() {
        assertEquals(-332014, Int64Ring.trimmedMean(0.1)(int64Sample))
        assertEquals(-323126, Int64Ring.trimmedMean(0.25)(int64Sample))
        assertEquals(-231805, Int64Ring.trimmedMean(0.5)(int64Sample))
    }

    @Test
    fun weightedMeanFloat64() {
        assertEquals(0.6580, Float64Field.weightedMean(float64Sample, float64Weights), 0.0005)
        assertEquals(Float64Field.mean(float64Sample), Float64Field.weightedMean(float64Sample, floatOnesWeights), 0.0005)
    }

    @Test
    fun weightedMeanInt32() {
        assertEquals(10758, Int32Ring.weightedMean(int32Sample, int32Weights))
        assertEquals(Int32Ring.mean(int32Sample) - 1, Int32Ring.weightedMean(int32Sample, int32OnesWeights))
    }

    @Test
    fun weightedMeanInt64() {
        assertEquals(-71877742, Int64Ring.weightedMean(int64Sample, int64Weights))
        assertEquals(Int64Ring.mean(int64Sample) + 1, Int64Ring.weightedMean(int64Sample, int64OnesWeights))
    }

    @Test
    fun zScoreFloat64() {
        assertEquals(0.2378, Float64Field.zScore(4)(float64Sample), 0.0005)
        assertEquals(-1.067, Float64Field.zScore(33)(float64Sample), 0.0005)
        assertEquals(0.3423, Float64Field.zScore(70)(float64Sample), 0.0005)
    }

    @Test
    fun zScoreInt32() {
        assertEquals(0.6154, Int32Ring.zScore(4)(int32Sample),0.0005)
        assertEquals(0.8678, Int32Ring.zScore(33)(int32Sample),0.0005)
        assertEquals(-1.2483, Int32Ring.zScore(70)(int32Sample),0.0005)
    }

    @Test
    fun zScoreInt64() {
        assertEquals(-1.4403, Int64Ring.zScore(4)(int64Sample),0.0005)
        assertEquals(0.6920, Int64Ring.zScore(33)(int64Sample),0.0005)
        assertEquals(-1.3374, Int64Ring.zScore(70)(int64Sample),0.0005)
    }
}