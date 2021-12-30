package ru.lenses.lensestoday.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentMainLensesBinding
import ru.lenses.lensestoday.extensions.countOverWearing
import ru.lenses.lensestoday.extensions.isOverWearing
import ru.lenses.lensestoday.model.WearingProgress
import ru.lenses.lensestoday.room.Lenses
import ru.lenses.lensestoday.ui.viewmodels.MainLensesViewModel
import java.util.*

@AndroidEntryPoint
class MainLensesFragment : Fragment() {

    private lateinit var binding: FragmentMainLensesBinding
    private val viewModel by viewModels<MainLensesViewModel>()

    private var notWearingSnackBar: Snackbar? = null
    private var wearingSnackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLensesBinding.inflate(inflater)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.readLensesLiveData.observe(viewLifecycleOwner) { lenses ->
            lenses?.let {
                binding.apply {
                    tvLensesTitle.text = getString(R.string.lenses_name, lenses.lensesTitle)
                    tvLensesReplacePeriod.text =
                        getString(R.string.replacement_period, lenses.lensesReplacePeriod)
                    tvLensesAlreadyWear.text =
                        getString(R.string.already_wear, lenses.lensesAlreadyWear)
                    tvProgressReplacementPeriod.text = lenses.lensesReplacePeriod.toString()
                }

                if (lenses.wearingDay != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    createWearingLensesDialog(lenses)
                }

                if (lenses.isWearing) {

                    notWearingSnackBar = Snackbar
                        .make(
                            binding.constraintLayout,
                            getString(R.string.decided_to_not_wear),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setupSnackBarStyle()
                        .setAction(getString(R.string.yes)) {
                            createDecidedToNotWearDialog(lenses)
                        }
                    notWearingSnackBar?.show()

                } else {
                    wearingSnackBar = Snackbar
                        .make(
                            binding.constraintLayout,
                            getString(R.string.decided_to_wear),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setupSnackBarStyle()
                        .setAction(getString(R.string.yes)) {
                            createDecidedToWearDialog(lenses)
                        }
                    wearingSnackBar?.show()
                }

                //TODO: диалог показывается до диалога о ношении и после. сделать только после
                if (lenses.isOverWearing()) createOverWearingDialog(lenses)

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
            increaseWearingDay(lenses)
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btn_negative_wearing).setOnClickListener {
            notWearingDay(lenses)
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

//    private fun showSnackbar(view: View, snackbarTitleId: Int) {
//        val snackbar = Snackbar.make(
//            binding.constraintLayout, "", Snackbar.LENGTH_INDEFINITE
//        )
//        val customSnackView = layoutInflater.inflate(R.layout.change_wear_status_snackbar, null)
//        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
//
//        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
//        snackbarLayout.setPadding(0, 0, 0, 0)
//        snackbarLayout.applyPaddingByInsets(applyBottom = true)
//
//
//        val textSnackbarTitle = customSnackView.findViewById<TextView>(R.id.text_snackbar_title)
//        textSnackbarTitle.text = getString(snackbarTitleId)
//
//        val btnChangedMind = customSnackView.findViewById<Button>(R.id.btn_changed_mind)
//        snackbarLayout.addView(customSnackView, 0)
//        snackbar.show()
//    }

    private fun Snackbar.setupSnackBarStyle() = this
        .setTextColor(Color.WHITE)
        .setActionTextColor(Color.WHITE)
        .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.teal))

    private fun createDecidedToWearDialog(lenses: Lenses) {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.decided_to_wear))
            .setMessage(getString(R.string.r_u_rly_decided_to_wear))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                increaseWearingDay(lenses)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun createDecidedToNotWearDialog(lenses: Lenses) {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.decided_to_not_wear))
            .setMessage(getString(R.string.r_u_rly_decided_to_not_wear))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                decreaseWearingDay(lenses)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun createOverWearingDialog(lenses: Lenses) {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.lenses_replacement))
            .setMessage(getString(R.string.warning_lenses_overwearing, lenses.countOverWearing()))
            .setPositiveButton(R.string.wear_anyway) { _, _ -> }
            .setNegativeButton(R.string.delete) { _, _ ->
                viewModel.deleteAllLenses()
            }
            .show()
    }

    private fun increaseWearingDay(lenses: Lenses) {
        viewModel.updateLenses(
            lenses.copy(
                lensesAlreadyWear = lenses.lensesAlreadyWear.inc(),
                wearingDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                isWearing = true,
            )
        )
    }

    private fun decreaseWearingDay(lenses: Lenses) {
        viewModel.updateLenses(
            lenses.copy(
                lensesAlreadyWear = lenses.lensesAlreadyWear.dec(),
                wearingDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                isWearing = false,
            )
        )
    }

    private fun notWearingDay(lenses: Lenses) {
        viewModel.updateLenses(
            lenses.copy(
                wearingDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                isWearing = false,
            )
        )
    }

    override fun onDetach() {
        super.onDetach()
        notWearingSnackBar?.takeIf { it.isShown }?.dismiss()
        wearingSnackBar?.takeIf { it.isShown }?.dismiss()
    }
}