package com.chuco.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.chuco.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface { QuickRegistrationScreen() }
            }
        }
    }
}

/* ---------------- Composable reutilizable ---------------- */
@Composable
fun LabelAndField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            leadingIcon = leadingIcon,
            isError = isError,
            supportingText = {
                if (supportingText != null) {
                    Text(supportingText)
                }
            },
//            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
//                capitalization = if (keyboardType == KeyboardType.Text)
//                    KeyboardCapitalization.Words else KeyboardCapitalization.None,
//                keyboardType = keyboardType,
//                imeAction = imeAction
//            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ---------------- Pantalla principal ---------------- */
@Composable
fun QuickRegistrationScreen() {
    // rememberSaveable → persiste en rotaciones
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var subscribe by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf<String?>(null) }

    // Validaciones simples
    val nameError = name.isNotBlank() && name.length < 3
    val emailError = email.isNotBlank() && !email.matches(Regex("^[\\w.+-]+@[\\w-]+\\.[\\w.-]{2,}$"))

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .fillMaxWidth(), // layout primero
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("📝", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.width(8.dp))
            Text("Registro rápido", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(Modifier.height(20.dp))

        LabelAndField(
            label = "NOMBRE",
            value = name,
            onValueChange = { name = it },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            isError = nameError,
            supportingText = if (nameError) "Mínimo 3 caracteres" else null,
            keyboardType = KeyboardType.Text
        )

        Spacer(Modifier.height(14.dp))

        LabelAndField(
            label = "CORREO",
            value = email,
            onValueChange = { email = it },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            isError = emailError,
            supportingText = if (emailError) "Correo no válido" else null,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📬 Suscribirme al boletín", modifier = Modifier.weight(1f))
            Switch(checked = subscribe, onCheckedChange = { subscribe = it })
        }

        Spacer(Modifier.height(20.dp))

        val formValid = name.isNotBlank() && !nameError && email.isNotBlank() && !emailError
        Button(
            onClick = {
                result = if (formValid) {
                    "✅ Registrado: $name — $email" + if (subscribe) " (con boletín)" else ""
                } else {
                    "❌ Completa el formulario correctamente."
                }
            },
            enabled = true,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth() // Layout → luego dibujo → luego interacción
                .height(52.dp)
        ) {
            Text("Registrar")
        }

        Spacer(Modifier.height(20.dp))

        Text("RESULTADO", style = MaterialTheme.typography.titleMedium)

        // Panel de resultado con orden de modifiers didáctico:
        // fillMaxWidth() → background() → border() → padding()
        Box(
            modifier = Modifier
                .fillMaxWidth() // LAYOUT
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(10.dp)) // DIBUJO
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp)) // DIBUJO
                .padding(16.dp) // INTERIOR al final
        ) {
            Text(text = result ?: "—")
        }
    }
}

/* ---------------- Previews ---------------- */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuickRegistrationPreview() {
    MyApplicationTheme { QuickRegistrationScreen() }
}
