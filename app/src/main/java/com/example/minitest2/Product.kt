package com.example.minitest2

import com.google.firebase.firestore.Exclude

data class Product(

    @Exclude var productId: String? = "",
    var productName: String? = "",
    var productType: String? = "",
    var productPrice: String? = "",
    var productImageLink: String? = ""
)
