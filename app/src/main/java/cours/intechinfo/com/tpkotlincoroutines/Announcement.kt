package cours.intechinfo.com.tpkotlincoroutines

import android.graphics.Color

data class Announcement(val bodyText: String,
                        val delayMs: Long,
                        val imageUrl: String,
                        val textColor: Int = Color.RED)