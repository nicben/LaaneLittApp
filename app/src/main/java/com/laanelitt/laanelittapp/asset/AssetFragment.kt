package com.laanelitt.laanelittapp.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding
import com.laanelitt.laanelittapp.objects.Loan
import com.laanelitt.laanelittapp.objects.LocalStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*



class AssetFragment : Fragment(){
    private lateinit var localStorage: LocalStorage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        localStorage = LocalStorage(requireContext())
        val userId = localStorage.getLoggedInUser?.id

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)

        val assetId = asset.id
        if(asset.users?.zipCode?.id == null){
            binding.ownerLocation.visibility = View.INVISIBLE
        }

        // Material Date Picker
        val fm = (activity as AppCompatActivity?)!!.supportFragmentManager
        val builder : MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()

        val constraintsBuilder = CalendarConstraints.Builder()
        val calendar = Calendar.getInstance()

        constraintsBuilder.setStart(calendar.timeInMillis)
        calendar.roll(Calendar.YEAR, 1)
        constraintsBuilder.setEnd(calendar.timeInMillis)
        builder.setCalendarConstraints(constraintsBuilder.build())

        builder.setTitleText("Velg dato")
        val picker: MaterialDatePicker<Pair<Long, Long>>  = builder.build()

        binding.pickDateButton.setOnClickListener {
            picker.show(fm, picker.toString())
        }

        picker.addOnPositiveButtonClickListener { selection ->
            val start = selection.first
            val end = selection.second

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            //format yyyy-MM-dd
            val startDateStr = sdf.format(start)
            val endDateStr = sdf.format(end)

            if (userId != null && startDateStr != null && endDateStr != null) {
                sendLoanRequest(userId, assetId, startDateStr, endDateStr)
            }

        }

        return binding.root
    }//end onCreateView

    private fun sendLoanRequest(userId: Int, assetId: Int, startDate: String, endDate: String) {

        val newLoan = Loan(startDate, endDate)
        //ApiService
        LaneLittApi.retrofitService.sendLoanRequest(userId, assetId, newLoan).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }
            }
        )
    }//end sendLoanRequest
}


