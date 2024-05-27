package expo.modules.toopinfo

import android.os.Build
import android.content.Intent
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.content.Context
import android.telephony.TelephonyManager
import android.content.pm.PackageManager
import android.app.admin.DevicePolicyManager
import android.net.Uri
import android.graphics.drawable.Drawable
import org.json.JSONObject
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.app.WallpaperManager
import android.graphics.ImageDecoder
import android.graphics.BitmapFactory

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoToopInfoModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoToopInfo")

    Function("openedByDpc") {
      try {
        val activity = appContext.activityProvider?.currentActivity

        if(activity != null) {
          activity.intent.getSerializableExtra("com.google.android.apps.work.clouddpc.EXTRA_LAUNCHED_AS_SETUP_ACTION")
        } else {
          "not-permitted"
        }
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("getImei") {
      try {
        getTelephonyManager().getImei(0)
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("getSerialNumber") {
      try {
        Build.getSerial()
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("getInstalledPackages") {
      try {
        val packageManager = getPackageManager()

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        val items = packageManager.queryIntentActivities(intent, 0);

        var applications = emptyArray<String>()

        for(item in items) {
          val applicationInfo = packageManager.getPackageInfo(item.activityInfo.packageName, 0)

          var icon = ""

          val drawable: Drawable = packageManager.getApplicationIcon(item.activityInfo.packageName)

          if(drawable is BitmapDrawable) {
            val outputStream = ByteArrayOutputStream()
            drawable.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            icon = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
          } else if (drawable is AdaptiveIconDrawable) {
            val backgroundDr = drawable.background
            val foregroundDr = drawable.foreground

            val drr = arrayOfNulls<Drawable>(2)
            drr[0] = backgroundDr
            drr[1] = foregroundDr

            val layerDrawable = LayerDrawable(drr)

            val width = layerDrawable.intrinsicWidth
            val height = layerDrawable.intrinsicHeight

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)

            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            icon = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
          }

          val application = applicationInfo.applicationInfo.loadLabel(packageManager).toString() + ";" + applicationInfo.packageName + ";" + applicationInfo.versionName + ";" + applicationInfo.longVersionCode.toString() + ";" + applicationInfo.firstInstallTime + ";" + applicationInfo.lastUpdateTime + ";" + icon

          applications = applications + application
        }

        applications
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("sendActivityResultOk") {
      try {
        val activity = appContext.activityProvider?.currentActivity

        if(activity != null) {
          val intent = Intent()
          activity.setResult(Activity.RESULT_OK, intent)
          activity.finish()
        } else {
          "not-permitted"
        }
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("launchApplication") { packageName: String ->
      try {
        val launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (launchIntent != null) {
          context.startActivity(launchIntent);
        } else {
          "not-permitted"
        }
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("getEnrollmentSpecificId") {
      try {
        getDevicePolicyManager().getEnrollmentSpecificId()
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("getDeviceId") {
      try {
        val uri = Uri.parse("content://com.google.android.gsf.gservices")

        val query = context.contentResolver.query(uri, null, null, arrayOf<String>("android_id"), null)

        if (query == null) {
          return@Function "not-found"
        }

        if (!query.moveToFirst() || query.columnCount < 2) {
          return@Function "not-found"
        }

        val toHexString = java.lang.Long.toHexString(query.getString(1).toLong())

        query.close()

        return@Function toHexString
      } catch (e: Exception) {
        "not-permitted"
      }
    }

    Function("setWallpaper") { uri: String ->
      try {
        val context: Context = context
        val wallpaperManager = WallpaperManager.getInstance(context)
        val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
        val bitmap = BitmapFactory.decodeStream(inputStream)

        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
      } catch (e: Exception) {
        "not-permitted"
      }
    }
  }

  private val context
  get() = requireNotNull(appContext.reactContext)

  private fun getTelephonyManager(): TelephonyManager {
    return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
  }

  private fun getPackageManager(): PackageManager {
    return context.packageManager
  }

  private fun getDevicePolicyManager(): DevicePolicyManager {
    return context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
  }
}
