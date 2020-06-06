package com.uk.iot.cache;

import com.uk.iot.model.IOTDevice;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Senthil on 03/06/2020.
 * Cache class to hold the IOT device and location details
 */
public class IOTDeviceCache {
    private static Map<String, Map<BigInteger, IOTDevice>> iotDeviceMap = null;

    public IOTDeviceCache() {
        iotDeviceMap = new HashMap<>();
    }

    /**
     * Method to load the IOT device details in in-memory dataset (hashmap)
     * @param iotDeviceList
     */
    public static void loadData(List<IOTDevice> iotDeviceList) {
        if(iotDeviceList.isEmpty())
            return;

        for(IOTDevice iotDevice : iotDeviceList) {
            Map<BigInteger, IOTDevice> aProductDevices = iotDeviceMap.get(iotDevice.getProductID());
            if(aProductDevices != null) {
                aProductDevices.put(iotDevice.getDateTime(), iotDevice);
            } else {
                aProductDevices = new TreeMap<>();
                aProductDevices.put(iotDevice.getDateTime(), iotDevice);
                iotDeviceMap.put(iotDevice.getProductID(), aProductDevices);
            }
        }
    }

    /**
     * Method to get the map of IOT devices for the respective productId
     * @param productId
     * @return
     */
    public static Map<BigInteger, IOTDevice> getDevicesForProduct(String productId) {
        return (!CollectionUtils.isEmpty(iotDeviceMap)
                && !CollectionUtils.isEmpty(iotDeviceMap.get(productId))
                && !iotDeviceMap.get(productId).isEmpty() ? iotDeviceMap.get(productId) : null);
    }

}
