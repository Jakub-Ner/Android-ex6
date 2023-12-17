package com.example.ex6

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class DataRepo {
    lateinit var uri: Uri

    fun getSharedList(): MutableList<DataItem>? {
        sharedStoreList?.clear()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentResolver: ContentResolver = ctx.contentResolver // requireContext().contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor == null) { // Error (e.g. no such volume)
        } else if (!cursor.moveToFirst()) { // no media in specified store
        } else {
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            do {
                var thisId = cursor.getLong(idColumn)
                var thisName = cursor.getString(nameColumn)
                var thisContentUri = ContentUris.withAppendedId(uri, thisId)
                var thisUriPath = thisContentUri.toString()
                sharedStoreList?.add(
                    DataItem(
                        thisName,
                        thisUriPath,
                        "No path yet",
                        thisContentUri
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
        Log.d("myTag", "getSharedList: ${sharedStoreList?.size}")
        return sharedStoreList
    }

    fun getAppList(): MutableList<DataItem>? {
        val dir: File? =
            ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        dir?.listFiles()
        appStoreList?.clear()
        if (dir?.isDirectory() == true) {
            var fileList = dir.listFiles()
            if (fileList != null) {
                for (value in fileList) {
                    var fileName =
                        value.name
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                            ".png"
                        ) || fileName.endsWith(".gif")
                    ) {
                        val tmpUri = FileProvider.getUriForFile(
                            ctx,
                            "${BuildConfig.APPLICATION_ID}.provider",
                            value
                        )
                        appStoreList?.add(
                            DataItem(
                                fileName,
                                value.toURI().path,
                                value.absolutePath,
                                tmpUri
                            )
                        )
                    }
                }
            }
        }
        val size = appStoreList?.size
        Log.d("myTag", "getAppList: $size")
        return appStoreList
    }


    fun setStorage(storage: Int): Boolean {
        if (storage != SHARED_S && storage != PRIVATE_S)
            return false
        photo_storage = storage
        return true
    }

    companion object {
        private var INSTANCE: DataRepo? = null
        private lateinit var ctx: Context
        val SHARED_S = 1
        val PRIVATE_S = 2
        var sharedStoreList: MutableList<DataItem>? = null
        var appStoreList: MutableList<DataItem>? = null

        fun getinstance(ctx: Context): DataRepo {
            if (INSTANCE == null) {
                INSTANCE = DataRepo()
                sharedStoreList = mutableListOf<DataItem>()
                appStoreList = mutableListOf<DataItem>()
                this.ctx = ctx
            }
            return INSTANCE as DataRepo
        }

        var photo_storage = SHARED_S

        fun getStorage(): Int {
            return photo_storage
        }
    }
}