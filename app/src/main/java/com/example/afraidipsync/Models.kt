package com.example.afraidipsync

data class DomainStatus(
    val external_ip: String? = null,
    val dns_ip: String? = null,
    val status: String? = null,
    val status_class: String? = null,
    val last_check: String? = null
)

typealias StatusResponse = Map<String, DomainStatus>
