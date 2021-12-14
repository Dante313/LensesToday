package ru.lenses.lensestoday.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lenses")
data class Lenses(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lenses_id")
    val lensesId: Int = 0,

    @ColumnInfo(name = "lenses_title")
    val lensesTitle: String,

    @ColumnInfo(name = "lenses_replace_period")
    val lensesReplacePeriod: Int,

    @ColumnInfo(name = "lenses_already_wear")
    val lensesAlreadyWear: Int
)