package report_models;

import java.util.List;

import visual.Reports.ConsolidatedInfractionsReport.ConsolidatedRow;

public class ConsolidatedInfractionsReportModel {

    public static void saveConsolidatedInfractions(List<ConsolidatedRow> rows, java.io.File file) throws Exception {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("No data to save in the report.");
        }

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
            document.open();
            document.add(new com.itextpdf.text.Paragraph("Consolidated Infractions by Type in Year"));
            document.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            String[] headers = {"Year", "Infraction Type", "Infractions Count", "Total Points Deducted", "Total Paid Fines", "Total Pending Fines"};
            for (String h : headers) {
                pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(h)));
            }
            for (ConsolidatedRow row : rows) {
                pdfTable.addCell(String.valueOf(row.getYear()));
                pdfTable.addCell(capitalize(row.getInfractionType()));
                pdfTable.addCell(String.valueOf(row.getCount()));
                pdfTable.addCell(String.valueOf(row.getTotalPoints()));
                pdfTable.addCell(String.valueOf(row.getPaidCount()));
                pdfTable.addCell(String.valueOf(row.getPendingCount()));
            }
            document.add(pdfTable);
            document.close();
        } catch (com.itextpdf.text.DocumentException | java.io.IOException e) {
            throw new Exception("Error generating PDF report", e);
        }
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }
}