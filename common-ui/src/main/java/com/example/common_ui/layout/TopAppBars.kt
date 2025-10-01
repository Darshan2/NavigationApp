package com.example.common_ui.layout

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common_ui.R
import com.example.common_ui.theme.NavigationAppTheme

@Composable
fun NavigationAppTopAppBar(modifier: Modifier = Modifier, @StringRes titleResId: Int, showUpBtn: Boolean, onUpBtnClick: () -> Unit) {
    Surface(modifier = modifier
        .fillMaxWidth()
        .height(56.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 8.dp
    ) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            if(showUpBtn) {
                IconButton(onClick = { onUpBtnClick() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.go_back))
                }
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(titleResId), maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    NavigationAppTheme {
        NavigationAppTopAppBar(titleResId = R.string.app_name, showUpBtn = true, onUpBtnClick = {})
    }
}