package com.laanelitt.laanelittapp.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import kotlinx.coroutines.launch
import com.laanelitt.laanelittapp.objects.Notification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListViewModel: ViewModel(){

    val status = arrayOf("timeout", "Failure", "Critical failure", "Success")

    private val _response = MutableLiveData<String>()
    private var _notifications = MutableLiveData<List<Notification>>()

    val response: LiveData<String>
        get() = _response
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    fun getNotifications(userId: String){
        viewModelScope.launch {
            try {
                val listResult= LaneLittApi.retrofitService.getNotifications(userId)
                _response.value = "Success: ${listResult.size}  requests retrieved ******"
                _notifications.value = listResult

            }catch (e: Exception){
                _response.value = "Failiure: ${e.message}"
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message == status[0]) {
                    _response.value = status[1]
                    getNotifications(userId)
                }else{
                    _response.value = status[2]
                }
            }

        }
    }
    fun reply(id:Int, userId: Int, reply: Int){
        LaneLittApi.retrofitService.replyRequest(userId.toString(), id.toString(), reply.toString()).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = status[3]
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = status[1]
                }
            }
        )
    }//end reply

}