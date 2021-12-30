package ru.lenses.lensestoday.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.lenses.lensestoday.R
import ru.lenses.lensestoday.databinding.FragmentInviteBinding


class InviteFragment : Fragment() {

    private lateinit var binding: FragmentInviteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteBinding.inflate(inflater)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.invite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_lenses) {
            findNavController().navigate(R.id.action_inviteFragment_to_addLensesFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}