
package space.kscience.kmath.stat

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Timeout
import space.kscience.kmath.operations.Float64Field
import space.kscience.kmath.random.RandomGenerator
import space.kscience.kmath.stat.TestBasicStatistics.Companion.size
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@FlowPreview
internal class TestFlowStatistics {
    val dispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
    val float64Sample = RandomGenerator.default(123).nextDoubleBuffer(size)
    val float64Buffers = arrayOf(float64Sample, float64Sample)


    @Test
    @Timeout(2000)
    fun meanFloat64() {
        runBlocking {
            val res = Float64Field.mean
                .flow(float64Buffers.asFlow(), dispatcher)
                .reduce{ first, second -> first + second }
            assertEquals(0.976, res, 0.005)
        }
    }
}