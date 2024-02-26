package com.onetouch.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.onetouch.data.Person
import com.onetouch.data.personList
import com.onetouch.life.R
import com.onetouch.life.ui.theme.DarkGray
import com.onetouch.life.ui.theme.Gray
import com.onetouch.life.ui.theme.Gray400
import com.onetouch.life.ui.theme.InterBold
import com.onetouch.life.ui.theme.InterMedium
import com.onetouch.life.ui.theme.InterRegular
import com.onetouch.life.ui.theme.InterSemibold
import com.onetouch.life.ui.theme.Line
import com.onetouch.life.ui.theme.Yellow
import com.onetouch.navigation.CHAT_SCREEN
import com.onetouch.navigation.STORY_SCREEN
import com.onetouch.navigation.VIDEO_SCREEN


@Composable
fun HomeScreen(
    navHostController: NavHostController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Header()
            LazyRow(modifier = Modifier.padding(vertical = 20.dp)) {
                item {
                    AddStoryLayout(modifier = Modifier.padding(start = 23.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                }
                items(personList, key = { it.id }) {
                    UserStory(person = it, navController = navHostController)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp, topEnd = 30.dp
                        )
                    )
                    .background(Color.White)
            ) {
                RoundedCorner(modifier = Modifier.align(TopCenter).padding(top = 15.dp))
                LazyColumn(
                    modifier = Modifier.padding(bottom = 15.dp, top = 30.dp)
                ) {
                    items(personList, key = { it.id }) {
                        UserEachRow(person = it){
                            navHostController.currentBackStackEntry?.savedStateHandle?.set("data",it)
                            navHostController.navigate(CHAT_SCREEN)
                        }
                    }
                }
            }
        }

    }
//    AddStoryScreen(navHostController)

}


@Composable
fun RoundedCorner(
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .width(90.dp)
            .height(5.dp)
            .clip(RoundedCornerShape(90.dp))
            .background(
                Gray400
            )
    )
}


@Composable
fun UserEachRow(
    person: Person,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .noRippleEffect { onClick() }
            .padding(horizontal = 20.dp, vertical = 5.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = person.icon),
                        contentDescription = "",
                        modifier = Modifier.size(60.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                    ) {
                        Text(
                            text = person.name, style = TextStyle(
                                color = Color.Black, fontSize = 15.sp, fontFamily = InterBold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Okay", style = TextStyle(
                                color = Gray, fontSize = 14.sp, fontFamily = InterMedium
                            )
                        )
                    }

                }
                Text(
                    text = "12:23 PM", style = TextStyle(
                        color = Gray, fontSize = 12.sp, fontFamily = InterMedium
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Line)
        }
    }

}

@Composable
fun UserStory(
    person: Person,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .clickable {
                navController.navigate(VIDEO_SCREEN)
            }
            .padding(end = 10.dp)
    ) {
        Card(
            modifier = Modifier.size(70.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp, Yellow),
            elevation = 0.dp,
            backgroundColor = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Yellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = person.icon),
                    contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = person.name, style = TextStyle(
                color = Color.White, fontSize = 13.sp, fontFamily = InterMedium
            ), modifier = Modifier.align(CenterHorizontally)
        )
    }
}



@Composable
fun AddStoryLayout(
    modifier: Modifier = Modifier
//    navController: NavController,
//    onClick: () -> Unit
) {
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
//                onClick()
            } else {
                // Permission is denied, handle accordingly
                // For simplicity, we're not doing anything here, you might want to display a message to the user
            }
        }

    Column(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .size(70.dp)
                .clickable(onClick = {
                    // Request camera permission when the button is clicked
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }),
            shape = CircleShape,
            elevation = 0.dp,
            backgroundColor = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Yellow
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.add_story),
            style = TextStyle(
                color = Color.White,
                fontSize = 13.sp,
                // Assuming InterMedium is defined elsewhere
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

//@Composable
//fun AddStoryScreen(navController: NavController) {
//    AddStoryLayout(navController = navController) {
//        navController.navigate(STORY_SCREEN)
//    }
//}

@Composable
fun Header() {

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.White,
                fontFamily = InterRegular,
                fontSize = 20.sp,
                fontWeight = FontWeight.W300
            )
        ) {
            append("Welcome back, ")
        }
        withStyle(
            style = SpanStyle(
                color = Color.White,
                fontFamily = InterSemibold,
                fontSize = 20.sp,
            )
        ) {
            append("Jayant!")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 60.dp)
    ) {
        Text(text = annotatedString)
    }

}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.noRippleEffect(onClick:()->Unit) = composed {
    clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) {
        onClick()
    }
}