package com.example.test_2.screens.home





import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.graphics.toArgb

@Composable
fun DashboardScreen(openDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Text(
            text = "Dashboard de Vendas",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        FilterSection()

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            KpiCard("Vendas Hoje", "R$ 1.200,00", Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            KpiCard("Total Mês", "R$ 25.300,00", Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            KpiCard("Produtos Vendidos", "320", Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            KpiCard("Clientes Ativos", "150", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            ChartCard(Modifier.weight(1f)) { PieChartView(LocalContext.current) }
            Spacer(Modifier.width(8.dp))
            ChartCard(Modifier.weight(1f)) { LineChartView(LocalContext.current) }
        }
    }
}

@Composable
fun FilterSection() {
    var categoria by remember { mutableStateOf("") }
    var periodo by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = periodo,
            onValueChange = { periodo = it },
            label = { Text("Período") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = { /* Ação de filtro */ },
            modifier = Modifier.alignByBaseline()
        ) {
            Text("Filtrar")
        }
    }
}

@Composable
fun KpiCard(title: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ChartCard(modifier: Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.height(250.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
fun PieChartView(context: Context) {
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                val entries = listOf(
                    PieEntry(40f, "Categoria A"),
                    PieEntry(30f, "Categoria B"),
                    PieEntry(30f, "Categoria C")
                )

                val dataSet = PieDataSet(entries, "Vendas por Categoria").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                }

                data = PieData(dataSet)
                description.isEnabled = false
                setUsePercentValues(true)
                animateY(1000)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun LineChartView(context: Context) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                val entries = listOf(
                    Entry(1f, 200f),
                    Entry(2f, 400f),
                    Entry(3f, 300f),
                    Entry(4f, 500f),
                    Entry(5f, 600f)
                )

                val dataSet = LineDataSet(entries, "Vendas Diárias").apply {
                    color = primaryColor
                    setCircleColor(primaryColor)
                    lineWidth = 2f
                    circleRadius = 4f
                    valueTextSize = 10f
                }

                data = LineData(dataSet)
                description.isEnabled = false
                animateX(1000)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}