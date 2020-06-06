package com.uk.iot.cache;

import com.uk.iot.model.IOTDevice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Senthil on 03/06/2020.
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({IOTDeviceCache.class, IOTDeviceCache.class})
public class IOTDeviceCacheTest {

    IOTDeviceCache iotDeviceCache;

    @Before
    public void setUp() throws Exception{
        iotDeviceCache = new IOTDeviceCache();
    }

    @Test
    public void testLoadData() {
        iotDeviceCache.loadData(getDeviceList());
        Map<BigInteger, IOTDevice> deviceMap =  iotDeviceCache.getDevicesForProduct("WG11155638");
        assertNotNull(" Unexpected result", deviceMap);
        assertEquals(" Wrong event id ", 10001, deviceMap.get(new BigInteger("1582605077000")).getEventID());
    }

    @Test
    public void testLoadData_NoInput() {
        iotDeviceCache.loadData(new ArrayList<>());
        assertNull(" Unexpected result", iotDeviceCache.getDevicesForProduct("WG11155638"));
    }

    private List<IOTDevice> getDeviceList() {
        List<IOTDevice> iotDeviceList = new ArrayList<>();

        IOTDevice iotDevice = new IOTDevice();
        //device 1
        iotDevice.setDateTime(new BigInteger("1582605077000"));
        iotDevice.setEventID(10001);
        iotDevice.setProductID("WG11155638");
        iotDevice.setLatitude("51.5185");
        iotDevice.setLongitude("-0.1736");
        iotDevice.setBattery(0.99);
        iotDevice.setLight("OFF");
        iotDevice.setAirPlaneMode("OFF");
        iotDeviceList.add(iotDevice);

        //device 2
        iotDevice = new IOTDevice();
        iotDevice.setDateTime(new BigInteger("1582605137000"));
        iotDevice.setEventID(10002);
        iotDevice.setProductID("WG11155638");
        iotDeviceList.add(iotDevice);

        //device 3
        iotDevice = new IOTDevice();
        iotDevice.setDateTime(new BigInteger("1582605257000"));
        iotDevice.setProductID("6900001001");
        iotDevice.setEventID(10003);
        iotDevice.setLatitude("40.73061");
        iotDevice.setLongitude("-73.935242");
        iotDevice.setBattery(0.11);
        iotDeviceList.add(iotDevice);

        return iotDeviceList;
    }
}
