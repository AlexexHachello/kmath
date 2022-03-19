/*
 * Copyright 2018-2021 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.test.misc

import space.kscience.kmath.misc.UnstableKMathAPI
import space.kscience.kmath.operations.*

class RationalWithMemorization private constructor(
    val value: Rational,
    override val memory : OperationsMemory
): WithMemorization {
    public companion object {
        /**
         * Constant containing the zero (the additive identity) of the [Rational] field.
         */
        public val ZERO: RationalWithMemorization = RationalWithMemorization(Rational.ZERO, object : Endpoint {})
        /**
         * Constant containing the one (the multiplicative identity) of the [Rational] field.
         */
        public val ONE: RationalWithMemorization = RationalWithMemorization(Rational.ONE, object : Endpoint {})
    }
    public constructor(numerator: BigInt, denominator: BigInt) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Int, denominator: BigInt) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Long, denominator: BigInt) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: BigInt, denominator: Int) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: BigInt, denominator: Long) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Int, denominator: Int) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Int, denominator: Long) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Long, denominator: Int) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: Long, denominator: Long) : this(Rational(numerator, denominator), object : Endpoint {})
    public constructor(numerator: BigInt) : this(Rational(numerator), object : Endpoint {})
    public constructor(numerator: Int) : this(Rational(numerator), object : Endpoint {})
    public constructor(numerator: Long) : this(Rational(numerator), object : Endpoint {})

    public operator fun unaryPlus(): RationalWithMemorization = this
    public operator fun unaryMinus(): RationalWithMemorization = RationalWithMemorization(
        -value,
        object : Negation {
            override val negated: OperationsMemory = memory
        }
    )
    public operator fun plus(other: RationalWithMemorization): RationalWithMemorization = RationalWithMemorization(
        value + other.value,
        object : Sum {
            override val augend: OperationsMemory = memory
            override val addend: OperationsMemory = other.memory
        }
    )
    public operator fun minus(other: RationalWithMemorization): RationalWithMemorization = RationalWithMemorization(
        value - other.value,
        object : Difference {
            override val minuend: OperationsMemory = memory
            override val subtrahend: OperationsMemory = other.memory
        }
    )
    public operator fun times(other: RationalWithMemorization): RationalWithMemorization = RationalWithMemorization(
        value * other.value,
        object : Product {
            override val multiplicand: OperationsMemory = memory
            override val multiplier: OperationsMemory = other.memory
        }
    )
    public operator fun div(other: RationalWithMemorization): RationalWithMemorization = RationalWithMemorization(
        value / other.value,
        object : Quotient {
            override val dividend: OperationsMemory = memory
            override val divisor: OperationsMemory = other.memory
        }
    )

    public override fun equals(other: Any?): Boolean =
        other is RationalWithMemorization && value == other.value

    public override fun hashCode(): Int = value.hashCode()

    public override fun toString(): String = value.toString()
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
public object RationalWithMemorizationRing : Ring<RationalWithMemorization> {
    override inline val zero: RationalWithMemorization get() = RationalWithMemorization.ZERO
    override inline val one: RationalWithMemorization get() = RationalWithMemorization.ONE

    override inline fun add(left: RationalWithMemorization, right: RationalWithMemorization): RationalWithMemorization = left + right
    override inline fun multiply(left: RationalWithMemorization, right: RationalWithMemorization): RationalWithMemorization = left * right

    override inline fun RationalWithMemorization.unaryMinus(): RationalWithMemorization = -this
    override inline fun RationalWithMemorization.plus(arg: RationalWithMemorization): RationalWithMemorization = this + arg
    override inline fun RationalWithMemorization.minus(arg: RationalWithMemorization): RationalWithMemorization = this - arg
    override inline fun RationalWithMemorization.times(arg: RationalWithMemorization): RationalWithMemorization = this * arg
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
public object RationalWithMemorizationField : Field<RationalWithMemorization> {
    override inline val zero: RationalWithMemorization get() = RationalWithMemorization.ZERO
    override inline val one: RationalWithMemorization get() = RationalWithMemorization.ONE

    override inline fun number(value: Number): RationalWithMemorization = RationalWithMemorization(value.toLong())

    override inline fun add(left: RationalWithMemorization, right: RationalWithMemorization): RationalWithMemorization = left + right
    override inline fun multiply(left: RationalWithMemorization, right: RationalWithMemorization): RationalWithMemorization = left * right
    override inline fun divide(left: RationalWithMemorization, right: RationalWithMemorization): RationalWithMemorization = left / right
    override inline fun scale(a: RationalWithMemorization, value: Double): RationalWithMemorization = a * number(value)

    override inline fun RationalWithMemorization.unaryMinus(): RationalWithMemorization = -this
    override inline fun RationalWithMemorization.plus(arg: RationalWithMemorization): RationalWithMemorization = this + arg
    override inline fun RationalWithMemorization.minus(arg: RationalWithMemorization): RationalWithMemorization = this - arg
    override inline fun RationalWithMemorization.times(arg: RationalWithMemorization): RationalWithMemorization = this * arg
    override inline fun RationalWithMemorization.div(arg: RationalWithMemorization): RationalWithMemorization = this / arg
}