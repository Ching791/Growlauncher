package com.ching791.growlauncher.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object AppLauncher {
    sealed class LaunchResult {
        data object Launched : LaunchResult()
        data object NotInstalled : LaunchResult()
        data class LaunchFailed(val message: String) : LaunchResult()
    }

    fun launchGrowtopia(context: Context): LaunchResult {
        if (!isGrowtopiaInstalled(context)) return LaunchResult.NotInstalled
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.GROWTOPIA_SCHEME)
                setPackage(Constants.GROWTOPIA_PACKAGE)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            LaunchResult.Launched
        } catch (_: ActivityNotFoundException) {
            LaunchResult.NotInstalled
        } catch (exception: Exception) {
            LaunchResult.LaunchFailed(exception.localizedMessage ?: "Unable to launch Growtopia")
        }
    }

    fun isGrowtopiaInstalled(context: Context): Boolean =
        runCatching { context.packageManager.getPackageInfo(Constants.GROWTOPIA_PACKAGE, 0) }.isSuccess
}
