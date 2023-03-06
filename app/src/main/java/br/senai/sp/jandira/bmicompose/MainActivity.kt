package br.senai.sp.jandira.bmicompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.bmicompose.ui.theme.BMIComposeTheme
import br.senai.sp.jandira.bmicompose.utils.bmiCalculate
import br.senai.sp.jandira.bmicompose.utils.getColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BMICalculator() {
    var weightText by rememberSaveable() {
        mutableStateOf("")
    }
    var heightText by rememberSaveable() {
        mutableStateOf("")
    }
    var expandState by remember {
        mutableStateOf(false)
    }
    var bmiScoreState by remember {
        mutableStateOf(0.0)
    }
    // objeto que controla a requisição de foco
    val weightFocus = remember { FocusRequester() }
    // variáveis booleanas de estado (observam e esperam uma mudança de forma dinâmica)
    var isWeightError by remember {
        mutableStateOf(false)
    }
    var isHeightError by remember {
        mutableStateOf(false)
    }
    // COMENTAR
    var bmiStatus by remember {
        mutableStateOf("")
    }
    if(bmiScoreState < 18.5)
        bmiStatus = "Under weight"
    else if (bmiScoreState >= 18.5 && bmiScoreState <= 24.9)
        bmiStatus = "Normal weight"
    else if (bmiScoreState >= 25 && bmiScoreState <= 29.9)
        bmiStatus = "Overweight"
    else if (bmiScoreState >= 30 && bmiScoreState <= 34.9)
        bmiStatus = "Obesity I"
    else if (bmiScoreState >= 35 && bmiScoreState <= 39.9)
        bmiStatus = "Obesity II"
    else
        bmiStatus = "Obesity III"
    // Content
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = "Application icon",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = stringResource(id = R.string.app_title),
                color = Color.Blue,
                fontSize = 26.sp,
                letterSpacing = 4.sp
            )
        }
        // Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.weight),
                modifier = Modifier.padding(8.dp),
                fontSize = 18.sp
            )
            OutlinedTextField(
                value = weightText,
                onValueChange = {
                    // retorna a quantidade de caracteres da string -1 (último carácter)
                    var lastChar = if (it.length == 0) {
                        // COMENTAR
                        isWeightError = true
                        it
                    } else {
                        it.get(it.length - 1)
                        // COMENTAR
                        isWeightError = false
                    }
                    // verifica se o último caracter digitado foi '.' ou ','
                    var newValue = if (lastChar == '.' || lastChar == ',')
                        // apaga o último caracter
                        it.dropLast(1)
                    else
                        it
                    // recebe o que está sendo digitado na caixa de texto e armazena em uma variável
                    weightText = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(weightFocus),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LineWeight, contentDescription = "")
                },
                trailingIcon = {
                    // COMENTAR
                    if (isWeightError)
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                },
                // indica que a caixa de texto está vazia deixando ela com as bordas vermelhas
                isError = isWeightError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            // C0MENTAR
            if (isWeightError) {
                Text(
                    text = stringResource(id = R.string.weight_error),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.height),
                modifier = Modifier.padding(8.dp),
                fontSize = 18.sp
            )
            OutlinedTextField(
                value = heightText,
                onValueChange = {
                    // retorna a quantidade de caracteres da string -1 (último carácter)
                    var lastChar = if (it.length == 0) {
                        // COMENTAR
                        isHeightError = true
                        it
                    } else {
                        it.get(it.length - 1)
                        // COMENTAR
                        isHeightError = false
                    }
                    // apaga o último caracter se ele for '.'
                    var newValue = if (lastChar == '.' || lastChar == ',')
                        it.dropLast(1)
                    else
                        it
                    // recebe o que está sendo digitado na caixa de texto e armazena em uma variável
                    heightText = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Height, contentDescription = "")
                },
                trailingIcon = {
                    // COMENTAR
                    if (isHeightError)
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "")
                },
                // indica que a caixa de texto está vazia deixando ela com as bordas vermelhas
                isError = isHeightError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            // C0MENTAR
            if (isHeightError) {
                Text(
                    text = stringResource(id = R.string.height_error),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }
            Button(
                onClick = {
                    // verifica se a quantidade de caracteres da varivável
                    isWeightError = weightText.length == 0
                    isHeightError = heightText.length == 0
                    // verifica se as duas caixas de texto não estão vazias
                    if (isWeightError == false && isHeightError == false) {
                        bmiScoreState = bmiCalculate(weightText.toInt(), heightText.toDouble())
                        expandState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 12.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(40, 150, 30))
            ) {
                Text(
                    text = stringResource(id = R.string.button),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
        // Footer
        AnimatedVisibility(
            visible = expandState,
            enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
            exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f),
                shape = RoundedCornerShape(32.dp, 32.dp),
                // COMENTAR
                backgroundColor = getColor(bmiScoreState)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.score),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("%.2f", bmiScoreState),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bmiStatus,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    //Spacer(modifier = Modifier.height(32.dp))
                    Row() {
                        Button(
                            onClick = {
                                expandState = false
                                weightText = ""
                                heightText = ""
                                weightFocus.requestFocus()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(119, 119, 199, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.reset),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(119, 119, 199, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.share),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun BMICalculatorPreview() {
    BMICalculator()
}
