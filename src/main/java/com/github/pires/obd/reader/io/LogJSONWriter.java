package com.github.pires.obd.reader.io;

import android.os.Environment;

import com.github.pires.obd.enums.AvailableCommandNames;
import com.github.pires.obd.reader.net.ObdReading;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by frank on 15/8/5.
 */
public class LogJSONWriter {
    private BufferedWriter buf;

    public LogJSONWriter(String filename, String dirname) {

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

    public void closeLogJSONWriter() {
        try {
            buf.flush();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLineJSON(ObdReading reading) {
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("timestamp",reading.getTimestamp());
            jsonObject.put("altitude", reading.getAltitude());
            jsonObject.put("latitude", reading.getLatitude());
            jsonObject.put("longitude", reading.getLongitude());
            jsonObject.put("vehicleID", reading.getVin());
            jsonObject.put("direction", reading.getDirection());

            Map<String, String> otherPairs=reading.getReadings();

            jsonObject.put("speed", otherPairs.get(AvailableCommandNames.SPEED.name()));
            jsonObject.put("fuel", otherPairs.get(AvailableCommandNames.FUEL_ECONOMY.name()));

            addLine(jsonObject.toString());
        }catch(Exception ex){

        }
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
