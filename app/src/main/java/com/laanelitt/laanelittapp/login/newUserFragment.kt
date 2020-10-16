package com.laanelitt.laanelittapp.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNewUserBinding


class NewUserFragment : Fragment() {

    private lateinit var binding: FragmentNewUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_user, container, false
        )

        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newUsernamet = view.findViewById<EditText>(R.id.new_username)
        val newPassword1 = view.findViewById<EditText>(R.id.new_password_1)
        val newPassword2 = view.findViewById<EditText>(R.id.new_password_2)

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

        binding.newUserBtn.setOnClickListener {
            register(
                newPassword1.text.toString(),
                newPassword2.text.toString()
            )
        }
    }

    private fun register(password1: String, password2: String) {
        if (password1 == password2) {
            findNavController().navigate(R.id.searchPageFragment)

        } else {
            Toast.makeText(requireContext(),"Feil passord", Toast.LENGTH_LONG).show();
        }
    }
}