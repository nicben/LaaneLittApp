package com.laanelitt.laanelittapp.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNewUserBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.AssetOwner
import kotlinx.android.synthetic.main.fragment_new_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val args = NewUserFragmentArgs.fromBundle(requireArguments())

        new_username.getEditText()?.setText(args.newUsername)
        new_password_1.getEditText()?.setText(args.newPassword)

//        Toast.makeText(context, "username: ${args.newUsername}, password: ${args.newPassword}", Toast.LENGTH_LONG).show()


        binding.newUserBtn.setOnClickListener {
            val usernameInput = new_username.getEditText()?.getText()
            val passwordInput1 = new_password_1.getEditText()?.getText()
            val passwordInput2 = new_password_2.getEditText()?.getText()
            register(
                usernameInput.toString(),
                passwordInput1.toString(),
                passwordInput2.toString()
            )
        }
    }

    private fun register(username: String, password1: String, password2: String) {
        if (password1 == password2) {

            val newUser = AssetOwner(null, null, null,null, null, password1, username)
            println(""+newUser.email + " " + password1  )
            LaneLittApi.retrofitService.registerUser(newUser).enqueue(
                object: Callback<Code>{
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        println("lagd ny bruker? "+response.body()?.toString())
                        if(response.body()?.code.toString()=="200"){
                            findNavController().navigate(NewUserFragmentDirections.actionNewUserFragmentToLoginFragment(
                                username,
                                password1
                            )
                            )
                        }
                        else{
                            Toast.makeText(requireContext(),"Epost ikke gyldig/eksisterer allerede", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println("lagde ikke ny bruker?")
                        Toast.makeText(requireContext(),"Du skulle ikke se denne, noe har gått galt", Toast.LENGTH_LONG).show()
                    }
                }
            )

        } else {
            Toast.makeText(requireContext(),"Passord ulikt", Toast.LENGTH_LONG).show()
        }
    }
}