package com.studio.timeclock3.calendar

import java.util.*


fun Calendar.isSameDay(newDate: Calendar): Boolean =
        this.get(Calendar.DAY_OF_MONTH) == newDate.get(Calendar.DAY_OF_MONTH)