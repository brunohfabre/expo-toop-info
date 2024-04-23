package expo.modules.toopinfo

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
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        val apps = getPackageManager().queryIntentActivities(intent, 0);

        var allApps = emptyArray<String>()

        for (item in apps) {
          val appName = item.loadLabel(getPackageManager()).toString()
          val packageName = item.activityInfo.packageName
          val versionCode = getPackageManager().getPackageInfo(packageName, 0).getLongVersionCode()

          var iconDrawable = item.activityInfo.loadIcon(getPackageManager())
          var icon: Bitmap

          if(iconDrawable.getIntrinsicWidth() <= 0 || iconDrawable.getIntrinsicHeight() <= 0) {
            icon = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
          } else {
            icon = Bitmap.createBitmap(iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
          }
          
          val canvas = Canvas(icon)
          iconDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
          iconDrawable.draw(canvas)

          val byteArrayOutputStream = ByteArrayOutputStream()
          icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
          val byteArray = byteArrayOutputStream.toByteArray()
          val encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP)
          
          val appInfo = appName + ";" + packageName + ";" + versionCode + ";" + encoded

          allApps = allApps + appInfo
        }

        allApps
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
  }

  private val context
  get() = requireNotNull(appContext.reactContext)

  private fun getTelephonyManager(): TelephonyManager {
    return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
  }

  private fun getPackageManager(): PackageManager {
    return context.packageManager
  }
}
