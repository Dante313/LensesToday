package ru.lenses.lensestoday

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.lenses.lensestoday.databinding.FragmentMainLensesBinding


class MainLensesFragment : Fragment() {

    private lateinit var binding: FragmentMainLensesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLensesBinding.inflate(inflater)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarDelete)
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

}