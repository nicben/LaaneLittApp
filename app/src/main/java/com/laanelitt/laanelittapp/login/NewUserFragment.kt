package com.laanelitt.laanelittapp.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNewUserBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.User
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_new_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewUserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var binding: FragmentNewUserBinding
    private lateinit var firstnameInput: Editable
    private lateinit var lastnameInput: Editable
    private lateinit var usernameInput: Editable
    private lateinit var passwordInput1: Editable
    private lateinit var passwordInput2: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_user, container, false
        )

        val application = requireNotNull(activity).application
        val viewModelFactory = FirebaseViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FirebaseViewModel::class.java)
        binding.lifecycleOwner = this

        viewModel.response.observe(viewLifecycleOwner, Observer{
            if ( it == viewModel.status[0]) {
                //Sender brukernavn og passord til LoginFragment
                findNavController().navigate(
                    NewUserFragmentDirections.actionNewUserFragmentToLoginFragment(
                        usernameInput.toString(),
                        passwordInput1.toString()
                    )
                )
            }
            else if(it == viewModel.status[1]){
                Toast.makeText(
                    requireContext(),
                    "Noe gikk galt. Prøv igjen!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //
        auth = FirebaseAuth.getInstance()

        //Henter brukernavn og passord fra LoginFragment hvis tekstfeltene har blit fylt
        val args = NewUserFragmentArgs.fromBundle(requireArguments())
        new_username.editText?.setText(args.newUsername)
        new_password_1.editText?.setText(args.newPassword)


        binding.newUserBtn.setOnClickListener {
            //Navn, postnr, brukernavn og passord fra tekstfeltene
            firstnameInput = new_firstname.editText?.text!!
            lastnameInput = new_lastname.editText?.text!!
            usernameInput = new_username.editText?.text!!
            passwordInput1 = new_password_1.editText?.text!!
            passwordInput2 = new_password_2.editText?.text!!

            //Validerer inndataene og registrer ny bruker
            validateUserInfo()
        }
    }

    private fun validateUserInfo() {
        new_firstname.error = null
        new_lastname.error = null
        new_username.error = null
        new_password_1.error = null
        new_password_2.error = null
        terms_checkbox.error = null

        if (firstnameInput.isEmpty()) {
            new_firstname.error = "Fyll inn fornavn"
            new_firstname.requestFocus()
            return
        }
        if (lastnameInput.isEmpty()) {
            new_lastname.error = "Fyll inn etternavn"
            new_lastname.requestFocus()
            return
        }
        if (usernameInput.isEmpty()) {
            new_username.error = "Fyll inn epost"
            new_username.requestFocus()
            return
        }
        if (passwordInput1.isEmpty()) {
            new_password_1.error = "Fyll inn passord"
            new_password_1.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(usernameInput)
                .matches()
        ) {
            new_username.error = "Uyldig"
            new_username.requestFocus()
            return
        }
        if (passwordInput1.toString().length < 6) {
            new_password_1.error = "Minimum 6 tegn"
            new_password_1.requestFocus()
            return
        }
        if (passwordInput2.isEmpty()) {
            new_password_2.error = "Bekreft passord"
            new_password_2.requestFocus()
            return
        }
        if (passwordInput1.toString() != passwordInput2.toString()) {
            new_password_2.error = "Ulike passord"
            new_password_2.requestFocus()
            return
        }
        if(!terms_checkbox.isChecked){
            terms_checkbox.error = ""
            return
        }
        //Oppretter ny bruker i firebase
        viewModel.firebaseSignIn(
            firstnameInput.toString(),
            lastnameInput.toString(),
            usernameInput.toString(),
            passwordInput1.toString(),
            auth)
    }

}