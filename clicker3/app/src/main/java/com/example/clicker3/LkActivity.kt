package com.example.clicker3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.clicker3.data.viewmodel.PurchaseViewModel
import com.example.clicker3.ui.theme.Clicker3Theme
import com.example.clicker3.ui.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.random.Random
import androidx.compose.runtime.produceState
import com.example.clicker3.ui.LoadingScreen
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

data class Animal(
    val name: String,
    val imageResId: Int,
    val price: Int,
    val isExotic: Boolean = false
)

@AndroidEntryPoint
class LkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val darkTheme = ThemeManager.isDarkTheme.collectAsState()
                //Здесь мы проверяем, включена ли темная тема в приложении.

            Clicker3Theme(darkTheme = darkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                    // Здесь будет содержимое нашего экрана
                ) {
                    LkScreen(
                        isDarkTheme = darkTheme.value,
                        onThemeChange = { ThemeManager.toggleTheme() },
                        onNavigateToMain = {
                            //гл экран

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onNavigateToPurchases = {
                            //магазин

                            val intent = Intent(this, PurchasedImagesActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}


enum class LoadingScreen { MAIN, SHOP, COLLECTION }

@Composable
fun LkScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    purchaseViewModel: PurchaseViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var nextScreen by remember { mutableStateOf<LoadingScreen?>(null) }
    val clickCount by purchaseViewModel.clickCount.collectAsState(initial = 0)
    val customFont = FontFamily(Font(R.font.text))

    val context = LocalContext.current

    if (isLoading) {
        LoadingScreen()
        LaunchedEffect(nextScreen) {
            delay(300)
            when (nextScreen) {
                LoadingScreen.MAIN -> onNavigateToMain()
                LoadingScreen.COLLECTION -> onNavigateToPurchases()
                LoadingScreen.SHOP -> {
                    val intent = Intent(context, LkActivity::class.java)
                    context.startActivity(intent)
                }
                else -> {}
            }
            isLoading = false
            nextScreen = null
        }
        return
    }

    val regularAnimals = listOf(
        Animal("МАНЧКИН", R.drawable.manchkin, 150),
        Animal("ШОТЛАНДСКАЯ ВИСЛОУХАЯ", R.drawable.shotlandcat, 180),
        Animal("ДЕВОН РЕКС", R.drawable.devonreks, 120),
        Animal("СИАМСКАЯ КОШКА", R.drawable.siamcat, 130),
        Animal("МЕЙН-КУН", R.drawable.maykun1, 190),
        Animal("ПУДЕЛЬ", R.drawable.pudel, 160),
        Animal("ШПИЦ", R.drawable.siba, 170),
        Animal("ДАЛМАТИНЕЦ", R.drawable.dalmatinets, 140),
        Animal("ШИ-ТЦУ", R.drawable.shitsu, 110),
        Animal("КОРГИ", R.drawable.korgi, 200),
        Animal("МОПС", R.drawable.mops, 175),
        Animal("ХАСКИ", R.drawable.haski, 195),
        Animal("КАКАДУ", R.drawable.kakdu, 165),
        Animal("КОРЕЛЛА", R.drawable.corella, 125),
        Animal("ПОПУГАЙ АРА", R.drawable.popugaiara, 185),
        Animal("ЦЫПЛЕНОК", R.drawable.cyplonok, 100),
        Animal("ЕЖИК", R.drawable.esh1, 145),
        Animal("КРОЛИК", R.drawable.krolik, 135),
        Animal("ЗМЕЙКА", R.drawable.python, 155),
        Animal("ХОМЯК", R.drawable.homyachok, 105)
    )


    val exoticAnimals = listOf(
        Animal("КОАЛА", R.drawable.coala, 1500, true),
        Animal("ЛЕМУР", R.drawable.lemur, 1200, true),
        Animal("ЛЕНИВЕЦ", R.drawable.lenivets, 1800, true),
        Animal("ПАНДА", R.drawable.panda, 2000, true),
        Animal("СУРИКАТ", R.drawable.surikat, 1300, true),
        Animal("ТУШКАНЧИК", R.drawable.tushkanchik, 1100, true),
        Animal("УТКОНОС", R.drawable.utkonos, 1700, true),
        Animal("ПЕСОК", R.drawable.pesok, 1000, true),
        Animal("ЛЕВ", R.drawable.lev, 1900, true),
        Animal("ВЫДРА", R.drawable.vydra, 1400, true),
        Animal("ФЕНЕК", R.drawable.fenek, 1600, true)
    )


    val fantasticAnimals = listOf(
        Animal("ДРАКОН", R.drawable.drakon, 5000, true),
        Animal("ЕДИНОРОГ", R.drawable.edinorog, 5000, true),
        Animal("ГРИФОН", R.drawable.grifon, 5000, true),
        Animal("ФЕНИКС", R.drawable.phonex, 5000, true),
        Animal("ПЕГАС", R.drawable.pegas, 5000, true)
    )


    val legendaryAnimals = listOf(
        Animal("МЕДВЕЖОНОК МУЗЫКАНТ", R.drawable.medvedmusic, 10000, true),
        Animal("ПИНГВИН ФОКУСНИК", R.drawable.pingvin, 10000, true),
        Animal("КОТ-СУПЕРГЕРОЙ", R.drawable.catsuper, 10000, true),
        Animal("ПЕСИК-РОБОТ", R.drawable.robopes, 10000, true)
    )


    val allAnimals = regularAnimals + exoticAnimals + fantasticAnimals + legendaryAnimals

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val isCompactHeight = screenHeight < 600.dp

        Image(
            painter = painterResource(id = R.drawable.fon6),
            contentDescription = "Фоновое изображение",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = R.drawable.niznayaplashka),
            contentDescription = "Верхняя плашка",
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isCompactHeight) 60.dp else 80.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )

        if (isLoading) {
            LoadingScreen()
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = if (isCompactHeight) 20.dp else 26.dp)
                    .clickable {
                        nextScreen = LoadingScreen.COLLECTION
                        isLoading = true
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.collectionmagazine),
                    contentDescription = "Плашка коллекция",
                    modifier = Modifier.width(if (screenWidth < 400.dp) 160.dp else 200.dp),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = "КОЛЛЕКЦИЯ",
                    color = Color.White,
                    fontSize = if (screenWidth < 400.dp) 18.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFont,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = if (isCompactHeight) 70.dp else 90.dp)
                    .clickable {
                        nextScreen = LoadingScreen.SHOP
                        isLoading = true
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.magazinefon),
                    contentDescription = "Плашка магазин",
                    modifier = Modifier.width(if (screenWidth < 400.dp) 160.dp else 200.dp),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = "МАГАЗИН",
                    color = Color.White,
                    fontSize = if (screenWidth < 400.dp) 22.sp else 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFont,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        bottom = 10.dp,  // Adjust this value to move it up/down
                        start = 20.dp    // Adjust this value to move it left/right
                    )
                    .clickable { onThemeChange() }
                    .zIndex(10f)
            ) {

                Image(
                    painter = painterResource(
                        id = if (isDarkTheme) R.drawable.svetlaya else R.drawable.temnaya
                    ),
                    contentDescription = if (isDarkTheme) "Светлая тема" else "Темная тема",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }




            Image(
                painter = painterResource(id = R.drawable.list),
                contentDescription = "Лист",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(if (isCompactHeight) 120.dp else 140.dp)
                    .offset(y = -(if (isCompactHeight) 10.dp else 5.dp))
                    .offset(x = if (isCompactHeight) 20.dp else 5.dp),
                contentScale = ContentScale.Fit
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (isCompactHeight) 170.dp else 200.dp,
                        bottom = if (isCompactHeight) 90.dp else 110.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                items(allAnimals) { animal ->
                    AnimalItem(

                        animal = animal,
                        onBuyClick = { purchaseViewModel.purchaseImage(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            Image(
                painter = painterResource(id = R.drawable.niznayaplashka),
                contentDescription = "Нижняя плашка",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompactHeight) 60.dp else 80.dp)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                // Внутри Box добавляем текст с балансом пользователя
            ) {
                Image(
                    painter = painterResource(id = R.drawable.list),
                    contentDescription = "лист",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(if (isCompactHeight) 140.dp else 160.dp)
                        .offset(y = -(if (isCompactHeight) 20.dp else 15.dp))
                        .offset(x = if (isCompactHeight) 30.dp else 15.dp),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-20).dp,x = (27).dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "БАЛАНС:",
                        color = Color.White,
                        fontSize = if (screenWidth < 400.dp) 16.sp else 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = customFont,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = clickCount.toString(),
                        color = Color.White,
                        fontSize = if (screenWidth < 400.dp) 20.sp else 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        fontStyle = FontStyle.Normal
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompactHeight) 20.dp else 30.dp)
                .clickable {
                    nextScreen = LoadingScreen.MAIN
                    isLoading = true
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.glavnmagazine),
                contentDescription = "Плашка главная",
                modifier = Modifier.width(if (screenWidth < 400.dp) 160.dp else 200.dp),
                contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "ГЛАВНАЯ",
                    color = Color.White,
                    fontSize = if (screenWidth < 400.dp) 26.sp else 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFont,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 27.dp)
                )
            }
        }
    }
}

@Composable
fun AnimalItem(
    animal: Animal,
    onBuyClick: (Animal) -> Unit,
    purchaseViewModel: PurchaseViewModel = hiltViewModel()
) {

    val purchasedImages by purchaseViewModel.purchasedImages.collectAsState(initial = emptyList())
    val petCount = purchasedImages.count { it.imageName == animal.name }
    val isPurchased = purchasedImages.any { it.imageName == animal.name }
    val currentPrice by produceState(initialValue = animal.price, animal, purchasedImages) {
        value = purchaseViewModel.getCurrentPrice(animal)
    }
    //Здесь мы получаем список всех купленных животных и проверяем, есть ли среди них текущее животное.

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {

        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.windowanimal),
                contentDescription = "Animal background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )


            Image(
                painter = painterResource(id = animal.imageResId),
                contentDescription = animal.name,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit,


                colorFilter = if (!isPurchased) ColorFilter.tint(
                    Color.Black,
                    BlendMode.SrcAtop
                ) else null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.plashkamagaz),
                contentDescription = "Buy background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = animal.name,
                    color = when {
                        animal.price >= 10000 -> Color(0xFFFF8C00) // Orange for legendary
                        animal.price >= 5000 -> Color(0xFF9370DB)  // Purple for fantastic
                        animal.price >= 1000 -> Color(0xFF1E90FF)  // Blue for exotic
                        else -> Color.Gray                         // Gray for regular
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.text)),
                    maxLines = 1
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(30.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBuyClick(animal) }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.magazinefon),
                        contentDescription = "Buy button",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "КУПИТЬ",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.text)),
                            fontStyle = FontStyle.Italic
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$currentPrice",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.text))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "x$petCount",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily(Font(R.font.text))
                            )
                        }
                    }
                }
            }
        }
    }
}

