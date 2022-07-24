package chowk.packag.com.onlinestorekotlin

class Product {


    var id: Int
    var name: String
    var price: Int
    var pictureName: String
    // we haven't mentioned brand as the user has already chosen the brand and that itself will be used

    constructor(id: Int, name: String, price: Int,
                picture: String) {

        this.id = id
        this.name = name
        this.price = price
        this.pictureName = picture

        // we have to use 'this' keyword as the name of the variable and parameter is same
    }


}