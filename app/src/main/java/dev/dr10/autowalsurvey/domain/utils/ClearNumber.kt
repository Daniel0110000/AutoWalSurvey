package dev.dr10.autowalsurvey.domain.utils

fun String.clearNumber(): String = this.replace("%", "").replace("\"", "")