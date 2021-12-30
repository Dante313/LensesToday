package ru.lenses.lensestoday.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentAddLensesBinding
import ru.lenses.lensestoday.room.Lenses
import ru.lenses.lensestoday.ui.viewmodels.AddLensesViewModel

@AndroidEntryPoint
class AddLensesFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddLensesBinding
    private val viewModel by viewModels<AddLensesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        if (bottomSheetDialog is BottomSheetDialog) {
            bottomSheetDialog.behavior.apply {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLensesBinding.inflate(inflater)
        createReplacementPeriodAdapter()

        binding.editTextLensesAlreadyWearing.transformationMethod = null

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancelCreation.setOnClickListener {
            findNavController().popBackStack(R.id.inviteFragment, false)
        }

        binding.btnCreateLenses.setOnClickListener {
            val lensesTitle = binding.editTextLensesTitle.text.toString()
            val lensesReplacePeriod = when (binding.dropdownTvReplacePeriod.text.toString()) {
                "14 дней" -> 14
                "1 месяц" -> 30
                "3 месяца" -> 90
                else -> 14
            }

            if (validateLensesInput(lensesReplacePeriod)) {
                val lensesAlreadyWear = binding.editTextLensesAlreadyWearing.text.toString().toInt()
                viewModel.addLenses(
                    Lenses(
                        lensesTitle = lensesTitle,
                        lensesReplacePeriod = lensesReplacePeriod,
                        lensesAlreadyWear = lensesAlreadyWear
                    )
                )
                findNavController().navigate(R.id.action_addLensesFragment_to_mainLensesFragment)
            }
        }
    }

    private fun createReplacementPeriodAdapter() {
        val items = listOf("14 дней", "1 месяц", "3 месяца")
        val adapter = ArrayAdapter(requireContext(), R.layout.wearing_period_list, items)
        binding.dropdownTvReplacePeriod.apply {
            setText(items[0])
            setAdapter(adapter)
        }
    }

    private fun validateLensesInput(replacementPeriod: Int): Boolean {
        binding.textInputLensesTitle.error = null
        binding.textInputLensesAlreadyWearing.error = null

        return when {
            binding.editTextLensesTitle.text.isNullOrBlank() -> {
                binding.textInputLensesTitle.apply {
                    requestFocus()
                    error = getString(R.string.empty_input_error)
                }
                false
            }
            binding.editTextLensesAlreadyWearing.text.toString()
                .toInt() >= replacementPeriod -> {
                binding.textInputLensesAlreadyWearing.apply {
                    requestFocus()
                    error = getString(R.string.faulty_input_error)
                }
                false
            }
            else -> true
        }
    }
}