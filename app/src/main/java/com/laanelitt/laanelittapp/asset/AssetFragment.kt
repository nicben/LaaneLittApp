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
import com.laanelitt.laanelittapp.login.LoginFragment
import com.laanelitt.laanelittapp.objects.Loan
import com.laanelitt.laanelittapp.objects.RequestStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


//profil til item - info

class AssetFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(AssetViewModel::class.java)


//        // Material Date Picker  -->
//        // https://brandonlehr.com/android/learn-to-code/2018/08/19/callling-android-datepicker-fragment-from-a-fragment-and-getting-the-date
//        val fm = (activity as AppCompatActivity?)!!.supportFragmentManager
//        val builder : MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()
//
//        builder.setTitleText("Velg dato")
//        val picker : MaterialDatePicker<*> = builder.build()
//
//        binding.btnPickDate.setOnClickListener {
//            picker.show(fm, picker.toString())
//        }
//        picker.addOnPositiveButtonClickListener{
//            //dateTextView.setText("Valgt dato:" + picker.selection)
//            val dates = picker.headerText
//            Toast.makeText(context, "Valgt dato: ${dates}", Toast.LENGTH_LONG).show()
//            //sendLoanRequest()
//
//        }

        var userId = 61 //LoginFragment.Pref.getUserId(requireContext(), "ID", "null")
        var assetId = 36

        //------DatePicker-------
        val fm = (activity as AppCompatActivity?)!!.supportFragmentManager
        val builder : MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()

        val constraintsBuilder = CalendarConstraints.Builder()  // 1
        val calendar = Calendar.getInstance()

        constraintsBuilder.setStart(calendar.timeInMillis)   //   2
        calendar.roll(Calendar.YEAR, 1)   //   3
        constraintsBuilder.setEnd(calendar.timeInMillis)   // 4
        builder.setCalendarConstraints(constraintsBuilder.build())   //  5

        builder.setTitleText("Velg dato")
        val picker: MaterialDatePicker<Pair<Long, Long>>  = builder.build()

        binding.btnPickDate.setOnClickListener {
            picker.show(fm, picker.toString())
        }
//        picker.addOnPositiveButtonClickListener{
//            //dateTextView.setText("Valgt dato:" + picker.selection)
//            val dates = picker.headerText
//            Toast.makeText(context, "Valgt dato: ${dates}", Toast.LENGTH_LONG).show()
//            //sendLoanRequest()
//
//        }

        picker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>> { selection ->
            val start = selection.first
            val startDate = start?.let { Date(it) }
            val end = selection.second
            val endDate = end?.let { Date(it) }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            //format yyyy-MM-dd
            val startDateStr = sdf.format(start)
            val endDateStr = sdf.format(end)



//            if (userId != null && assetId !=null && startDate != null && endDate != null) {
//                sendLoanRequest(userId, assetId, startDate, endDate)
//            }

            Toast.makeText(context, "Valgt dato: ${picker.headerText}", Toast.LENGTH_LONG).show()
        })

    return binding.root

    }


    private fun sendLoanRequest(userId: Int, assetId: Int, startDate: Date, endDate: Date) {
        val statusSendt = RequestStatus (null, null )
        val newLoan = Loan(null, null, null, startDate, endDate, statusSendt)

        LaneLittApi.retrofitService.sendLoanRequest(userId, assetId, newLoan).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println("Nytt lån? " + response.body())
                    if (response.body() == "Låneforhold er opprettet") {
                        Toast.makeText(requireContext(), "Godkjent", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "Ikke godkjent", Toast.LENGTH_LONG).show()
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


