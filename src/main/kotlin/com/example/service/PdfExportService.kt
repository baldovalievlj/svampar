package com.example.service

import com.example.models.dto.OrderDTO
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PdfExportService {
    fun createPdf(order: OrderDTO, zoneId: ZoneId): ByteArray = order.toHtml(zoneId).toPdf()

    fun OrderDTO.toHtml(zoneId: ZoneId): String {
        val itemsHtml = items.mapIndexed { number, it ->
            """
            <tr>
            <td>${number + 1}</td>
            <td>${it.type.name}</td>
            <td>${it.price} Kr</td>
            <td>${it.amount} Kg</td>
            <td>${String.format("%.2f", (it.amount.multiply(it.price)))} Kr</td>
            </tr>
        """
        }.joinToString("")

        return """
<!DOCTYPE html>
<html>
<head>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            padding-top: 50px;
            /*padding: 50px 0 20px 0;*/
        }
        th, td {
            text-align: left;
        }
        thead {
            border-bottom: 2px solid black;
        }
        body {
            padding: 10px;
            font-family: Arial,sans-serif;
        }
        .horizontal-line {
             width: 100%;
        }
    </style>
</head>
<body>
<h3 style="color: #444445">Inköp: #$id</h3>
<p><b>Inköpsdatum:</b> ${ZonedDateTime.parse(dateCreated).withZoneSameInstant(zoneId).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))}</p>
<p><b>Inköpare:</b> ${user.firstName} ${user.lastName}</p>
<p><b>Leverantör:</b> ${seller.id} ${seller.name}</p>
<div style="padding: 50px 0 50px 0;">
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Artikel</th>
                <th>Baspris</th>
                <th>Bruttovikt</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
        $itemsHtml
        </tbody>
    </table>
</div>

<div style="float:right">
    <p><b>Totalvikt:</b> ${String.format("%.2f", totalAmount)} Kg</p>
    <p><b>Totalbelopp:</b> ${String.format("%.2f", totalPrice)} Kr</p>
</div>

</body>
</html>
    """.trimIndent()
    }

    fun String.toPdf(): ByteArray {
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(this)
        renderer.layout()

        val os = ByteArrayOutputStream()
        renderer.createPDF(os)
        return os.toByteArray()
    }
}