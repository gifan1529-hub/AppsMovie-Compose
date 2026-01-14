package com.example.compose.Uii.Screen.Home.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.example.compose.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.compose.Uii.Screen.Home.HomeScreen
import com.example.compose.ui.theme.ComposeTheme

@Composable
fun TopBar(
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical =30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.propil),
            contentDescription = "Profile",
            modifier = Modifier
                .size(35.dp)
                .clickable(onClick = onProfileClick)
        )
        Image(
            painter = painterResource(R.drawable.movie),
            contentDescription = "Movie",
            modifier = Modifier
                .size(45.dp)
        )
        Image(
            painter = painterResource(R.drawable.search),
            contentDescription = "Search",
            modifier = Modifier
                .size(35.dp)
                .clickable(onClick = onSearchClick)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1F1D2B)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        TopBar()
    }
}