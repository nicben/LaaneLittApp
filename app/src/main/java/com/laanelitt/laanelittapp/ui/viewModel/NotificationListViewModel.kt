package com.laanelitt.laanelittapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import kotlinx.coroutines.launch
import com.laanelitt.laanelittapp.data.model.Notification
import com.laanelitt.laanelittapp.utils.progressStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListViewModel: ViewModel(){

    private val _response = MutableLiveData<String>()
    private var _notifications = MutableLiveData<List<Notification>>()

    val response: LiveData<String>
        get() = _response
    val notifications: LiveData<List<Notification>>
        get() = _notifications

    fun getNotifications(userId: String){
        viewModelScope.launch {
            _response.value = progressStatus[0]
            try {
                val listResult = LaneLittApi.retrofitService.getNotifications(userId)
                println("Success: ${listResult.size}" )
                _notifications.value = listResult
                _response.value = progressStatus[1]
            }catch (e: Exception){
                println( "Failiure: ${e.message}")
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message == progressStatus[4]){
                    println( "Failiure: ${e.message}")
                    _response.value = progressStatus[2]
                    getNotifications(userId)
                }else{
                    _response.value = progressStatus[3]
                }
            }
        }
    }
    fun reply(id:Int, userId: String, reply: Int){
        _response.value = progressStatus[0]
        LaneLittApi.retrofitService.replyRequest(userId, id.toString(), reply.toString()).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = progressStatus[1]
                    getNotifications(userId)
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = progressStatus[3]
                }
            }
        )
    }//end reply

}