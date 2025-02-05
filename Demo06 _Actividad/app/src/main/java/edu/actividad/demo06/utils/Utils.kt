package edu.actividad.demo06.utils


import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import edu.actividad.demo06.R
import edu.actividad.demo06.ui.main.MainActivity

private const val CHANNEL_ID = "cities_channel_id"
private const val NOTIFICATION_ID = 1001

/**
 * createNotificationChannel
 * Método que crea un canal de notificación
 *
 * @param context contexto de la aplicación
 *
 * @autor Jose Pinilla
 */
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "CitiesChannel"
        val desc = "Canal para avisos de visitas a ciudades"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = desc
            enableLights(true)
            lightColor = Color.CYAN
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

/**
 * sendNotification
 * Método que envía una notificación
 *
 * @param context contexto de la aplicación
 *
 * @autor Jose Pinilla
 */
fun sendNotification(context: Context) {
    //  Comprobar permisos
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            // No hacemos nada, o logueamos si se desea
            Log.i("sendNotification", "No POST_NOTIFICATIONS permission. Aborting.")
            return
        }
    }

    // Se crea la notificación
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_visibility)
        .setContentTitle("Información actualizada")
        .setContentText("¡Se han actualizado las visitas de las ciudades!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    // Al hacer clic, vuelve a MainActivity
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )
    builder.setContentIntent(pendingIntent)

    // Lanzar la notificación
    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, builder.build())
    }
}

