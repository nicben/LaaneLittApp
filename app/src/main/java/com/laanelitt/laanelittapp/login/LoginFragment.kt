package com.laanelitt.laanelittapp.login


import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.categorylist.CategoryListFragmentDirections
import com.laanelitt.laanelittapp.databinding.FragmentLoginBinding
import com.laanelitt.laanelittapp.objects.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(){
    private lateinit var auth: FirebaseAuth
    private lateinit var localStorage: LocalStorage
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var usernameInput: Editable
    private lateinit var passwordInput: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        val application = requireNotNull(activity).application
        val viewModelFactory = FirebaseViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FirebaseViewModel::class.java)
        binding.lifecycleOwner = this

        viewModel.response.observe(viewLifecycleOwner, Observer{
            if ( it == viewModel.status[0]) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(R.id.homePageFragment)
            }
            else if(it == viewModel.status[1]){
                username.error = null
                password.error = null
                username.error = "Feil brukernavn/passord"
                password.error = "Feil brukernavn/passord"
                username.requestFocus()
            }
        })

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        localStorage = LocalStorage(requireContext())

        //Brukernavn og passord fra tekstfeltene
        usernameInput = username.editText?.text!!
        passwordInput = password.editText?.text!!

        //Henter brukernavn og passord fra NewUserFragment hvis det har blitt opprettet en ny bruker
        val args = LoginFragmentArgs.fromBundle(requireArguments())
        username.editText?.setText(args.username)
        password.editText?.setText(args.password)

        binding.loginBtn.setOnClickListener {
            //Brukernavn og passord fra tekstfeltene
            usernameInput = username.editText?.text!!
            passwordInput = password.editText?.text!!

            //Validerer inndataene og logger inn bruker
            authUser()
        }

        binding.registerButton.setOnClickListener {
            //Brukernavn og passord fra tekstfeltene
            usernameInput = username.editText?.text!!
            passwordInput = password.editText?.text!!

            //Sender brukernavn og passord til NewUserFragment hvis man velger å opprettet en ny bruker
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToNewUserFragment(
                    usernameInput.toString(),
                    passwordInput.toString()
                )
            )
        }
    }


    private fun authUser() {
        username.error = null
        password.error = null
        if (usernameInput.isEmpty()) {
            username.error = "Fyll inn epost"
            username.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(usernameInput)
                .matches()
        ) {
            username.error = "Uyldig"
            username.requestFocus()
            return
        }
        if (passwordInput.isEmpty()) {
            password.error = "Fyll inn passord"
            password.requestFocus()
            return
        }else {
            //Firebase autentisering
            viewModel.firebaseAuth(
                usernameInput.toString(),
                passwordInput.toString(),
                auth,
                localStorage
            )
        }
    }
}



