package com.laanelitt.laanelittapp.objects

import android.content.res.Resources
import com.laanelitt.laanelittapp.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.NullPointerException


class Asset {
    var assetName: String
    var imageLink: String

    constructor(jsonAsset:JSONObject){
        assetName=jsonAsset.optString("assetName")
        val assImgs = jsonAsset.getJSONArray("assetImages")
        val test=assImgs[0] as JSONObject
        imageLink="https://lanelitt.no/AssetImages/"+test.optString("imageUrl")
    }
    companion object{
        val client = OkHttpClient()
        fun makeAssetListe(resources: Resources): ArrayList<Asset>{
            //run("https://lanelitt.no/public/assets/getAssetType/134/11")
            val url="https://lanelitt.no/public/assets/getAssetType/134/11"

            val assetList=ArrayList<Asset>()
            val r = Request.Builder()
                .url(url)
                .build()
            //var body=client.newCall(r).execute().body().toString()
            //println(body)
            try{
                val jsonAssets: String= readAssetDataFromFile(resources)
                val jsonAssetTable=JSONArray(jsonAssets)
                /*
                val jsonAllAss=JSONObject(jsonAssets)
                val jsonAssetTable=jsonAllAss.optJSONArray()*/

                for (i in 0 until jsonAssetTable.length()){
                    val jsonAsset=jsonAssetTable[i] as JSONObject
                    //val assImgs = jsonAsset.getJSONArray("assetImage")
                    val thisAsset= Asset(jsonAsset)
                    println("hei"+thisAsset.assetName)
                    assetList.add(thisAsset)
                }
            }catch (e: Exception){
            }
            /*client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("*********************************************************************************")

                }
                override fun onResponse(call: Call, response: Response) {
                    println("*********************************************************************************"+response.body()?.string())

                }
            })*/
            return assetList

        }
        @Throws(IOException::class, NullPointerException::class)
        private fun readAssetDataFromFile(resources: Resources):String{

            var oneLine: String?
            var wholeFile=StringBuilder()

            try {
                val inputStream=resources.openRawResource(R.raw.assetsdata)
                val reader=BufferedReader(InputStreamReader(inputStream))

                while (reader.readLine().also { oneLine=it }!=null){
                    wholeFile=wholeFile.append(oneLine)
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
            return wholeFile.toString()
        }
        fun run(url: String){
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("*********************************************************************************")}
                override fun onResponse(call: Call, response: Response) {
                    println("*********************************************************************************"+response.body()?.string())

                }
            })
        }
    }
}//*/