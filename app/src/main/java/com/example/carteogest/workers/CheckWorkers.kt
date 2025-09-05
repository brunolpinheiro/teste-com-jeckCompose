package com.example.carteogest.workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.carteogest.MainActivity
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log

class ExpirationCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getDatabase(applicationContext, this)
            val viewModel = ProductViewModel(database.productsDao(), database.validityDao())
            // Coletar a lista de produtos com validades do Flow
            val productsWithValidities = viewModel.produtosComValidades.first()

            // Iterar sobre os produtos com índice para IDs únicos
            productsWithValidities.forEachIndexed { index, productWithValidities ->
                productWithValidities.validades.forEach { validity ->
                    if (verificationDay(validity.validity)) {
                        sendNotification(
                            productWithValidities.product.name,
                            validity.validity,
                            index + (productWithValidities.product.name + validity.validity).hashCode()
                        )
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("ExpirationCheckWorker", "Falha ao processar produtos: ${e.message}")
            Result.failure()
        }
    }

    private fun verificationDay(date: String): Boolean {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val validityDate = dateFormat.parse(date) ?: return false
            val currentDate = Date()
            val diffInMillis = validityDate.time - currentDate.time
            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
            return diffInDays in 0..7
        } catch (e: Exception) {
            Log.e("ExpirationCheckWorker", "Falha ao verificar data: ${e.message}")
            return false
        }
    }

    private fun sendNotification(productName: String, validity: String, notificationId: Int) {
        // Verificar permissão de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("ExpirationCheckWorker", "Permissão POST_NOTIFICATIONS não concedida")
            return
        }

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        // Criar intent para abrir MainActivity
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir a notificação
        val notification = NotificationCompat.Builder(applicationContext, MainActivity.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // Substitua pelo ícone do seu app
            .setContentTitle("Produto Perto de Vencer")
            .setContentText("O produto $productName expira em $validity")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Enviar notificação com ID único
        notificationManager.notify(notificationId, notification)
    }
}