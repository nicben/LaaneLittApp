package com.laanelitt.laanelittapp.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNotificationListBinding
import com.laanelitt.laanelittapp.objects.LocalStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListFragment : Fragment() {

    private lateinit var localStorage: LocalStorage
    private lateinit var binding: FragmentNotificationListBinding
    private val notificationViewModel: NotificationListViewModel by lazy {
        ViewModelProvider(this).get(NotificationListViewModel()::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notification_list, container, false)

        binding.notificationViewModel = notificationViewModel

        localStorage = LocalStorage(requireContext())
        val userId = localStorage.getLoggedInUser?.id.toString()

        notificationViewModel.getNotifications(userId)
        binding.lifecycleOwner = this

        notificationViewModel.response.observe(viewLifecycleOwner, {
            if(it == notificationViewModel.status[1]){
                Toast.makeText(context, "Feilet, prøver på nytt", Toast.LENGTH_LONG).show()
            }else if(it == notificationViewModel.status[2]){
                Toast.makeText(context, "Noe gikk galt", Toast.LENGTH_LONG).show()
                this.findNavController().navigate(R.id.homePageFragment)
            }else if(it == notificationViewModel.status[3]) {
               this. findNavController().navigate(R.id.notificationsFragment)
            }
        })
        return binding.root
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        binding.notificationPhotosGrid.adapter=NotificationListAdapter(NotificationListAdapter.OnClickListener{
            //Alert Dialog
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.alert_title))
                .setMessage(it.users?.firstName+ " "+it.users?.lastName+" ønsker å låne "+it.assets?.assetName +"\n"+ it.dateStart + " - " + it.dateEnd)
                .setNeutralButton(resources.getString(R.string.close)) { _, _ ->
                    // Respond to neutral button press
                }
                .setNegativeButton(resources.getString(R.string.decline)) { _, _ ->
                    notificationViewModel.reply(it.id!!, localStorage.getLoggedInUser!!.id!!, 2)// Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    notificationViewModel.reply(it.id!!, localStorage.getLoggedInUser!!.id!!, 1)
                }
                .show()
        })

    }//end onViewCreated

    private fun observeAuthenticationState() {
        val loggedInUser = localStorage.getLoggedInUser
        if (loggedInUser == null) {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }//end observeAuthenticationState
}