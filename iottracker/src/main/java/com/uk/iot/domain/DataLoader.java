package com.uk.iot.domain;

import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.model.Result;
import com.uk.iot.util.ResultBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Senthil on 03/06/2020.
 */
public class DataLoader {
    public Result loadData(String fileLocation) {
        //return the result (success / error)
        return getData(fileLocation);
    }

    private Result getData(String fileLocation) {
        int batchSize = 10; // TODO - move to a property
        boolean hasMoreLines = true;
        BufferedReader bufferedReader = null;
        ResultBuilder resultBuilder = new ResultBuilder();
        try{

            if(StringUtils.isBlank(fileLocation))
                throw new FileNotFoundException(" Invalid file ");
            File file = new File(fileLocation.trim());
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
            return resultBuilder.buildGenericResult(HttpStatus.NOT_FOUND, "ERROR: no data file found");
        } catch (Exception e) {
            //TODO - need to add more details
            return resultBuilder.buildGenericResult(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: A technical exception occurred");
        }
        return resultBuilder.buildGenericResult(HttpStatus.OK, "data refreshed");
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
        iotDevice.setDateTime(new BigDecimal(tokens[0]).toBigInteger());
        iotDevice.setEventID(Integer.valueOf(tokens[1]));
        iotDevice.setProductID(tokens[2]);
        iotDevice.setLatitude(tokens[3]);
        iotDevice.setLongitude(tokens[4]);
        iotDevice.setBattery(Double.valueOf(tokens[5]));
        iotDevice.setLight(tokens[6]);
        iotDevice.setAirPlaneMode(tokens[6]);

        return iotDevice;

    }
}
