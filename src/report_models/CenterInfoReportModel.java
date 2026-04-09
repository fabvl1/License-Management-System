package report_models;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import model.Center;

import java.io.FileOutputStream;

public class CenterInfoReportModel {
    public static void saveCenterToPdf(Center center, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new com.itextpdf.text.Paragraph("License Management Center Information Report"));
            document.add(new Paragraph("Center Code: " + center.getCenterCode()));
            document.add(new Paragraph("Center Name: " + center.getCenterName()));
            document.add(new Paragraph("Postal Address: " + center.getPostalAddress()));
            document.add(new Paragraph("Phone Number: " + center.getPhoneNumber()));
            document.add(new Paragraph("General Director: " + center.getGeneralDirector()));
            document.add(new Paragraph("HR Manager: " + center.getHrManager()));
            document.add(new Paragraph("Accounting Manager: " + center.getAccountingManager()));
            document.add(new Paragraph("Union Secretary: " + center.getUnionSecretary()));
            document.add(new Paragraph("Contact Email: " + center.getContactEmail()));
            if (center.getLogo() != null && center.getLogo().length > 0) {
                com.itextpdf.text.Image logoImage = com.itextpdf.text.Image.getInstance(center.getLogo());
                logoImage.scaleToFit(120, 120);
                document.add(logoImage);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
