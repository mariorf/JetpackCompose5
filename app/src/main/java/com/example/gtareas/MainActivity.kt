package com.example.gtareas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtareas.ui.theme.GTareasTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.MutableState
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar



//MutableState es para que se actualice en todos lados

data class Tarea(val nombre: String, var completada: MutableState<Boolean>, var prioridad: MutableState<String>, var fecha: MutableState<String>, var expanded: MutableState<Boolean>)

class MainActivity : ComponentActivity() {

    private val tareas = mutableStateListOf<Tarea>()

    private fun borrarTarea(tarea: Tarea) {
        tareas.remove(tarea)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GTareasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    // Enanitos y una lista
                    var nombreTarea by remember { mutableStateOf(TextFieldValue("")) }

                    val listaPrioridaad = listOf("Baja", "Media", "Alta", "Urgente")

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(38.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BasicTextField(
                                value = nombreTarea,
                                onValueChange = {
                                    // it (iterator) es lo que contiene en el momento
                                    nombreTarea = it
                                },
                                modifier = Modifier
                                    .border(1.dp, Color.Black)
                                    .fillMaxSize()
                                    .background(Color.White),
                                textStyle = TextStyle(color = Color.Black, fontSize = 36.sp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(38.dp),
                            horizontalArrangement = Arrangement.Center
                        ){

                            Button(
                                onClick = {

                                    //Agregar la tarea a la lista de tareas
                                    if (nombreTarea.text.isNotBlank()) {
                                        tareas.add(
                                            Tarea(
                                                nombreTarea.text,
                                                mutableStateOf(false),
                                                mutableStateOf("Baja"),
                                                mutableStateOf("--/--/--"),
                                                mutableStateOf(false)
                                            )
                                        )
                                        //Limpiar el campo de texto después de agregar la tarea
                                        nombreTarea = TextFieldValue("")
                                    }
                                }
                            ) {
                                Text("Añadir tarea")
                            }
                        }

                        // Lista de tareas dentro de un LazyColumn para que pueda scrollear
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(tareas) { tarea ->

                                var backgroundColor = Color.Green

                                if (tarea.prioridad.value.equals("Baja")) {
                                    backgroundColor = Color.Green
                                }
                                if (tarea.prioridad.value.equals("Media")) {
                                    backgroundColor = Color.Yellow
                                }
                                if (tarea.prioridad.value.equals("Alta")) {
                                    backgroundColor = Color.Red
                                }
                                if (tarea.prioridad.value.equals("Urgente")) {
                                    backgroundColor = Color(175, 38, 233, 255)
                                }

                                val context = LocalContext.current
                                val calendar = Calendar.getInstance()

                                //coge el actual gracias al objeto Calendar y sus funciones
                                val year = calendar[Calendar.YEAR]
                                val month = calendar[Calendar.MONTH]
                                val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

                                val datePicker = DatePickerDialog(
                                    context,
                                    //Pone la información seleccionada en el calendario dentro de en este caso tarea.fecha.value
                                    { DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                                        tarea.fecha.value = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                                    }, year, month, dayOfMonth
                                )
                                //DatePicker.mindate para que no sea menor a hoy
                                datePicker.datePicker.minDate = calendar.timeInMillis

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .background(backgroundColor)
                                        .clickable {

                                            datePicker.show()
                                        },

                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = tarea.nombre,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 24.sp,
                                    )

                                    Text(
                                        text = tarea.fecha.value
                                    )
                                    Checkbox(
                                        checked = tarea.completada.value,
                                        onCheckedChange = { isChecked ->
                                            tarea.completada.value = isChecked
                                        }
                                    )
                                    IconButton(
                                        onClick = {borrarTarea(tarea)}
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar tarea",
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(80.dp)
                                    ) {

                                        Text(
                                            text = tarea.prioridad.value,
                                            modifier = Modifier
                                                .padding(14.dp)
                                                .fillMaxHeight()
                                                .clickable {
                                                    tarea.expanded.value = true
                                                })

                                        DropdownMenu(
                                            expanded = tarea.expanded.value,

                                            //Si haces click fuera o tiras hacia atras
                                            onDismissRequest = { tarea.expanded.value = false }) {

                                            listaPrioridaad.forEach { itemPrioridad ->

                                                DropdownMenu(
                                                    expanded = tarea.expanded.value,
                                                    onDismissRequest = { tarea.expanded.value = false }
                                                ) {
                                                    listaPrioridaad.forEach { itemPrioridad ->
                                                        DropdownMenuItem(
                                                            {
                                                                Text(
                                                                    text = itemPrioridad,
                                                                    color = Color.Black
                                                                )
                                                            },
                                                            onClick = {
                                                                tarea.prioridad.value = itemPrioridad
                                                                tarea.expanded.value = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


