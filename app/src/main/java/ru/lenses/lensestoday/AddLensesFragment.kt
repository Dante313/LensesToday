package ru.lenses.lensestoday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import ru.lenses.lensestoday.databinding.FragmentAddLensesBinding

class AddLensesFragment : Fragment() {

    private lateinit var binding: FragmentAddLensesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLensesBinding.inflate(inflater)

        val items = listOf("Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.dropdownTvReplacePeriod.setAdapter(adapter)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateLenses.setOnClickListener {
            findNavController().navigate(R.id.action_addLensesFragment_to_mainLensesFragment)
        }
    }

}