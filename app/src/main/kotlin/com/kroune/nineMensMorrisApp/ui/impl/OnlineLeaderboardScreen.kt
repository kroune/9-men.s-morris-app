package com.kroune.nineMensMorrisApp.ui.impl

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH

@Composable
fun RenderLeaderboardScreen(
    players: SnapshotStateList<Player>?
) {
    Box(
        modifier = Modifier
            .padding(0.dp, BUTTON_WIDTH * 9.5f, 0.dp, 0.dp)
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        OnlineLeaderboard(players = players)
    }
}

@Composable
fun OnlineLeaderboard(
    players: SnapshotStateList<Player>?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White
        )
        players?.forEach { player ->
            LeaderboardItem(player = player)
        }
    }
}

@Composable
fun LeaderboardItem(player: Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Gray)
            .border(2.dp, Color.DarkGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            val imageBitmap = BitmapFactory.decodeByteArray(player.pictureByteArray, 0, player.pictureByteArray.size).asImageBitmap()

            Image(
                bitmap = imageBitmap,
                contentDescription = "Player Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = player.accountName,
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                Text(
                    text = "Rating: ${player.accountRating}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

// Data class for player information
data class Player(
    val accountName: String,
    val pictureByteArray: ByteArray,
    val accountRating: Long
)
