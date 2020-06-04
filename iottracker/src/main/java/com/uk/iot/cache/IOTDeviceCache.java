package com.uk.iot.cache;

import com.uk.iot.model.IOTDevice;

import java.io.*;
import java.util.*;

/**
 * Created by Senthil on 03/06/2020.
 */
public class IOTDeviceCache {

    private static Map<String, List<IOTDevice>> iotDeviceMap = null;

    public IOTDeviceCache() {
        iotDeviceMap = new HashMap<>();
    }

    public static void loadData(List<IOTDevice> iotDeviceList) {
        if(iotDeviceList.isEmpty())
            return;

        for(IOTDevice iotDevice : iotDeviceList) {
            List<IOTDevice> deviceListOfAProduct = iotDeviceMap.get(iotDevice.getProductID());
            if(deviceListOfAProduct != null) {
                deviceListOfAProduct.add(iotDevice);
            } else {
                deviceListOfAProduct = new ArrayList<>();
                deviceListOfAProduct.add(iotDevice);
                iotDeviceMap.put(iotDevice.getProductID(), deviceListOfAProduct);
            }
        }
    }

    public IOTDevice getIOTDevice(String productID, long timeStampParam) {
        return null; //TODO
    }

}
