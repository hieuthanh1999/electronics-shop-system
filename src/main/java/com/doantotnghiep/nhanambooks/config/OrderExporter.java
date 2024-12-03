package com.doantotnghiep.nhanambooks.config;

import com.doantotnghiep.nhanambooks.model.Order;
import com.doantotnghiep.nhanambooks.model.OrderDetail;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
public class OrderExporter {
    private Order order;
    public static final String FONT = new File("src/main/resources/arial/arial.ttf").getAbsolutePath();

    public final BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
    public OrderExporter(Order order) throws IOException {
        this.order = order;
    }

    private void writeTableOrder(PdfPTable table) throws IOException {
        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(Color.RED);
        cellHeader.setPadding(7);

        Font font = new Font(baseFont);
        font.setColor(Color.WHITE);

        Font fontData = new Font(baseFont);

        cellHeader.setColspan(2);
        cellHeader.setPhrase(new Phrase("Họ tên người nhận",font));
        table.addCell(cellHeader);
        PdfPCell cellData = new PdfPCell();
        cellData.setColspan(4);
        cellData.setPhrase(new Phrase(order.getFullname(),fontData));
        table.addCell(cellData);
        cellHeader.setPhrase(new Phrase("Địa chỉ",font));
        table.addCell(cellHeader);
        cellData.setPhrase(new Phrase(order.getAddress(),fontData));
        table.addCell(cellData);
        cellHeader.setPhrase(new Phrase("Số điện thoại",font));
        table.addCell(cellHeader);
        cellData.setPhrase(new Phrase(order.getPhone()));
        table.addCell(cellData);
    }

    private void writeOrderItemTable(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = new Font(baseFont);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Tên", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tác giả", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Giá", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Số lượng", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Thành tiền", font));
        table.addCell(cell);
        font.setColor(Color.BLACK);
        int total = 0;
        int discount = 0;
        for (OrderDetail item : order.getOrderDetails()) {
            table.addCell(new Phrase(item.getProduct().getName(),font));
            table.addCell(new Phrase(item.getProduct().getAuthor(),font));
            table.addCell(new Phrase(String.valueOf(item.getProduct().getPrice() - item.getProduct().getDiscount()),font) );
            table.addCell(new Phrase(String.valueOf(item.getQuantity()),font));
            table.addCell(new Phrase(String.valueOf((item.getProduct().getPrice() - item.getProduct().getDiscount()) * item.getQuantity()),font));
            total += (item.getProduct().getPrice() - item.getProduct().getDiscount()) * item.getQuantity();
        }
        if(order.getDiscount() != null){
            total -= order.getDiscount().getDiscount();
            discount = order.getDiscount().getDiscount();
        }
        PdfPCell lastCell = new PdfPCell();
        lastCell.setColspan(4);
        lastCell.setPhrase(new Phrase("Giảm :",font));
        table.addCell(lastCell);
        table.addCell(new Phrase(String.valueOf(discount),font));
        lastCell.setPhrase(new Phrase("Số tiền phải thanh toán"));
        table.addCell(lastCell);
        table.addCell(new Phrase(String.valueOf(total),font));
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        Font fontIntro = new Font(baseFont);
        fontIntro.setStyle(Font.BOLD);
        fontIntro.setSize(18);
        String textOpen = "Cảm ơn bạn đã mua tại cửa hàng chúng tôi. Thông tin đơn hàng của bạn như sau:";
        Paragraph openning = new Paragraph(textOpen,fontIntro);
        openning.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(openning);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        writeTableOrder(table);
        document.add(table);
        PdfPTable tableDetail = new PdfPTable(5);
        tableDetail.setWidthPercentage(100f);
        tableDetail.setSpacingBefore(10);
        writeOrderItemTable(tableDetail);
        document.add(tableDetail);
        document.close();
    }
}
