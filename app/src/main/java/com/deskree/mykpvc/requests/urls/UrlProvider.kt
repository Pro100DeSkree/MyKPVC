package com.deskree.mykpvc.requests.urls

interface UrlProvider {
    // Login
    val URL_COOKIE: String
    val URL_LOGIN: String
    // Profile
    val URL_PROFILE: String
    // Changes
    val URL_SCHE_CHANGES: String
    val URL_CHECK_REP: String
    // Schedule
    val URL_SCHEDULE: String
    // Teachers
    val COLLEGE_TEACHERS: String
    val MY_TEACHERS: String
    val TEACHER_INFO: String
    val TEACHER_AVATAR: String
    // journal
    val JOURNALS: String
    val CONDUCTED_DISCIPLINE_CLASSES: String
    val GRADES_DISCIPLINE: String
}