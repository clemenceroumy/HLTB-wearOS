import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.soywiz.klock.Time
import com.soywiz.klock.hours
import com.soywiz.klock.minutes
import com.soywiz.klock.plus
import com.soywiz.klock.seconds

data class SubmitRequest(
    val submissionId: Int,
    val userId: Int,
    val gameId: Int,
    val title: String,
    val platform: String,
    val storefront: String,
    val lists: Lists,
    val general: General
)

data class Lists(
    val playing: Boolean = true,
    val backlog: Boolean = false,
    val replay: Boolean = false,
    val custom: Boolean = false,
    val custom2: Boolean = false,
    val custom3: Boolean = false,
    val completed: Boolean = false,
    val retired: Boolean = false
)

data class General(
    val progress: Progress,
    val progressBefore: Progress,
)

data class Progress(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {
    fun toTime(): Time = Time(hours, minutes, seconds)

    companion object {
        fun fromTime(time: Time): Progress = Progress(time.hour, time.minute, time.second)

        fun progressTime(time: Time, addedTime: Time): Progress {
            val totalTime = time.plus(addedTime.hour.hours + addedTime.minute.minutes + addedTime.second.seconds)
            return Progress(totalTime.hour, totalTime.minute, totalTime.second)
        }
    }
}