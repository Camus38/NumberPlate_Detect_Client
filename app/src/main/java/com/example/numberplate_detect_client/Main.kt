package com.example.numberplate_detect_client

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.numberplate_detect_client.databinding.FragmentMainBinding
import com.google.android.material.behavior.SwipeDismissBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.jar.Manifest


class Main : Fragment() {
    private var binding : FragmentMainBinding? =null
    lateinit var mainActivity: MainActivity
    //var ip = "192.168.148.128"
    var ip = "35.216.104.58"
    var port = 30000
    var send = Send()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentMainBinding.inflate(inflater,container,false)
        mainActivity = context as MainActivity

        GlobalScope.launch {

        }


        binding!!.button.setOnClickListener {
            val caPermission = ContextCompat.checkSelfPermission(
                mainActivity,android.Manifest.permission.CAMERA
            )
            if (caPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    mainActivity, arrayOf(android.Manifest.permission.CAMERA)
                    ,1000
                )
            } else{
                val REQUEST_IMAGE_CAPTURE = 1

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                activityResult.launch(intent)

            }
        }

        binding!!.button2.setOnClickListener{
            var image : Bitmap? = null
            var text = ""
            var plate = ""
            CoroutineScope(Dispatchers.IO).launch {
                send.connect(ip,port)
                send.send()

                image = send.recv()
                //send.sends()
                //plate = send.recvnum()
                //send.send()

                //var number : String = send.recvnum()
                send.disconnet()
                CoroutineScope(Dispatchers.Main).launch {
                    binding!!.PlateView.setImageBitmap(image)
                    binding!!.platenum.text = plate
                }
            }

            //binding!!.platenum.text = number

        }

        return binding!!.root
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null){

            val extras = it.data!!.extras

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(mainActivity,"권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }


    inner class Client : Thread(){

    }


}