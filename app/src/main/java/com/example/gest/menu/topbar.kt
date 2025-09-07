package com.example.gest.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.Gest.R
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogo(
    onMenuClick: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
    openDrawer: () -> Unit,
    navController: NavController
) {


    val usuario by userViewModel.usuarioLogado.collectAsState()


    CenterAlignedTopAppBar(
        modifier = Modifier .background(MaterialTheme.colorScheme.primary,shape = RoundedCornerShape(72.dp)),
        title = {
            Text(
                text = "Olá, $usuario",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Abrir menu", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color.White)
            }
            IconButton(onClick =  {navController.navigate("UserPanelScreen") }) {
                Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
            }

        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF004AAD) // mesma cor do drawer para combinar
        )
    )
}

