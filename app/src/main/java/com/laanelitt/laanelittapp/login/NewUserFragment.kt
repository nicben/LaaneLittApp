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
import com.laanelitt.laanelittapp.databinding.FragmentNewUserBinding
import com.laanelitt.laanelittapp.objects.AssetOwner
import com.laanelitt.laanelittapp.objects.Code
import kotlinx.android.synthetic.main.fragment_new_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentNewUserBinding
    lateinit var usernameInput: Editable
    lateinit var passwordInput1: Editable
    lateinit var passwordInput2: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_user, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Henter brukernavn og passord fra LoginFragment hvis tekstfeltene har blit fylt
        val args = NewUserFragmentArgs.fromBundle(requireArguments())
        new_username.getEditText()?.setText(args.newUsername)
        new_password_1.getEditText()?.setText(args.newPassword)

        binding.newUserBtn.setOnClickListener {
            //Brukernavn og passord fra tekstfeltene
            usernameInput = new_username.getEditText()?.getText()!!
            passwordInput1 = new_password_1.getEditText()?.getText()!!
            passwordInput2 = new_password_2.getEditText()?.getText()!!
            //Validerer inndataene og registrer ny bruker
            registerUser()
        }
    }

    private fun registerUser() {
        new_username.error = null
        new_password_1.error = null
        new_password_2.error = null

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
            new_username.error = "Fyll inn epost"
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
        //Oppretter ny bruker i firebase
        firebaseSignIn(usernameInput.toString(), passwordInput1.toString())

    }

    private fun firebaseSignIn(username: String, password: String) {
        //Firebase
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(requireActivity()) { task ->
                println("auth")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    println("funka")
                    //Funsksjonen for å kallet på register-APIet
                    register(
                        username,
                        password,
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Brukeren eksisterer allerede",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun register(username: String, password1: String) {
        //Oppretter et nytt bruker-objekt
        val newUser = AssetOwner(null, null, null, null, null, password1, username)
        println("" + newUser.email + " " + password1)
        //API-kallet
        LaneLittApi.retrofitService.registerUser(newUser).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    println("lagd ny bruker? " + response.body()?.toString())
                    if (response.body()?.code.toString() == "200") { //Godkjent
                        //Sender brukernavn og passord til LoginFragment
                        findNavController().navigate(
                            NewUserFragmentDirections.actionNewUserFragmentToLoginFragment(
                                username,
                                password1
                            )
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Epost ikke gyldig/eksisterer allerede",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    println("lagde ikke ny bruker?")
                    Toast.makeText(
                        requireContext(),
                        "Du skulle ikke se denne, noe har gått galt",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}