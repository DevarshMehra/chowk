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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
// this is basically the login activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // when we click on login button in login page
        activity_main_btnLogin.setOnClickListener {

            // connecting to our database with the help of below URL -- instead of localhost we put our pc's IP
            // inserting user credentials with the help of the URL
            val loginURL = "http://192.168.1.124/OnlineStoreApp/" +
                    "login_app_user.php?email=" +
                    activity_main_edtEmail.text.toString() +
                    "&pass=" + activity_main_edtPassword.text.toString()

            val requestQ = Volley.newRequestQueue(this@MainActivity)

            // StringRequest() can be used only with the help of Volley library
            val stringRequest = StringRequest(Request.Method.GET, loginURL, Response.Listener
            { response -> // response that we get from the URL


                if (response.equals("The user does exist")) {
                    // we must use the exact same response text that we've used in php code
                    // to compare equals() properly

                    // this is used to keep track of the user who has just logged in
                    Person.email = activity_main_edtEmail.text.toString()

                    // display a toast message -> the 'response' -> "The user does exist"
                    Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()

                    // using intent, start home screen activity
                    val homeIntent = Intent(this@MainActivity, HomeScreen::class.java)
                    startActivity(homeIntent)

                } else { // if user doesnt exist

                    val dialogBuilder= AlertDialog.Builder(this)
                    dialogBuilder.setTitle("Message")
                    dialogBuilder.setMessage(response)
                    dialogBuilder.create().show()

                }

            }, Response.ErrorListener { error -> // if some error comes and we dont get response from URL
                // errors can come as we're dealing with internet ( eg- network connection, etc.)

                val dialogBuilder= AlertDialog.Builder(this)
                dialogBuilder.setTitle("Message")
                dialogBuilder.setMessage(error.message)
                dialogBuilder.create().show()

            })

            requestQ.add(stringRequest)


        }


        activity_main_btnSignUp.setOnClickListener {
            // here when we click on sign up button on the main activity (login page),
            // it will start the sign up activity
            var signUpIntent = Intent(this@MainActivity, SignUpLayout::class.java)
            startActivity(signUpIntent)

        }
    }
}
