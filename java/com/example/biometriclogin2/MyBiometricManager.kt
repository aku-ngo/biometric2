package com.example.biometriclogin2

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import javax.security.auth.callback.Callback

class MyBiometricManager () {
    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: PromptInfo? = null
    private var context: Context? = null
    private var fragmentActivity: FragmentActivity? = null
    private var callback: Callback? = null

    companion object {
        private var instance: MyBiometricManager? = null
        const val REQUEST_CODE = 100
        fun getInstance(context: Context): MyBiometricManager? {
            if (instance == null) {
                instance = MyBiometricManager()
            }
            instance!!.init(context)
            return instance
        }
    }
    private fun init(context: Context) {
        this.context = context
        fragmentActivity = context as FragmentActivity
        callback = Callback
    }
    fun checkIfBiometricFeatureAvailable(): Boolean {
        val biometricManager = BiometricManager.from(
            context!!
        )
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(
                    "MY_APP_TAG",
                    "App can authenticate using biometrics."
                )
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e(
                    "MY_APP_TAG",
                    "No biometric Feature available"
                )
                Toast.makeText(
                    context, "No biometric feature available",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e(
                    "MY_APP_TAG", "Biometric features are" +
                            "currently unavailable."
                )
                Toast.makeText(
                    context, "Biometric features are currently unavailable.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                //prompts user to create credentials that your app accepts
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    android.hardware.biometrics.
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            android.hardware.biometrics.BiometricManager
                                .Authenticators.DEVICE_CREDENTIAL
                )
                fragmentActivity!!.startActivityForResult(enrollIntent, REQUEST_CODE)
                return false
            }
        }
        return false

    }
    fun authenticate(){
        setupBiometric()
        biometricPrompt!!.authenticate(promptInfo!!)
    }
    private fun setupBiometric(){
        executor= ContextCompat.getMainExecutor(context!!)
        biometricPrompt = BiometricPrompt(
            fragmentActivity!!, executor!!,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_ERROR,
                        errString
                    )
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_SUCCESSFUL,
                        ""
                    )
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback!!.onBiometricAuthenticationResult(
                        Callback.AUTHENTICATION_FAILED, "")
                }
            })
        showBiometricPrompt()
    }
    private fun showBiometricPrompt(){
        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric authentication for fufu")
            .setSubtitle("Log in using fingerprint")
            .setNegativeButtonText("Use account password")
            .build()
    }
    internal interface Callback{
        fun onBiometricAuthenticationResult(result: String?, errString: CharSequence?)
        fun onActivityResult(requestCode: Any, requestCode1: Any, data: Any)

        companion object : Callback {
            const val AUTHENTICATION_SUCCESSFUL ="AUTHENTICATION_SUCCESSFUL"
            const val AUTHENTICATION_FAILED ="AUTHENTICATION_FAILED"
            const val AUTHENTICATION_ERROR ="AUTHENTICATION_ERROR"
            override fun onBiometricAuthenticationResult(
                result: String?,
                errString: CharSequence?
            ) {
                TODO("Not yet implemented")
            }

            override fun onActivityResult(requestCode: Any, requestCode1: Any, data: Any) {
                TODO("Not yet implemented")
            }
        }
    }

}

