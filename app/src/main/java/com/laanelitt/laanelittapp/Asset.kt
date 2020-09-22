package com.laanelitt.laanelittapp

import android.content.res.Resources
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
        assetName=jsonAsset.optString("name")
        imageLink=jsonAsset.optString("img")
    }
    companion object{
        fun makeAssetListe(resources: Resources): ArrayList<Asset>{
            val assetList=ArrayList<Asset>()
            try{
                val jsonAssets: String=readAssetDataFromFile(resources)
                val jsonAllAss=JSONObject(jsonAssets)
                val jsonAssetTable=jsonAllAss.optJSONArray("assets")

                for (i in 0 until jsonAssetTable.length()){
                    val jsonAsset=jsonAssetTable[i] as JSONObject
                    val thisAsset=Asset(jsonAsset)
                    assetList.add(thisAsset)
                }
                return assetList
            }catch (e: Exception){
                return assetList
            }
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
    }

}