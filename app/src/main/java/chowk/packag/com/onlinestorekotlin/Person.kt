package chowk.packag.com.onlinestorekotlin

class Person { // this class is created to keep a track of the user who has just logged in or signed up
    // see SignUpLayout.kt and MainActivity.kt
    // we use -> Person.email = activity_main_edtEmail.text.toString()

    companion object { // In kotlin, in order to create a class variable we use companion object
        var email: String = ""
        var addToCartProductID = 0
    }


}