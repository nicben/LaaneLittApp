package com.laanelitt.laanelittapp.asset

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding
import kotlinx.android.synthetic.main.fragment_asset.*


//profil til item - info

class AssetFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(AssetViewModel::class.java)


        // Material Date Picker  -->
        // https://brandonlehr.com/android/learn-to-code/2018/08/19/callling-android-datepicker-fragment-from-a-fragment-and-getting-the-date
        val fm = (activity as AppCompatActivity?)!!.supportFragmentManager
        val builder : MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()

        builder.setTitleText("Velg dato")
        val picker : MaterialDatePicker<*> = builder.build()

        binding.btnPickDate.setOnClickListener {
            picker.show(fm, picker.toString())
        }
        /*picker.addOnPositiveButtonClickListener{
            dateTextView.setText("Valgt dato:" + picker.headerText)
        }*/


        // val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        /* val args = AssetFragmentArgs.fromBundle(requireArguments())
     Toast.makeText(context, "catId: ${args.assetId}", Toast.LENGTH_LONG).show()

     binding.btn.tOnClickListener { view : View ->
         view.findNavController().navigate(R.id.action_assetFragment_to_loanRequestPopupFragment)
     }*/

    return binding.root

    }

}