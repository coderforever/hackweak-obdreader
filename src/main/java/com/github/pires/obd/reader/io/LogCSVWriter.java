package com.github.pires.obd.reader.io;

import android.os.Environment;
import android.util.Log;

import com.github.pires.obd.enums.AvailableCommandNames;
import com.github.pires.obd.reader.net.ObdReading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by Max Christ on 14.07.15.
 */

public class LogCSVWriter {

    private BufferedWriter buf;

    public LogCSVWriter(String filename, String dirname) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + File.separator + dirname);
        dir.mkdirs();

        File file = new File(dir, filename);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos);
        this.buf = new BufferedWriter(osw);

    }

    public void closeLogCSVWriter() {
        try {
            buf.flush();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLineCSV(ObdReading reading) {
        String line="";

        line+=reading.getTimestamp()+",";
        line+=reading.getAltitude()+",";
        line+=reading.getLatitude()+",";
        line+=reading.getLongitude()+",";
        line+=reading.getVin()+",";
        line+=reading.getDirection()+",";

        Map<String, String> otherPairs=reading.getReadings();
        line+=otherPairs.get(AvailableCommandNames.SPEED.name())+",";
        line+=otherPairs.get(AvailableCommandNames.FUEL_ECONOMY.name())+",";

        addLine(line.substring(0, line.length() - 1));
    }


    private void addLine(String line) {
        if (line != null) {
            try {
                buf.write(line, 0, line.length());
                buf.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
