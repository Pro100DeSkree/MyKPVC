package com.deskree.mykpvc.requests.urls

object urls : UrlProvider{
    // Login
    override val URL_COOKIE = "https://api.college.ks.ua/sanctum/csrf-cookie"
    override val URL_LOGIN = "https://api.college.ks.ua/api/login"
    // Profile
    override val URL_PROFILE = "https://api.college.ks.ua/api/users/profile/my"
    // Changes
    override val URL_SCHE_CHANGES = "https://api.college.ks.ua/api/lessons/shedule/replacements:%s"
    override val URL_CHECK_REP = "https://api.college.ks.ua/api/lessons/shedule/replacements/checkrep"
    // Schedule
    override val URL_SCHEDULE = ""
    // Teachers
    override val COLLEGE_TEACHERS = "https://api.college.ks.ua/api/teachers"
    override val MY_TEACHERS = "$COLLEGE_TEACHERS/my"
    override val TEACHER_INFO = "$COLLEGE_TEACHERS/%s"
    override val TEACHER_AVATAR = "$COLLEGE_TEACHERS/%s/avatar"
    // journal
    override val JOURNALS = "https://api.college.ks.ua/api/journals"
    override val CONDUCTED_DISCIPLINE_CLASSES = "https://api.college.ks.ua/api/journals/%s"
    override val GRADES_DISCIPLINE = "https://api.college.ks.ua/api/journals/%s/marks"
}
