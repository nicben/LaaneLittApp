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
import com.laanelitt.laanelittapp.objects.*
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModel
import kotlinx.android.synthetic.main.fragment_my_asset.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyAssetFragment : Fragment() {
    private lateinit var binding: FragmentMyAssetBinding
    private lateinit var localStorage: LocalStorage
    private lateinit var loggedInUser: User
    private lateinit var viewModel: AssetViewModel
    private lateinit var assetNameTextView: TextView
    private lateinit var assetDescriptionTextView: TextView
    private lateinit var assetNameInput: Editable
    private lateinit var assetDescriptionInput: Editable
    var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_asset,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!

        //Henter det valgte eiendel-objektet
        val application = requireNotNull(activity).application
        val asset = MyAssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

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
            viewModel.editAsset(
                userId!!,
                editAssetNameText.text.toString(),
                editAssetDescriptionText.text.toString(),
                asset
            )
            //Viser eiendelprofilen
            showfinalSite()
        }

        binding.deleteAssetButton.setOnClickListener {
            viewModel.deleteAsset(asset.id)
            findNavController().navigate(R.id.myAssetsListFragment)
        }
    }


    private fun showEditSite() {
        //Skjuler TextView'ene og endre-knappen
        assetNameTextView.visibility = View.INVISIBLE
        assetDescriptionTextView.visibility = View.INVISIBLE
        editAssetButton.visibility = View.INVISIBLE

        //Viser TextField'ene, lagre- og slett-knappene
        editAssetName.visibility = View.VISIBLE
        editAssetDescription.visibility = View.VISIBLE
        saveAssetButton.visibility = View.VISIBLE
        deleteAssetButton.visibility = View.VISIBLE

        //Henter teksten fra TextView'ene og viser det i TextFieldene
        editAssetName.editText?.setText(assetNameTextView.text)
        editAssetDescription.editText?.setText(assetDescriptionTextView.text)
    }


    private fun showfinalSite() {
        //Material textfield
        assetNameInput = editAssetName.editText?.text!!
        assetDescriptionInput = editAssetDescription.editText?.text!!

        //Viser TextView'ene og endre-knappen
        assetNameTextView.visibility = View.VISIBLE
        assetDescriptionTextView.visibility = View.VISIBLE
        editAssetButton.visibility = View.VISIBLE

        //Skjuler TextField'ene, lagre- og slett-knappene
        editAssetName.visibility = View.INVISIBLE
        editAssetDescription.visibility = View.INVISIBLE
        saveAssetButton.visibility = View.INVISIBLE
        deleteAssetButton.visibility = View.INVISIBLE

        //Henter teksten fra TextFieldene og viser det i TextView'ene
        assetNameTextView.text = assetNameInput.toString()
        assetDescriptionTextView.text = assetDescriptionInput.toString()
    }
}


