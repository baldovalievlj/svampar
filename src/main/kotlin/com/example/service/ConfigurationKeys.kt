package com.example.service

import com.example.models.Configuration
import com.example.models.ConfigurationType

object ConfigurationKeys {

    val EMAIL_TEMPLATE = Configuration(
        key = "email_template",
        value = "<p>Hello,<br/><br/>Here is your order.<br/><br/>Regards.</p>",
        type = ConfigurationType.RICH_TEXT
    )

    val EMAIL_SUBJECT = Configuration(
        key = "email_subject",
        value = "Invoice",
        type = ConfigurationType.TEXT
    )

    val EMAIL_FROM = Configuration(
        key = "email_from",
        value = "admin@svenskasvampar.se",
        type = ConfigurationType.TEXT
    )

    val EMAIL_FROM_NAME = Configuration(
        key = "email_from_name",
        value = "Svenska Svampar",
        type = ConfigurationType.TEXT
    )

    val EMAIL_API = Configuration(
        key = "email_api",
        value = "https://api.elasticemail.com/v2/email/send",
        type = ConfigurationType.TEXT
    )
}