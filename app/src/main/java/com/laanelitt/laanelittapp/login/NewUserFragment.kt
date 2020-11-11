package com.laanelitt.laanelittapp.login

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
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNewUserBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.User
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
        val newUsername = view.findViewById<EditText>(R.id.new_username)
        val newPassword1 = view.findViewById<EditText>(R.id.new_password_1)
        val newPassword2 = view.findViewById<EditText>(R.id.new_password_2)

        newUsername.setText(args.newUsername)
        newPassword1.setText(args.newPassword)

        Toast.makeText(context, "username: ${args.newUsername}, password: ${args.newPassword}", Toast.LENGTH_LONG).show()

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

        binding.newUserBtn.setOnClickListener {

            register(

                newUsername.text.toString(),
                newPassword1.text.toString(),
                newPassword2.text.toString()
            )
        }
    }

    private fun register(username: String, password1: String, password2: String) {
        if (password1 == password2) {

            val newUser: User=User(null, null, null,null)
            newUser.email=username
            newUser.password=password1

            LaneLittApi.retrofitService.registerUser(newUser).enqueue(
                object: Callback<Code>{
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        println("lagd ny bruker? "+response.body()?.code.toString())
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