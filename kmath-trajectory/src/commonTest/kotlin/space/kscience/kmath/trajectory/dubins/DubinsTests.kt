/*
 * Copyright 2018-2022 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.trajectory.dubins

import space.kscience.kmath.geometry.Euclidean2DSpace
import space.kscience.kmath.trajectory.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class DubinsTests {

    @Test
    fun dubinsTest() = with(Euclidean2DSpace){
        val straight = StraightTrajectory2D(vector(0.0, 0.0), vector(100.0, 100.0))
        val lineP1 = straight.shift(1, 10.0).inverse()

        val start = DubinsPose2D(straight.end, straight.bearing)
        val end = DubinsPose2D(lineP1.start, lineP1.bearing)
        val radius = 2.0
        val dubins = DubinsPath.all(start, end, radius)

        val absoluteDistance = start.distanceTo(end)
        println("Absolute distance: $absoluteDistance")

        val expectedLengths = mapOf(
            DubinsPath.TYPE.RLR to 13.067681939031397,
            DubinsPath.TYPE.RSR to 12.28318530717957,
            DubinsPath.TYPE.LSL to 32.84955592153878,
            DubinsPath.TYPE.RSL to 23.37758938854081,
            DubinsPath.TYPE.LSR to 23.37758938854081
        )

        expectedLengths.forEach {
            val path = dubins.find { p -> p.type === it.key }
            assertNotNull(path, "Path ${it.key} not found")
            println("${it.key}: ${path.length}")
            assertTrue(it.value.equalFloat(path.length))

            assertTrue(start.equalsFloat(path.a.start))
            assertTrue(end.equalsFloat(path.c.end))

            // Not working, theta double precision inaccuracy
            if (path.b is CircleTrajectory2D) {
                val b = path.b as CircleTrajectory2D
                assertTrue(path.a.end.equalsFloat(b.start))
                assertTrue(path.c.start.equalsFloat(b.end))
            } else if (path.b is StraightTrajectory2D) {
                val b = path.b as StraightTrajectory2D
                assertTrue(path.a.end.equalsFloat(DubinsPose2D(b.start, b.bearing)))
                assertTrue(path.c.start.equalsFloat(DubinsPose2D(b.end, b.bearing)))
            }
        }
    }
}
