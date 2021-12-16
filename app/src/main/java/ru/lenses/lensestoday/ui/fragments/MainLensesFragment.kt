package ru.lenses.lensestoday.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentMainLensesBinding

@AndroidEntryPoint
class MainLensesFragment : Fragment() {

    private lateinit var binding: FragmentMainLensesBinding
    private val viewModel by viewModels<MainLensesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLensesBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.readLensesLiveData.observe(viewLifecycleOwner) { lenses ->
            binding.tvLensesTitle.text = lenses.lensesTitle
            binding.tvLensesReplacePeriod.text = lenses.lensesReplacePeriod.toString()
            binding.tvLensesAlreadyWear.text = lenses.lensesAlreadyWear.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_lenses) {
            viewModel.deleteAllLenses()
            findNavController().navigate(R.id.action_mainLensesFragment_to_inviteFragment)
        }
        return super.onOptionsItemSelected(item)
    }

}