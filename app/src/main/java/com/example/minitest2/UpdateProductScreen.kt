package com.example.minitest2

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.UUID

//name: String?, type: String?, price: String?, linkImage: String?
//
@Composable
fun UpdateProductScreen(
    navController: NavController,
    productId: String?,
    productName: String?,
    productType: String?,
    productPrice: String?,
    productImageLink: String?,
) {
    //val productId = remember { mutableStateOf(productId) }
    val productName = remember { mutableStateOf(productName) }
    val productType = remember { mutableStateOf(productType) }
    val productPrice = remember { mutableStateOf(productPrice) }
    val productImageLink = remember { mutableStateOf(productImageLink) }
    Column {
        Text(
            text = "Cap nhat san pham",
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productName.value.toString(),
            onValueChange = {
                productName.value = it
            },
            placeholder = { Text(text = "Nhap ten san pham") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Blue,
                unfocusedPlaceholderColor = Color.Black,
            ),
            singleLine = true,
        )
        OutlinedTextField(
            value = productType.value.toString(),
            onValueChange = { productType.value = it },
            placeholder = { Text(text = "Nhap loai san pham") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Red,
                unfocusedPlaceholderColor = Color.Blue,
            ),
            singleLine = true,
        )
        OutlinedTextField(
            value = productPrice.value.toString(),
            onValueChange = { productPrice.value = it },
            placeholder = { Text(text = "Nhap gia san pham") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Red,
                unfocusedPlaceholderColor = Color.Blue,
            ),
            singleLine = true,
        )
        OutlinedTextField(
            value = productImageLink.value.toString(),
            onValueChange = { productImageLink.value = it },
            placeholder = { Text(text = "Nhap link anh san pham") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Red,
                unfocusedPlaceholderColor = Color.Blue,
            ),
            singleLine = true,
        )

        val context = LocalContext.current
        Button(
            onClick = {
                if (TextUtils.isEmpty(productName.value)) {
                    Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
                    //showToastMessage(message = "Please enter product name")
                } else if (TextUtils.isEmpty(productType.value)) {
                    Toast.makeText(context, "Please enter product type", Toast.LENGTH_SHORT)
                        .show()
                } else if (TextUtils.isEmpty(productPrice.value)) {
                    Toast.makeText(context, "Please enter product price", Toast.LENGTH_SHORT)
                        .show()

                } else if (TextUtils.isEmpty(productImageLink.value)) {
                    Toast.makeText(context, "Please enter product image link", Toast.LENGTH_SHORT)
                        .show()

                } else {

                    updateProductToFirestore(
                        Product(
                            productId,
                            productName.value,
                            productType.value,
                            productPrice.value,
                            productImageLink.value
                        ), context
                    )
                    updateProductToFirestore( productId, productName.value, productType.value, productPrice.value, productImageLink.value, context)
                    navController.navigate(Screen.ViewCategory.route)
                }

            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Cap Nhat San Pham")
        }
    }
}

//fun addProductToFirestore(product: Product, context: Context) {
//    val db = Firebase.firestore
//    db.collection("Products")
//        .add(product)
//        .addOnSuccessListener { documentReference ->
//            Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
//        }
//        .addOnFailureListener { e ->
//            Toast.makeText(context, "Error adding product", Toast.LENGTH_SHORT).show()
//        }
//}


fun updateProductToFirestore(product: Product, context: Context) {
    val db = Firebase.firestore

    db.collection("Products")
        .document(product.productId.toString())
        .set(product)
        .addOnSuccessListener {
            Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()

    }
}

fun updateProductToFirestore(id: String?,
                             name: String?,
                             type: String?,
                             price: String?,
                             linkImage: String?,
                             context: Context,) {

    val db = Firebase.firestore
    val updateProduct = Product(id, name, type, price, linkImage)
    db.collection("Products")
        .document(id.toString())
        .set(updateProduct)
        .addOnSuccessListener {
            Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
        }
}
