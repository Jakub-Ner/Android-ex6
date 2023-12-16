package com.example.ex6

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ex6.databinding.ListRowBinding
import java.io.FileNotFoundException
import java.io.InputStream


class PhotoListAdapter(val appContext: Context, val dList: MutableList<DataItem>) :
    RecyclerView.Adapter<PhotoListAdapter.MyViewHolder>() {
    inner class MyViewHolder(viewBinding: ListRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val tv1 = viewBinding.tv1
        val tv2 = viewBinding.tv2
        val tv3 = viewBinding.tv3
        val img = viewBinding.img

    }

    companion object {
        const val PAGE_COUNT = 2
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoListAdapter.MyViewHolder {
        val viewBinding = ListRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(vHolder: PhotoListAdapter.MyViewHolder, position: Int) {
        vHolder.tv1.text = dList[position].name
        vHolder.tv2.text =
            dList[position].uripath
        vHolder.tv3.text = dList[position].curi?.path //temporary this data
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            dList[position].curi?.let {
                vHolder.img.setImageBitmap(
                    appContext.contentResolver.loadThumbnail(
                        it,
                        Size(72, 72),
                        null
                    )
                )
            }
        } else
            vHolder.img.setImageBitmap(getBitmapFromUri(appContext, dList[position].curi))
    }

    fun getBitmapFromUri(mContext: Context, uri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val image_stream: InputStream
            try {
                image_stream = uri?.let {
                    mContext.getContentResolver().openInputStream(it)
                }!!
                bitmap = BitmapFactory.decodeStream(image_stream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }


}