package com.example.numberplate_detect_client

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import java.io.InputStream

class ReadImage  {

    fun setImage(index: Int) : Bitmap {

        var inputStream: InputStream

        val name : String = "image_" + index + ".jpg"

        inputStream = getContext.instance.assets.open(name)

        val image = BitmapFactory.decodeStream(inputStream)


        return image
    }

}