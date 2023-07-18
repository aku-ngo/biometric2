package com.example.biometriclogin2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biometriclogin2.MyBiometricManager.Callback.Companion.AUTHENTICATION_ERROR
import com.example.biometriclogin2.MyBiometricManager.Callback.Companion.AUTHENTICATION_FAILED
import com.example.biometriclogin2.MyBiometricManager.Callback.Companion.AUTHENTICATION_SUCCESSFUL
import com.example.biometriclogin2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MyBiometricManager.Callback {
    private lateinit var binding: ActivityMainBinding
    private var myBiometricManager: MyBiometricManager? = null

    override fun onCreate(savesInstanceState: Bundle?) {
        super.onCreate(savesInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        myBiometricManager = MyBiometricManager.getInstance(this)

        binding.btnFingerPrintAuth.setOnClickListener{
            if (myBiometricManager!!.checkIfBiometricFeatureAvailable()){
                myBiometricManager!!.authenticate()
            }
        }
    }

    override fun onBiometricAuthenticationResult(
        result: String?, errString:
        CharSequence?
    ) {
        when (result) {
            AUTHENTICATION_SUCCESSFUL -> {
                Toast.makeText(
                    this@MainActivity,
                    "Authentication Succeeded!", Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG", "AUTHENTICATION_SUCCESSFUL")
            }

            AUTHENTICATION_FAILED -> {
                Toast.makeText(
                    this@MainActivity,
                    "Authentication failed!", Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG", "AUTHENTICATION_FAILED")
            }

            AUTHENTICATION_ERROR -> {
                Toast.makeText(
                    this@MainActivity,
                    "Authentication error: $errString", Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG", "AUTHENTICATION_ERROR")
            }
        }

    }

    override fun onActivityResult(requestCode: Any, requestCode1: Any, data: Any) {


    }




}

