package com.example.numberplate_detect_client


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.numberplate_detect_client.databinding.ActivityMainBinding
import java.io.*
import java.util.*



class MainActivity : AppCompatActivity() {
    var ip = "192.168.148.128"
    var port = 30000
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var send = Send();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replacement(1)

        binding.homeFrag.setOnClickListener { replacement(1) }
        binding.listFrag.setOnClickListener { replacement(3) }

    }

    fun replacement(number : Int){
        supportFragmentManager.commit{
            setReorderingAllowed(true)
            when(number) {
                1 -> replace<Main>(R.id.main_my_fragment)
                3 -> replace<List>(R.id.main_my_fragment)
                else -> return@commit
            }
            addToBackStack(null)

        }
    }

    var mBackWait : Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackWait >= 2000){
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 두번 시 종료합니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }



}