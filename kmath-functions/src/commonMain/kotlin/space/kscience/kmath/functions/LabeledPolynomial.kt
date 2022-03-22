/*
 * Copyright 2018-2021 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.functions

import space.kscience.kmath.expressions.Symbol
import space.kscience.kmath.operations.Ring
import space.kscience.kmath.operations.ScaleOperations
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName
import kotlin.math.max


/**
 * Represents multivariate polynomials with labeled variables.
 *
 * @param C Ring in which the polynomial is considered.
 */
public data class LabeledPolynomial<C>
internal constructor(
    /**
     * Map that collects coefficients of the polynomial. Every non-zero monomial
     * `a x_1^{d_1} ... x_n^{d_n}` is represented as pair "key-value" in the map, where value is coefficients `a` and
     * key is map that associates variables in the monomial with multiplicity of them occurring in the monomial.
     * For example polynomial
     * ```
     * 5 a^2 c^3 - 6 b + 0 b c
     * ```
     * has coefficients represented as
     * ```
     * mapOf(
     *      mapOf(
     *          a to 2,
     *          c to 3
     *      ) to 5,
     *      mapOf(
     *          b to 1
     *      ) to (-6)
     * )
     * ```
     * where `a`, `b` and `c` are corresponding [Symbol] objects.
     */
    public val coefficients: Map<Map<Symbol, UInt>, C>
) : Polynomial<C> {
    override fun toString(): String = "LabeledPolynomial$coefficients"
}

/**
 * Returns the same degrees' description of the monomial, but without zero degrees.
 */
internal fun Map<Symbol, UInt>.cleanUp() = filterValues { it > 0U }

// Waiting for context receivers :( TODO: Replace with context receivers when they will be available

@Suppress("FunctionName", "NOTHING_TO_INLINE")
internal inline fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(coefs: Map<Map<Symbol, UInt>, C>, toCheckInput: Boolean = true) : LabeledPolynomial<C> = ring.LabeledPolynomial(coefs, toCheckInput)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> A.LabeledPolynomial(coefs: Map<Map<Symbol, UInt>, C>, toCheckInput: Boolean = true) : LabeledPolynomial<C> {
    if (!toCheckInput) return LabeledPolynomial<C>(coefs)

    val fixedCoefs = mutableMapOf<Map<Symbol, UInt>, C>()

    for (entry in coefs) {
        val key = entry.key.cleanUp()
        val value = entry.value
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return LabeledPolynomial<C>(
        fixedCoefs.filterValues { it != zero }
    )
}

@Suppress("FunctionName", "NOTHING_TO_INLINE")
internal inline fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(pairs: Collection<Pair<Map<Symbol, UInt>, C>>, toCheckInput: Boolean = true) : LabeledPolynomial<C> = ring.LabeledPolynomial(pairs, toCheckInput)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> A.LabeledPolynomial(pairs: Collection<Pair<Map<Symbol, UInt>, C>>, toCheckInput: Boolean = true) : LabeledPolynomial<C> {
    if (!toCheckInput) return LabeledPolynomial<C>(pairs.toMap())

    val fixedCoefs = mutableMapOf<Map<Symbol, UInt>, C>()

    for (entry in pairs) {
        val key = entry.first.cleanUp()
        val value = entry.second
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return LabeledPolynomial<C>(
        fixedCoefs.filterValues { it != zero }
    )
}

@Suppress("FunctionName", "NOTHING_TO_INLINE")
internal inline fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(vararg pairs: Pair<Map<Symbol, UInt>, C>, toCheckInput: Boolean = true) : LabeledPolynomial<C> = ring.LabeledPolynomial(pairs = pairs, toCheckInput = toCheckInput)
@Suppress("FunctionName")
internal fun <C, A: Ring<C>> A.LabeledPolynomial(vararg pairs: Pair<Map<Symbol, UInt>, C>, toCheckInput: Boolean = true) : LabeledPolynomial<C> {
    if (!toCheckInput) return LabeledPolynomial<C>(pairs.toMap())

    val fixedCoefs = mutableMapOf<Map<Symbol, UInt>, C>()

    for (entry in pairs) {
        val key = entry.first.cleanUp()
        val value = entry.second
        fixedCoefs[key] = if (key in fixedCoefs) fixedCoefs[key]!! + value else value
    }

    return LabeledPolynomial<C>(
        fixedCoefs.filterValues { it != zero }
    )
}

@Suppress("FunctionName")
public fun <C, A: Ring<C>> A.LabeledPolynomial(coefs: Map<Map<Symbol, UInt>, C>) : LabeledPolynomial<C> = LabeledPolynomial(coefs, toCheckInput = true)
@Suppress("FunctionName")
public fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(coefs: Map<Map<Symbol, UInt>, C>) : LabeledPolynomial<C> = LabeledPolynomial(coefs, toCheckInput = true)

@Suppress("FunctionName")
public fun <C, A: Ring<C>> A.LabeledPolynomial(pairs: Collection<Pair<Map<Symbol, UInt>, C>>) : LabeledPolynomial<C> = LabeledPolynomial(pairs, toCheckInput = true)
@Suppress("FunctionName")
public fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(pairs: Collection<Pair<Map<Symbol, UInt>, C>>) : LabeledPolynomial<C> = LabeledPolynomial(pairs, toCheckInput = true)

@Suppress("FunctionName")
public fun <C, A: Ring<C>> A.LabeledPolynomial(vararg pairs: Pair<Map<Symbol, UInt>, C>) : LabeledPolynomial<C> = LabeledPolynomial(*pairs, toCheckInput = true)
@Suppress("FunctionName")
public fun <C, A: Ring<C>> LabeledPolynomialSpace<C, A>.LabeledPolynomial(vararg pairs: Pair<Map<Symbol, UInt>, C>) : LabeledPolynomial<C> = LabeledPolynomial(*pairs, toCheckInput = true)

//context(A)
//public fun <C, A: Ring<C>> Symbol.asLabeledPolynomial() : LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(mapOf(this to 1u) to one))
//context(LabeledPolynomialSpace<C, A>)
//public fun <C, A: Ring<C>> Symbol.asLabeledPolynomial() : LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(mapOf(this to 1u) to constantOne))

public fun <C> C.asLabeledPolynomial() : LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to this))

/**
 * Space of polynomials.
 *
 * @param C the type of operated polynomials.
 * @param A the intersection of [Ring] of [C] and [ScaleOperations] of [C].
 * @param ring the [A] instance.
 */
public class LabeledPolynomialSpace<C, A : Ring<C>>(
    public override val ring: A,
) : MultivariatePolynomialSpace<C, Symbol, LabeledPolynomial<C>>, PolynomialSpaceOverRing<C, LabeledPolynomial<C>, A> {
    public override operator fun Symbol.plus(other: Int): LabeledPolynomial<C> =
        if (other == 0) LabeledPolynomial<C>(mapOf(
            mapOf(this@plus to 1U) to constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this@plus to 1U) to constantOne,
            emptyMap<Symbol, UInt>() to constantOne * other,
        ))
    public override operator fun Symbol.minus(other: Int): LabeledPolynomial<C> =
        if (other == 0) LabeledPolynomial<C>(mapOf(
            mapOf(this@minus to 1U) to -constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this@minus to 1U) to -constantOne,
            emptyMap<Symbol, UInt>() to constantOne * other,
        ))
    public override operator fun Symbol.times(other: Int): LabeledPolynomial<C> =
        if (other == 0) zero
        else LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to constantOne * other,
        ))

    public override operator fun Int.plus(other: Symbol): LabeledPolynomial<C> =
        if (this == 0) LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to constantOne,
            emptyMap<Symbol, UInt>() to constantOne * this@plus,
        ))
    public override operator fun Int.minus(other: Symbol): LabeledPolynomial<C> =
        if (this == 0) LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to -constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to -constantOne,
            emptyMap<Symbol, UInt>() to constantOne * this@minus,
        ))
    public override operator fun Int.times(other: Symbol): LabeledPolynomial<C> =
        if (this == 0) zero
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to constantOne * this@times,
        ))

    /**
     * Returns sum of the polynomial and the integer represented as polynomial.
     *
     * The operation is equivalent to adding [other] copies of unit polynomial to [this].
     */
    public override operator fun LabeledPolynomial<C>.plus(other: Int): LabeledPolynomial<C> =
        if (other == 0) this
        else
            LabeledPolynomial(
                coefficients
                    .toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = getOrElse(degs) { constantZero } + other

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
    /**
     * Returns difference between the polynomial and the integer represented as polynomial.
     *
     * The operation is equivalent to subtraction [other] copies of unit polynomial from [this].
     */
    public override operator fun LabeledPolynomial<C>.minus(other: Int): LabeledPolynomial<C> =
        if (other == 0) this
        else
            LabeledPolynomial(
                coefficients
                    .toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = getOrElse(degs) { constantZero } - other

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
    /**
     * Returns product of the polynomial and the integer represented as polynomial.
     *
     * The operation is equivalent to sum of [other] copies of [this].
     */
    public override operator fun LabeledPolynomial<C>.times(other: Int): LabeledPolynomial<C> =
        if (other == 0) zero
        else LabeledPolynomial(
            coefficients
                .applyAndRemoveZeros {
                    for (degs in keys) this[degs] = this[degs]!! * other
                }
        )

    /**
     * Returns sum of the integer represented as polynomial and the polynomial.
     *
     * The operation is equivalent to adding [this] copies of unit polynomial to [other].
     */
    public override operator fun Int.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this == 0) other
        else
            LabeledPolynomial(
                other.coefficients
                    .toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = this@plus + getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
    /**
     * Returns difference between the integer represented as polynomial and the polynomial.
     *
     * The operation is equivalent to subtraction [this] copies of unit polynomial from [other].
     */
    public override operator fun Int.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this == 0) other
        else
            LabeledPolynomial(
                other.coefficients
                    .toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = this@minus - getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
    /**
     * Returns product of the integer represented as polynomial and the polynomial.
     *
     * The operation is equivalent to sum of [this] copies of [other].
     */
    public override operator fun Int.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this == 0) zero
        else LabeledPolynomial(
            other.coefficients
                .applyAndRemoveZeros {
                    for (degs in keys) this[degs] = this@times * this[degs]!!
                }
        )

    /**
     * Converts the integer [value] to polynomial.
     */
    public override fun number(value: Int): LabeledPolynomial<C> = number(constantNumber(value))

    public override operator fun C.plus(other: Symbol): LabeledPolynomial<C> =
        if (isZero()) LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to constantOne,
            emptyMap<Symbol, UInt>() to this@plus,
        ))
    public override operator fun C.minus(other: Symbol): LabeledPolynomial<C> =
        if (isZero()) LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to -constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to -constantOne,
            emptyMap<Symbol, UInt>() to this@minus,
        ))
    public override operator fun C.times(other: Symbol): LabeledPolynomial<C> =
        if (isZero()) zero
        else LabeledPolynomial<C>(mapOf(
            mapOf(other to 1U) to this@times,
        ))

    public override operator fun Symbol.plus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) LabeledPolynomial<C>(mapOf(
            mapOf(this@plus to 1U) to constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this@plus to 1U) to constantOne,
            emptyMap<Symbol, UInt>() to other,
        ))
    public override operator fun Symbol.minus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) LabeledPolynomial<C>(mapOf(
            mapOf(this@minus to 1U) to -constantOne,
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this@minus to 1U) to -constantOne,
            emptyMap<Symbol, UInt>() to other,
        ))
    public override operator fun Symbol.times(other: C): LabeledPolynomial<C> =
        if (other.isZero()) zero
        else LabeledPolynomial<C>(mapOf(
            mapOf(this@times to 1U) to other,
        ))

    /**
     * Returns sum of the constant represented as polynomial and the polynomial.
     */
    override operator fun C.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this.isZero()) other
        else with(other.coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to this@plus))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = this@plus + getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    /**
     * Returns difference between the constant represented as polynomial and the polynomial.
     */
    override operator fun C.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this.isZero()) other
        else with(other.coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to this@minus))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        forEach { (degs, c) -> if(degs.isNotEmpty()) this[degs] = -c }

                        val degs = emptyMap<Symbol, UInt>()

                        val result = this@minus - getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    /**
     * Returns product of the constant represented as polynomial and the polynomial.
     */
    override operator fun C.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        if (this.isZero()) zero
        else LabeledPolynomial<C>(
            other.coefficients
                .applyAndRemoveZeros {
                    for (degs in keys) this[degs] = this@times * this[degs]!!
                }
        )

    /**
     * Returns sum of the constant represented as polynomial and the polynomial.
     */
    override operator fun LabeledPolynomial<C>.plus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) this
        else with(coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to other))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        val degs = emptyMap<Symbol, UInt>()

                        val result = getOrElse(degs) { constantZero } + other

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    /**
     * Returns difference between the constant represented as polynomial and the polynomial.
     */
    override operator fun LabeledPolynomial<C>.minus(other: C): LabeledPolynomial<C> =
        if (other.isZero()) this
        else with(coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to other))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        forEach { (degs, c) -> if(degs.isNotEmpty()) this[degs] = -c }

                        val degs = emptyMap<Symbol, UInt>()

                        val result = getOrElse(degs) { constantZero } - other

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    /**
     * Returns product of the constant represented as polynomial and the polynomial.
     */
    override operator fun LabeledPolynomial<C>.times(other: C): LabeledPolynomial<C> =
        if (other.isZero()) zero
        else LabeledPolynomial<C>(
            coefficients
                .applyAndRemoveZeros {
                    for (degs in keys) this[degs] = this[degs]!! * other
                }
        )

    /**
     * Converts the constant [value] to polynomial.
     */
    public override fun number(value: C): LabeledPolynomial<C> =
        if (value == 0) zero
        else LabeledPolynomial(mapOf(emptyMap<Symbol, UInt>() to value))

    public override operator fun Symbol.unaryPlus(): LabeledPolynomial<C> =
        LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to constantOne,
        ))
    public override operator fun Symbol.unaryMinus(): LabeledPolynomial<C> =
        LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to -constantOne,
        ))
    public override operator fun Symbol.plus(other: Symbol): LabeledPolynomial<C> =
        if (this == other) LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to constantOne * 2
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to constantOne,
            mapOf(other to 1U) to constantOne,
        ))
    public override operator fun Symbol.minus(other: Symbol): LabeledPolynomial<C> =
        if (this == other) zero
        else LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U) to constantOne,
            mapOf(other to 1U) to -constantOne,
        ))
    public override operator fun Symbol.times(other: Symbol): LabeledPolynomial<C> =
        if (this == other) LabeledPolynomial<C>(mapOf(
            mapOf(this to 2U) to constantOne
        ))
        else LabeledPolynomial<C>(mapOf(
            mapOf(this to 1U, other to 1U) to constantOne,
        ))

    public override operator fun Symbol.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        with(other.coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(mapOf(this@plus to 1u) to constantOne))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        val degs = mapOf(this@plus to 1U)

                        val result = constantOne + getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    public override operator fun Symbol.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        with(other.coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(mapOf(this@minus to 1u) to constantOne))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        forEach { (degs, c) -> if(degs.isNotEmpty()) this[degs] = -c }

                        val degs = mapOf(this@minus to 1U)

                        val result = constantOne - getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    public override operator fun Symbol.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            other.coefficients
                .mapKeys { (degs, _) -> degs.toMutableMap().also{ it[this] = if (this in it) it[this]!! + 1U else 1U } }
        )

    public override operator fun LabeledPolynomial<C>.plus(other: Symbol): LabeledPolynomial<C> =
        with(coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(mapOf(other to 1u) to constantOne))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        val degs = mapOf(other to 1U)

                        val result = constantOne + getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    public override operator fun LabeledPolynomial<C>.minus(other: Symbol): LabeledPolynomial<C> =
        with(coefficients) {
            if (isEmpty()) LabeledPolynomial<C>(mapOf(mapOf(other to 1u) to constantOne))
            else LabeledPolynomial<C>(
                toMutableMap()
                    .apply {
                        val degs = mapOf(other to 1U)

                        val result = constantOne - getOrElse(degs) { constantZero }

                        if (result.isZero()) remove(degs)
                        else this[degs] = result
                    }
            )
        }
    public override operator fun LabeledPolynomial<C>.times(other: Symbol): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            coefficients
                .mapKeys { (degs, _) -> degs.toMutableMap().also{ it[other] = if (other in it) it[other]!! + 1U else 1U } }
        )

    /**
     * Returns negation of the polynomial.
     */
    override fun LabeledPolynomial<C>.unaryMinus(): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            coefficients.mapValues { -it.value }
        )
    /**
     * Returns sum of the polynomials.
     */
    override operator fun LabeledPolynomial<C>.plus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            buildCoefficients(coefficients.size + other.coefficients.size) {
                other.coefficients.mapValuesTo(this) { it.value }
                other.coefficients.mapValuesTo(this) { (key, value) -> if (key in this) this[key]!! + value else value }
            }
        )
    /**
     * Returns difference of the polynomials.
     */
    override operator fun LabeledPolynomial<C>.minus(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        LabeledPolynomial<C>(
            buildCoefficients(coefficients.size + other.coefficients.size) {
                other.coefficients.mapValuesTo(this) { it.value }
                other.coefficients.mapValuesTo(this) { (key, value) -> if (key in this) this[key]!! - value else -value }
            }
        )
    /**
     * Returns product of the polynomials.
     */
    override operator fun LabeledPolynomial<C>.times(other: LabeledPolynomial<C>): LabeledPolynomial<C> =
        when {
            isZero() -> zero
            other.isZero() -> zero
            else -> LabeledPolynomial<C>(
                buildCoefficients(coefficients.size * other.coefficients.size) {
                    for ((degs1, c1) in coefficients) for ((degs2, c2) in other.coefficients) {
                        val degs = degs1.toMutableMap()
                        degs2.mapValuesTo(degs) { (variable, deg) -> degs.getOrElse(variable) { 0u } + deg }
                        val c = c1 * c2
                        this[degs] = if (degs in this) this[degs]!! + c else c
                    }
                }
            )
        }

    /**
     * Instance of zero polynomial (zero of the polynomial ring).
     */
    override val zero: LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to constantZero))
    /**
     * Instance of unit polynomial (unit of the polynomial ring).
     */
    override val one: LabeledPolynomial<C> = LabeledPolynomial<C>(mapOf(emptyMap<Symbol, UInt>() to constantOne))

    /**
     * Checks equality of the polynomials.
     */
    override infix fun LabeledPolynomial<C>.equalsTo(other: LabeledPolynomial<C>): Boolean =
        when {
            this === other -> true
            else -> coefficients.size == other.coefficients.size &&
                    coefficients.all { (key, value) -> with(other.coefficients) { key in this && this[key] == value } }
        }

    /**
     * Degree of the polynomial, [see also](https://en.wikipedia.org/wiki/Degree_of_a_polynomial). If the polynomial is
     * zero, degree is -1.
     */
    override val LabeledPolynomial<C>.degree: Int
        get() = coefficients.entries.maxOfOrNull { (degs, c) -> if (c.isZero()) -1 else degs.values.sum().toInt() } ?: -1
    /**
     * Map that associates variables (that appear in the polynomial in positive exponents) with their most exponents
     * in which they are appeared in the polynomial.
     *
     * As consequence all values in the map are positive integers. Also, if the polynomial is constant, the map is empty.
     * And keys of the map is the same as in [variables].
     */
    public override val LabeledPolynomial<C>.degrees: Map<Symbol, UInt>
        get() =
            buildMap {
                coefficients.entries.forEach { (degs, c) ->
                    if (c.isNotZero()) degs.mapValuesTo(this) { (variable, deg) ->
                        max(getOrElse(variable) { 0u }, deg)
                    }
                }
            }
    /**
     * Counts degree of the polynomial by the specified [variable].
     */
    public override fun LabeledPolynomial<C>.degreeBy(variable: Symbol): UInt =
        coefficients.entries.maxOfOrNull { (degs, c) -> if (c.isZero()) 0u else degs.getOrElse(variable) { 0u } } ?: 0u
    /**
     * Counts degree of the polynomial by the specified [variables].
     */
    public override fun LabeledPolynomial<C>.degreeBy(variables: Collection<Symbol>): UInt =
        coefficients.entries.maxOfOrNull { (degs, c) -> if (c.isZero()) 0u else degs.filterKeys { it in variables }.values.sum() } ?: 0u
    /**
     * Set of all variables that appear in the polynomial in positive exponents.
     */
    public override val LabeledPolynomial<C>.variables: Set<Symbol>
        get() =
            buildSet {
                coefficients.entries.forEach { (degs, c) -> if (c.isNotZero()) addAll(degs.keys) }
            }
    /**
     * Count of all variables that appear in the polynomial in positive exponents.
     */
    public override val LabeledPolynomial<C>.countOfVariables: Int get() = variables.size

    /**
     * Checks if the instant is constant polynomial (of degree no more than 0) over considered ring.
     */
    override fun LabeledPolynomial<C>.isConstant(): Boolean =
        coefficients.all { (degs, c) -> degs.isEmpty() || c.isZero() }
    /**
     * Checks if the instant is constant non-zero polynomial (of degree no more than 0) over considered ring.
     */
    override fun LabeledPolynomial<C>.isNonZeroConstant(): Boolean =
        with(coefficients) {
            var foundAbsoluteTermAndItIsNotZero = false
            for ((degs, c) in this) {
                if (degs.isNotEmpty()) if (c.isNotZero()) return@with false
                else {
                    if (c.isZero()) return@with false
                    else foundAbsoluteTermAndItIsNotZero = true
                }
            }
            foundAbsoluteTermAndItIsNotZero
        }
    /**
     * If polynomial is a constant polynomial represents and returns it as constant.
     * Otherwise, (when the polynomial is not constant polynomial) returns `null`.
     */
    override fun LabeledPolynomial<C>.asConstantOrNull(): C? =
        with(coefficients) {
            if(isConstant()) getOrElse(emptyMap()) { constantZero }
            else null
        }

//    @Suppress("NOTHING_TO_INLINE")
//    public inline fun LabeledPolynomial<C>.substitute(argument: Map<Symbol, C>): LabeledPolynomial<C> = this.substitute(ring, argument)
//    @Suppress("NOTHING_TO_INLINE")
//    @JvmName("substitutePolynomial")
//    public inline fun LabeledPolynomial<C>.substitute(argument: Map<Symbol, LabeledPolynomial<C>>): LabeledPolynomial<C> = this.substitute(ring, argument)
//
//    @Suppress("NOTHING_TO_INLINE")
//    public inline fun LabeledPolynomial<C>.asFunction(): (Map<Symbol, C>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }
//    @Suppress("NOTHING_TO_INLINE")
//    public inline fun LabeledPolynomial<C>.asFunctionOnConstants(): (Map<Symbol, C>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }
//    @Suppress("NOTHING_TO_INLINE")
//    public inline fun LabeledPolynomial<C>.asFunctionOnPolynomials(): (Map<Symbol, LabeledPolynomial<C>>) -> LabeledPolynomial<C> = { this.substitute(ring, it) }
//
//    @Suppress("NOTHING_TO_INLINE")
//    public inline operator fun LabeledPolynomial<C>.invoke(argument: Map<Symbol, C>): LabeledPolynomial<C> = this.substitute(ring, argument)
//    @Suppress("NOTHING_TO_INLINE")
//    @JvmName("invokePolynomial")
//    public inline operator fun LabeledPolynomial<C>.invoke(argument: Map<Symbol, LabeledPolynomial<C>>): LabeledPolynomial<C> = this.substitute(ring, argument)

    // TODO: Move to other internal utilities with context receiver
    @JvmName("applyAndRemoveZerosInternal")
    internal fun MutableMap<Map<Symbol, UInt>, C>.applyAndRemoveZeros(block: MutableMap<Map<Symbol, UInt>, C>.() -> Unit) : MutableMap<Map<Symbol, UInt>, C> {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        block()
        for ((degs, c) in this) if (c.isZero()) this.remove(degs)
        return this
    }
    internal fun Map<Map<Symbol, UInt>, C>.applyAndRemoveZeros(block: MutableMap<Map<Symbol, UInt>, C>.() -> Unit) : Map<Map<Symbol, UInt>, C> =
        toMutableMap().applyAndRemoveZeros(block)
    @OptIn(ExperimentalTypeInference::class)
    internal inline fun buildCoefficients(@BuilderInference builderAction: MutableMap<Map<Symbol, UInt>, C>.() -> Unit): Map<Map<Symbol, UInt>, C> {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        return buildMap {
            builderAction()
            for ((degs, c) in this) if (c.isZero()) this.remove(degs)
        }
    }
    @OptIn(ExperimentalTypeInference::class)
    internal inline fun buildCoefficients(capacity: Int, @BuilderInference builderAction: MutableMap<Map<Symbol, UInt>, C>.() -> Unit): Map<Map<Symbol, UInt>, C> {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        return buildMap(capacity) {
            builderAction()
            for ((degs, c) in this) if (c.isZero()) this.remove(degs)
        }
    }
}