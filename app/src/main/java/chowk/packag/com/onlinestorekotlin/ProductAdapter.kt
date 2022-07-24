package chowk.packag.com.onlinestorekotlin

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.e_product_row.view.*

// Adapter == Controller ( MVC Design Pattern )
class ProductAdapter(var context: Context, var arrayList: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
// This class inherits from RecyclerView.Adapter -- ArrayList inherits EProducts file which will hold our products


    // Since we're inheriting from the RecyclerView.Adapter class, the methods onCreateViewHolder, getItemCount, onBindViewHolder need to
    // be implemented otherwise it gives error


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder { // this will be called to create each item
     // when we scroll, the top most product vanishes and a new product comes in at the bottom -- this bottom product is created by this func

        val productView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false)
        // context is used so that we can use resources outside of this activity (eg from Main Activity)
        // parent - this is gonna hold our products

        return ProductViewHolder(productView)
        // the constructor of ProductViewHolder() will create an object and that object will be returned
    }

    override fun getItemCount(): Int { // Returns the total "number" of items in the data set held by the adapter

        return arrayList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    // This is called by recycler view to display the data at the specified position.
        // The method should update the contents of the "itemview" to reflect the item at a given position.

        (holder as ProductViewHolder).initializeRowUIComponents(arrayList.get(position).id, arrayList.get(position).name, arrayList.get(position).price, arrayList.get(position).pictureName)
        // holder parameter is downcasted to ProductViewHolder type
        // called initializeRowUIComponents func
    }


    inner class ProductViewHolder(pView: View)
        : RecyclerView.ViewHolder(pView) {


        // initialising UI Components of e_product_row.xml
        fun initializeRowUIComponents(id: Int, name: String, price: Int, picName: String) {

            // itemView is that particular e_product_row
            itemView.txtId.text = id.toString() // doing toString cuz we want in text format
            itemView.txtName.text = name
            itemView.txtPrice.text = price.toString()

            var picUrl = "http://192.168.1.124/OnlineStoreApp/osimages/"
            // this URL holds the images of all the products
            // basically this URL is generated from our NetBeans proj where osimages is a folder
            // inside our OnlineStoreApp folder

            picUrl = picUrl.replace(" ", "%20")
            // in URL we cant have spaces, will give error, hence if any image has a space bymistake
            // then we replace that space with %20 [ %20 means space in URL ]
            // hence we wont get any error

            Picasso.get().load(picUrl + picName).into(itemView.imgProduct)
            // picasso library is used to download image from server and put it inside the imageView


            itemView.imgAdd.setOnClickListener {
                //imgAdd is the id of the + sign at the rhs

                Person.addToCartProductID = id // assigning the id of the product that the user is currently purchasing
                // so that this productID can be added in our temp_order table
                // this adding process is being done in AmountFragment.kt

                var amountFragment = AmountFragment() // creating an object of AmountFragment class
                var fragmentManager = (itemView.context as Activity).fragmentManager
                amountFragment.show(fragmentManager, "TAG")



            }


        }



    }

}