package com.laanelitt.laanelittapp.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.LaneLittApi
import kotlinx.coroutines.launch
import com.laanelitt.laanelittapp.objects.Notification

class NotificationListViewModel: ViewModel(){

    private val _response=MutableLiveData<String>()
    private var _notifications=MutableLiveData<List<Notification>>()

    val response: LiveData<String>
        get() = _response
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    private val _navigateToSelectedProperty=MutableLiveData<Notification>()

    val navigateToSelectedProperty: LiveData<Notification>
        get() = _navigateToSelectedProperty

    fun getNotifications(userId: String){
        viewModelScope.launch {
            try {
                val listResult= LaneLittApi.retrofitService.getNotifications(userId)
                _response.value = "Success: ${listResult.size}  requests retrieved ******"
                _notifications.value = listResult
                println(_response.value)
            }catch (e: Exception){
                _response.value="Failiure: ${e.message}"
                println(_response.value)
            }

        }
    }

    fun displayPropertyDetails(notification: Notification) {
        _navigateToSelectedProperty.value = notification
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }


}