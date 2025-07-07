package com.example.clicker3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.clicker3.data.entity.PurchasedImageEntity



@AndroidEntryPoint
class PurchasedImagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = ThemeManager.isDarkTheme.collectAsState()

            Clicker3Theme(darkTheme = darkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PurchasedImagesScreen(
                        onNavigateToMain = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onNavigateToShop = {
                            val intent = Intent(this, LkActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}






@Composable
fun PurchasedImagesScreen(
    purchaseViewModel: PurchaseViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToShop: () -> Unit
) {
    val purchasedImages by purchaseViewModel.purchasedImages.collectAsState(initial = emptyList())
    val regularAnimals by purchaseViewModel.regularAnimals.collectAsState(initial = emptyList())
    val exoticAnimals by purchaseViewModel.exoticAnimals.collectAsState(initial = emptyList())
    val fantasticAnimals by purchaseViewModel.fantasticAnimals.collectAsState(initial = emptyList())
    val legendaryAnimals by purchaseViewModel.legendaryAnimals.collectAsState(initial = emptyList())
    val clickCount by purchaseViewModel.clickCount.collectAsState(initial = 0)
    val customFont = FontFamily(Font(R.font.text))
    val context = LocalContext.current
    val (selectedAnimal, setSelectedAnimal) = remember { mutableStateOf<PurchasedImageEntity?>(null) }




    val allRegularAnimals = listOf(
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

    val allExoticAnimals = listOf(
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

    val allFantasticAnimals = listOf(
        Animal("ДРАКОН", R.drawable.drakon, 5000, true),
        Animal("ЕДИНОРОГ", R.drawable.edinorog, 5000, true),
        Animal("ГРИФОН", R.drawable.grifon, 5000, true),
        Animal("ФЕНИКС", R.drawable.phonex, 5000, true),
        Animal("ПЕГАС", R.drawable.pegas, 5000, true)
    )

    val allLegendaryAnimals = listOf(
        Animal("МЕДВЕЖОНОК МУЗЫКАНТ", R.drawable.medvedmusic, 10000, true),
        Animal("ПИНГВИН ФОКУСНИК", R.drawable.pingvin, 10000, true),
        Animal("КОТ-СУПЕРГЕРОЙ", R.drawable.catsuper, 10000, true),
        Animal("ПЕСИК-РОБОТ", R.drawable.robopes, 10000, true)
    )





// Show dialog when an animal is selected
    selectedAnimal?.let { animal ->
        var petName by remember { mutableStateOf(animal.customName ?: animal.imageName) }

        AlertDialog(
            onDismissRequest = { setSelectedAnimal(null) },
            title = { Text(animal.imageName) },
            text = {
                Column {
                    Text("Что вы хотите сделать с этим питомцем?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Имя питомца:", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = petName,
                        onValueChange = { petName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Введите имя питомца") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {

                    purchaseViewModel.updatePetNameAndVisibility(animal, petName, context)
                    setSelectedAnimal(null)
                }) {
                    Text(if (purchaseViewModel.isAnimalShown(animal.id)) "Скрыть" else "Показать")
                }
            },
            dismissButton = {
                TextButton(onClick = { setSelectedAnimal(null) }) {
                    Text("Отмена")
                }
            }
        )
    }



    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val isCompactHeight = screenHeight < 600.dp
        val darkTheme = ThemeManager.isDarkTheme.collectAsState()
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.fon6),
            contentDescription = "Фоновое изображение",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = if (isCompactHeight) 190.dp else 220.dp,
                end = 16.dp,
                bottom = if (isCompactHeight) 90.dp else 110.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {

            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plashkamagaz),
                        contentDescription = "Плашка обычных животных",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Text(
                        text = "ОБЫЧНЫЕ ${regularAnimals.size}/20",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


            items(allRegularAnimals) { animal ->

                val isPurchased = regularAnimals.any { it.imageName == animal.name }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.windowanimal),
                        contentDescription = "Ячейка животного",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    Image(
                        painter = painterResource(id = animal.imageResId),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .clickable(enabled = isPurchased) {
                                if (isPurchased) {
                                    val purchasedAnimal = regularAnimals.find { it.imageName == animal.name }
                                    purchasedAnimal?.let { setSelectedAnimal(it) }
                                }
                            },
                        contentScale = ContentScale.Fit,
                        colorFilter = if (!isPurchased) ColorFilter.tint(Color.Black, BlendMode.SrcAtop) else null
                    )
                }
            }




            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plashkamagaz),
                        contentDescription = "Плашка экзотических животных",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Text(
                        text = "ЭКЗОТИЧЕСКИЕ ${exoticAnimals.size}/11",
                        color = Color(0xFF1E90FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


            items(allExoticAnimals) { animal ->
                val isPurchased = exoticAnimals.any { it.imageName == animal.name }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.windowanimal),
                        contentDescription = "Ячейка животного",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painter = painterResource(id = animal.imageResId),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .clickable(enabled = isPurchased) {
                                if (isPurchased) {
                                    val purchasedAnimal = exoticAnimals.find { it.imageName == animal.name }
                                    purchasedAnimal?.let { setSelectedAnimal(it) }
                                }
                            },
                        contentScale = ContentScale.Fit,
                        colorFilter = if (!isPurchased) ColorFilter.tint(Color.Black, BlendMode.SrcAtop) else null
                    )
                }
            }



            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plashkamagaz),
                        contentDescription = "Плашка фантастических животных",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Text(
                        text = "ФАНТАСТИЧЕСКИЕ ${fantasticAnimals.size}/5",
                        color = Color(0xFF9370DB),  // Фиолетовый для фантастических
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


            items(allFantasticAnimals) { animal ->

                val isPurchased = fantasticAnimals.any { it.imageName == animal.name }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.windowanimal),
                        contentDescription = "Ячейка животного",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    Image(
                        painter = painterResource(id = animal.imageResId),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .clickable(enabled = isPurchased) {
                                if (isPurchased) {
                                    val purchasedAnimal = fantasticAnimals.find { it.imageName == animal.name }
                                    purchasedAnimal?.let { setSelectedAnimal(it) }
                                }
                            },
                        colorFilter = if (!isPurchased) ColorFilter.tint(Color.Black) else null,
                        contentScale = ContentScale.Fit
                    )
                }
            }



            item(span = { GridItemSpan(5) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plashkamagaz),
                        contentDescription = "Плашка легендарных животных",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Text(
                        text = "ЛЕГЕНДАРНЫЕ ${legendaryAnimals.size}/5",
                        color = Color(0xFFFF8C00),  // Оранжевый для легендарных
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


            items(allLegendaryAnimals) { animal ->
                // Проверяем, куплено ли это животное
                val isPurchased = legendaryAnimals.any { it.imageName == animal.name }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.windowanimal),
                        contentDescription = "Ячейка животного",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    Image(
                        painter = painterResource(id = animal.imageResId),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .clickable(enabled = isPurchased) {
                                if (isPurchased) {
                                    val purchasedAnimal = legendaryAnimals.find { it.imageName == animal.name }
                                    purchasedAnimal?.let { setSelectedAnimal(it) }
                                }
                            },
                        colorFilter = if (!isPurchased) ColorFilter.tint(Color.Black) else null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }


        Image(
            painter = painterResource(id = R.drawable.niznayaplashka),
            contentDescription = "Верхняя плашка",
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isCompactHeight) 60.dp else 80.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )


        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = if (isCompactHeight) 10.dp else 16.dp)
                .clickable {
                    val intent = Intent(context, LkActivity::class.java)
                    context.startActivity(intent)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.collectionmagazine),
                contentDescription = "Плашка магазин",
                modifier = Modifier.width(if (screenWidth < 400.dp) 180.dp else 220.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "МАГАЗИН",
                color = Color.White,
                fontSize = if (screenWidth < 400.dp) 20.sp else 24.sp,
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
                .clickable { onNavigateToShop() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.magazinefon),
                contentDescription = "Плашка коллекция",
                modifier = Modifier.width(if (screenWidth < 400.dp) 200.dp else 240.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "КОЛЛЕКЦИЯ",
                color = Color.White,
                fontSize = if (screenWidth < 400.dp) 20.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 10.dp)
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
                .align(Alignment.BottomStart)
                .padding(
                    bottom = 10.dp,  // Adjust this value to move it up/down
                    start = 20.dp    // Adjust this value to move it left/right
                )
                .clickable { ThemeManager.toggleTheme() }
                .zIndex(10f)
        ) {

            Image(
                painter = painterResource(
                    id = if (darkTheme.value) R.drawable.svetlaya else R.drawable.temnaya
                ),
                contentDescription = if (darkTheme.value) "Светлая тема" else "Темная тема",
                modifier = Modifier
                    .size(70.dp)     // Adjust this value to change the size
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompactHeight) 20.dp else 30.dp)
                .clickable { onNavigateToMain() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.glavnmagazine),
                contentDescription = "Плашка главная",
                modifier = Modifier.width(if (screenWidth < 400.dp) 160.dp else 200.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "ГЛАВНАЯ",
                color = Color.White,
                fontSize = if (screenWidth < 400.dp) 25.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 25.dp)
            )

        }
    }
}
