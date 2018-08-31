package aplicacionmovil.isavanzados.com.mx.mymaps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import aplicacionmovil.isavanzados.com.mx.mymaps.Fragments.MainFragment
import aplicacionmovil.isavanzados.com.mx.mymaps.Fragments.MapFragment

class MainActivity : AppCompatActivity() {

    var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            changeFragment(MainFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.menu_welcome -> {
                currentFragment = MainFragment()
            }

            R.id.menu_map -> {
                currentFragment = MapFragment()
            }

        }
        changeFragment(currentFragment!!)
        return super.onOptionsItemSelected(item)

    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

}
