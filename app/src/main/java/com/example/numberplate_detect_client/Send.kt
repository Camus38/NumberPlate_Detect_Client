package com.example.numberplate_detect_client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Send {
    val readImage = ReadImage()
    lateinit var socket: Socket
    lateinit var input : InputStream
    lateinit var dis : DataInputStream
    lateinit var output : OutputStream
    lateinit var dos : DataOutputStream
    lateinit var baos : ByteArrayOutputStream
    lateinit var buffer : ByteArray

    fun connect(ip:String,Port: Int){
        try {
            socket = Socket()
            socket.connect(InetSocketAddress(ip,Port))//서버에 연결 요청

            input = socket.getInputStream()
            dis = DataInputStream(input)

            output = socket.getOutputStream()
            dos = DataOutputStream(output)
        }catch (e : Exception){
            e.printStackTrace()
            println("Connect error")
        }

    }

    fun send(){
        CoroutineScope(Dispatchers.IO).launch {
            var data =getImageByteArray(readImage.setImage(1))
            var b = ByteBuffer.allocate(4)
            b.order(ByteOrder.LITTLE_ENDIAN)
            b.putInt(data.size)
            //dos.flush()
            try{
                dos.write(b.array(),0,4)
                dos.flush()
                dos.write(data,0,data.size)

            }catch (e : IOException){
                e.printStackTrace()
            }

        }

    }
    fun sends(){
        var str = "Hi There"
        output.write(str.toByteArray())
        //output.flush()
    }
    fun disconnet(){
        dos.writeInt(0)
        dos.flush()

        socket.close()
    }

    fun recv() : Bitmap{
        baos = ByteArrayOutputStream()
        buffer = ByteArray(4096)

        while(true){
            var size = dis.read(buffer)
            if(size == -1)
                size = 0
            baos.write(buffer,0,size)
            if(size <= 0)
                break
        }

        val result = baos.toByteArray()
        var image = byteArrayToBitmap(result)
        baos.flush()


        return image
    }
    fun recvnum() : String{
        var num = ""
        var buf = ByteArray(1024)
        var available = input.available()
        if(available > 0){
            var size = input.read(buf)
            num = String(buf,0,size)
        }
        return num
    }

    fun getImageByteArray(bitmap: Bitmap) : ByteArray{
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)

        return out.toByteArray()
    }

    fun byteArrayToBitmap(data: ByteArray) : Bitmap{
        var image = BitmapFactory.decodeByteArray(data,0,data.size)
        return image
    }
}