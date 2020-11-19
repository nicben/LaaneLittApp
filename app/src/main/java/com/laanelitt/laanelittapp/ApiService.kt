package com.laanelitt.laanelittapp

import com.laanelitt.laanelittapp.objects.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File

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
    suspend fun getCatAssets(@Path("catNr") categoryNr:String): List<Asset>

    @GET("assets/getMyAsset/{userId}/")
    suspend fun getMyAssets(@Path("userId") userId:String): List<Asset>

    @GET("assets/search/{userId}/{search}/")
    suspend fun getAssetSearch(@Path("userId") userId:String, @Path("search") search:String): List<Asset>

    @GET("api/login/{uName}/{pWord}")
    fun login(@Path("uName") userName:String, @Path("pWord") password:String): Call<LoggedInUser>

    @POST("api/register")
    fun registerUser(@Body newUser: AssetOwner): Call<Code>

    @POST("user/{iUserId}/asset/{iAssetId}/request")
    fun sendLoanRequest(@Path ("iUserId") userId:String, @Path("iAssetId") assetId:Int, @Body newLoan: Loan): Call<String>

    @PUT("assets/editAsset/{userId}/{assetId}")
    fun editAsset(@Path ("userId") userId:Int, @Path("assetId") assetId:Int, @Body editAsset: Asset): Call<Asset>

    @DELETE("assets/removeAsset/{assetId}")
    fun deleteAsset(@Path("assetId") assetId:Int): Call<String>

    @POST("updatePassword")
    fun updatePassword(@Body user: AssetOwner): Call<String>

    @POST("user/{iUserId}/edit")
    fun editUser(@Path ("iUserId") userId:Int, @Body user: User): Call<Code>

    @GET("user/{iUserId}/loanRequest")
    suspend fun getNotifications(@Path("iUserId") userId: String): List<Notification>


    @POST("profileimageUpload")
    fun uploadProfileImage(@Body profilePicture: ProfilePicture): Call<Code>

    @POST("assets/addAsset")
    fun addAsset(@Body NewAsset: AddAsset): Call<String>
/*


    @POST("assets/addAsset")
    fun addAsset(@Body NewAsset: Asset): Call<Code>

    getLoanRequest:
        path: /user/{iUserId}/loanRequest
        controller: App\Controller\LoanController::getLoanRequest
        methods:    GET

    replyLoanRequests:
      path: /user/{iUserId}/loanRequest/{iLoanId}/{iStatus}
      controller: App\Controller\LoanController::replyLoanRequest
      methods:    POST
  */

}

// Her kan du definere flere metoder for å kalle andre API-endepunkter
// Public objekt som initialiserer Retrofit tjenestene i ApiService for applikasjonen
object LaneLittApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}
