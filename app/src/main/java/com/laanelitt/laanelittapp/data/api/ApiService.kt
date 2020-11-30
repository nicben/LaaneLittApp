package com.laanelitt.laanelittapp.data.api

import com.laanelitt.laanelittapp.data.model.*
import com.laanelitt.laanelittapp.objects.NewUser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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
    @GET("assets/getAssetType/{userId}/{catNr}/")
    suspend fun getCatAssets(@Path("userId") userId:Int,@Path("catNr") catNr:String): List<Asset>

    @GET("assets/getMyAsset/{userId}/")
    suspend fun getMyAssets(@Path("userId") userId:Int): List<Asset>

    @GET("assets/search/{userId}/{search}/")
    suspend fun getAssetSearch(@Path("userId") userId:Int, @Path("search") search:String): List<Asset>

    @GET("api/login/{username}/{password}")
    fun login(@Path("username") username:String, @Path("password") password:String): Call<LoggedInUser>

    @POST("api/register")
    fun registerUser(@Body newUser: NewUser): Call<Code>

    @POST("user/{iUserId}/asset/{iAssetId}/request")
    fun sendLoanRequest(@Path ("iUserId") userId:Int, @Path("iAssetId") assetId:Int, @Body newLoan: Loan): Call<String>

    @PUT("assets/editAsset/{userId}/{assetId}")
    fun editAsset(@Path ("userId") userId:Int, @Path("assetId") assetId:Int, @Body editAsset: Asset): Call<Asset>

    @DELETE("assets/removeAsset/{assetId}")
    fun deleteAsset(@Path("assetId") assetId:Int): Call<String>

    @POST("user/{iUserId}/edit")
    fun editUser(@Path ("iUserId") userId:Int, @Body user: User): Call<Code>

    @GET("user/{iUserId}/loanRequest")
    suspend fun getNotifications(@Path("iUserId") userId: String): List<Notification>

    @Multipart
    @POST("profileimageUpload")
    fun uploadProfileImage(
        @Part ("userId") userId: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<Code>

    @Multipart
    @POST("assets/addAsset")
    fun addAsset(
        @Part ("userId") userId: RequestBody,
        @Part ("typeId") typeId: RequestBody,
        @Part ("condition") condition: RequestBody,
        @Part ("assetName") assetName: RequestBody,
        @Part ("public") public: RequestBody,
        @Part ("description") description: RequestBody,
        @Part ("mainImage") mainImage: RequestBody,
        @Part file:MultipartBody.Part
    ): Call<Int>

    @POST("setPublished/{userId}/{assetId}/1")
    fun publishAsset(@Path("userId")userId:Int, @Path("assetId")assetId:Int): Call<String>


    @POST("user/{iUserId}/loanRequest/{iLoanId}/{iStatus}")
    fun replyRequest(@Path("iUserId") userId: String,@Path("iLoanId") loanId: String,@Path("iStatus") status: String): Call<String>
}

// Public objekt som initialiserer Retrofit tjenestene i ApiService for applikasjonen
object LaneLittApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}
