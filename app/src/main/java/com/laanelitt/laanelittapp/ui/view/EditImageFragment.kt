package com.laanelitt.laanelittapp.ui.view

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditImageBinding
import com.laanelitt.laanelittapp.data.model.Code
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.User
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class EditImageFragment : Fragment() {
    private lateinit var binding: FragmentEditImageBinding
    private lateinit var localStorage: LocalStorage
    private lateinit var loggedInUser: User
    private var pathToPictureFile = ""
    private var originalFile: File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//      Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!
        val userId= localStorage.getLoggedInUser!!.id.toString()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_image,
            container,
            false
        )


        binding.takePicture.setOnClickListener {
            if (allPermissionsGranted())  {
                takePicture()
            }
            else{
                //Hvis applikasjonen ikke har tilgang til kameraet så må den spør om det
                requestPermissions(
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
        binding.saveButton.setOnClickListener{
            save(userId)
        }

        return binding.root
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profilePicture = "https://lanelitt.no/profileImages/"+loggedInUser.profileImage.toString()
        val imageUri = profilePicture.toUri().buildUpon().scheme("https").build()
        //Viser profilbilde til brukeren, hvis det har ett.
        if(profilePicture != "https://lanelitt.no/profileImages/"){
            Glide.with(requireContext()).load(imageUri).into(binding.image)
        }

    }//End onViewCreated


    private fun save(userId:String){
        if(originalFile != null){
            //Denne funksjonen bruker multipart på API kallet for å få sende bilde til backenden,
            //https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server har delvis blit fulgt her

            val userIdPart = RequestBody.create(MultipartBody.FORM, userId)

            //Bilde som skal sendest med API kallet
            val filePart = RequestBody.create(MediaType.parse("image/*"), originalFile !!)
            val file = MultipartBody.Part.createFormData("file", originalFile !!.name, filePart)

            LaneLittApi.retrofitService.uploadProfileImage(userIdPart, file).enqueue(
                object : Callback<Code> {
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        Toast.makeText(context, "Bilde opplastning gikk bra", Toast.LENGTH_LONG).show()
                        loggedInUser.profileImage=response.body()!!.image

                        localStorage.updateUser(loggedInUser)
                        findNavController().navigate(R.id.myAssetsListFragment)
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                    }
                }
            )
        }else{
            Toast.makeText(requireContext(), "velg ett bilde før du lagrer", Toast.LENGTH_LONG).show()
        }
    }//end Save

    //Funksjon som starter et Intent for å ta bilde
    private fun takePicture(){
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            try {
                //Oppretter en fil slik at vi kan lagre bilde, med høy oppløsning
                originalFile = createImageFile()
            }catch (e: IOException){
            }
            if(originalFile !=null){
                val fileUri= FileProvider.getUriForFile(requireContext(),
                    "com.laanelitt.laanelittapp.fileprovider", originalFile !!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                pathToPictureFile = originalFile !!.absolutePath
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }//end takePicture

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?){

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                Glide.with(this).load(pathToPictureFile).into(binding.image)
            }
        }
    }//end onActivityResult

    //funksjon for å opprette en fil som bilde kan lagres i
    @Throws(IOException::class)
    private fun createImageFile():File?{
        var imageFile:File? = null
        val photoStorageDir = getPhotoDirectory()
        if (photoStorageDir != null){
            //Oppretter et unikt navn for bilde filen
            val timeStamp= SimpleDateFormat("yyyyMMdd-HHmss").format(Date())
            val imageFileName = photoStorageDir.path+File.separator.toString()+"LaaneLitt_"+timeStamp+".jpeg"
            imageFile = File(imageFileName)
        }
        return imageFile
    }//end createImageFile

    private fun getPhotoDirectory() : File? {
        // Finner / lager undermappe for mine bilder under Pictures mappen som er felles for alle apper
        val mediaStorageDirectory = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_PHOTO_FOLDER)
        // Sjekk om mappen finnes
        if (!mediaStorageDirectory.exists()) {
            // Hvis ikke: Lag mappen
            if (!mediaStorageDirectory.mkdirs()) {
                Toast.makeText(requireContext(), "Klarte ikke å lage mappen: " +
                        mediaStorageDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show()
                return null
            }
        }
        return mediaStorageDirectory
    }//end  getPhotoDirectory

    //Sjekk for å se om applikasjonen har tilgang til kameraet
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }//end allPermissionsGranted

    //Etter at brukeren har git eller nektet tilatelse så kjører denne funksjonen
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode== REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                Toast.makeText(context, "Permission granted", Toast.LENGTH_LONG).show()
                takePicture()
            }else{
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show()

            }
        }
    }//end onRequestPermissionsResult

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val MY_PHOTO_FOLDER = "LaaneLitt"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }//end companion

}