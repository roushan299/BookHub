package activity

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import com.roushan.bookhub.R
import fragment.AboutFragment
import fragment.DashboardFragment
import fragment.FavouritesFragment
import fragment.ProfileFragment


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView

    private var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout= findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigatorView)

        setUpToolBar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

         navigationView.setNavigationItemSelectedListener {

             if(previousMenuItem != null){
                 previousMenuItem?.isChecked = false
             }

             it.isCheckable = true
             it.isChecked = true
             previousMenuItem = it

             when(it.itemId){
                 R.id.dashboard -> {
                     openDashboard()
                     drawerLayout.closeDrawers()
                     }
                 R.id.favourite -> {
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.frame, FavouritesFragment())
                         .commit()
                        supportActionBar?.title="Favourite"
                     drawerLayout.closeDrawers()
                }
                 R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()
                     supportActionBar?.title="Profile"
                     drawerLayout.closeDrawers()
                }
                 R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutFragment())
                        .commit()
                     supportActionBar?.title="About App"
                     drawerLayout.closeDrawers()
                }
             }

             return@setNavigationItemSelectedListener true
         }


    }

    private fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title= "Tool Bar Tittle"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDashboard(){
        val fragment = DashboardFragment()
        val transcation = supportFragmentManager.beginTransaction()
        transcation.replace(R.id.frame,fragment)
        transcation.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
            val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }
    }

}

