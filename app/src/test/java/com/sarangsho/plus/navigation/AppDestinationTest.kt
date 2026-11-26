package com.sarangsho.plus.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppDestinationTest {
    @Test
    fun everyDestinationRoundTripsThroughItsStableRoute() {
        AppDestination.entries.forEach { destination ->
            assertEquals(destination, AppDestination.fromRoute(destination.route))
        }
    }

    @Test
    fun everyRouteIsUnique() {
        val routes = AppDestination.entries.map(AppDestination::route)
        assertEquals(routes.size, routes.toSet().size)
    }

    @Test
    fun unknownOrMissingRoutesAreRejected() {
        assertNull(AppDestination.fromRoute(null))
        assertNull(AppDestination.fromRoute(""))
        assertNull(AppDestination.fromRoute("article/"))
        assertNull(AppDestination.fromRoute("not-a-real-screen"))
    }
}
