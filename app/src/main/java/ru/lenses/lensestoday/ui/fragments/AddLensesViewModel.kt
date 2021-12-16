package ru.lenses.lensestoday.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lenses.lensestoday.repository.LensesRepository
import ru.lenses.lensestoday.room.Lenses
import javax.inject.Inject

@HiltViewModel
class AddLensesViewModel @Inject constructor(
    private val lensesRepository: LensesRepository
) : ViewModel() {

    fun addLenses(lenses: Lenses) {
        viewModelScope.launch(Dispatchers.IO) {
            lensesRepository.addLenses(lenses)
        }
    }

}