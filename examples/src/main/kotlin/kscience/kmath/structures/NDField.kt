package kscience.kmath.structures

import kotlinx.coroutines.GlobalScope
import kscience.kmath.nd.NDAlgebra
import kscience.kmath.nd.NDStructure
import kscience.kmath.nd.field
import kscience.kmath.nd.real
import kscience.kmath.nd4j.Nd4jArrayField
import kscience.kmath.operations.RealField
import kscience.kmath.operations.invoke
import org.nd4j.linalg.factory.Nd4j
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.system.measureTimeMillis

internal inline fun measureAndPrint(title: String, block: () -> Unit) {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val time = measureTimeMillis(block)
    println("$title completed in $time millis")
}

fun main() {
    // initializing Nd4j
    Nd4j.zeros(0)
    val dim = 1000
    val n = 1000

    // automatically build context most suited for given type.
    val autoField = NDAlgebra.field(RealField, Buffer.Companion::auto, dim, dim)
    // specialized nd-field for Double. It works as generic Double field as well
    val specializedField = NDAlgebra.real(dim, dim)
    //A generic boxing field. It should be used for objects, not primitives.
    val boxingField =  NDAlgebra.field(RealField, Buffer.Companion::boxing, dim, dim)
    // Nd4j specialized field.
    val nd4jField = Nd4jArrayField.real(dim, dim)

    measureAndPrint("Automatic field addition") {
        autoField {
            var res: NDStructure<Double> = one
            repeat(n) { res += 1.0 }
        }
    }

    measureAndPrint("Element addition") {
        boxingField {
        var res: NDStructure<Double> = one
            repeat(n) { res += 1.0 }
        }
    }

    measureAndPrint("Specialized addition") {
        specializedField {
            var res: NDStructure<Double> = one
            repeat(n) { res += 1.0 }
        }
    }

    measureAndPrint("Nd4j specialized addition") {
        nd4jField {
            var res:NDStructure<Double> = one
            repeat(n) { res += 1.0 }
        }
    }

    measureAndPrint("Lazy addition") {
        val res = specializedField.one.mapAsync(GlobalScope) {
            var c = 0.0
            repeat(n) {
                c += 1.0
            }
            c
        }

        res.elements().forEach { it.second }
    }

    measureAndPrint("Generic addition") {
        //genericField.run(action)
        boxingField {
            var res: NDStructure<Double> = one
            repeat(n) {
                res += 1.0 // couldn't avoid using `one` due to resolution ambiguity }
            }
        }
    }
}
