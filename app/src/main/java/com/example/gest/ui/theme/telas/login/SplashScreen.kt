package com.example.gest.ui.telas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.Gest.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: UserViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        delay(2000L) // Delay de 1.5 segundos para exibir a splash (opcional, remova se não quiser)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icone),
            contentDescription = "Imagem do projeto",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Gest",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}