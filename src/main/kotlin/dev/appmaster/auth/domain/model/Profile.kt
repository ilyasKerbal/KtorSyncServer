package dev.appmaster.auth.domain.model

import dev.appmaster.auth.data.entity.EntityUser
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Profile(
    val name: String,
    val email: String,
    val signupDate: LocalDateTime
) {
   companion object {
       fun fromUserEntity(entity: EntityUser) = Profile(
           name = entity.name,
           email = entity.email,
           signupDate = jodaToLocalDateTime(entity.createDate)
       )

       fun jodaToLocalDateTime(joda: DateTime): LocalDateTime {
           return LocalDateTime(
               year = joda.year,
               monthNumber = joda.monthOfYear,
               dayOfMonth = joda.dayOfMonth,
               hour = joda.hourOfDay,
               minute = joda.minuteOfHour,
               second = joda.secondOfMinute,
               nanosecond = 0
           )
       }
   }
}


