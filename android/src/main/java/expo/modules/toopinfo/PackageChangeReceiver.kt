package expo.modules.toopinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.os.Bundle

class PackageChangeReceiver(private val sendEvent: (name: String, body: Bundle) -> Unit) : BroadcastReceiver() {
  private fun onPackageChanged(data: String) {
    sendEvent(
      PACKAGE_CHANGE_EVENT_NAME,
      Bundle().apply {
        putString("action", "changed")
        putString("data", data)
      }
    )
  }

  override fun onReceive(context: Context, intent: Intent) {
    if(intent.action == Intent.ACTION_PACKAGE_CHANGED) {
      val data = intent.data.toString()

      onPackageChanged(data)
    }
  }
}