package report_models;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.License;
import model.Violation;

import java.io.FileOutputStream;
import java.util.List;

public class DriverInfoReportModel {
    public static void saveDriverInfoReport(
            String filePath,
            String fullName,
            String driverId,
            String address,
            String phone,
            String licenseStatus,
            List<License> licenses,
            List<Violation> violations
    ) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        document.add(new Paragraph("Driver Information Report", titleFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Name: " + fullName, normalFont));
        document.add(new Paragraph("ID Number: " + driverId, normalFont));
        document.add(new Paragraph("Address: " + address, normalFont));
        document.add(new Paragraph("Phone: " + phone, normalFont));
        document.add(new Paragraph("License Status: " + licenseStatus, normalFont));
        document.add(Chunk.NEWLINE);

        // Licenses section
        document.add(new Paragraph("Licenses issued:", sectionFont));
        PdfPTable licTable = new PdfPTable(3);
        licTable.setWidthPercentage(100);
        licTable.addCell("Type");
        licTable.addCell("Issued");
        licTable.addCell("Expires");
        for (License lic : licenses) {
            licTable.addCell(lic.getLicenseType());
            licTable.addCell(String.valueOf(lic.getIssueDate()));
            licTable.addCell(String.valueOf(lic.getExpirationDate()));
        }
        document.add(licTable);
        document.add(Chunk.NEWLINE);

        // Violations section
        document.add(new Paragraph("Registered violations:", sectionFont));
        PdfPTable vioTable = new PdfPTable(3);
        vioTable.setWidthPercentage(100);
        vioTable.addCell("Type");
        vioTable.addCell("Date");
        vioTable.addCell("Points");
        for (Violation v : violations) {
            vioTable.addCell(v.getViolationType());
            vioTable.addCell(String.valueOf(v.getDate()));
            vioTable.addCell(String.valueOf(v.getDeductedPoints()));
        }
        document.add(vioTable);

        document.close();
    }

    /*public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new java.io.File("DriverInfoReport.pdf"));
        int userSelection = fileChooser.showSaveDialog(dialog);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            try {
                exportToPDF(filePath, fullName, driverId, address, phone, licenseStatus, licenses, allViolations);
                JOptionPane.showMessageDialog(dialog, "Report exported successfully!", "Export PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Could not export PDF:\n" + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }*/
}

