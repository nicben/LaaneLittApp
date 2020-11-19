package com.laanelitt.laanelittapp.profile.settings

import android.content.res.Configuration
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
import com.laanelitt.laanelittapp.databinding.FragmentEditNameBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.User
import com.laanelitt.laanelittapp.objects.UserLocalStore
import kotlinx.android.synthetic.main.fragment_edit_name.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditNameFragment : Fragment() {
    var loggedInUser: User? = null

    private lateinit var binding: FragmentEditNameBinding

    var userLocalStore: UserLocalStore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_name, container, false
        )

        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(requireContext(), "Landscape Mode edit name", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(requireContext(), "Portrait Mode edit name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocalStore = UserLocalStore(requireContext())

        loggedInUser = userLocalStore?.getLoggedInUser

        displayUserDetails()

        binding.editNameBtn.setOnClickListener {
            val firstnameInput = edit_firstname.getEditText()?.getText()
            val middlenameInput = edit_middlename.getEditText()?.getText()
            val lastnameInput = edit_lastname.getEditText()?.getText()

            editUser(
                loggedInUser!!.id!!,
                firstnameInput.toString(),
                middlenameInput.toString(),
                lastnameInput.toString(),
                loggedInUser!!
            )

            Toast.makeText(
                requireContext(),
                "" + firstnameInput.toString() + " " + middlenameInput.toString() + " " + lastnameInput.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun editUser(userId: Int, firstname: String, middlename: String, lastname: String, user: User) {
        user.firstname = firstname
        user.middlename = middlename
        user.lastname = lastname

        println(user.usertype + user.firstname + user.lastname)

        println("**" + "editUser" + "**")
        LaneLittApi.retrofitService.editUser(userId, user).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    println("Endre? "+response.body()?.code.toString())
                    if(response.body()?.code.toString()=="200"){

                        user.firstname = firstname
                        user.middlename = middlename
                        user.lastname = lastname

                        userLocalStore?.updateUser(user)

                        findNavController().navigate(R.id.settingsFragment)
                        //Toast.makeText(requireContext(), "Eiendelen er slettet", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Navnet ble ikke oppdatert",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    println("Ikke endret?")
                    Toast.makeText(requireContext(), "Noe har gått galt", Toast.LENGTH_LONG)
                        .show()
                }
            }
        )
    }


    private fun displayUserDetails() {
        edit_firstname.getEditText()?.setText(loggedInUser?.firstname)
        edit_middlename.getEditText()?.setText(loggedInUser?.middlename)
        edit_lastname.getEditText()?.setText(loggedInUser?.lastname)
    }

}
