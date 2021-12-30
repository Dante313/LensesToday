package ru.lenses.lensestoday.extensions

import ru.lenses.lensestoday.room.Lenses

fun Lenses.isOverWearing() = this.lensesAlreadyWear > this.lensesReplacePeriod

fun Lenses.countOverWearing() = this.lensesAlreadyWear - this.lensesReplacePeriod