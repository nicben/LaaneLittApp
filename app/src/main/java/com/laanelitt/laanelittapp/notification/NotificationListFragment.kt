package com.laanelitt.laanelittapp.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNotificationListBinding
import com.laanelitt.laanelittapp.homepage.localStorage

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
            println(it.dateStart)
            notificationViewModel.displayPropertyDetails(it)
        })
        notificationViewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(NotificationListFragmentDirections.actionNotificationsFragmentToNotificationFragment(it))
                notificationViewModel.displayPropertyDetailsComplete()
                // Must find the NavController from the Fragment
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("lol")
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
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