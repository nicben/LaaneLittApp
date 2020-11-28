package com.laanelitt.laanelittapp.profile.settings.editimage

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
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditImageBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModel
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModelFactory
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
    private lateinit var viewModel: EditImageViewModel
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

        val application = requireNotNull(activity).application
        val viewModelFactory = EditImageViewModelFactory(loggedInUser, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditImageViewModel::class.java)

        binding.editViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.takePicture.setOnClickListener {
            if (allPermissionsGranted())  {
                println("if**************")
                takePicture()
            }
            else{
                println("else**************")
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var profilePicture = "https://lanelitt.no/profileImages/"+localStorage.getLoggedInUser!!.profileImage.toString()
        var  imageUri = profilePicture.toUri().buildUpon().scheme("https").build()
        if(Pref.getNewPicture(requireContext(), "ID", "null").toString()!="null"){
            profilePicture = Pref.getNewPicture(requireContext(), "ID", "null")!!
            imageUri = profilePicture.toUri()
            println("get from pref  :::  "+profilePicture)
        }

        if(profilePicture != "https://lanelitt.no/profileImages/"){
            Glide.with(requireContext()).load(imageUri).into(binding.image)
        }
    }

    object Pref {
        private val PREF_FILE: String =
            com.laanelitt.laanelittapp.BuildConfig.APPLICATION_ID.replace(
                ".",
                "_"
            )
        private var sharedPreferences: SharedPreferences? = null
        private fun openPref(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        }

        fun getNewPicture(context: Context, key: String?, defaultValue: String?): String? {
            openPref(context)
            val result = sharedPreferences!!.getString(key, defaultValue)
            sharedPreferences = null
            return result
        }

        fun setNewPicture(context: Context, key: String?, value: String?) {
            openPref(context)
            val prefsPrivateEditor: SharedPreferences.Editor = sharedPreferences!!.edit()
            prefsPrivateEditor.putString(key, value)
            prefsPrivateEditor.apply()
            sharedPreferences = null
        }

    }

    private fun save(userId:String){
        if(originalFile != null){

            val userIdPart = RequestBody.create(MultipartBody.FORM, userId)

            //Bilde som skal sendest med API kallet
            val filePart = RequestBody.create(MediaType.parse("image/*"), originalFile !!)
            val file = MultipartBody.Part.createFormData("file", originalFile !!.name, filePart)

            LaneLittApi.retrofitService.uploadProfileImage(userIdPart, file).enqueue(
                object : Callback<Code> {
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        Toast.makeText(context, "Bilde opplastning gikk bra", Toast.LENGTH_LONG).show()
                        Pref.setNewPicture(requireContext(), "ID", originalFile !!.toUri().toString())
                        findNavController().navigate(R.id.myAssetsListFragment)
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println(t.message+":::::::::::::::::::::::::")
                    }
                }
            )
        }else{
            Toast.makeText(requireContext(), "velg ett bilde før du lagrer", Toast.LENGTH_LONG).show()
        }
    }

    private fun takePicture(){
        println("take---------------------------------")
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            try {
                println("try---------------------------------")
                originalFile = createImageFile()
            }catch (e: IOException){
                println("catch---------------------------------")
            }
            if(originalFile !=null){
                println("if---------------------------------")
                val fileUri= FileProvider.getUriForFile(requireContext(),
                    "com.laanelitt.laanelittapp.fileprovider", originalFile !!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                pathToPictureFile = originalFile !!.absolutePath
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
        else{
            println("else---------------------------------")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?){

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                Glide.with(this).load(pathToPictureFile).into(binding.image)
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile():File?{
        var imageFile:File? = null
        val photoStorageDir = getPhotoDirectory()
        if (photoStorageDir != null){
            val timeStamp= SimpleDateFormat("YMMdd-HHmss").format(Date())
            val imageFileName = photoStorageDir.path+File.separator.toString()+"LaaneLitt_"+timeStamp+".jpeg"
            imageFile = File(imageFileName)
            println(imageFileName)
        }
        return imageFile
    }

    private fun getPhotoDirectory() : File? {
        // Finner / lager undermappe for mine bilder under Pictures mappen som er felles for alle apper
        val mediaStorageDirectory = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_PHOTO_FOLDER)
        // Sjekk om mappen finnes
        if (!mediaStorageDirectory.exists()) {
            // Hvis ikke: Lag mappen
            if (!mediaStorageDirectory.mkdirs()) {
                println("Laanelitt: " + "Klarte ikke å lage mappen: " +
                        mediaStorageDirectory.getAbsolutePath())
                Toast.makeText(requireContext(), "Klarte ikke å lage mappen: " +
                        mediaStorageDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show()
                return null
            }
        }
        return mediaStorageDirectory
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        println("allPermissionsGranted start**************")

        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode== REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                println("Permission granted")
                Toast.makeText(context, "Permission granted", Toast.LENGTH_LONG).show()
                takePicture()
            }else{
                println("Permission not granted")
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show()

            }
        }
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val MY_PHOTO_FOLDER = "LaaneLitt"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}