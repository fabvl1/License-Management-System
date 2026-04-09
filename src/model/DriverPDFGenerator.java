package model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.text.SimpleDateFormat;

public class DriverPDFGenerator {

    public static void createDriverPDF(Driver driver) {
        Document document = new Document();
        try {
            File outputDir = new File("reports");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            String outputPath = new File(outputDir, "driver_info.pdf").getPath();

            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            document.add(new Paragraph("Driver Information"));
            document.add(new Paragraph("------------------------"));
            document.add(new Paragraph("Driver ID: " + driver.getDriverId()));
            document.add(new Paragraph("First Name: " + driver.getFirstName()));
            document.add(new Paragraph("Last Name: " + driver.getLastName()));
            document.add(new Paragraph("Birth Date: " + sdf.format(driver.getBirthDate())));
            document.add(new Paragraph("Address: " + driver.getAddress()));
            document.add(new Paragraph("Phone Number: " + driver.getPhoneNumber()));
            document.add(new Paragraph("Email: " + driver.getEmail()));
            document.add(new Paragraph("License Status: " + driver.getLicenseStatus()));

            System.out.println("PDF creado correctamente en: " + outputPath);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
