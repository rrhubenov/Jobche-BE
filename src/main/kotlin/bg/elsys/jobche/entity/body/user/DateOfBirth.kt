package bg.elsys.jobche.entity.body.user

data class DateOfBirth(val day: Int, val month: Int, val year: Int) {
    override fun toString(): String {
        return "$day-$month-$year"
    }
}

