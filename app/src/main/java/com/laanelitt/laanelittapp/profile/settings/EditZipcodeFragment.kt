package com.laanelitt.laanelittapp.profile.settings

import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditZipcodeBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import kotlinx.android.synthetic.main.fragment_edit_zipcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditZipcodeFragment : Fragment() {

    private lateinit var binding: FragmentEditZipcodeBinding
    lateinit var zipcodeInput: Editable
    private var localStorage: LocalStorage? = null
    private var loggedInUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_zipcode, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage?.getLoggedInUser

        //Viser postnr til brukeren i tekstfeltene
        //edit_zipcode.getEditText()?.setText(loggedInUser?.zipcode)
        displayZipcode()

        binding.editZipcodeBtn?.setOnClickListener {
            zipcodeInput = edit_zipcode.getEditText()?.getText()!!
            editZipcode()
        }

    }
    private fun editZipcode() {
        edit_zipcode.error = null
        if (zipcodeInput.isEmpty()) {
            edit_zipcode.error = "Fyll inn postnummer"
            edit_zipcode.requestFocus()
            return
        }
        if (zipcodeInput.toString().length != 4) {
            edit_zipcode.error = "Ugyldig postnummer"
            edit_zipcode.requestFocus()
            return
        }
        //
        updateZipcode(
            zipcodeInput.toString(),
            loggedInUser!!
        )
    }

    private fun updateZipcode(zipcode: String, user: User) {
        //Legger til oppdatert postnr til bruker-objektet som skal sendes med API'et
        user.zipcode = zipcode

        println(user.firstname + " " + user.lastname + " " + user.zipcode)

        println("**" + "editZipcode" + "**")
        //API-kallet
        user.id?.let {
            LaneLittApi.retrofitService.editUser(it, user).enqueue(
                object : Callback<Code> {
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        println("Endre? "+response.body()?.code.toString())
                        if(response.body()?.code.toString()=="200"){ //Godkjent
                            //Legger til oppdatert postnr og oppdaterer bruker-objektet som er lagret
                            user.zipcode = zipcode
                            localStorage?.updateUser(user)
                            //Sendes videre til innstillinger-siden
                            findNavController().navigate(R.id.settingsFragment)
                            Toast.makeText(requireContext(), "Endring lagret " + user.zipcode, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Posrnr ble ikke oppdatert",
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
    }


    private fun displayZipcode() {
        edit_zipcode.getEditText()?.setText(loggedInUser?.zipcode)
    }
}