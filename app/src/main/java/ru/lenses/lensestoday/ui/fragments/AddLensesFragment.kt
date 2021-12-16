package ru.lenses.lensestoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentAddLensesBinding
import ru.lenses.lensestoday.room.Lenses

@AndroidEntryPoint
class AddLensesFragment : Fragment() {

    private lateinit var binding: FragmentAddLensesBinding
    private val viewModel by viewModels<AddLensesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLensesBinding.inflate(inflater)
        createReplacementPeriodAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancelCreation.setOnClickListener {
            findNavController().popBackStack(R.id.inviteFragment, false)
        }

        binding.btnCreateLenses.setOnClickListener {
            createNewLenses()
            findNavController().navigate(R.id.action_addLensesFragment_to_mainLensesFragment)
        }
    }

    private fun createReplacementPeriodAdapter() {
        val items = listOf("14 дней", "1 месяц", "3 месяца")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.dropdownTvReplacePeriod.apply {
            setText(items[0])
            setAdapter(adapter)
        }
    }

    private fun createNewLenses() {
        val lensesTitle = binding.editTextLensesTitle.text.toString()
        val lensesReplacePeriod = when (binding.dropdownTvReplacePeriod.text.toString()) {
            "14 дней" -> 14
            "1 месяц" -> 30
            "3 месяца" -> 90
            else -> 14
        }
        val lensesAlreadyWear = binding.editTextLensesAlreadyWearing.text.toString().toInt()
        viewModel.addLenses(
            Lenses(
                lensesTitle = "lensesTitle",
                lensesReplacePeriod = 14,
                lensesAlreadyWear = 3
            )
        )
    }
}