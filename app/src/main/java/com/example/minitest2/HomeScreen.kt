package com.example.minitest2

import android.service.controls.ControlsProviderService.TAG
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.content.Context

//import com.google.api.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val productId = remember { mutableStateOf("") }
    val productName = remember { mutableStateOf("") }
    val productType = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("") }
    val productImageLink = remember { mutableStateOf("") }
//    var productName by remember { mutableStateOf("") }
//    var productType by remember { mutableStateOf("") }
//    var productPrice by remember { mutableStateOf("") }
//    var productLinkImage by remember { mutableStateOf("") }


    Column(modifier = Modifier.background(Color.White)) {
        Text(
            text = "Du lieu san pham",
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif
        )

        LazyColumn {
            item {
                InputValueField(
                    value = productName.value,
                    placeholder = "Nhap ten san pham",
                    onValueChange = { productName.value = it })
                InputValueField(
                    value = productType.value,
                    placeholder = "Nhap loai san pham",
                    onValueChange = { productType.value = it })
                InputValueField(
                    value = productPrice.value,
                    placeholder = "Nhap gia san pham",
                    onValueChange = { productPrice.value = it })
                InputValueField(
                    value = productImageLink.value,
                    placeholder = "Nhap link anh san pham",
                    onValueChange = { productImageLink.value = it },
                )


//                InputField(
//                    value = productName,
//                    placeholder = "Nhap ten san pham",
//                    onValueChange = { productName = it }
//                )
//                InputField(
//                    value = productType,
//                    placeholder = "Nhap loai san pham",
//                    onValueChange = { productType = it }
//                )
//                InputField(
//                    value = productPrice,
//                    placeholder = "Nhap gia san pham",
//                    onValueChange = { productPrice = it }
//                )
//                InputField(
//                    value = productLinkImage,
//                    placeholder = "Nhap link anh san pham",
//                    onValueChange = { productLinkImage = it },
//                    imeAction = ImeAction.Done
//                )

//                Button(
//                    onClick = { }
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                ) {
//                    Text(text = "Them San Pham")
//                }
                val context = LocalContext.current
                Button(
                    onClick = {
                        if (TextUtils.isEmpty(productName.value)) {
                            Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT)
                                .show()
                            //showToastMessage(message = "Please enter product name")
                        } else if (TextUtils.isEmpty(productType.value)) {
                            Toast.makeText(context, "Please enter product type", Toast.LENGTH_SHORT)
                                .show()
                        } else if (TextUtils.isEmpty(productPrice.value)) {
                            Toast.makeText(
                                context,
                                "Please enter product price",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        } else if (TextUtils.isEmpty(productImageLink.value)) {
                            Toast.makeText(
                                context,
                                "Please enter product image link",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        } else {

                            productId.value = UUID.randomUUID().toString()
                            Log.d("productId", productId.value)
                            addProductToFirestore(
                                Product(
                                    productId.value,
                                    productName.value,
                                    productType.value,
                                    productPrice.value,
                                    productImageLink.value
                                ), context
                            )


                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Them San Pham")
                }

                Button(
                    onClick = { navController.navigate(Screen.ViewCategory.route) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Xem Danh Sach San Pham")
                }
            }
        }
        CtgScreen()
    }
}

@Composable
fun InputValueField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholder) },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Red,
            unfocusedPlaceholderColor = Color.Blue,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
    )
}


fun addProductToFirestore(product: Product, context: Context) {
    val db = Firebase.firestore
    db.collection("Products")
        .add(product)
        .addOnSuccessListener {
            Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to add product\n$e", Toast.LENGTH_SHORT).show()
        }
}



@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}

