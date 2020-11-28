package com.laanelitt.laanelittapp.profile.settings.editname

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditNameBinding
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import kotlinx.android.synthetic.main.fragment_edit_name.*

class EditNameFragment : Fragment() {

    private lateinit var localStorage: LocalStorage
    private lateinit var loggedInUser: User
    private lateinit var viewModel: EditNameViewModel
    private lateinit var binding: FragmentEditNameBinding
    private lateinit var firstnameInput: Editable
    private lateinit var lastnameInput: Editable


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        adapter = BookSearchResultsAdapter()
//        viewModel = ViewModelProviders.of(this)[BookSearchViewModel::class.java]
//        viewModel.init()
//        viewModel.getVolumesResponseLiveData().observe(this,
//            Observer<Any?> { volumesResponse ->
//                if (volumesResponse != null) {
//                    adapter.setResults(volumesResponse.getItems())
//                }
//            })
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_name,
            container,
            false
        )

        val application = requireNotNull(activity).application
        val viewModelFactory = EditNameViewModelFactory(loggedInUser, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditNameViewModel::class.java)

        binding.editViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.editNameBtn.setOnClickListener {
            //Henter tekstene far tekstfeltene
            firstnameInput = edit_firstname.editText?.text!!
            lastnameInput = edit_lastname.editText?.text!!

            //Validerer inndataene og lagrer det nye navnet
            validateName()
        }

        // Observer for the Game finished event
//        viewModel.editUser()?.observe(viewLifecycleOwner, {
//            Log.d(TAG, "Hallloooooooooooooooooooooooooo")
//
//        })
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        //Henter bruker-objektet som er lagret
//        localStorage = LocalStorage(requireContext())
//        loggedInUser = localStorage.getLoggedInUser!!

        //Viser fornavnet og etternavnet til brukeren i tekstfeltene
        //displayUsersName()

//        binding.editNameBtn.setOnClickListener {
//            //Henter tekstene far tekstfeltene
//            firstnameInput = edit_firstname.editText?.text!!
//            lastnameInput = edit_lastname.editText?.text!!
//
//            //Validerer inndataene og lagrer det nye navnet
//            validateName()
//        }
        return binding.root
    }

    private fun validateName() {
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
        viewModel.updateName(
            loggedInUser.id!!,
            firstnameInput.toString(),
            lastnameInput.toString(),
            loggedInUser,
            localStorage
        )
        //Sendes videre til innstillinger-siden
    findNavController().navigate(R.id.settingsFragment)
            Toast.makeText(
        requireContext(),
        "Oppdatert",
        Toast.LENGTH_LONG
    ).show()
    }
//
//    private fun updateName(userId: Int, firstname: String, lastname: String, user: User, localStorage: LocalStorage) {
//        //Legger til de oppdaterte navnene til bruker-objektet som skal sendes med editUser
//        user.firstname = firstname
//        user.lastname = lastname
//
//        //ApiService
//        LaneLittApi.retrofitService.editUser(userId, user).enqueue(
//            object : Callback<Code> {
//                override fun onResponse(call: Call<Code>, response: Response<Code>) {
//                    if(response.body()?.code.toString()=="200"){
//                        Log.d(TAG, "editUser(name): Suksess " +response.body()?.code.toString())
//                        //Legger til de oppdaterte navnene og oppdaterer bruker-objektet som er lagret
//                        localStorage.updateUser(user)
//                        Toast.makeText(
//                            requireContext(),
//                            "Oppdatert",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        //Sendes videre til innstillinger-siden
//                        findNavController().navigate(R.id.settingsFragment)
//                    } else {
//                        Log.d(TAG, "editUser(name): Feilet " +response.body()?.code.toString())
//                        Toast.makeText(
//                            requireContext(),
//                            "Navnet ble ikke oppdatert",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<Code>, t: Throwable) {
//                    Log.d(TAG, "editUser(name): onFailure$t")
//                    Toast.makeText(requireContext(), "Noe gikk galt", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
//        )
//    }


//    private fun displayUsersName() {
//        edit_firstname.editText?.setText(loggedInUser.firstname)
//        edit_lastname.editText?.setText(loggedInUser.lastname)
//    }
}
