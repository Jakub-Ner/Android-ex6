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
}