package com.deskree.mykpvc.requests

object urls {
    // Login
    const val URL_COOKIE = "https://api.college.ks.ua/sanctum/csrf-cookie"
    const val URL_LOGIN = "http://api.college.ks.ua/api/login"
    // Profile
    const val URL_PROFILE = "http://api.college.ks.ua/api/users/profile/my"
    // Changes
    const val URL_SCHE_CHANGES = "https://api.college.ks.ua/api/lessons/shedule/replacements:%s"
    const val URL_CHECK_REP = "https://api.college.ks.ua/api/lessons/shedule/replacements/checkrep"
    // Schedule
    const val URL_SCHEDULE = ""
    // Teachers
    const val COLLEGE_TEACHERS = "https://api.college.ks.ua/api/teachers"
    const val MY_TEACHERS = "$COLLEGE_TEACHERS/my"
    const val TEACHER_INFO = "$COLLEGE_TEACHERS/%s"
    const val TEACHER_AVATAR = "$COLLEGE_TEACHERS/%s/avatar"
    // journal
    const val JOURNALS = "https://api.college.ks.ua/api/journals"
    const val CONDUCTED_DISCIPLINE_CLASSES = "https://api.college.ks.ua/api/journals/%s"
    const val GRADES_DISCIPLINE = "https://api.college.ks.ua/api/journals/%s/marks"

}