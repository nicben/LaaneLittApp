package com.laanelitt.laanelittapp.profile

import android.Manifest
import android.content.Intent
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
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentAddAssetBinding
import com.laanelitt.laanelittapp.homepage.userLocalStore
import com.laanelitt.laanelittapp.objects.AddAsset
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws


class AddAssetFragment : Fragment() {

    //private val vievModel: AddAssetViewModel()
    private lateinit var binding:FragmentAddAssetBinding
    private var pathTilBildeFil=""
    private var file:File?=null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userId= userLocalStore?.getLoggedInUser!!.id.toString()
        observeAuthenticationState()



        if (userId == "") {
            findNavController().navigate(R.id.loginFragment)}
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_asset,container,false)

        binding.addImage.setOnClickListener {
            pickImageFromGallery()
        }
        binding.takePicture.setOnClickListener {
            if (allPermissionsGranted())  {
                println("if**************")
                takePicture()
            }
            else{
                println("else**************")
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                //ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        binding.saveButton.setOnClickListener { view : View ->
            if (userId != null) {
                save(userId)
            }
            //view.findNavController().navigate(R.id.action_addAssetFragment_to_myAssetsListFragment)
        }

        return binding.root

    }
    fun observeAuthenticationState() {

        val loggedInUser = userLocalStore?.getLoggedInUser
        if (loggedInUser == null) {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.run {
//            putString(STATE_IMAGE_PATH, pathTilBildeFil)
//        }
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        savedInstanceState?.run{
//            pathTilBildeFil= getString(STATE_IMAGE_PATH).toString()
//        }
//    }

    fun save(userId:String){
        if(file!=null){
            val newAsset= AddAsset(userId, binding.name.toString(), binding.description.toString(), 1)
            LaneLittApi.retrofitService.addAsset(newAsset).enqueue(
                object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        println(response.body()+" -----  "+ call.toString())
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println(call.toString()+"   ++++    "+t.message)
                    }

                }
            )
/*            val profilePicture=ProfilePicture(userId, file)
            LaneLittApi.retrofitService.uploadProfileImage(profilePicture).enqueue(
                object : Callback<Code>{
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {

                        println(">>>>>>>>>>>>>>>>>>>"+response.body()?.code)
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println(t.message+":::::::::::::::::::::::::")
                    }
                }
            )*/
        }else{
            Toast.makeText(requireContext(), "velg ett bilde", Toast.LENGTH_LONG).show()
        }
    }

    fun takePicture(){
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            try {
                println("take picture try")
                file=createImageFile()
                println("take picture try2")
            }catch (e:IOException){
                println(e.message+"loLoLOLOLOLO")
            }
            if(file!=null){
                println("take picture if1")
                val fileUri= FileProvider.getUriForFile(requireContext(),
                    "com.laanelitt.laanelittapp.fileprovider", file!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                pathTilBildeFil=file!!.absolutePath
                println("take picture if2 "+ fileUri+" : "+pathTilBildeFil)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
        //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
    }

    fun pickImageFromGallery(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
        println("valg av bilde har skjedd")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?){
        println("Nå skal bilde komme opp: "+requestCode+" ")
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                Glide.with(this).load(pathTilBildeFil).into(binding.image)
//                val extras = returnIntent!!.extras
//                val imageBitmap = extras!!["data"] as Bitmap?
//                binding.image.setImageBitmap(imageBitmap)
            }
            if (requestCode == REQUEST_PICK_IMAGE) {
                file = File(returnIntent?.data?.path)
                println(file?.name + " :::::: " + file?.path + " :::: " + file?.extension + ":::::")
                println(returnIntent?.data)

                //            val image=BitmapFactory.decodeFile(returnIntent?.dataString)
                //            println(image)
                //            binding.image.setImageBitmap(image)

                binding.image.setImageURI(returnIntent?.data)

            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile():File?{
        var imageFile:File?=null
        val photoStorageDir=getPhotoDirectory()
        if (photoStorageDir!=null){
            val timeStamp= SimpleDateFormat("YMMdd-HHmss").format(Date())
            val imageFileName=photoStorageDir.path+File.separator.toString()+"LaaneLitt_"+timeStamp+".jpg"
            imageFile= File(imageFileName)
            println(imageFileName)
        }
        return imageFile
    }

    private fun getPhotoDirectory() : File? {
        // Finner / lager undermappe for mine bilder under Pictures mappen som er felles for alle apper
        val mediaStorageDirectory = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_PHOTO_FOLDER);
        // Sjekk om mappen finnes
        if (!mediaStorageDirectory.exists()) {
            // Hvis ikke: Lag mappen
            if (!mediaStorageDirectory.mkdirs()) {
                println("Laanelitt: " + "Klarte ikke å lage mappen: " +
                        mediaStorageDirectory.getAbsolutePath())
                Toast.makeText(requireContext(), "Klarte ikke å lage mappen: " +
                        mediaStorageDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return null;
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
                println("Permisjons granted")
                Toast.makeText(context, "Permisjons granted", Toast.LENGTH_LONG)
                takePicture()
            }else{
                println("Permisjons not granted")
                Toast.makeText(context, "Permisjons not granted", Toast.LENGTH_LONG)

            }
        }
    }
    companion object {
        val STATE_IMAGE_PATH="curentPath"
        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_PICK_IMAGE = 2
        private const val MY_PHOTO_FOLDER = "LaaneLitt"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}