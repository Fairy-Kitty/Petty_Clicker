package com.example.clicker3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.clicker3.data.viewmodel.ClickerViewModel
import com.example.clicker3.ui.theme.Clicker3Theme
import com.example.clicker3.ui.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clicker3.data.entity.PurchasedImageEntity
import com.example.clicker3.data.viewmodel.PurchaseViewModel
import kotlinx.coroutines.delay
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.w3c.dom.Element
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import android.media.MediaPlayer




        @AndroidEntryPoint
        class MainActivity : ComponentActivity() {
                private var mediaPlayer: MediaPlayer? = null
            private val viewModel: ClickerViewModel by viewModels()
            private val purchaseViewModel: PurchaseViewModel by viewModels()

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                enableEdgeToEdge()
                purchaseViewModel.loadShownAnimalId(this)

                // --- ВСТАВЬ ВОТ СЮДА ---
                viewModel.startLegendaryAutoClicker()
                // --- КОНЕЦ ВСТАВКИ ---
                mediaPlayer = MediaPlayer.create(this, R.raw.mine) // имя файла без расширения!
                mediaPlayer?.isLooping = true // Зацикливаем музыку
                mediaPlayer?.start()

                setContent {
                    val darkTheme = ThemeManager.isDarkTheme.collectAsState()
                    val count = viewModel.clickCount.collectAsState()

                    Clicker3Theme(darkTheme = darkTheme.value) {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            TapCounter(
                                count = count.value,
                                onIncrement = { viewModel.incrementCount() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
            override fun onDestroy() {
                super.onDestroy()
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClickBonusAnimation(
    bonus: Int,
    visible: Boolean,
    offset: Offset,
    color: Color
) {
    val targetOffsetY = if (visible) -60.dp else 0.dp
    val animatedOffsetY by animateDpAsState(
        targetValue = targetOffsetY,
        animationSpec = tween(durationMillis = 600)
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(350)),
        exit = fadeOut(animationSpec = tween(350)),
        initiallyVisible = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "+$bonus",
                color = color,
                fontSize = (32 + bonus * 2).coerceAtMost(64).sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = offset.x.toInt(),
                            y = (offset.y + animatedOffsetY.value).toInt()
                        )
                    }
            )
        }
    }
}

@Composable
fun TapCounter(
    count: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    purchaseViewModel: PurchaseViewModel = hiltViewModel()
) {
    var showAnim by remember { mutableStateOf(false) }
    var lastBonus by remember { mutableStateOf(1) }
    var tapOffset by remember { mutableStateOf(Offset.Zero) }
    val clickerViewModel: ClickerViewModel = hiltViewModel()
    val autoClickEvent by clickerViewModel.autoClickEvent.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val customFont = FontFamily(Font(R.font.text))
    val darkTheme = ThemeManager.isDarkTheme.collectAsState()
    val shownAnimalId by purchaseViewModel.shownAnimalId.collectAsState()
    val purchasedImages by purchaseViewModel.purchasedImages.collectAsState(initial = emptyList())
    var quote by remember { mutableStateOf("") }

    // Цвет для анимации в зависимости от темы
    val bonusTextColor = if (darkTheme.value) {
        Color.White.copy(alpha = 0.7f)
    } else {
        Color.Black.copy(alpha = 0.7f)
    }

    // Автоматическое скрытие "+x" через 1 секунду
    if (showAnim) {
        LaunchedEffect(showAnim, lastBonus, tapOffset) {
            delay(1000)
            showAnim = false
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            quote = getQuote()
            delay(10000)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val density = LocalDensity.current
        val isCompactHeight = screenHeight < 600.dp
        val centerOffset = Offset(
            x = with(density) { (screenWidth / 2).toPx() },
            y = with(density) { (screenHeight / 2).toPx() }
        )

        LaunchedEffect(autoClickEvent) {
            if (autoClickEvent) {
                lastBonus = clickerViewModel.calculateBonusClicks()
                tapOffset = centerOffset
                showAnim = true
                delay(1000)
                showAnim = false
                clickerViewModel.resetAutoClickEvent()
            }
        }

        Image(
            painter = painterResource(id = if (darkTheme.value) R.drawable.font else R.drawable.fons),
            contentDescription = "Фоновое изображение",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = if (isCompactHeight) 20.dp else 26.dp)
                .clickable {
                    val intent = Intent(context, PurchasedImagesActivity::class.java)
                    context.startActivity(intent)
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
                    .offset(y = (-20).dp, x = (27).dp),
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
                    text = count.toString(),
                    color = Color.White,
                    fontSize = if (screenWidth < 400.dp) 20.sp else 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFont,
                    fontStyle = FontStyle.Normal
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (isCompactHeight) 60.dp else 80.dp,
                    bottom = if (isCompactHeight) 60.dp else 80.dp
                )
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        coroutineScope.launch {
                            val bonus = clickerViewModel.calculateBonusClicks()
                            lastBonus = bonus
                            tapOffset = offset
                            showAnim = true
                            clickerViewModel.incrementCount(bonus)
                        }
                    }
                }

        ) {
            purchasedImages.find { it.id == shownAnimalId }?.let { animal ->
                var isPressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.9f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = quote,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = customFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .widthIn(max = 300.dp)
                    )

                    Image(
                        painter = painterResource(id = animal.imageResId),
                        contentDescription = animal.imageName,
                        modifier = Modifier
                            .size(450.dp)
                            .scale(scale)
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when {
                                            event.type == PointerEventType.Press -> isPressed = true
                                            event.type == PointerEventType.Release -> {
                                                isPressed = false
                                            }
                                            event.type == PointerEventType.Exit -> isPressed = false
                                        }
                                    }
                                }
                            },
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = animal.customName ?: animal.imageName,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFont,
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
            ClickBonusAnimation(
                bonus = lastBonus,
                visible = showAnim,
                offset = tapOffset,
                color = bonusTextColor
            )
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
                    bottom = 10.dp,
                    start = 20.dp
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
                    .size(70.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompactHeight) 20.dp else 30.dp)
                .clickable {
                    val intent = Intent(context, LkActivity::class.java)
                    context.startActivity(intent)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.glavnmagazine),
                contentDescription = "Плашка магазин",
                modifier = Modifier.width(if (screenWidth < 400.dp) 180.dp else 200.dp),
                contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "МАГАЗИН",
                    color = Color.White,
                    fontSize = if (screenWidth < 400.dp) 27.sp else 27.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFont,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 27.dp)
                )
            }
        }
    }
}

suspend fun getQuote(): String {
    return withContext(Dispatchers.IO) {
        val url = "http://api.forismatic.com/api/1.0/"
        val client = OkHttpClient()

        val maxAttempts = 1000
        var attempts = 0

        while (attempts < maxAttempts) {
            attempts++

            val formBody = FormBody.Builder()
                .add("method", "getQuote")
                .add("format", "json")
                .add("lang", "ru")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string() ?: ""
                        val jsonObject = JSONObject(responseBody)
                        if (jsonObject.has("quoteText")) {
                            val fullQuote = jsonObject.getString("quoteText")
                            val cleanQuote = fullQuote.replace("\\s*\\([^)]*\\)".toRegex(), "")

                            if (cleanQuote.length <= 45) {
                                return@withContext cleanQuote
                            }
                        }
                    }
                }

                delay(300)

            } catch (e: Exception) {
                // В случае ошибки продолжаем цикл
            }
        }

        val defaultQuotes = listOf(
            "Тише едешь — дальше будешь"
        )
        defaultQuotes.random()
    }
}

@Preview(showBackground = false)
@Composable
fun TapCounterPreview() {
    Clicker3Theme {
        TapCounter(
            count = 42,
            onIncrement = { }
        )
    }
}

@Preview(name = "Светлая тема", showBackground = false)
@Composable
fun TapCounterLightPreview() {
    Clicker3Theme(darkTheme = false) {
        TapCounter(
            count = 42,
            onIncrement = { }
        )
    }
}

@Preview(name = "Темная тема", showBackground = false, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TapCounterDarkPreview() {
    Clicker3Theme(darkTheme = true) {
        TapCounter(
            count = 42,
            onIncrement = { }
        )
    }
}
