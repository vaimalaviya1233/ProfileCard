package com.vaibhav.profilecard

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.vaibhav.profilecard.ui.theme.Blake80
import com.vaibhav.profilecard.ui.theme.MyTheme
import com.vaibhav.profilecard.ui.theme.dark_lime
import com.vaibhav.profilecard.ui.theme.dark_lime_green
import com.vaibhav.profilecard.ui.theme.shapes
import com.vaibhav.profilecard.ui.theme.smoothGreen
import com.vaibhav.profilecard.ui.theme.smoothRed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTheme {
                UserApplication()
            }
        }
    }
}


@Composable
fun UserApplication(userProfiles: List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list"){
        
        composable("users_list"){
            UserListScreen(userProfiles, navController)
        }
        
        composable(
            route = "users_details/{userId}",
            arguments = listOf(navArgument("userId"){
                    type = NavType.IntType
            })
        ){navBackStackEntry ->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"),navController)
        }
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavHostController?) {
    Scaffold(topBar = {
        AppBar(
            title = "Users List",
            icon = Icons.Default.Home
        ){/* noting to pass*/ }
    })
    {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Blake80
        ) {
           LazyColumn {
               items(userProfiles){ userProfile ->
                   ProfileCard(userProfile = userProfile){
                       navController?.navigate("users_details/${userProfile.id}")
                   }
               }
           }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavHostController?) {
//fun UserProfileDetailsScreen(userProfile: UserProfile = userProfileList[0]) {
    val userProfile = userProfileList.first{ userProfile -> userId == userProfile.id }
    Scaffold(topBar = {
        AppBar(
            title = "Users List",
            icon = Icons.Default.ArrowBack
        ){
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Blake80
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(userProfile.pictureUrl, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
            
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AppBar(
    title: String,
    icon: ImageVector,
    iconClickAction: () -> Unit
){
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = icon,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { iconClickAction.invoke() },
                contentDescription = "Home Icon"
            )
        },
        title= {
            Text(text = title)
        },
        modifier = Modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF2C3F38),
            titleContentColor = Color(0xFF3AC37A),
            navigationIconContentColor = Color(0xFF3AC37A)
        )
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 18.dp),
        colors = CardDefaults.cardColors(
            containerColor = dark_lime_green,
            contentColor = dark_lime
        ),
        shape = shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }
    }
}

@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = if(onlineStatus)
                3.dp else 2.dp,
            color = if(onlineStatus)
                smoothGreen
            else
                smoothRed
            ),
        modifier = Modifier
            .padding(16.dp)
            .alpha(
                if (onlineStatus)
                    1f else 0.7f
            ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)

    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUrl)
                .crossfade(true)
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(R.drawable.profile_picture),
            contentDescription = "None",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
        )
        
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus:Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ){
        Text(
            text=userName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alpha(
                if(onlineStatus)
                    1f else 0.5f
            )
        )
        Text(
            text=if(onlineStatus)
                "Active now"
            else "Offline",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(0.4f)
        )
    }
}


@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun UserProfileDetailsPreview() {
    MyTheme{
        UserProfileDetailsScreen(userId = 0, null)
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun UserListPreview() {
    MyTheme{
        UserListScreen(userProfiles = userProfileList, navController = null)
    }
}
