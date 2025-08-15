package com.example.carteogest.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.CarteoGest.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogo(
    onMenuClick: () -> Unit,
    userName: String = "Usuário Teste",
    openDrawer: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier .background(MaterialTheme.colorScheme.primary,shape = RoundedCornerShape(72.dp)),
        title = {
            Text(
                text = "Olá, $userName",
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
            IconButton(onClick = {/*onNotificationsClick"*/}) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color.White)
            }
            IconButton(onClick = {/*onNotificationsClick"*/}) {
                Icon(
                    painter = painterResource(id = R.drawable.icone),
                    contentDescription = "Perfil do usuário",
                    tint = Color.Unspecified, // mantém cores originais do avatar
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF004AAD) // mesma cor do drawer para combinar
        )
    )
}
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarExpanded(
    onMenuClick: () -> Unit,
    userName: String = "Usuario Teste",
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Olá, $userName",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Abrir menu", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color.White)
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    painter = painterResource(id = R.drawable.icone),
                    contentDescription = "Perfil do usuário",
                    tint = Color.Unspecified, // mantém cores originais do avatar
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF004AAD)
        )
    )
}*/
