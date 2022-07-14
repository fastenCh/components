package com.ch.core.ktx

fun String.equalsDate(str: String): Boolean = this.toDate().equals(str.toDate())

fun String.beforeDate(str: String): Boolean = this.toDate().before(str.toDate())

fun String.afterDate(str: String): Boolean = this.toDate().after(str.toDate())