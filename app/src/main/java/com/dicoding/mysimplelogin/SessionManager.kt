package com.dicoding.mysimplelogin

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.securepreferences.SecurePreferences

class SessionManager(context: Context) {
    companion object {
        const val KEY_LOGIN = "isLogin"
        const val KEY_USERNAME = "username"
    }

    private var pref: SharedPreferences = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val spec = KeyGenParameterSpec.Builder( // create new instance of encryption keys
            MasterKey.DEFAULT_MASTER_KEY_ALIAS, // specify alias (unique identifier) for the cryptographic key
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT // sets the purpose of the key (encrypt & decrypt)
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM) // set block mode. GCM is operation mode for symmetric key block ciphers that provides confidentiality & data integrity
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE) // set the size of encryption key (this case use the default). it determine the strength of the encryption
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE) // set encryption padding to none (GCM commonly don't require padding). padding used to ensure input data is a multiple of the block size
            .build()
        val masterKey = MasterKey.Builder(context) // create new instance
            .setKeyGenParameterSpec(spec) // set the cryptographic specification that has been written before
            .build()
        EncryptedSharedPreferences.create( // create an instance of Encrypted Shared Preferences
            context,
            "Session", // name of the SharedPreferences file (Session.xml) (check on Device Explorer)
            masterKey, // master key used to encrypt & decrypt data stored in SharedPreferences
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // define encryption scheme to encrypt keys (AES-256 with SIV)
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // define encryption scheme to encrypt the data values (AES-256 with GCM)
        )
    } else {
        SecurePreferences(
            context,
            "dicoding", // password of the SecurePreferences
            "Session" // name of the SharedPreferences file (Session.xml) (check on Device Explorer)
        )
    }

    private var editor: SharedPreferences.Editor = pref.edit()

    fun createLoginSession() {
        editor.putBoolean(KEY_LOGIN, true)
            .commit()
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }

    val isLogin: Boolean = pref.getBoolean(KEY_LOGIN, false)

    fun saveToPreference(key: String, value: String) = editor.putString(key, value).commit()

    fun getFromPreference(key: String) = pref.getString(key, "")

}
