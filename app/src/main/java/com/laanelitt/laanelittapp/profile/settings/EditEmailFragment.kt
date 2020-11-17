package com.laanelitt.laanelittapp.profile.settings

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
import com.laanelitt.laanelittapp.databinding.FragmentEditEmailBinding
import com.laanelitt.laanelittapp.databinding.FragmentEditNameBinding
import com.laanelitt.laanelittapp.databinding.FragmentSettingsBinding
import com.laanelitt.laanelittapp.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_edit_email.*

class EditEmailFragment : Fragment() {


    private lateinit var binding: FragmentEditEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_email, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = edit_email.getEditText()?.getText()
        //mTextInputLayout.getEditText().setText("ur text");

        binding.editEmailBtn.setOnClickListener {
            Toast.makeText(context, "ny mail: " + text.toString(), Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.settingsFragment)
        }

    }
}