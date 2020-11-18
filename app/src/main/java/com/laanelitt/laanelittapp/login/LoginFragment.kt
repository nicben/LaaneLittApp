package com.laanelitt.laanelittapp.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentLoginBinding
import com.laanelitt.laanelittapp.objects.LoggedInUser
import com.laanelitt.laanelittapp.objects.AssetOwner
import com.laanelitt.laanelittapp.objects.UserLocalStore
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    var userLocalStore: UserLocalStore? = null


    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

//        Toast.makeText(context, "username: ${args.username}, password: ${args.password}", Toast.LENGTH_LONG).show()


        binding.loginBtn.setOnClickListener {
            val usernameInput = username.getEditText()?.getText()
            val passwordInput = password.getEditText()?.getText()
            login(
                usernameInput.toString(),
                passwordInput.toString()
            )
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
                        Pref.setUserId(requireContext(), "ID", response.body()?.user?.id.toString())
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

    object Pref {

        private val PREF_FILE: String =
            com.laanelitt.laanelittapp.BuildConfig.APPLICATION_ID.replace(
                ".",
                "_"
            )
        private var sharedPreferences: SharedPreferences? = null
        private fun openPref(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        }


        fun setUserId(context: Context, key: String?, value: String?) {
            openPref(context)
            val prefsPrivateEditor: SharedPreferences.Editor = sharedPreferences!!.edit()
            prefsPrivateEditor.putString(key, value)
            prefsPrivateEditor.apply()
            sharedPreferences = null
        }

        fun getUserId(context: Context, key: String?, defaultValue: String?): String? {
            openPref(context)
            val result = sharedPreferences!!.getString(key, defaultValue)
            sharedPreferences = null
            return result
        }

    }
}


