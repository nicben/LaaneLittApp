package com.laanelitt.laanelittapp.asset

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
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding
import com.laanelitt.laanelittapp.homepage.localStorage
import com.laanelitt.laanelittapp.objects.Loan
import com.laanelitt.laanelittapp.objects.LocalStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*



class AssetFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        localStorage = LocalStorage(requireContext())
        val userId = localStorage?.getLoggedInUser?.id

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)
        val assetId = asset.id

//        // Material Date Picker  -->
//        // https://brandonlehr.com/android/learn-to-code/2018/08/19/callling-android-datepicker-fragment-from-a-fragment-and-getting-the-date

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

        picker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>> { selection ->
            val start = selection.first
            val end = selection.second

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            //format yyyy-MM-dd
            val startDateStr = sdf.format(start)
            val endDateStr = sdf.format(end)

            if (userId != null  && startDateStr != null && endDateStr != null) {
                sendLoanRequest(userId, assetId, startDateStr, endDateStr, picker.headerText)
            }

        })

    return binding.root
    }

    private fun sendLoanRequest(userId: Int, assetId: Int, startDate: String, endDate: String, dates: String) {

        val newLoan = Loan(startDate, endDate)
        //API-kallet
        LaneLittApi.retrofitService.sendLoanRequest(userId, assetId, newLoan).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println("Nytt lån? " + response.body())
                    if (response.body() == "Låneforhold er opprettet") {
                        Toast.makeText(requireContext(), "Valgt dato: ${dates}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Låneforholdet ble ikke opprettet", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("Ikke nytt lån?")
                    Toast.makeText(requireContext(), "Noe har gått galt", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}


