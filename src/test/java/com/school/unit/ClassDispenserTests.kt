package com.school.unit

import com.school.service.utils.oneLess
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ClassDispenserTests {
    private val auxiliaryMap = HashMap<KeyMock, Int>()
    private val mockedKeys = listOf(KeyMock("1"), KeyMock("2"), KeyMock("3"))
    private val decrementStart = 5

    @BeforeEach
    fun seedAuxiliaryMap() {
        for (key in mockedKeys) {
            auxiliaryMap[key] = decrementStart
        }
    }

    @Test
    fun mapClearedInMovesEqualToAllValuesSize() {
        var counter = 0
        val moves = auxiliaryMap.keys.size * decrementStart
        println(auxiliaryMap)
        while (auxiliaryMap.isNotEmpty()) {
            auxiliaryMap oneLess auxiliaryMap.keys.random()
            counter++
        }
        assertEquals(moves, counter)
    }

    inner class KeyMock(private val identifier: String) {
        override fun toString(): String {
            return "KeyMock(identifier='$identifier')"
        }
    }
}