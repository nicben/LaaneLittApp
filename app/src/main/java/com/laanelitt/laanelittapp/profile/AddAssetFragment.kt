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
import android.widget.ArrayAdapter
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
import com.laanelitt.laanelittapp.objects.Code
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


class AddAssetFragment : Fragment() {

    //private val vievModel: AddAssetViewModel()
    private lateinit var binding:FragmentAddAssetBinding
    private var pathTilBildeFil=""
    private var ogFile:File?=null
    private var userId:String?=null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (userId == "") {
            findNavController().navigate(R.id.loginFragment)
        }

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_asset,container,false)

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.categories,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.category.adapter=adapter
            }
        }
        binding.category.setSelection(8)
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
            save(userId!!)
            //view.findNavController().navigate(R.id.action_addAssetFragment_to_myAssetsListFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeAuthenticationState()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observeAuthenticationState() {

        val loggedInUser = userLocalStore?.getLoggedInUser
        if (loggedInUser == null) {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }else{
            userId= userLocalStore?.getLoggedInUser!!.id.toString()
        }
    }

    fun save(userId:String){
        if(ogFile!=null && binding.name.text.toString()!="" && binding.description.text.toString()!=""){

            var categoryId=11
            if(binding.category.selectedItemPosition<3){
                categoryId=binding.category.selectedItemPosition+1
            }else{
                categoryId=binding.category.selectedItemPosition+3
            }
            println("CATEGORY ID "+categoryId)

            //Verdier som skal sendest med API kallet
            val userIdPart=RequestBody.create(MultipartBody.FORM, userId)
            val typeIdPart=RequestBody.create(MultipartBody.FORM, ""+categoryId)
            val conditionPart=RequestBody.create(MultipartBody.FORM, "0")
            val namePart=RequestBody.create(MultipartBody.FORM, binding.name.text.toString())
            val publicPart=RequestBody.create(MultipartBody.FORM, "true")
            val descriptionPart=RequestBody.create(MultipartBody.FORM, binding.description.text.toString())
            val mainImagePart=RequestBody.create(MultipartBody.FORM, "1")

            //Bilde som skal sendest med API kallet
            val filePart=RequestBody.create(MediaType.parse("image/*"), ogFile!!)
            val file=MultipartBody.Part.createFormData("file", ogFile!!.name, filePart)

            println(ogFile!!.absolutePath+" :: "+ogFile!!.toURI())


            //Kall som oppretter får backenden til å opprette en eiendel i databasen
            LaneLittApi.retrofitService.addAsset(userIdPart, typeIdPart, conditionPart, namePart, publicPart, descriptionPart, mainImagePart, file).enqueue(
                object: Callback<Int>{
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {

                        //Opprettinga av eiendelen publiserer den ikke så det gjøres i ett eget API kall
                        LaneLittApi.retrofitService.publishAsset(userId.toInt(), response.body()!!).enqueue(
                            object: Callback<String>{
                                override fun onResponse(call: Call<String>, response: Response<String>
                                ) {
                                    Toast.makeText(requireContext(), response.body(), Toast.LENGTH_LONG).show()

                                    //Tar oss til oversikten over mine eiendeler
                                    findNavController().navigate(R.id.myAssetsListFragment)
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(requireContext(), "Eiendelen ble opprettet men ikke publisert", Toast.LENGTH_LONG).show()
                                    println(" " + t.message+ "::::::::::::::::::::::")
                                }
                            }
                        )
                    }
                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Toast.makeText(requireContext(), "Noe gikk galt: "+t.message,Toast.LENGTH_LONG).show()
                    }
                }
            )

            LaneLittApi.retrofitService.uploadProfileImage(userIdPart, file).enqueue(
                object : Callback<Code>{
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {

                        println(">>>>>>>>>>>>>>>>>>>"+response.body()?.code)
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println(t.message+":::::::::::::::::::::::::")
                    }
                }
            )
        }else{
            Toast.makeText(requireContext(), "velg ett bilde og/eller fyll in teksten", Toast.LENGTH_LONG).show()
        }
    }

    fun takePicture(){
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            try {
                ogFile=createImageFile()
            }catch (e:IOException){
            }
            if(ogFile!=null){
                val fileUri= FileProvider.getUriForFile(requireContext(),
                    "com.laanelitt.laanelittapp.fileprovider", ogFile!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                pathTilBildeFil=ogFile!!.absolutePath
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    fun pickImageFromGallery(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?){

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                Glide.with(this).load(pathTilBildeFil).into(binding.image)
            }
            if (requestCode == REQUEST_PICK_IMAGE) {
                ogFile = File(returnIntent?.data?.path)

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
            val imageFileName=photoStorageDir.path+File.separator.toString()+"LaaneLitt_"+timeStamp+".jpeg"
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
                Toast.makeText(context, "Permisjons granted", Toast.LENGTH_LONG).show()
                takePicture()
            }else{
                println("Permisjons not granted")
                Toast.makeText(context, "Permisjons not granted", Toast.LENGTH_LONG).show()

            }
        }
    }
    companion object {
        val STATE_IMAGE_PATH="curentPath"
        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_PICK_IMAGE = 2
        private const val MY_PHOTO_FOLDER = "LaaneLitt"
        const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}