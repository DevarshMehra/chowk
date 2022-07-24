package chowk.packag.com.onlinestorekotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.sign_up_layout.*

class SignUpLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_layout) // connct with xml file

        sign_up_layout_btnSignUp.setOnClickListener {

            // Register user only if password == confirm password
            if (sign_up_layout_edtPassword.text.toString().equals(
                            sign_up_layout_edtConfirmPassword.text.toString())) {

                // Registration Process

                // connecting to our database with the help of below URL -- instead of localhost we put our pc's IP
                // inserting new user data into the DB with the help of the URL
                val signUpURL = "http://192.168.1.124/OnlineStoreApp/join_new_user.php?email=" +
                        sign_up_layout_edtEmail.text.toString() +
                        "&username=" +
                        sign_up_layout_edtUsername.text.toString() +
                        "&pass=" + sign_up_layout_edtPassword.text.toString()

                val requestQ = Volley.newRequestQueue(this@SignUpLayout)

                // Volley library is req to be downloaded to use StringRequest()
                val stringRequest = StringRequest(Request.Method.GET, signUpURL, Response.Listener
                { response -> // this is the response that we get from the URL

                    if (response.equals("A user with this Email Address already exists")) {
                        // we have to use the exact same text message that we've used in our php code
                        // when No.OfRows != 0

                        val dialogBuilder= AlertDialog.Builder(this)
                        dialogBuilder.setTitle("Message")
                        dialogBuilder.setMessage(response)
                        dialogBuilder.create().show()


                    } else { // if user didnt previously exist then register user

//                        val dialogBuilder= AlertDialog.Builder(this)
//                        dialogBuilder.setTitle("Message")
//                        dialogBuilder.setMessage(response) // display the message that response holds
                        // "User has been registered succesfully"
//                        dialogBuilder.create().show()

                        // this is used to keep track of the user who has just signed up
                        Person.email = sign_up_layout_edtEmail.text.toString()

                        // display a toast message -> the 'response' -> "Congratulations, User has been
                        // registered succesfully"
                        Toast.makeText(this@SignUpLayout, response, Toast.LENGTH_SHORT).show()

                        // using intent, start home screen activity
                        // from sign up go straight to home screen ( no need to go to login )
                        val homeIntent = Intent(this@SignUpLayout, HomeScreen::class.java)
                        startActivity(homeIntent)

                    }

                }, Response.ErrorListener { error -> // since we're interacting with the internet,
                    // if any error comes then this message will be alerted instead of response

                    val dialogBuilder= AlertDialog.Builder(this)
                    dialogBuilder.setTitle("Message")
                    dialogBuilder.setMessage(error.message)
                    dialogBuilder.create().show()

                })

                requestQ.add(stringRequest) // we have to add stringRequest to requestQ

            } else { // Creating Alert Dialog box that the password != confirm password --> "Password Mismatch"

                val dialogBuilder= AlertDialog.Builder(this)
                dialogBuilder.setTitle("Message")
                dialogBuilder.setMessage("Password Mismatch")
                dialogBuilder.create().show()

            }


        }

        sign_up_layout_btnLogin.setOnClickListener {

            // when login button is clicked on the sign up page, we will finish signup activity
            // and hence signup activity will be removed from the top of the stack
            // and login page will come up
            finish()

        }

    }
}

// We have to keep up this way of naming => sign_up_layout_btnSignUp
// where we specify ids in such a way that we can come to know that button (or anything) is in
// which layout and in that layout what are referring too
// This helps in large scale projects
