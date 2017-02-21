package com.polidea.androidthings.driver.a4988.driver

enum class A4988Resolution(val id: Int) {
    FULL(Metadata.ID_FULL),
    HALF(Metadata.ID_HALF),
    QUARTER(Metadata.ID_QUARTER),
    EIGHT(Metadata.ID_EIGHT),
    SIXTEENTH(Metadata.ID_SIXTEENTH);

    fun getStepMultiplier()
            = when (this) {
                FULL -> 16
                HALF -> 8
                QUARTER -> 4
                EIGHT -> 2
                SIXTEENTH -> 1
            }

    companion object {
        fun getFromId(id: Int): A4988Resolution =
                values().firstOrNull { it.id == id } ?:
                        throw IllegalArgumentException("Invalid resolution id: $id")
    }

    private class Metadata {
        companion object {
            val ID_FULL = 1
            val ID_HALF = 2
            val ID_QUARTER = 3
            val ID_EIGHT = 4
            val ID_SIXTEENTH = 5
        }
    }
}