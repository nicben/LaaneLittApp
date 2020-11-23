package com.laanelitt.laanelittapp.profile.myassets

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.asset.AssetViewModel
import com.laanelitt.laanelittapp.asset.AssetViewModelFactory
import com.laanelitt.laanelittapp.databinding.FragmentMyAssetBinding
import com.laanelitt.laanelittapp.homepage.localStorage
import com.laanelitt.laanelittapp.objects.*
import kotlinx.android.synthetic.main.fragment_my_asset.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//profil til item - info



class MyAssetFragment : Fragment(){

    private lateinit var binding: FragmentMyAssetBinding
    var userId: Int? = null
    lateinit var loggedInUser: User
    lateinit var assetNameTextView: TextView
    lateinit var assetDescriptionTextView: TextView
    lateinit var  assetNameInput: Editable
    lateinit var  assetDescriptionInput: Editable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_asset, container, false
        )
        binding.lifecycleOwner = this

        return binding.root

   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("******************************my asset viewCreated")
        super.onViewCreated(view, savedInstanceState)
        localStorage = LocalStorage(requireContext())
        //Henter det valgte eiendel-objektet
        val application = requireNotNull(activity).application
        val asset = MyAssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)

        //Bruker-objektet som er lagret
        loggedInUser = localStorage?.getLoggedInUser!!

        //TextView
        assetNameTextView = view.findViewById(R.id.assetName)
        assetDescriptionTextView = view.findViewById(R.id.assetDescription)

        binding.editAssetButton.setOnClickListener {
            //Viser endringssiden
            showEditSite()
        }

        binding.saveAssetButton.setOnClickListener {
            //Henter brukerId'en
            userId = loggedInUser.id!!
            //Endrer eiendelen
            editAsset(userId!!, assetNameTextView.text.toString(), assetDescriptionTextView.text.toString(), asset)
            //Viser eiendelprofilen
            showfinalSite()
        }

        binding.deleteAssetButton.setOnClickListener {
            deleteAsset(asset.id)
        }
    }

    private fun editAsset(userId: Int, assetName: String, assetDescription: String,  editAsset: Asset) {
        editAsset.assetName = assetName
        editAsset.description = assetDescription

        println("" + userId + " " + editAsset.id + " " + editAsset.toString() + " " + editAsset.assetCondition + " " + editAsset.public)
        //API-kallet
        LaneLittApi.retrofitService.editAsset(userId, editAsset.id, editAsset).enqueue(
            object : Callback<Asset> {
                override fun onResponse(call: Call<Asset>, response: Response<Asset>) {
                    println("Endring? "+response.body()?.toString())
                    if (response.body()?.id!=null) {
                      // Toast.makeText(requireContext(), "Lagret", Toast.LENGTH_LONG).show()
                    } else {
                       // Toast.makeText(requireContext(), "Ikke lagret", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Asset>, t: Throwable) {
                    println("Ikke endret?")
                   // Toast.makeText(requireContext(), "Noe har gått galt", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun deleteAsset(assetId: Int) {
        LaneLittApi.retrofitService.deleteAsset(assetId).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println("Slett? " + response.body())
                    if (response.body() == "Eiendel slettet") {
                        findNavController().navigate(R.id.myAssetsListFragment)
                    } else {
                        Toast.makeText(requireContext(), "Eiendelen ble ikke slettet", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("Ikke slettet?")
                    Toast.makeText(requireContext(), "Noe har gått galt", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun showEditSite(){
        //Skjuler TextView'ene og endre-knappen
        assetNameTextView.setVisibility(View.INVISIBLE)
        assetDescriptionTextView.setVisibility(View.INVISIBLE)
        editAssetButton.setVisibility(View.INVISIBLE)

        //Viser TextField'ene, lagre- og slett-knappene
        editAssetName.visibility = View.VISIBLE
        editAssetDescription.visibility = View.VISIBLE
        saveAssetButton.setVisibility(View.VISIBLE)
        deleteAssetButton.setVisibility(View.VISIBLE)

        //Henter teksten fra TextView'ene og viser det i TextFieldene
        editAssetName.getEditText()?.setText(assetNameTextView.text)
        editAssetDescription.getEditText()?.setText(assetDescriptionTextView.text)
    }


    private fun showfinalSite(){
        //Material textfield
        assetNameInput = editAssetName.getEditText()?.getText()!!
        assetDescriptionInput = editAssetDescription.getEditText()?.getText()!!

        //Viser TextView'ene og endre-knappen
        assetNameTextView.setVisibility(View.VISIBLE)
        assetDescriptionTextView.setVisibility(View.VISIBLE)
        editAssetButton.setVisibility(View.VISIBLE)

        //Skjuler TextField'ene, lagre- og slett-knappene
        editAssetName.visibility = View.INVISIBLE
        editAssetDescription.visibility = View.INVISIBLE
        saveAssetButton.setVisibility(View.INVISIBLE)
        deleteAssetButton.setVisibility(View.INVISIBLE)

        //Henter teksten fra TextFieldene og viser det i TextView'ene
        assetNameTextView.setText(assetNameInput.toString())
        assetDescriptionTextView.setText(assetDescriptionInput.toString())
    }
}


