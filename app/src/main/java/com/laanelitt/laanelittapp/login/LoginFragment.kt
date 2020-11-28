package com.laanelitt.laanelittapp.login


import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        localStorage = LocalStorage(requireContext())

        val application = requireNotNull(activity).application
        val viewModelFactory = FirebaseViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FirebaseViewModel::class.java)


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
            findNavController().navigate(R.id.homePageFragment)
        }
    }
//
//    private fun firebaseAuth(username: String, password: String) {
//        //Firebase
//        auth.signInWithEmailAndPassword(username, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "firebaseAuth:suksess")
//                    //Funsksjon fra ApiService
//                    login(
//                        username,
//                        password
//                    )
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "firebaseAuth:feilet", task.exception)
//                    Toast.makeText(
//                        requireContext(), "Innlogging feilet.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }
//
//    private fun login(username: String, password: String) {
//        //ApiService
//        LaneLittApi.retrofitService.login(username, password).enqueue(
//            object : Callback<LoggedInUser> {
//                override fun onResponse(
//                    call: Call<LoggedInUser>,
//                    response: Response<LoggedInUser>
//                ) {
//                    if (response.body()?.user?.id != null) { //Godkjent
//                        Log.d(TAG, "login: suksess " + response.body()?.user?.id.toString())
//                        Toast.makeText(requireContext(), "Du er logget inn", Toast.LENGTH_LONG)
//                            .show()
//                        //Henter bruker-objektet
//                        val user = response.body()?.user
//                        //Lagrer bruker-objektet og setter userLoggedIn til true
//                        localStorage.storeUserData(user!!)
//                        localStorage.setUserLoggedIn(true)
//                        //Sendes videre til hovedsiden
//                        findNavController().navigate(R.id.homePageFragment)
//                    } else {
//                        Log.d(TAG, "login: Feilet " + response.body()?.toString())
//                        Toast.makeText(
//                            requireContext(),
//                            "Feil brukernavn/passord",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//
//                //Man kommer rett hit ved feil brukernavn/passord
//                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
//                    Log.d(TAG, "login: onFailure$t")
//                    Toast.makeText(requireContext(), "Noe gikk galt", Toast.LENGTH_LONG).show()
//                }
//            }
//        )
//    }
}



