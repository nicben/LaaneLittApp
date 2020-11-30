package com.laanelitt.laanelittapp.ui.view

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
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
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.databinding.FragmentAddAssetBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.utils.observeAuthenticationState
import kotlinx.android.synthetic.main.fragment_add_asset.*
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
    private lateinit var binding:FragmentAddAssetBinding
    private lateinit var localStorage: LocalStorage
    lateinit var titleInput: Editable
    lateinit var descriptionInput: Editable
    private var PathToPictureFile = ""
    private var originalFile:File? = null
    private var userId:String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        localStorage = LocalStorage(requireContext())

        if (userId == "") {
            findNavController().navigate(R.id.loginFragment)
        }

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_asset,container,false)

        //Følger kodemønsteret fra https://developer.android.com/guide/topics/ui/controls/spinner for å opprette en dropdown meny
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.categories,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.category.adapter = adapter
            }
        }

        binding.category.setSelection(0)
        binding.takePicture.setOnClickListener {
            if (allPermissionsGranted())  {
                takePicture()
            }
            else{
                //Hvis applikasjonen ikke har tilgang til kameraet så må den spør om det
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        binding.saveButton.setOnClickListener {
            titleInput = title.editText?.text!!
            descriptionInput = description.editText?.text!!
            addAsset()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.myAssetsListFragment)
        }
        return binding.root
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState(localStorage, this)
    }//end onViewCreated


    private fun addAsset() {
        userId = localStorage.getLoggedInUser!!.id.toString()
        title.error = null
        description.error = null
        //Sørger for at all nødvendig informasjon er skrivd in før en prøver å opprette eiendelen
        if (originalFile == null) {
            Toast.makeText(
                requireContext(),
                "Velg et bilde",
                Toast.LENGTH_LONG
            ).show()
            category.requestFocus()
            return
        }
        if (titleInput.isEmpty()) {
            title.error = "Fyll inn fornavn"
            title.requestFocus()
            return
        }
        if (descriptionInput.isEmpty()) {
            description.error = "Fyll inn fornavn"
            description.requestFocus()
            return
        }
        save(userId!!, titleInput.toString(), descriptionInput.toString())
    }//end addAsset

    private fun save(userId:String, title: String, description: String){
        //Denne funksjonen bruker multipart på API kallet for å få sende bilde til backenden,
        //https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server har delvis blit fulgt her
        val categoryId: Int
        if(binding.category.selectedItemPosition<3){
            categoryId = binding.category.selectedItemPosition+1
        }else{
            categoryId = binding.category.selectedItemPosition+3
        }

        //Verdier som skal sendest med API kallet
        val userIdPart= RequestBody.create(MultipartBody.FORM, userId)
        val typeIdPart= RequestBody.create(MultipartBody.FORM, ""+categoryId)
        val conditionPart= RequestBody.create(MultipartBody.FORM, "0")
        val namePart = RequestBody.create(MultipartBody.FORM, title)
        val publicPart= RequestBody.create(MultipartBody.FORM, "true")
        val descriptionPart = RequestBody.create(MultipartBody.FORM, description)
        val mainImagePart= RequestBody.create(MultipartBody.FORM, "1")

        //Bilde som skal sendest med API kallet
        val filePart = RequestBody.create(MediaType.parse("*/*"), originalFile!!)
        val file = MultipartBody.Part.createFormData("file", originalFile!!.name, filePart)

        //Kall som oppretter får backenden til å opprette en eiendel i databasen
        LaneLittApi.retrofitService.addAsset(userIdPart, typeIdPart, conditionPart, namePart, publicPart, descriptionPart, mainImagePart, file).enqueue(
            object: Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {

                    //Opprettinga av eiendelen publiserer den ikke så det gjøres i ett eget API kall
                    LaneLittApi.retrofitService.publishAsset(userId.toInt(), response.body()!!).enqueue(
                        object: Callback<String>{
                            override fun onResponse(call: Call<String>, response: Response<String>
                            ) {
                                //Tar oss til oversikten over mine eiendeler
                                findNavController().navigate(R.id.myAssetsListFragment)
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    t.message?.let { Log.d(TAG, it) }
                    Toast.makeText(
                        requireContext(),
                        "Velg et bilde",
                        Toast.LENGTH_LONG
                    ).show()
                    category.requestFocus()
                }
            }
        )
    }//end Save

    //Funksjon som starter et Intent for å ta bilde
    private fun takePicture(){
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            try {
                //Oppretter en fil slik at vi kan lagre bilde, med høy oppløsning
                originalFile=createImageFile()
            }catch (e:IOException){
            }
            if(originalFile!=null){
                val fileUri= FileProvider.getUriForFile(requireContext(),
                    "com.laanelitt.laanelittapp.fileprovider", originalFile!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                PathToPictureFile=originalFile!!.absolutePath
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }//end takePicture

    //Viser bilde etter at det er blitt tatt
    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?){
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Glide.with(this).load(PathToPictureFile).into(binding.image)
            }
        }
    }//end onActivityResult

    //funksjon for å opprette en fil som bilde kan lagres i
    @Throws(IOException::class)
    private fun createImageFile():File?{
        var imageFile:File? = null
        val photoStorageDir = getPhotoDirectory()
        if (photoStorageDir != null){
            //Genererer ett unikt filnavn for bilde
            val timeStamp = SimpleDateFormat("yyyyMMdd-HHmss").format(Date())
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
                //Går videre til å ta bilde
                takePicture()
            }else{
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show()
            }
        }
    }//end onRequestPermissionsResult

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val MY_PHOTO_FOLDER = "LaaneLitt"
        const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }//end companion

}