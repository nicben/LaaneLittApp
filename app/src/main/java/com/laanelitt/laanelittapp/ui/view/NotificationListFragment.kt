package com.laanelitt.laanelittapp.ui.view

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
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentNotificationListBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.ui.adapter.NotificationListAdapter
import com.laanelitt.laanelittapp.ui.viewModel.NotificationListViewModel
import com.laanelitt.laanelittapp.utils.observeAuthenticationState
import com.laanelitt.laanelittapp.utils.progressStatus
import kotlinx.android.synthetic.main.fragment_category_list.*

class NotificationListFragment : Fragment() {

    private lateinit var localStorage: LocalStorage
    private lateinit var binding: FragmentNotificationListBinding
    private val notificationViewModel: NotificationListViewModel by lazy {
        ViewModelProvider(this).get(NotificationListViewModel()::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        //Henter id'en til brukeren som er innlogget
        localStorage = LocalStorage(requireContext())
        val userId = localStorage.getLoggedInUser?.id.toString()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notification_list, container, false)

        binding.notificationViewModel = notificationViewModel
        notificationViewModel.getNotifications(userId)
        binding.lifecycleOwner = this

        notificationViewModel.response.observe(viewLifecycleOwner, {
            if (it == progressStatus[0]){
                progressbar.visibility = View.VISIBLE
            }
            else if (it == progressStatus[1]){
                progressbar.visibility = View.GONE
            }
            else if (it == progressStatus[3]){
                progressbar.visibility = View.GONE
                Toast.makeText(context, "Noe gikk galt",Toast.LENGTH_LONG).show()
                this.findNavController().navigate(R.id.homePageFragment)
            }
        })
        return binding.root
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState(localStorage, this)

        binding.notificationPhotosGrid.adapter = NotificationListAdapter(NotificationListAdapter.OnClickListener{
            //Alert Dialog
            //https://material.io/components/dialogs
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.alert_title))
                .setMessage(it.users?.firstName+ " "+it.users?.lastName+" ønsker å låne "+it.assets?.assetName +"\n"+ it.dateStart + " - " + it.dateEnd)
                .setNeutralButton(resources.getString(R.string.close)) { _, _ ->
                }
                .setNegativeButton(resources.getString(R.string.decline)) { _, _ ->
                    notificationViewModel.reply(it.id!!, localStorage.getLoggedInUser!!.id!!.toString(), 2)// Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    notificationViewModel.reply(it.id!!, localStorage.getLoggedInUser!!.id!!.toString(), 1)
                }
                .show()
        })
    }//end onViewCreated
}