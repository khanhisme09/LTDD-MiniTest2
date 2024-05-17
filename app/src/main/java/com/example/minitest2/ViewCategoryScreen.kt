package com.example.minitest2

import android.content.Context
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ViewCategoryScreen(navController: NavController) {
    val context = LocalContext.current

    val products = remember { mutableStateOf(listOf<Product>()) }

    LaunchedEffect(key1 = Unit) {
        getProductsFromFirestore(products)
    }

    //getProductsFromFirestore(products)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Button(onClick = { navController.navigate(Screen.Home.route) }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Way Back Home")
            }
        }
        items(products.value) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .height(100.dp)
                    .clickable { showProductInfo(item.productId, context) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .padding(end = 10.dp).border(1.dp, Color.Black)
                ) {
                    DisplayImageFromUrl(product = item)
                }

                Column(modifier = Modifier.weight(6f)) {
                    DisplayProductInfo(product = item)
                    //DisplayProductLinkImage(product = item)
                }
                Column(modifier = Modifier.weight(1f)) {
                    //
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            val encodedProductImageLink = item.productImageLink?.let {
                                URLEncoder.encode(
                                    it,
                                    StandardCharsets.UTF_8.toString()
                                )
                            }
                            navController.navigate(Screen.UpdateProduct.route + "/${item.productId}/${item.productName}/${item.productType}/${item.productPrice}/${encodedProductImageLink}")
                        })
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.clickable {
                            deleteProductFromFirestore(item.productId, context, products)
                        })
                }

            }
        }
    }

}

@Composable
fun CtgScreen() {
    val listCategories = fakeCategoryList()
    val products = remember { mutableStateOf(listOf<Product>()) }
    getProductsFromFirestore(products)

    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items(products.value) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .height(100.dp)
                    .border(1.dp, Color.Black),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .padding(end = 10.dp)
                        .border(1.dp, Color.Black)
                ) {
                    DisplayImageFromUrl(item)
                }

                Column(modifier = Modifier.weight(6f)) {

                    DisplayProductInfo(item)
                }

            }
        }
    }
}


fun fakeCategoryList(): List<Product> {
    val list = mutableListOf<Product>()
    for (i in 1..10) {
        val category = Product(
            productName = "Category $i",
            productType = "Type $i",
            productPrice = "Price $i",
            productImageLink = "Link Image $i"
        )
        list.add(category)
    }
    return list
}


@Composable
fun DisplayProductInfo(product: Product) {
    product.productName?.let { Text(text = "Ten sp: "+it) }
    product.productType?.let { Text(text = "Loai sp: "+it) }
    product.productPrice?.let { Text(text = "Gia sp: "+it) }
    //product.productImageLink?.let { Text(it) }
}


@Composable
fun DisplayImageFromUrl(product: Product) {
    product.productImageLink?.let {
        val painter = rememberAsyncImagePainter(model = it)
        Image(
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(100.dp)
        )
    }
}

fun getProductsFromFirestore(products: MutableState<List<Product>>) {
    val db = Firebase.firestore
    db.collection("Products")
        .get()
        .addOnSuccessListener { result ->
            val productList = result.map { document ->
                Product(
//                    productId = document.id,
                    productId = document.getString("productId"),
                    productName = document.getString("productName"),
                    productType = document.getString("productType"),
                    productPrice = document.getString("productPrice"),
                    productImageLink = document.getString("productImageLink")
                )
            }

            products.value = productList
        }
        .addOnFailureListener { exception ->
            Toast.makeText(null, "Failed to load data", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "Failed to load data: ", exception)
        }
}

fun deleteProductFromFirestore(
    productId: String?,
    context: Context,
    products: MutableState<List<Product>>,
) {
    val db = Firebase.firestore

    if (productId != null) {
        db.collection("Products")
            .document(productId.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                getProductsFromFirestore(products)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to delete product\n$e", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(context, "Product ID is null", Toast.LENGTH_SHORT).show()
    }
}

fun showProductInfo(productInfo: String?, context: Context) {
    if (productInfo != null) {
        Toast.makeText(context, "Product ID: $productInfo", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Product ID is null", Toast.LENGTH_SHORT).show()
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewViewCategoryScreen() {
    ViewCategoryScreen(navController = rememberNavController())
}

