package com.laanelitt.laanelittapp.profile.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditPasswordBinding
import com.laanelitt.laanelittapp.objects.User
import com.laanelitt.laanelittapp.objects.UserLocalStore
import kotlinx.android.synthetic.main.fragment_edit_password.*

class EditPasswordFragment : Fragment() {
    var loggedInUser: User? = null
    var userLocalStore: UserLocalStore? = null
    private lateinit var binding: FragmentEditPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_password, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocalStore = UserLocalStore(requireContext())

        loggedInUser = userLocalStore?.getLoggedInUser

        binding.editPasswordBtn.setOnClickListener {
            val password1Input = edit_password_1.getEditText()?.getText()
            val password2Input = edit_password_2.getEditText()?.getText()
//            updatePassword(password1Input.toString(),password2Input.toString(), loggedInUser!!)
        }
    }

//    private fun updatePassword(password1: String, password2: String, user: EditUser) {
//        if (password1 == password2) {
//
//            user.password=password1
//
//            LaneLittApi.retrofitService.updatePassword(user).enqueue(
//                object: Callback<String> {
//                    override fun onResponse(call: Call<String>, response: Response<String>) {
//                        println("Oppdatere passord? "+response.body())
//                        if(response.body()=="200"){
//
//                            findNavController().navigate(R.id.settingsFragment)
//                        }
//                        else{
//                            Toast.makeText(requireContext(),"Error passord", Toast.LENGTH_LONG).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<String>, t: Throwable) {
//                        println("Passord ikke oppdatert?")
//                        Toast.makeText(requireContext(),"Noe har gått galt", Toast.LENGTH_LONG).show()
//                    }
//                }
//            )
//
//        } else {
//            Toast.makeText(requireContext(),"Ulike passord", Toast.LENGTH_LONG).show()
//        }
//    }
}