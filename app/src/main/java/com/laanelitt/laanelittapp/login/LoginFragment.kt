package com.laanelitt.laanelittapp.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

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

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

        binding.loginBtn.setOnClickListener {
            login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
              if(Pref.getUserId(requireContext(), "ID", "null") == "Logget inn"){
              findNavController().navigate(R.id.searchPageFragment)
           }

        }

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.newUserFragment)
        }

    }

    private fun login(username: String, password: String) {
        if (username == "1" && password == "1") {
            Pref.setUserId(requireContext(),"ID", "Logget inn");
        } else {
            Toast.makeText(requireContext(),"Feil brukernavn/passord", Toast.LENGTH_LONG).show();
        }
    }


    object Pref {
        private val PREF_FILE: String = com.laanelitt.laanelittapp.BuildConfig.APPLICATION_ID.replace(".", "_")
        private var sharedPreferences: SharedPreferences? = null
        private fun openPref(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        }

        fun getUserId(context: Context, key: String?, defaultValue: String?): String? {
            openPref(context)
            val result = sharedPreferences!!.getString(key, defaultValue)
            sharedPreferences = null
            return result
        }

        fun setUserId(context: Context, key: String?, value: String?) {
            openPref(context)
            val prefsPrivateEditor: SharedPreferences.Editor = sharedPreferences!!.edit()
            prefsPrivateEditor.putString(key, value)
            prefsPrivateEditor.commit()
            sharedPreferences = null
        }

        fun removeUserId(context: Context, key: String?, value: String?) {
            openPref(context)
            sharedPreferences?.edit()?.clear()?.apply();
        }
    }

}


