package com.laanelitt.laanelittapp

import com.laanelitt.laanelittapp.objects.Assets
import com.laanelitt.laanelittapp.objects.LoginUser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL="https://lanelitt.no/public/"

// Bruk Moshi builder for å bygge et Moshi objekt
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Bruk Retrofit builder for å lage et Retrofit-objekt med Moshi som "converter"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Lag et interface som definerer hvordan Retrofit kaller APIet
interface ApiService {
    @GET("assets/getAssetType/134/{catNr}/")
    suspend fun getCatAssets(@Path("catNr") categoiNr:String): List<Assets>

    @GET("assets/getAssetType/134/{}/")
    fun getSearchAssets(): Call<List<Assets>>

    @GET("api/login/{uName}/{pWord}")
    fun getLoggin(@Path("uName") userName:String, @Path("pWord") password:String): Call<LoginUser>

   // @POST("/api/register")
    //fun registerUser():

}

// Her kan du definere flere metoder for å kalle andre API-endepunkter
// Public objekt som initialiserer Retrofit tjenestene i ApiService for applikasjonen
object LaneLittApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}
