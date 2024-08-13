package com.deskree.mykpvc.requests.urls

object urlsTest : UrlProvider{
    // Login
    override val URL_COOKIE = "https://kspk.pp.ua/sanctum/csrf-cookie"
    override val URL_LOGIN = "https://kspk.pp.ua/api/login"
    // Profile
    override val URL_PROFILE = "https://kspk.pp.ua/api/users/profile/my"
    // Changes
    override val URL_SCHE_CHANGES = "https://kspk.pp.ua/api/lessons/shedule/replacements:%s"
    override val URL_CHECK_REP = "https://kspk.pp.ua/api/lessons/shedule/replacements/checkrep"
    // Schedule
    override val URL_SCHEDULE = ""
    // Teachers
    override val COLLEGE_TEACHERS = "https://kspk.pp.ua/api/teachers"
    override val MY_TEACHERS = "$COLLEGE_TEACHERS/my"
    override val TEACHER_INFO = "$COLLEGE_TEACHERS/%s"
    override val TEACHER_AVATAR = "$COLLEGE_TEACHERS/%s/avatar"
    // journal
    override val JOURNALS = "https://kspk.pp.ua/api/journals"
    override val CONDUCTED_DISCIPLINE_CLASSES = "https://kspk.pp.ua/api/journals/%s"
    override val GRADES_DISCIPLINE = "https://kspk.pp.ua/api/journals/%s/marks"
}
