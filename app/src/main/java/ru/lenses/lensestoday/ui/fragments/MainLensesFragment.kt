package ru.lenses.lensestoday.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentMainLensesBinding
import ru.lenses.lensestoday.model.WearingProgress
import ru.lenses.lensestoday.room.Lenses
import java.util.*

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
            lenses?.let {
                binding.tvLensesTitle.text = getString(R.string.lenses_name, lenses.lensesTitle)
                binding.tvLensesReplacePeriod.text =
                    getString(R.string.replacement_period, lenses.lensesReplacePeriod)
                binding.tvLensesAlreadyWear.text =
                    getString(R.string.already_wear, lenses.lensesAlreadyWear)

                if (lenses.wearingDay != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    createWearingLensesDialog(lenses)
                }

                viewModel.countWearingProgress(lenses.lensesReplacePeriod, lenses.lensesAlreadyWear)
            } ?: findNavController().navigate(R.id.action_mainLensesFragment_to_inviteFragment)
        }

        viewModel.wearingProgressLiveData.observe(viewLifecycleOwner) { wearingProgress ->
            setWearingProgress(wearingProgress)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_lenses) {
            createDeleteLensesDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDeleteLensesDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.deleting_lenses))
            .setMessage(getString(R.string.are_you_want_to_delete_lenses))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteAllLenses()
                findNavController().navigate(R.id.action_mainLensesFragment_to_inviteFragment)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun createWearingLensesDialog(lenses: Lenses) {
        val view = View.inflate(requireActivity(), R.layout.wearing_dialog, null)
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setCancelable(false)
            .show()

        view.findViewById<Button>(R.id.btn_positive_wearing).setOnClickListener {
            viewModel.updateLenses(
                lenses.copy(
                    lensesAlreadyWear = lenses.lensesAlreadyWear.inc(),
                    wearingDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            )
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_negative_wearing).setOnClickListener {
            viewModel.updateLenses(
                lenses.copy(wearingDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            )
            dialog.dismiss()
        }
    }

    private fun setWearingProgress(wearingProgress: WearingProgress) {
        binding.pbWearingProgress.apply {
            max = wearingProgress.maxDays
            progress = wearingProgress.leftDays
            progressTintList = ColorStateList.valueOf(
                when {
                    wearingProgress.percentLeft <= 5 -> {
                        binding.imgLensStatus.setImageResource(R.drawable.lens_dead)
                        Color.RED
                    }
                    wearingProgress.percentLeft in 6..50 -> {
                        binding.imgLensStatus.setImageResource(R.drawable.lens_damaged)
                        Color.YELLOW
                    }
                    else -> {
                        binding.imgLensStatus.setImageResource(R.drawable.lens_new)
                        Color.GREEN
                    }
                }
            )
        }
    }
}