package com.example.service

import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.utils.io.streams.*

class EmailService(
    private val configurationService: ConfigurationService,
    private val http: HttpClient,
    private val config: ApplicationConfig
) {


    suspend fun sendEmail(to: String, file: ByteArray, fileName: String, date: String): HttpResponse =
        http.submitFormWithBinaryData(
            url = configurationService.emailApi,
            formData = formData {
                append("apikey", config.property("elastic.apiKey").getString())
                append("from", configurationService.emailFrom)
                append("fromName", configurationService.emailFromName)
                append("subject", "${configurationService.emailSubject} - $date")
                append("to", to)
                append("bodyHtml", configurationService.emailTemplate)
                append("isTransactional", "true")
                append(
                    "file",
                    InputProvider(file.size.toLong()) { file.inputStream().asInput() },
                    Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${fileName.quote()}")
                    }
                )
            }
        )
}
