package com.example.service

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.streams.*

class EmailService(private val configurationService: ConfigurationService, private val http: HttpClient) {
    private val apiKey =
        "20818331D9DB829E6332E1B09749467D2B3EBFDBFA48048048C204E7DFA36B8F3A6C40B6E5A1FCB7D296398080CA9B49"  // Replace with your API key

    suspend fun sendEmail(to: String, file: ByteArray, fileName: String, date: String): HttpResponse =
        http.submitFormWithBinaryData(
            url = configurationService.emailApi,
            formData = formData {
                append("apikey", apiKey)
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
