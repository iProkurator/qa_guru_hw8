import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import domain.Person;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileParsingTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void ZipXlsTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/sample.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();
            String EntryFileName = zipEntry.getName().toLowerCase();

            if (EntryFileName.endsWith(".xls")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    XLS xls = new XLS(is);
                    assertThat(xls.excel.getSheetAt(0).
                            getRow(10).
                            getCell(1).
                            getStringCellValue()).isEqualTo(("ООО \"Ромашка\""));
                }
            }
        }
    }

    @Test
    void ZipPdfTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/sample.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();
            String EntryFileName = zipEntry.getName().toLowerCase();

            if (EntryFileName.endsWith(".pdf")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    PDF pdf = new PDF(is);
                    assertThat(pdf.author).contains("Marc Philipp");
                    assertThat(pdf.numberOfPages).isEqualTo(166);
                }
            }
        }
    }

    @Test
    void ZipCsvTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/sample.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();
            String EntryFileName = zipEntry.getName().toLowerCase();

            if (EntryFileName.endsWith(".csv")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(is));
                    List<String[]> csvContent = csvReader.readAll();
                    assertThat(csvContent.get(1)[1]).isEqualTo("Pilatov");
                }
            }
        }
    }

    @Test
    void ZipJsonTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/sample.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();
            String EntryFileName = zipEntry.getName().toLowerCase();

            if (EntryFileName.endsWith(".json")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    Gson gson = new Gson();
                    String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Person jsonObject = gson.fromJson(json, Person.class);
                    assertThat(jsonObject.name).isEqualTo("Pavel");
                    assertThat(jsonObject.age).isEqualTo(39);
                    assertThat(jsonObject.address.city).isEqualTo("Khimki");
                    assertThat(jsonObject.favoriteLanguage.get(0)).isEqualTo("Java");
                }
            }
        }
    }


    @Test
    void ZipAllFilesTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File(classLoader.getResource("files/sample.zip").toURI()));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {

            ZipEntry zipEntry = entries.nextElement();
            String zipEntryFileName = zipEntry.getName().toLowerCase();

            // Работа с XLS
            if (zipEntryFileName.endsWith(".xls")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    XLS xls = new XLS(is);
                    assertThat(xls.excel.getSheetAt(0).
                            getRow(10).
                            getCell(1).
                            getStringCellValue()).isEqualTo(("ООО \"Ромашка\""));
                }
            }
            // Работа с CSV
            if (zipEntryFileName.endsWith(".csv")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(is));
                    List<String[]> csvContent = csvReader.readAll();
                    assertThat(csvContent.get(1)[0]).isEqualTo("Pavel");
                }
            }
            // Работа с PDF
            if (zipEntryFileName.endsWith(".pdf")) {
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    PDF pdf = new PDF(is);
                    assertThat(pdf.author).contains("Marc Philipp");
                    assertThat(pdf.numberOfPages).isEqualTo(166);
                }
            }
            // Работа с JSON
            if (zipEntryFileName.endsWith(".json")) {
                Gson gson = new Gson();
                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Person jsonObject = gson.fromJson(json, Person.class);
                    assertThat(jsonObject.name).isEqualTo("Pavel");
                    assertThat(jsonObject.age).isEqualTo(39);
                    assertThat(jsonObject.address.city).isEqualTo("Khimki");
                    assertThat(jsonObject.favoriteLanguage.get(0)).isEqualTo("Java");
                }
            }
        }
    }
}

