package cours.intechinfo.com.tpkotlincoroutines

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val myAnnouncement = Announcement("Ceci est un announcement :)",
                10_000,
                "https://aae-fgi.org/sites/default/files/field/annonce/annonce_2.jpg",
                Color.parseColor("#00FF00"))
        val myAnnouncement2 = Announcement("Coucou", 2_000,
                "https://aae-fgi.org/sites/default/files/field/annonce/annonce_2.jpg")
        launchAnnouncement(myAnnouncement)
        launchAnnouncement(myAnnouncement2)

    }

    fun launchAnnouncement(announcement: Announcement) {
        launch(UI) {
            val announcementToDisplay = scheduleAnnouncement(announcement)
            announcementToDisplay.second?.let {
                imageView.setImageDrawable(it)
            } ?: return@launch
            textView.apply {
                text = announcement.bodyText
                setTextColor(announcement.textColor)
            }
        }
    }

    suspend fun scheduleAnnouncement(announcement: Announcement): Pair<Announcement, Drawable?> {
        val start = System.currentTimeMillis()
        var image: Drawable? = null
        try {
            image = async {
                Glide.with(applicationContext)
                        .load(announcement.imageUrl)
                        .into(500, 500)
                        .get()
            }.await()
        } catch (e: SecurityException) {

        }
        val delayRemaining = announcement.delayMs - (System.currentTimeMillis() - start)
        if(delayRemaining > 0) {
            delay(delayRemaining)
        }

        return Pair(announcement, image)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
