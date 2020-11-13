package com.laanelitt.laanelittapp.asset

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentMyAssetBinding
import com.laanelitt.laanelittapp.login.LoginFragment
import com.laanelitt.laanelittapp.login.LoginFragmentDirections
import com.laanelitt.laanelittapp.objects.*
import kotlinx.android.synthetic.main.fragment_my_asset.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//profil til item - info

class MyAssetFragment : Fragment(){

    private lateinit var binding: FragmentMyAssetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //val binding = FragmentMyAssetBinding.inflate(inflater)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_asset, container, false
        )
        binding.lifecycleOwner = this

    return binding.root

   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("******************************my asset viewCreated")
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(activity).application
        val asset = MyAssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)

        val userId = LoginFragment.Pref.getUserId(requireContext(), "ID", "null")?.toInt()

        val assetNameTextView = view.findViewById<TextView>(R.id.assetName)
        val assetDescriptionTextView = view.findViewById<TextView>(R.id.assetDescription)

        val assetNameEditText = view.findViewById<EditText>(R.id.editAssetName)
        val assetDescriptionEditText = view.findViewById<EditText>(R.id.editAssetDescription)


        binding.editAssetButton.setOnClickListener {
            assetNameTextView.setVisibility(View.INVISIBLE)
            assetDescriptionTextView.setVisibility(View.INVISIBLE);

            editAssetButton.setVisibility(View.INVISIBLE)
            saveAssetButton.setVisibility(View.VISIBLE)
            deleteAssetButton.setVisibility(View.VISIBLE)

            assetNameEditText.setVisibility(View.VISIBLE)
            assetDescriptionEditText.setVisibility(View.VISIBLE)

            assetNameEditText.setText(assetNameTextView.text)
            assetDescriptionEditText.setText(assetDescriptionTextView.text)
        }

        binding.saveAssetButton.setOnClickListener {
            assetNameTextView.setVisibility(View.VISIBLE)
            assetDescriptionTextView.setVisibility(View.VISIBLE)

            editAssetButton.setVisibility(View.VISIBLE)
            saveAssetButton.setVisibility(View.INVISIBLE)
            deleteAssetButton.setVisibility(View.INVISIBLE)

            assetNameEditText.setVisibility(View.INVISIBLE)
            assetDescriptionEditText.setVisibility(View.INVISIBLE)

            assetNameTextView.setText(assetNameEditText.text)
            assetDescriptionTextView.setText(assetDescriptionEditText.text)

            if (userId != null ) {
                editAsset(userId, assetNameEditText.text.toString(), assetDescriptionEditText.text.toString(), asset)
            }
        }

        binding.deleteAssetButton.setOnClickListener {
            deleteAsset(asset.id)
            findNavController().navigate(R.id.myAssetsFragment)
        }
    }

    private fun editAsset(userId: Int, assetName: String, assetDescription: String,  editAsset: Asset) {

        editAsset.assetName = assetName
        editAsset.description = assetDescription

        println("" + userId + " " + editAsset.id + " " + editAsset.toString() + " " + editAsset.assetCondition + " " + editAsset.public)
        LaneLittApi.retrofitService.editAsset(userId, editAsset.id, editAsset).enqueue(

            object : Callback<Asset> {
                override fun onResponse(call: Call<Asset>, response: Response<Asset>) {
                    println("Endring? "+response.body()?.toString())
                    if (response.body()?.id!=null) {
                        Toast.makeText(requireContext(), "Lagret", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Ikke lagret", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Asset>, t: Throwable) {
                    println("Ikke endret?")
                    Toast.makeText(requireContext(), "Noe har gått galt", Toast.LENGTH_LONG).show()
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
                        //Toast.makeText(requireContext(), "Eiendelen er slettet", Toast.LENGTH_LONG).show()
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
}


