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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentLoginBinding
import com.laanelitt.laanelittapp.objects.LoggedInUser
import com.laanelitt.laanelittapp.objects.LocalStorage
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    lateinit var usernameInput: Editable
    lateinit var passwordInput: Editable
    private var localStorage: LocalStorage? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localStorage = LocalStorage(requireContext())

        //Henter brukernavn og passord fra NewUserFragment hvis det har blitt opprettet en ny bruker
        val args = LoginFragmentArgs.fromBundle(requireArguments())
        username.getEditText()?.setText(args.username)
        password.getEditText()?.setText(args.password)

        binding.loginBtn.setOnClickListener {
            //Brukernavn og passord fra tekstfeltene
            usernameInput = username.getEditText()?.getText()!!
            passwordInput = password.getEditText()?.getText()!!
            authUser()
        }

        binding.registerButton.setOnClickListener {
            //Brukernavn og passord fra tekstfeltene
            usernameInput = username.getEditText()?.getText()!!
            passwordInput = password.getEditText()?.getText()!!
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
        }
        //Firebase autentisering
        firebaseAuth(usernameInput.toString(), passwordInput.toString())
    }

    private fun firebaseAuth(username: String, password: String){
        //Firebase
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    //Funsksjonen for å kalle på login-APIet
                    login(
                        username,
                        password
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun login(username: String, password: String) {
        //API-kallet
        LaneLittApi.retrofitService.login(username, password).enqueue(
            object : Callback<LoggedInUser> {
                override fun onResponse(
                    call: Call<LoggedInUser>,
                    response: Response<LoggedInUser>) {
                    println("LOGIN YES")
                    println(response.body()?.user?.id)
                    if (response.body()?.user?.id != null) { //Godkjent
                        Toast.makeText(requireContext(), "Du er logget inn", Toast.LENGTH_LONG)
                            .show()
                        println(response.body()?.user?.id.toString())
                        //Henter bruker-objektet
                        val user = response.body()?.user
                        //Lagrer bruker-objektet og setter userLoggedIn til true
                        localStorage?.storeUserData(user!!)
                        localStorage?.setUserLoggedIn(true)
                        //Sendes videre til hovedsiden
                        findNavController().navigate(R.id.homePageFragment)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Feil brukernavn/passord",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                //Man kommer rett hit ved feil brukernavn/passord
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    println("LOGIN NO")
                    println("**" + t + "**")
                    Toast.makeText(requireContext(), "Noe skjedde galt", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}