package com.uk.iot.cache;

import com.uk.iot.model.IOTDevice;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Senthil on 03/06/2020.
 */
public class IOTDeviceCache {

    //private static Map<String, List<IOTDevice>> iotDeviceMap = null;
    private static Map<String, Map<BigInteger, IOTDevice>> iotDeviceMap = null;

    public IOTDeviceCache() {
        iotDeviceMap = new HashMap<>();
    }

/*    public static void loadData(List<IOTDevice> iotDeviceList) {
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
    }*/

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

    public static Map<String, Map<BigInteger, IOTDevice>> getIOTDevices() {
        return iotDeviceMap; //TODO
    }

    public static Map<BigInteger, IOTDevice> getDevicesForProduct(String productId) {
        return (!iotDeviceMap.get(productId).isEmpty() ? iotDeviceMap.get(productId) : null);
    }
}
