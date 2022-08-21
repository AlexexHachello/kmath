/*
 * Copyright 2018-2021 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.multik

import org.jetbrains.kotlinx.multik.default.DefaultEngine
import space.kscience.kmath.nd.StructureND
import space.kscience.kmath.nd.one
import space.kscience.kmath.operations.DoubleField
import space.kscience.kmath.tensors.core.DoubleTensorAlgebra
import space.kscience.kmath.tensors.core.tensorAlgebra
import kotlin.test.Test
import kotlin.test.assertTrue

internal class MultikNDTest {
    val multikAlgebra = MultikDoubleAlgebra(DefaultEngine())

    @Test
    fun basicAlgebra(): Unit = with(multikAlgebra) {
        one(2, 2) + 1.0
    }

    @Test
    fun dotResult() {
        val dim = 100

        val tensor1 = DoubleTensorAlgebra.randomNormal(shape = intArrayOf(dim, dim), 12224)
        val tensor2 = DoubleTensorAlgebra.randomNormal(shape = intArrayOf(dim, dim), 12225)

        val multikResult = with(multikAlgebra) {
            tensor1 dot tensor2
        }

        val defaultResult = with(DoubleField.tensorAlgebra) {
            tensor1 dot tensor2
        }

        assertTrue {
            StructureND.contentEquals(multikResult, defaultResult)
        }

    }
}