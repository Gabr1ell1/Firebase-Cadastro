package com.example.appaula

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appaula.ui.theme.AppAulaTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAulaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppAula()
                }
            }
        }
    }
}

@Composable
fun AppAula() {
    Column( Modifier
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //CABEÇALHO
        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center

        ) {
            Text(text = "CADASTRO",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp
                )
            )
        }

    var Nome by remember { mutableStateOf("") }
    var Endereco by remember { mutableStateOf("") }
    var Bairro by remember { mutableStateOf("") }
    var Cep by remember { mutableStateOf("") }
    var Cidade by remember { mutableStateOf("") }
    var Estado by remember { mutableStateOf("") }

    val users = remember { mutableStateListOf<User>() }

    // Carrega os dados quando a tela é exibida
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                users.clear()
                for (document in querySnapshot) {
                    val user = document.toObject(User::class.java).copy(id = document.id)
                    users.add(user)
                }
            }
            .addOnFailureListener { exception ->
                // Tratar erro
                Log.w("Firestore", "Error getting documents", exception)
            }
    }
        
        Row(
            Modifier.fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Nome,
                onValueChange = { Nome = it },
                label = { Text("Nome Completo") })
        }

        Row(Modifier .fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Endereco,
                onValueChange = { Endereco = it },
                label = { Text("Endereço") })
        }
        Row(Modifier.fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Bairro, onValueChange = { Bairro = it }, label = { Text("Bairro") })
        }

        Row(Modifier .fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Cep, onValueChange = { Cep = it }, label = { Text("CEP") })
        }
        Row(Modifier .fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Cidade, onValueChange = { Cidade = it }, label = { Text("Cidade") })
        }

        Row(Modifier .fillMaxWidth().padding(top = 25.dp), Arrangement.Center
        ) {
            TextField(
                value = Estado, onValueChange = { Estado = it }, label = { Text("Estado") })
        }

        Row(Modifier .fillMaxWidth().padding(10.dp), Arrangement.Center
        ) {
            Column(Modifier.padding(top = 10.dp))
            {
                Button(
                    onClick = {
                        val db = Firebase.firestore
                        val user = hashMapOf(
                            "Nome" to Nome,
                            "Endereco" to Endereco,
                            "Bairro" to Bairro,
                            "Cep" to Cep,
                            "Cidade" to Cidade,
                            "Estado" to Estado,
                        )

                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener {
                                Nome = ""
                                Endereco = ""
                                Bairro = ""
                                Cep = ""
                                Cidade = ""
                                Estado = ""

                                // Recarrega a lista
                                db.collection("users").get()
                                    .addOnSuccessListener { querySnapshot ->
                                        users.clear()
                                        for (document in querySnapshot) {
                                            val user = document.toObject(User::class.java)
                                                .copy(id = document.id)
                                            users.add(user)
                                        }
                                    }
                            }
                            .addOnFailureListener { e ->
                                // erro
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black,
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                )
                {
                    Text("Cadastrar", fontSize = 20.sp)
                }
            }
        }
        Row() {
            Column() {
                // Lista de usuários
                LazyColumn {
                    items(users) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Nome: ${user.Nome}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(text = "Endereço: ${user.Endereco}")
                                Text(text = "Bairro: ${user.Bairro}")
                                Text(text = "CEP: ${user.Cep}")
                                Text(text = "Cidade: ${user.Cidade}")
                                Text(text = "Estado: ${user.Estado}")
                            }
                        }
                    }
                }

            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun appAulaPreview() {
    AppAulaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), // Definindo que a surface deve ocupar todo o tamanho disponível.
            color = MaterialTheme.colorScheme.background
        ) {
            AppAula()
        }
    }
}