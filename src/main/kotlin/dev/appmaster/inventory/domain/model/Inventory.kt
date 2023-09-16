package dev.appmaster.inventory.domain.model

import dev.appmaster.inventory.data.entity.EntityItem
import dev.appmaster.inventory.external.request.InventoryRequest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class Inventory(
    var id: String? = null,
    var title: String,
    var description: String,
    var imageTag: String? = null,
    var barCode: String? = null,
    var lowStockAlert: Boolean,
    var lowStock: Int? = null,
    var expiryDateAlert: Boolean,
    var expiryDate: LocalDate? = null
){
    companion object {
        fun fromItemEntity(entityItem: EntityItem) = Inventory(
            id = entityItem.id.value.toString(),
            title = entityItem.title,
            description = entityItem.description,
            imageTag = entityItem.imageTag,
            barCode = entityItem.barcode,
            lowStockAlert = entityItem.lowStockAlert,
            lowStock = entityItem.lowStock,
            expiryDateAlert = entityItem.expiryDateAlert,
            expiryDate = entityItem.expiryDate?.let { jodaDateTime2KotlinxDate(it) }
        )

        fun fromInventoryRequest(request: InventoryRequest) = Inventory(
            title = request.title,
            description = request.description,
            barCode = request.barCode,
            lowStockAlert = request.lowStockAlert,
            lowStock = request.lowStock,
            expiryDateAlert = request.expiryDateAlert,
            expiryDate = requestDateToLocalDate(request.expiryYear, request.expiryMonth, request.expiryDay)
        )

        fun fromLocalDateToJoda(localDate: LocalDate?): DateTime? = localDate?.let {
            DateTime(
                localDate.year, localDate.monthNumber, localDate.dayOfMonth, 0, 0
            )
        }

        private fun jodaDateTime2KotlinxDate(date: DateTime): LocalDate = LocalDate(
            year = date.year,
            monthNumber = date.monthOfYear,
            dayOfMonth = date.dayOfMonth
        )

        private fun requestDateToLocalDate(year: Int?, month: Int?, day: Int?): LocalDate? {
            if (year == null || month == null || day == null) return null
            return LocalDate(
                year = year,
                monthNumber = month,
                dayOfMonth = day
            )
        }
    }
}
