package com.example.mazdacompanionapp.ui.screens.Bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mazdacompanionapp.data.BluetoothDevices.DeviceItem

@Composable
fun EditBluetoothItem(
    item: DeviceItem,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.h6,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        Icons.Default.Edit, contentDescription = "Edit",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )

                }
                Switch(
                    checked = item.isEnabled,
                    onCheckedChange = { },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = {  },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Device",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}