package com.example.service

import com.example.models.dto.OrderDTO
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

class PdfExportService {
    fun createPdf(order: OrderDTO): ByteArray = order.toHtml().toPdf()

    fun OrderDTO.toHtml(): String {
        val itemsHtml = items.mapIndexed { number, it ->
            """
            <tr>
            <td>${number + 1}</td>
            <td>${it.type.name}</td>
            <td>${it.price} Kr</td>
            <td>${it.amount} Kg</td>
            <td>${String.format("%.2f", (it.amount * it.price))} Kr</td>
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
<h3 style="color: #444445">Order ID: $id</h3>
<p><b>Date:</b> ${dateCreated.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))}</p>
<p><b>User:</b> ${user.username}</p>
<p><b>Seller:</b> ${seller.name}</p>
<div style="padding: 50px 0 50px 0;">
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Item</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
        $itemsHtml
        </tbody>
    </table>
</div>

<div style="float:right">
    <p><b>Total Amount:</b> ${String.format("%.2f", totalAmount)} Kg</p>
    <p><b>Total Price:</b> ${String.format("%.2f", totalPrice)} Kr</p>
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