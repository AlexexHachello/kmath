package kscience.kmath.nd

import kscience.kmath.nd.*
import kscience.kmath.operations.*
import kscience.kmath.structures.BufferFactory
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public interface BufferNDAlgebra<T, C> : NDAlgebra<T, C> {
    public val strides: Strides
    public val bufferFactory: BufferFactory<T>

    override fun produce(initializer: C.(IntArray) -> T): NDBuffer<T> = NDBuffer(
        strides,
        bufferFactory(strides.linearSize) { offset ->
            elementContext.initializer(strides.index(offset))
        }
    )

    public val NDStructure<T>.ndBuffer: NDBuffer<T>
        get() = when {
            !shape.contentEquals(this@BufferNDAlgebra.shape) -> throw ShapeMismatchException(
                this@BufferNDAlgebra.shape,
                shape
            )
            this is NDBuffer && this.strides == this@BufferNDAlgebra.strides -> this
            else -> produce { this@ndBuffer[it] }
        }

    override fun map(arg: NDStructure<T>, transform: C.(T) -> T): NDBuffer<T> {
        val argAsBuffer = arg.ndBuffer
        val buffer = bufferFactory(strides.linearSize) { offset ->
            elementContext.transform(argAsBuffer.buffer[offset])
        }
        return NDBuffer(strides, buffer)
    }

    override fun mapIndexed(arg: NDStructure<T>, transform: C.(index: IntArray, T) -> T): NDStructure<T> {
        val argAsBuffer = arg.ndBuffer
        val buffer = bufferFactory(strides.linearSize) { offset ->
            elementContext.transform(
                strides.index(offset),
                argAsBuffer[offset]
            )
        }
        return NDBuffer(strides, buffer)
    }

    override fun combine(a: NDStructure<T>, b: NDStructure<T>, transform: C.(T, T) -> T): NDStructure<T> {
        val aBuffer = a.ndBuffer
        val bBuffer = b.ndBuffer
        val buffer = bufferFactory(strides.linearSize) { offset ->
            elementContext.transform(aBuffer.buffer[offset], bBuffer[offset])
        }
        return NDBuffer(strides, buffer)
    }
}

public open class BufferedNDSpace<T, R : Space<T>>(
    final override val shape: IntArray,
    final override val elementContext: R,
    final override val bufferFactory: BufferFactory<T>,
) : NDSpace<T, R>, BufferNDAlgebra<T, R> {
    override val strides: Strides = DefaultStrides(shape)
    override val zero: NDBuffer<T> by lazy { produce { zero } }
}

public open class BufferedNDRing<T, R : Ring<T>>(
    shape: IntArray,
    elementContext: R,
    bufferFactory: BufferFactory<T>,
) : BufferedNDSpace<T, R>(shape, elementContext, bufferFactory), NDRing<T, R> {
    override val one: NDBuffer<T> by lazy { produce { one } }
}

public open class BufferedNDField<T, R : Field<T>>(
    shape: IntArray,
    elementContext: R,
    bufferFactory: BufferFactory<T>,
) : BufferedNDRing<T, R>(shape, elementContext, bufferFactory), NDField<T, R>

// space factories
public fun <T, A : Space<T>> NDAlgebra.Companion.space(
    space: A,
    bufferFactory: BufferFactory<T>,
    vararg shape: Int,
): BufferedNDSpace<T, A> = BufferedNDSpace(shape, space, bufferFactory)

public inline fun <T, A : Space<T>, R> A.ndSpace(
    noinline bufferFactory: BufferFactory<T>,
    vararg shape: Int,
    action: BufferedNDSpace<T, A>.() -> R,
): R {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    return NDAlgebra.space(this, bufferFactory, *shape).run(action)
}

//ring factories
public fun <T, A : Ring<T>> NDAlgebra.Companion.ring(
    ring: A,
    bufferFactory: BufferFactory<T>,
    vararg shape: Int,
): BufferedNDRing<T, A> = BufferedNDRing(shape, ring, bufferFactory)

public inline fun <T, A : Ring<T>, R> A.ndRing(
    noinline bufferFactory: BufferFactory<T>,
    vararg shape: Int,
    action: BufferedNDRing<T, A>.() -> R,
): R {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    return NDAlgebra.ring(this, bufferFactory, *shape).run(action)
}

//field factories
public fun <T, A : Field<T>> NDAlgebra.Companion.field(
    field: A,
    bufferFactory: BufferFactory<T>,
    vararg shape: Int,
): BufferedNDField<T, A> = BufferedNDField(shape, field, bufferFactory)

public inline fun <T, A : Field<T>, R> A.ndField(
    noinline bufferFactory: BufferFactory<T>,
    vararg shape: Int,
    action: BufferedNDField<T, A>.() -> R,
): R {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    return NDAlgebra.field(this, bufferFactory, *shape).run(action)
}