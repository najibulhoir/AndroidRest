package com.example.najib.restclient;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class MahasiswaXmlParser {
    // Kelas ini merepresentasikan satu data pada xml
    // Kelas ini mencakup tag nim, nama, id_jurusan, alamat
    public static class DataMahasiswa {
        private String nim;
        private String nama;
        private String id_jurusan;
        private String alamat;
        public DataMahasiswa(){};
        public DataMahasiswa(String nim, String nama, String id_jurusan, String alamat) {
            this.nim = nim;
            this.nama = nama;
            this.id_jurusan = id_jurusan;
            this.alamat = alamat;
        }
        public void setNim(String nim) {
            this.nim = nim;
        }
        public void setNama(String nama) {
            this.nama = nama;
        }
        public void setId_jurusan(String id_jurusan) {
            this.id_jurusan = id_jurusan;
        }
        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }
        public String getNim() {
            return nim;
        }
        public String getNama() {
            return nama;
        }
        public String getId_jurusan() {
            return id_jurusan;
        }
        public String getAlamat() {
            return alamat;
        }
    }
    public List<DataMahasiswa> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            // pada transaksi ini tidak menggunakan namespaces
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readXML(parser);
        } finally {
            in.close();
        }
    }
    private List<DataMahasiswa> readXML(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        List<DataMahasiswa> mahasiswaList = new ArrayList<DataMahasiswa>();
        //parser membaca apakah sekaranga berada pada tag xml
        parser.require(XmlPullParser.START_TAG, null, "xml");
        //jika berada pada tag xml maka akan dilanjutkan untuk membaca tag selanjutnya
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                mahasiswaList.add(readDataMahasiswa(parser));
            } else {
                skip(parser);
            }
        }
        return mahasiswaList;
    }
    private DataMahasiswa readDataMahasiswa(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        DataMahasiswa newDataMahasiswa = new DataMahasiswa();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("nim")) {
                newDataMahasiswa.setNim(readNim(parser));
            }
            else if (name.equals("nama")) {
                newDataMahasiswa.setNama(readNama(parser));
            }
            else if (name.equals("alamat")) {
                newDataMahasiswa.setAlamat(readAlamat(parser));
            }
            else if (name.equals("id_jurusan")) {
                newDataMahasiswa.setId_jurusan(readId_jurusan(parser));
            }
            else {
                skip(parser);
            }
        }
        return new DataMahasiswa(newDataMahasiswa.getNim(),
                newDataMahasiswa.getNama(),
                newDataMahasiswa.getId_jurusan(),newDataMahasiswa.getAlamat());
    }
    private String readNim(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "nim");
        String nim = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "nim");
        return nim;
    }
    private String readNama(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "nama");
        String nama = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "nama");
        return nama;
    }
    private String readId_jurusan(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "id_jurusan");
        String id_jurusan = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "id_jurusan");
        return id_jurusan;
    }
    private String readAlamat(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "alamat");
        String alamat = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "alamat");
        return alamat;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}