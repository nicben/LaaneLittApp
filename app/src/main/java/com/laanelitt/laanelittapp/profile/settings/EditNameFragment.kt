package com.laanelitt.laanelittapp.profile.settings

import android.os.Bundle
import android.text.Editable
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
import com.laanelitt.laanelittapp.objects.LocalStorage
import kotlinx.android.synthetic.main.fragment_edit_name.*
import kotlinx.android.synthetic.main.fragment_edit_zipcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNameFragment : Fragment() {

    private lateinit var binding: FragmentEditNameBinding
    lateinit var firstnameInput: Editable
    lateinit var lastnameInput: Editable
    private var localStorage: LocalStorage? = null
    private var loggedInUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_name, container, false
        )
        return binding.root
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(requireContext(), "Landscape Mode edit name", Toast.LENGTH_SHORT).show()
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(requireContext(), "Portrait Mode edit name", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage?.getLoggedInUser

        //Viser fornavnet, mellomnavnet og etternavnet til brukeren i tekstfeltene
        displayUsersName()

        binding.editNameBtn.setOnClickListener {
            //Henter tekstene far tekstfeltene
            firstnameInput = edit_firstname.getEditText()?.getText()!!
            lastnameInput = edit_lastname.getEditText()?.getText()!!
            //Funsksjonen for å kalle på editUser-APIet
            editName()
        }
    }

    private fun editName() {
        edit_firstname.error = null
        edit_lastname.error = null
        if (firstnameInput.isEmpty()) {
            edit_firstname.error = "Fyll inn fornavn"
            edit_firstname.requestFocus()
            return
        }
        if (lastnameInput.isEmpty()) {
            edit_lastname.error = "Fyll inn etternavn"
            edit_lastname.requestFocus()
            return
        }
        //
        updateUser(
            loggedInUser!!.id!!,
            firstnameInput.toString(),
            lastnameInput.toString(),
            loggedInUser!!
        )
    }

    private fun updateUser(userId: Int, firstname: String, lastname: String, user: User) {
        //Legger til de oppdaterte navnene til bruker-objektet som skal sendes med API'et
        user.firstname = firstname
        user.lastname = lastname

        println(user.usertype + user.firstname + user.lastname)

        println("**" + "editUser" + "**")
        //API-kallet
        LaneLittApi.retrofitService.editUser(userId, user).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    println("Endre? "+response.body()?.code.toString())
                    if(response.body()?.code.toString()=="200"){ //Godkjent
                        //Legger til de oppdaterte navnene og oppdaterer bruker-objektet som er lagret
                        user.firstname = firstname
                        user.lastname = lastname
                        localStorage?.updateUser(user)
                        //Sendes videre til innstillinger-siden
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


    private fun displayUsersName() {
        edit_firstname.getEditText()?.setText(loggedInUser?.firstname)
        edit_lastname.getEditText()?.setText(loggedInUser?.lastname)
    }
}
