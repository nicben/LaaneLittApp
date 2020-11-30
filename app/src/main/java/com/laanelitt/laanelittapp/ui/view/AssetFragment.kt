package com.laanelitt.laanelittapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.ui.viewModel.AssetViewModel
import com.laanelitt.laanelittapp.ui.factory.AssetViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class AssetFragment : Fragment() {
    private lateinit var binding: FragmentAssetBinding
    private lateinit var localStorage: LocalStorage
    private lateinit var viewModel: AssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Henter id'en til brukeren som er logget inn
        localStorage = LocalStorage(requireContext())
        val userId = localStorage.getLoggedInUser?.id

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        //Henter valgt eiendel
        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        //Kobler til AssetViewModelFactory og AssetViewModel
        val viewModelFactory = AssetViewModelFactory(asset, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)
        binding.viewModel = viewModel

        //Fjerner lokasjon og deaktiverer låne-knappen
        val assetId = asset.id
        if (userId == asset.users?.id) {
            binding.ownerLocation.visibility = View.INVISIBLE
            binding.pickDateButton.isEnabled = false
        }

        //Material Date Picker
        //https://ahsensaeed.com/android-material-design-library-date-picker-dialog/
        val fm = (activity as AppCompatActivity?)!!.supportFragmentManager
        val builder: MaterialDatePicker.Builder<Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()

        val constraintsBuilder = CalendarConstraints.Builder()
        val calendar = Calendar.getInstance()

        constraintsBuilder.setStart(calendar.timeInMillis)
        calendar.roll(Calendar.YEAR, 1)
        constraintsBuilder.setEnd(calendar.timeInMillis)
        builder.setCalendarConstraints(constraintsBuilder.build())

        builder.setTitleText("Velg dato")
        val picker: MaterialDatePicker<Pair<Long, Long>> = builder.build()

        binding.pickDateButton.setOnClickListener {
            picker.show(fm, picker.toString())
        }

        picker.addOnPositiveButtonClickListener { selection ->
            val start = selection.first
            val end = selection.second
            //Datoformat
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateStr = sdf.format(start)
            val endDateStr = sdf.format(end)
            //Registrer låneforespørselen med valgte datoer
            if (userId != null && startDateStr != null && endDateStr != null) {
                viewModel.sendLoanRequest(userId, assetId, startDateStr, endDateStr)
                Toast.makeText(
                    requireContext(),
                    "Valgt dato: " + picker.headerText,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return binding.root
    }//end onCreateView
}



