package ru.lenses.lensestoday.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lenses.lensestoday.repository.LensesRepository
import ru.lenses.lensestoday.room.Lenses
import javax.inject.Inject

@HiltViewModel
class MainLensesViewModel @Inject constructor(
    private val lensesRepository: LensesRepository
) : ViewModel() {

    val readLensesLiveData: LiveData<Lenses> = lensesRepository.readLenses()

    fun deleteAllLenses() = viewModelScope.launch(Dispatchers.IO) {
        lensesRepository.deleteAllLenses()
    }
}