package com.croumy.hltb_wearos.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme

@Composable
fun HomeScreen() {
    Scaffold {
        Column(
            Modifier.padding(it)
        ) {

        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HLTBwearosTheme {
        HomeScreen()
    }
}