package com.laanelitt.laanelittapp.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.laanelitt.laanelittapp.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.databinding.FragmentNotificationBinding
import com.laanelitt.laanelittapp.homepage.userLocalStore
import com.laanelitt.laanelittapp.profile.myassets.MyAssetFragmentArgs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notification, container, false
        )
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notification = NotificationFragmentArgs.fromBundle(requireArguments()).notification

        binding.popupWindowTitle.text=notification.dateStart+" - "+notification.dateEnd
        binding.popupWindowText.text=notification.users?.firstName+" "+notification.users?.lastName+" vil gjerne låne "+notification.assets?.assetName

        binding.accept.setOnClickListener {
            reply(notification.id!!, userLocalStore?.getLoggedInUser!!.id!!, 1)
        }
        binding.deny.setOnClickListener {
            reply(notification.id!!, userLocalStore?.getLoggedInUser!!.id!!, 2)
        }
    }
    fun reply(id:Int, userId: Int, reply: Int){
        LaneLittApi.retrofitService.replyRequest(userId.toString(), id.toString(), reply.toString()).enqueue(
            object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Toast.makeText(requireContext(), response.body(), Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.notificationsFragment)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )

    }
}