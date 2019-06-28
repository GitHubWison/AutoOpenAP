package studio.dex.autoopenap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dex.toolslibrary.isSystemSettingsCanWrite
import com.dex.toolslibrary.requestModifySystemSettings
import studio.dex.autoopenaplibrary.AutoOpenAp

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!this.isSystemSettingsCanWrite()) {
            this.requestModifySystemSettings()
        }

    }

    fun openAp(view: View)
    {
        AutoOpenAp.openAP(this)
    }
    fun closeAp(view:View)
    {
        AutoOpenAp.closeAP()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            Log.e("bbb==b","${this.isSystemSettingsCanWrite()}")
        }
    }
}
