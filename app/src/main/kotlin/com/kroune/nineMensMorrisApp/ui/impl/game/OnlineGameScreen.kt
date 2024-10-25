package com.kroune.nineMensMorrisApp.ui.impl.game

import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisLib.Position
import kotlin.math.roundToInt

/**
 * renders online game screen
 */
@Composable
fun RenderOnlineGameScreen(
    pos: Position,
    selectedButton: Int?,
    moveHints: List<Int>,
    onClick: (Int) -> Unit,
    handleUndo: () -> Unit,
    handleRedo: () -> Unit,
    onGiveUp: () -> Unit,
    gameEnded: Boolean,
    isGreen: Boolean?,
    timeLeft: Long,
    ownAccountName: String?,
    ownPictureByteArray: ByteArray?,
    ownAccountRating: Long?,
    enemyAccountName: String?,
    enemyPictureByteArray: ByteArray?,
    enemyAccountRating: Long?,
    navController: NavHostController
) {
    var offsetY by remember { mutableStateOf(0f) }
    var isDraggingEnabled by remember { mutableStateOf(false) }

    fun toggleDragging() {
        isDraggingEnabled = !isDraggingEnabled
    }

    val density = LocalDensity.current.density
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayersUI(
                pos = pos,
                dragDesk = isDraggingEnabled,
                onToggleDragging = { toggleDragging() },
                timeLeft = timeLeft,
                isGreen = isGreen,
                ownAccountName = ownAccountName,
                ownPictureByteArray = ownPictureByteArray,
                ownAccountRating = ownAccountRating,
                enemyAccountName = enemyAccountName,
                enemyPictureByteArray = enemyPictureByteArray,
                enemyAccountRating = enemyAccountRating,
            )

            if (displayGiveUpConfirmation.value) {
                GiveUpConfirm(
                    giveUp = {
                        onGiveUp()
                    }, navController = navController
                )
            }
            if (!gameEnded) {
                BackHandler {
                    displayGiveUpConfirmation.value = true
                }
            }

            Box(modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        if (isDraggingEnabled) {
                            offsetY = (offsetY + delta).coerceIn(-50 * density, 200 * density)
                        }
                    }, orientation = Orientation.Vertical
                )
            ) {
                RenderGameBoard(
                    pos = pos,
                    selectedButton = selectedButton,
                    moveHints = moveHints,
                    onClick = onClick
                )
            }
            RenderUndoRedo(handleUndo = handleUndo, handleRedo = handleRedo)
        }
    }
}

private val displayGiveUpConfirmation = mutableStateOf(false)

@Composable
private fun GiveUpConfirm(
    giveUp: () -> Unit, navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), contentAlignment = Alignment.Center
    ) {
        Column {
            Text("Are you sure you want to give up?")
            Button(onClick = {
                giveUp()
                navController.navigateSingleTopTo(Navigation.Welcome)
            }) {
                Text("Yes")
            }
            Button(onClick = {
                displayGiveUpConfirmation.value = false
            }) {
                Text("No")
            }
        }
    }
}

@Composable
fun TurnTimerUI(timeLeft: Long) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "Time left: $timeLeft s",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Composable
fun PlayerCard(
    playerName: String?,
    pictureByteArray: ByteArray?,
    isGreen: Boolean,
    rating: Long?,
    pos: Position
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        if (pictureByteArray != null) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    .asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, shape = CircleShape)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.baseline_account_circle_48),
                contentDescription = "own profile loading icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = playerName ?: "loading info...", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .size(
                        BUTTON_WIDTH * when {
                            isGreen && pos.pieceToMove -> 1.5f
                            !isGreen && !pos.pieceToMove -> 1.5f
                            else -> 1f
                        }
                    )
                    .background(if (isGreen) Color.Green else Color.Blue, CircleShape)
                    .alpha(if (pos.freeGreenPieces == 0.toUByte()) 0f else 1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    color = if (!isGreen) {
                        Color.Green
                    } else {
                        Color.Blue
                    },
                    text = if (!isGreen) {
                        pos.freeBluePieces.toString()
                    } else {
                        pos.freeGreenPieces.toString()
                    },
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${rating ?: "loading..."}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PlayersUI(
    pos: Position,
    dragDesk: Boolean,
    onToggleDragging: () -> Unit,
    timeLeft: Long,
    isGreen: Boolean?,
    ownAccountName: String?,
    ownPictureByteArray: ByteArray?,
    ownAccountRating: Long?,
    enemyAccountName: String?,
    enemyPictureByteArray: ByteArray?,
    enemyAccountRating: Long?,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerCard(
                playerName = ownAccountName,
                pictureByteArray = ownPictureByteArray,
                isGreen = isGreen == true,
                rating = ownAccountRating,
                pos = pos,
            )

            Spacer(modifier = Modifier.width(16.dp))
            PlayerCard(
                playerName = enemyAccountName,
                pictureByteArray = enemyPictureByteArray,
                isGreen = isGreen == false,
                rating = enemyAccountRating,
                pos = pos,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            TurnTimerUI(timeLeft)
            Button(
                onClick = { onToggleDragging() },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(if (dragDesk) "Deactivate Move" else "Activate Move")
            }
        }

    }
}

