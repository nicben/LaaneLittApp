package com.laanelitt.laanelittapp.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentLoginBinding
import com.laanelitt.laanelittapp.homepage.userLocalStore
import com.laanelitt.laanelittapp.objects.LoggedInUser
import com.laanelitt.laanelittapp.objects.UserLocalStore
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocalStore = UserLocalStore(requireContext())

        val args = LoginFragmentArgs.fromBundle(requireArguments())
        username.getEditText()?.setText(args.username)
        password.getEditText()?.setText(args.password)

        binding.loginBtn.setOnClickListener {
            val usernameInput = username.getEditText()?.getText()
            val passwordInput = password.getEditText()?.getText()

            auth.signInWithEmailAndPassword(usernameInput.toString(), passwordInput.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")

                        login(
                            usernameInput.toString(),
                            passwordInput.toString()
                        )

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }

        binding.registerButton.setOnClickListener {
            val usernameInput = username.getEditText()?.getText()
            val passwordInput = password.getEditText()?.getText()
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToNewUserFragment(
                    usernameInput.toString(),
                    passwordInput.toString()
                )
            )
        }
    }


    private fun login(username: String, password: String) {

        LaneLittApi.retrofitService.login(username, password).enqueue(
            object : Callback<LoggedInUser> {
                override fun onResponse(
                    call: Call<LoggedInUser>,
                    response: Response<LoggedInUser>
                ) {
                    println("LOGIN YES")
                    println(response.body()?.user?.id)
                    if (response.body()?.user?.id != null) {
                        //Pref.setUserId(requireContext(), "ID", response.body()?.user?.id.toString())
                        Toast.makeText(requireContext(), "Du er logget inn", Toast.LENGTH_LONG)
                            .show()
                        println(response.body()?.user?.id.toString())

                        val user = response.body()?.user
                        userLocalStore?.storeUserData(user!!)
                        userLocalStore?.setUserLoggedIn(true)

                        findNavController().navigate(R.id.searchPageFragment)


                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Feil brukernavn/passord",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                //              Man kommer rett hit ved feil brukernavn/passord
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    println("LOGIN NO")
                    println("**" + t + "**")
                    Toast.makeText(requireContext(), "Noe skjedde galt", Toast.LENGTH_LONG).show()
                }

            }

        )

    }

}


