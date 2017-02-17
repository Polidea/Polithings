package com.polidea.androidthings.driver.uln2003.driver

enum class ULN2003Resolution(val id: Int) {
    FULL(Metadata.ID_FULL),
    HALF(Metadata.ID_HALF);

    companion object {
        fun getFromId(id: Int): ULN2003Resolution {
            val resolution = when (id) {
                Metadata.ID_FULL -> FULL
                Metadata.ID_HALF -> HALF
                else -> throw IllegalArgumentException("Invalid resolution id: $id")
            }

            return resolution
        }
    }

    private class Metadata {
        companion object {
            val ID_FULL = 1
            val ID_HALF = 2
        }
    }
}