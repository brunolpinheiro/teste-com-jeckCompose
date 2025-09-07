package com.example.gest.ui.telas.roomBackup





import androidx.compose.runtime.staticCompositionLocalOf
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

val LocalRoomBackup = staticCompositionLocalOf<RoomBackup> {
    error("Nenhuma instância de RoomBackup fornecida")
}