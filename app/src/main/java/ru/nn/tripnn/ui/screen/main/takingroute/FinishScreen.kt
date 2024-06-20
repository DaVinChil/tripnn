package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun FinishScreen(
    modifier: Modifier = Modifier,
    toMainScreen: () -> Unit,
    addToFavourite: () -> Unit,
    removeFromFavourite: () -> Unit,
    currentRoute: CurrentRoute
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.confetti))
    LottieAnimation(composition = composition)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(TripNnTheme.colorScheme.bottomSheetBackground)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        var favourite by rememberSaveable { mutableStateOf(currentRoute.favourite) }
        Icon(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.End)
                .rippleClickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    onClick = {
                        favourite = if (favourite) {
                            removeFromFavourite()
                            false
                        } else {
                            addToFavourite()
                            true
                        }
                    }
                ),
            painter = painterResource(id = if (favourite) R.drawable.gold_bookmark else R.drawable.gray_bookmark),
            contentDescription = stringResource(id = R.string.bookmark),
            tint = Color.Unspecified
        )

        Image(
            painter = painterResource(id = R.drawable.party_popper),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally),
            contentDescription = "party popper"
        )

        Spacer(modifier = Modifier.height(30.dp))

        MontsText(
            text = stringResource(id = R.string.route_finished),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryButton(
            text = stringResource(id = R.string.to_main_screen),
            onClick = toMainScreen,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}