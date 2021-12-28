package ru.lenses.lensestoday.model

data class WearingProgress(
    val maxDays: Int,
    val leftDays: Int,
    val percentLeft: Int
)