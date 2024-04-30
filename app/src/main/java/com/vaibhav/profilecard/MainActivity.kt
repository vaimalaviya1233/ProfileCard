package com.vaibhav.profilecard

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
//import com.google.accompanist.coil.rememberCoilPainter
import com.vaibhav.profilecard.ui.theme.*
import com.vaibhav.profilecard.ui.theme.MyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*ProfileCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
            MyTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(userProfiles: List<UserProfile> = userProfileList) {
    Scaffold(topBar = { AppBar() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Blake80
        ) {
           LazyColumn {
               items(userProfiles){ userProfile ->
                   ProfileCard(userProfile = userProfile)
               }
           }
        }
    }

}
@ExperimentalMaterial3Api
@Composable
fun AppBar(){
    TopAppBar(
        navigationIcon = {
            Icon(
                Icons.Default.Home,
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                contentDescription = "Home Icon"
            )
        },
        title= {
            Text(text = "Messaging Application users")
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
fun ProfileCard(userProfile: UserProfile) {
    Card(
        modifier = Modifier
//            .padding(vertical = 10.dp, horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 18.dp),
        colors = CardDefaults.cardColors(
//            containerColor = Color(255,255,255), // white background
            containerColor = dark_lime_green,
            contentColor = dark_lime
        ),
        shape = shapes.large

    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.pictureUrl, userProfile.status)
            ProfileContent(userProfile.name, userProfile.status)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean) {
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
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)

    ){
        /*Image(

            /*
            painter = rememberCoilPainter(
                request = pictureUrl,
                requestBuilder = {
                    transformations(CircleCropTransformation())
                }
            ),*/
            painter = rememberImagePainter(
                data = pictureUrl,
                imageLoader = coil.ImageLoader(LocalContext.current),
                builder = {
                    this.crossfade(true)
                    this.placeholder(R.drawable.profile_picture)
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = "none",
            modifier = Modifier.size((72.dp)),
            contentScale = ContentScale.Crop
        )*/
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUrl)
                .crossfade(true)
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(R.drawable.profile_picture),
            contentDescription = "None",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(72.dp)
        )
        
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus:Boolean) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
fun DefaultPreview() {
    MyTheme{
        MainScreen()
    }
}