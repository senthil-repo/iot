package com.uk.iot.domain;

import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.model.Result;
import com.uk.iot.util.ResultBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Senthil on 03/06/2020.
 */
public class DataLoader {

    static final Logger logger = LogManager.getLogger(DataLoader.class);
    /**
     * Method to load the iot device details to in-memory dataset.
     * @param fileLocation
     * @return
     */
    public Result loadData(String fileLocation) {
        return getData(fileLocation);
    }

    /**
     * Method does the following:
     * Reads the csv file from the supplied location.
     * Loads the file to a in-memory dataset (hashMap) through IOTDeviceCache class.
     * @param fileLocation
     * @return
     */
    private Result getData(String fileLocation) {
        int batchSize = 100; // Ideally this should be a property sitting outside of the application...!
        boolean hasMoreLines = true;
        BufferedReader bufferedReader;
        ResultBuilder resultBuilder = new ResultBuilder();
        try{

            if(StringUtils.isBlank(fileLocation))
                throw new FileNotFoundException(" Invalid file ");

            File file = new File(fileLocation.trim());
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.readLine(); //skip the 1st (header) line

            batchLoadTheData(batchSize, hasMoreLines, bufferedReader);

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            logger.error(" ****** No data file found for the supplied filePath **** " + e.getMessage());
            return resultBuilder.buildGenericResult(HttpStatus.NOT_FOUND, "ERROR: no data file found");
        } catch (Exception e) {
            logger.error(" ****** A technical exception occured, please contact system admin **** " + e.getMessage());
            return resultBuilder.buildGenericResult(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: A technical exception occurred. Please contact system admin." +
                    " Once the issue is resolved, please reload the data.");
        }
        logger.info(" ***** Successfully loaded the data ***** ");
        return resultBuilder.buildGenericResult(HttpStatus.OK, "data refreshed");
    }

    /**
     * ****** BATCH LOAD the data ******
     * Based on the batch size, update the hashMap in IOTDeviceCache class
     */
    private void batchLoadTheData(int batchSize, boolean hasMoreLines, BufferedReader bufferedReader) throws IOException {
        IOTDeviceCache iotDeviceCache = new IOTDeviceCache();

        while(hasMoreLines) {
            List<IOTDevice> iotDeviceList = readBatchData(bufferedReader, batchSize);
            //update the cache
            iotDeviceCache.loadData(iotDeviceList);

            if(iotDeviceList.size() < batchSize) {//for the last batch set, iotDeviceList's size should be less than the batch size.
                hasMoreLines = false;
            }
        }
    }

    /**
     * Read each line of data for every batch and build the IOTDevice out of it
     * @param bufferedReader
     * @param batchSize
     * @return
     * @throws IOException
     */
    private List<IOTDevice> readBatchData(BufferedReader bufferedReader, int batchSize) throws IOException {
        List<IOTDevice> iotDeviceList = new ArrayList<>();
        for(int index = 0; index < batchSize; index++) {
            String nextLine = bufferedReader.readLine();
            if(nextLine != null) {
                String tokens[] = nextLine.split(",");
                iotDeviceList.add(getIOTDevice(tokens));
            } else {
                return iotDeviceList;
            }
        }
        return iotDeviceList;
    }

    /**
     * Build the IOTDevice
     * @param tokens
     * @return
     */
    private IOTDevice getIOTDevice(String tokens[]) {
        IOTDevice iotDevice = new IOTDevice();
        iotDevice.setDateTime(new BigDecimal(tokens[0]).toBigInteger());
        iotDevice.setEventID(Integer.valueOf(tokens[1]));
        iotDevice.setProductID(tokens[2]);
        iotDevice.setLatitude(tokens[3]);
        iotDevice.setLongitude(tokens[4]);
        iotDevice.setBattery(Double.valueOf(tokens[5]));
        iotDevice.setLight(tokens[6]);
        iotDevice.setAirPlaneMode(tokens[7]);
        return iotDevice;
    }
}
