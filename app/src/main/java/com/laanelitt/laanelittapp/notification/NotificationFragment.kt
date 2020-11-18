package com.laanelitt.laanelittapp.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.NotificationListViewModel
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNotificationBinding
import com.laanelitt.laanelittapp.login.LoginFragment

class NotificationFragment : Fragment() {



    private val notificationViewModel: NotificationListViewModel by lazy {
        ViewModelProvider(this).get(NotificationListViewModel()::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding=FragmentNotificationBinding.inflate(inflater)

        binding.lifecycleOwner=this
        binding.notificationViewModel=notificationViewModel

        val userId = LoginFragment.Pref.getUserId(requireContext(), "ID", "null")
        if(userId!=null) {
            notificationViewModel.getNotifications(userId)
        }
        binding.notificationPhotosGrid.adapter=NotificationAdapter(NotificationAdapter.OnClickListener{
            println(it.dateStart)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("lol")
        super.onViewCreated(view, savedInstanceState)
    }

}