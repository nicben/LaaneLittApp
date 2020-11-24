package com.laanelitt.laanelittapp.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNotificationListBinding
import com.laanelitt.laanelittapp.homepage.localStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListFragment : Fragment() {

    private val notificationViewModel: NotificationListViewModel by lazy {
        ViewModelProvider(this).get(NotificationListViewModel()::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding= FragmentNotificationListBinding.inflate(inflater)

        binding.lifecycleOwner=this
        binding.notificationViewModel=notificationViewModel

        val userId = localStorage?.getLoggedInUser?.id.toString()

        notificationViewModel.getNotifications(userId)



        /*val helper=ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )=false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        })
        helper.attachToRecyclerView(binding.notificationPhotosGrid)*/


        binding.notificationPhotosGrid.adapter=NotificationListAdapter(NotificationListAdapter.OnClickListener{
            //Alert Dialog
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.alert_title))
                .setMessage(it.users?.firstName+ " "+it.users?.lastName+" ønsker å låne "+it.assets?.assetName +"\n"+ it.dateStart + " - " + it.dateEnd)
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    reply(it.id!!, localStorage?.getLoggedInUser!!.id!!, 2)// Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    reply(it.id!!, localStorage?.getLoggedInUser!!.id!!, 1)
                }
                .show()
            println(it.dateStart)
//            notificationViewModel.displayPropertyDetails(it)
        })
//        notificationViewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
//            if (null != it) {
//                this.findNavController().navigate(NotificationListFragmentDirections.actionNotificationsFragmentToNotificationFragment(it))
//                notificationViewModel.displayPropertyDetailsComplete()
//                // Must find the NavController from the Fragment
//            }
//        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("lol")
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
    }
    fun reply(id:Int, userId: Int, reply: Int){
        LaneLittApi.retrofitService.replyRequest(userId.toString(), id.toString(), reply.toString()).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //Toast.makeText(requireContext(), response.body(), Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.notificationsFragment)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )

    }

    fun observeAuthenticationState() {

        val loggedInUser = localStorage?.getLoggedInUser
        if (loggedInUser != null) {
          //  val userInfo = ""+ loggedInUser.id + " " + loggedInUser.firstname + " " + loggedInUser.lastname + " " + loggedInUser.profileImage


        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }

}