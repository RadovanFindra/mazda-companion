package com.example.mazdacompanionapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.AppInfo

class InstalledAppsGetter(private val context: Context) {
    fun getInstalledApps(): List<AppInfo> {
        val packageManager: PackageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

        val appInfoList = resolveInfoList.map { resolveInfo ->
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val appIcon = resolveInfo.loadIcon(packageManager).toBitmap()
            AppInfo(name = appName, icon = appIcon)
        }.toMutableList()

        val phoneInfoAppName = "PhoneInfo"
        val phoneInfoAppIcon =
            AppCompatResources.getDrawable(context, R.drawable.ic_launcher_foreground)?.toBitmap()
        if (phoneInfoAppIcon != null) {
            appInfoList.add(
                AppInfo(
                    name = phoneInfoAppName,
                    icon = (phoneInfoAppIcon)
                )
            )
        }
        return appInfoList.sortedBy { it.name }
    }
}