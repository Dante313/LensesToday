package ru.lenses.lensestoday.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lenses.lensestoday.model.WearingProgress
import ru.lenses.lensestoday.repository.LensesRepository
import ru.lenses.lensestoday.room.Lenses
import javax.inject.Inject

@HiltViewModel
class MainLensesViewModel @Inject constructor(
    private val lensesRepository: LensesRepository
) : ViewModel() {

    private val _wearingProgressLiveData: MutableLiveData<WearingProgress> = MutableLiveData()
    val wearingProgressLiveData: LiveData<WearingProgress> = _wearingProgressLiveData

    val readLensesLiveData: LiveData<Lenses> = lensesRepository.readLenses()

    fun deleteAllLenses() = viewModelScope.launch(Dispatchers.IO) {
        lensesRepository.deleteAllLenses()
    }

    fun countWearingProgress(
        maxDays: Int,
        wearingDays: Int
    ) {
        val leftToWear = maxDays - wearingDays
        val leftToWearPercent = 100 / maxDays * leftToWear

        _wearingProgressLiveData.postValue(
            WearingProgress(
                maxDays,
                leftToWear,
                leftToWearPercent
            )
        )
    }
}