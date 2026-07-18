package digitalmidday;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import javax.swing.JTable;
import java.io.File;
import java.awt.Desktop;
public class PDFReport {

    public static void generateReport(JTable table, String schoolId) {

        try {
            Document doc = new Document();
            String filePath = System.getProperty("user.home") + "/Downloads/MiddayMealReport.pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            Image logo = Image.getInstance(
                PDFReport.class.getResource("/digitalmidday/logo.png")
            );
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_CENTER);
            doc.add(logo);
            
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("MIDDAY MEAL REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            Font normal = new Font(Font.FontFamily.HELVETICA, 12);
            Paragraph school = new Paragraph(
                "School ID: " + schoolId, normal
            );
            school.setAlignment(Element.ALIGN_CENTER);
            doc.add(school);

            doc.add(new Paragraph(" ")); 

            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(
                    new Phrase(table.getColumnName(i))
                );
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            for (int rows = 0; rows < table.getRowCount(); rows++) {
                for (int cols = 0; cols < table.getColumnCount(); cols++) {
                    pdfTable.addCell(
                        table.getValueAt(rows, cols).toString()
                    );
                }
            }

            doc.add(pdfTable);

            doc.close();

            System.out.println("PDF Generated Successfully");
            
            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
