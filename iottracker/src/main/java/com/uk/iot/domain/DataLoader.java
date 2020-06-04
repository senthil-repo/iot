package com.uk.iot.domain;

import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.model.Result;
import com.uk.iot.util.ResultBuilder;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Senthil on 03/06/2020.
 */
public class DataLoader {
    public Result loadData(String fileLocation) {
        fileLocation = "/Users/Appa/Downloads/data-2.csv"; // TODO - remove
        //get the data from CSV file
        //return the result (success / error)
        return getData(fileLocation);
    }

    private Result getData(String fileLocation) {
        File file = new File(fileLocation);
        int batchSize = 10; // TODO - move to a property
        boolean hasMoreLines = true;
        BufferedReader bufferedReader = null;
        ResultBuilder resultBuilder = new ResultBuilder();
        try{
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.readLine(); //skip the 1st (header) line


            IOTDeviceCache iotDeviceCache = new IOTDeviceCache();
            while(hasMoreLines) {
                List<IOTDevice> iotDeviceList = readBatchData(bufferedReader, batchSize);

                //update the cache
                iotDeviceCache.loadData(iotDeviceList);

                if(iotDeviceList.size() < batchSize) {
                    hasMoreLines = false;
                }
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return resultBuilder.buildDataLoadResult(HttpStatus.NOT_FOUND, "ERROR: no data file found");
        } catch (Exception e) {
            //TODO - need to add more details
            return resultBuilder.buildDataLoadResult(HttpStatus.NOT_FOUND, "ERROR: A technical exception occurred");
        }
        return resultBuilder.buildDataLoadResult(HttpStatus.OK, "data refreshed");
    }

    private List<IOTDevice> readBatchData(BufferedReader bufferedReader, int batchSize) throws IOException {

        List<IOTDevice> iotDeviceList = new ArrayList<>();
        for(int index = 0; index < batchSize; index++) {
            String nextLine = bufferedReader.readLine();
            if(nextLine != null) {
                String tokens[] = nextLine.split(",");
                System.out.println(" ----- " + nextLine);

                iotDeviceList.add(getIOTDevice(tokens));
            }
        }
        return iotDeviceList;
    }

    private IOTDevice getIOTDevice(String tokens[]) {
        IOTDevice iotDevice = new IOTDevice();
        iotDevice.setDateTime(Timestamp.valueOf(tokens[0]));
        iotDevice.setEventID(Integer.valueOf(tokens[1]));
        iotDevice.setProductID(tokens[2]);
        iotDevice.setLatitude(tokens[3]);
        iotDevice.setLongitude(tokens[4]);
        iotDevice.setBattery(Double.valueOf(tokens[5]));
        iotDevice.setLight(tokens[6]);
        iotDevice.setAirPlaneMode(tokens[6]);

        return iotDevice;

    }

/*    public static void main(String[] args) {
        loadData("");
    }*/
}
