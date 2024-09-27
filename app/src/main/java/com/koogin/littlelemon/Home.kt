package com.koogin.littlelemon

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavHostController) {
    val url =
        "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"
    val context = LocalContext.current

    val categories = listOf("Starters", "Mains", "Desserts", "Drinks")

    val database by lazy {
        AppDatabase.getDatabase(context)
    }

    val menuItems = database.menuItemDao().getAll().observeAsState(emptyList())


    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }


    suspend fun getMenu(): List<MenuItemNetwork> {

        val response = client.get(url).body<MenuNetworkData>()
        return response.menu
    }

    fun saveMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toRoomMenuItem() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }


    val coroutineScope = rememberCoroutineScope()




    Scaffold(
        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp, bottom = 20.dp),

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {


                Image(
                    painter = painterResource(id = R.drawable.little_lemon_logo),
                    contentDescription = "LittleLemon Logo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(185.dp)
                        .height(45.dp)
                )

                Box(
                    modifier = Modifier.offset(x = 55.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(shape = RoundedCornerShape(50))
                            .clickable {
                                navController.navigate(Profile.route)
                            },
                        contentScale = ContentScale.Crop,
                    )
                }


            }


        }
    )
    { innerPadding ->


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF495E57))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Little Lemon",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF4CE14)
                )
                Text(
                    text = "Chicago",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.hero_image),
                        contentDescription = "Hero Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(125.dp)
                            .width(125.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                    )
                }

                val searchPhrase = rememberSaveable {
                    mutableStateOf("")
                }

                TextField(
                    placeholder = { Text("Enter search phrase") },
                    value = searchPhrase.value,
                    maxLines = 1,
                    onValueChange = { newValue ->
                        searchPhrase.value = newValue
                        menuItems.value.filter { it.title.contains(searchPhrase.value) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "ORDER FOR DELIVERY",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LazyRow(modifier = Modifier.padding(bottom = 10.dp)) {
                    items(categories) { category ->
                        Button(
                            onClick = {
                                 menuItems.value.filter { it.category == category }
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF495E57)
                            )
                        }
                    }
                }

            }

            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                HorizontalDivider(color = Color(0xFFAFAFAF), thickness = 1.dp)
            }

            LaunchedEffect(Unit) {
                coroutineScope.launch(Dispatchers.IO) {
                    if (database.menuItemDao().isEmpty()) {
                        val menuItemsNetwork = getMenu()
                        saveMenuToDatabase(menuItemsNetwork)
                    }
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(menuItems.value) { menuItem ->
                    MenuItem(
                        title = menuItem.title,
                        description = menuItem.description,
                        price = menuItem.price,
                        image = menuItem.image,
                    )
                    HorizontalDivider(color = Color(0xFFAFAFAF), thickness = 1.dp)
                }
            }
        }
    }
}


@Composable
fun MenuItem(title: String, description: String, price: String, image: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold)

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(0.7f)) {
                Text(text = description, maxLines = 2)
                Text(
                    text = "$$price",
                    color = Color(0xFFAFAFAF),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            GlideImage(image)

        }
    }
}


@Composable
fun GlideImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        modifier = modifier.size(100.dp),  // Adjust size as needed
        update = { imageView ->
            Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    )
}