package com.kroune.nineMensMorrisApp.ui.impl

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.LoadingCircle
import android.content.res.Resources

@Composable
fun RenderLeaderboardScreen(
    players: SnapshotStateList<Player>?,
    resources: Resources
) {

    OnlineLeaderboard(players = players, resources = resources)

}

@Composable
fun OnlineLeaderboard(
    players: SnapshotStateList<Player>?,
    modifier: Modifier = Modifier,
    resources: Resources
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
//            .fillMaxHeight()
            .background(Color.Gray)
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        Text(
            text = resources.getString(R.string.play_game_with_friends),
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 16.dp),
//            color = Color.White
        )
        LazyColumn (/*modifier = modifier.fillMaxHeight()*/) {
            items(players ?: listOf()) { player ->
                LeaderboardItem(player = player, resources = resources)
            }
        }
    }
}

@Composable
fun LeaderboardItem(player: Player, resources: Resources) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .border(2.dp, Color(0xFF616161), RoundedCornerShape(8.dp)),
           elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)/*.background(Color.LightGray)*/
        ) {

            if (player.pictureByteArray.value != null) {

                val imageBitmap = BitmapFactory.decodeByteArray(player.pictureByteArray.value, 0,
                    player.pictureByteArray.value?.size!!
                ).asImageBitmap()

                Image(
                    bitmap = imageBitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Player Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(1.dp))
//                        .border(2.dp, Color.White, CircleShape)
                )} else {
                LoadingCircle()
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = player.accountName.value.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${resources.getString(R.string.rating)}: ${player.accountRating.value}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Data class for player information (тут везде оставляем State)
data class Player(
    val accountName: State<String?>,
    val pictureByteArray: State<ByteArray?>,
    val accountRating: State<Long?>
)
