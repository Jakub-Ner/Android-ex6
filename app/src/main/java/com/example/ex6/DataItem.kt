package com.example.ex6

import android.net.Uri

class DataItem {
    var name: String = "Empty name"
    var uripath: String = " Empty uri"
    var path: String = " Empty path"
    var curi: Uri? = null //Content Uri //other possible constructors

    constructor(name: String, uripath: String, path: String, curi: Uri) : this() {
        this.name = name
        this.uripath = uripath
        this.path = path
        this.curi = curi
    }

    constructor()

    val SHARED_S = 1
    val PRIVATE_S = 2
    var photo_storage = SHARED_S
    fun setStorage(storage: Int): Boolean {
        if (storage != SHARED_S && storage != PRIVATE_S)
            return false else {
            photo_storage = storage
        }
        return true
    }

    fun getStorage(): Int {
        return photo_storage
    }
}