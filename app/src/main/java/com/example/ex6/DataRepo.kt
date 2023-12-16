package com.example.ex6

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class DataRepo {
    lateinit var uri: Uri

    fun getSharedList(): MutableList<DataItem>? {
        sharedStoreList?.clear()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver: ContentResolver =
            ctx.contentResolver // requireContext().contentResolver
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
        return sharedStoreList
    }

//    fun getAppList(): MutableList<DataItem>? {
////
//    }

    companion object {
        private var INSTANCE: DataRepo? = null
        private lateinit var ctx: Context
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
    }
}