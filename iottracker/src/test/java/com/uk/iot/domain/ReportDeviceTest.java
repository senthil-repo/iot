package com.uk.iot.domain;

import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.model.DeviceLocationResult;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.util.UtilHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Senthil on 04/06/2020.
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ReportDevice.class, IOTDeviceCache.class})
public class ReportDeviceTest {
    ReportDevice reportDevice;
    @Mock
    IOTDeviceCache iotDeviceCache;

    @Before
    public void setUp() throws Exception{
        reportDevice = new ReportDevice();
        PowerMockito.mockStatic(IOTDeviceCache.class);
        PowerMockito.whenNew(IOTDeviceCache.class).withAnyArguments().thenReturn(iotDeviceCache);
    }

    @Test
    public void testGetDeviceLocation_No_Timestamp() {
        when(iotDeviceCache.getDevicesForProduct("WG11155638")).
                thenReturn(getDeviceMapForCycleTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("WG11155638", "");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected Name ", "CyclePlusTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    @Test
    public void testGetDeviceLocation_WithTimestamp() {
        when(iotDeviceCache.getDevicesForProduct("WG11155638")).
                thenReturn(getDeviceMapForCycleTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("WG11155638", "1582605137000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        String dateTime = UtilHelper.getDateTime(Long.valueOf("1582605137000"));
        assertEquals(" Unexpected record ", dateTime, deviceLocationResult.getDeviceLocationDetails().getDatetime());
        assertEquals(" Unexpected Name ", "CyclePlusTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    @Test
    public void testGetDeviceLocation_GeneralTracker() {
        when(iotDeviceCache.getDevicesForProduct("6900001001")).
                thenReturn(getDeviceMapForGeneralTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("6900001001", "");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected Name ", "GeneralTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    @Test
    public void testGetDeviceLocation_AirplaneMode_ON() {
        when(iotDeviceCache.getDevicesForProduct("6900233111")).
                thenReturn(getDeviceMapForGeneralTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("6900233111", "1582605259000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Wrong description ", "SUCCESS: Location not available: Please turn off airplane mode",
                deviceLocationResult.getDeviceLocationDetails().getDescription());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    @Test
    public void testGetDeviceLocation_No_GPS_Data() {
        when(iotDeviceCache.getDevicesForProduct("6900233111")).
                thenReturn(getDeviceMapForGeneralTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("6900233111", "1582605259000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertNull(" Something wrong", deviceLocationResult.getDeviceLocationDetails().getLatitude());
        assertNull(" Something wrong", deviceLocationResult.getDeviceLocationDetails().getLongitude());
        assertEquals(" Should have inactive status", "Inactive", deviceLocationResult.getDeviceLocationDetails().getStatus());
    }

    @Test
    public void testGetDeviceLocation_BatteryLife() {
        when(iotDeviceCache.getDevicesForProduct("6900001001")).
                thenReturn(getDeviceMapForGeneralTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("6900001001", "1582605557000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected Name ", "GeneralTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
        assertEquals(" Unexpected battery life ", "Critical", deviceLocationResult.getDeviceLocationDetails().getBattery());
    }

    @Test
    public void testGetDeviceLocation_CouldNot_Locate() {
        when(iotDeviceCache.getDevicesForProduct("6900233111")).
                thenReturn(getDeviceMapForGeneralTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("6900233111", "1582612875000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected result ", "ERROR: Device could not be located", deviceLocationResult.getDeviceLocationDetails().getDescription());
    }

    @Test
    public void testGetDeviceLocation_No_Device() {
        when(iotDeviceCache.getDevicesForProduct("1111111111")).
                thenReturn(new HashMap<BigInteger, IOTDevice>());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("1111111111", "");
        assertEquals(" Unexpected result ", "ERROR: Id 1111111111 not found", deviceLocationResult.getDeviceLocationDetails().getDescription());
    }

    @Test
    public void testGetDeviceLocation_DynamicActivity_Tracker_Inactive() {
        when(iotDeviceCache.getDevicesForProduct("WG11155638")).
                thenReturn(getDeviceMapForCycleTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("WG11155638", "1582605257000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected device status ", "Inactive", deviceLocationResult.getDeviceLocationDetails().getStatus());
        assertEquals(" Unexpected Name ", "CyclePlusTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    @Test
    public void testGetDeviceLocation_DynamicActivity_Tracker_NotApplicable() {
        when(iotDeviceCache.getDevicesForProduct("WG11155800")).
                thenReturn(getDeviceMapForCycleTracker());
        DeviceLocationResult deviceLocationResult =  reportDevice.getDeviceLocation("WG11155800", "1582605437000");
        assertNotNull(" Something wrong ", deviceLocationResult);
        assertEquals(" Unexpected device status ", "N/A", deviceLocationResult.getDeviceLocationDetails().getStatus());
        assertEquals(" Unexpected Name ", "CyclePlusTracker", deviceLocationResult.getDeviceLocationDetails().getName());
        assertEquals(" Wrong HTTP status ", "200 OK", deviceLocationResult.getHttpStatus());
    }

    private Map<BigInteger, IOTDevice> getDeviceMapForCycleTracker() {
        List<IOTDevice> iotDeviceList = getCycleTrackerRecords().stream().map(s -> getIotDevice(s)).collect(Collectors.toList());
        Map<BigInteger, IOTDevice> deviceMap = iotDeviceList.stream().
                        collect(Collectors.toMap(IOTDevice::getDateTime,  iotDevice -> iotDevice));

        return deviceMap;
    }

    private Map<BigInteger, IOTDevice> getDeviceMapForGeneralTracker() {
        List<IOTDevice> iotDeviceList = getGeneralTrackerRecords().stream().map(s -> getIotDevice(s)).collect(Collectors.toList());
        Map<BigInteger, IOTDevice> deviceMap = iotDeviceList.stream().
                collect(Collectors.toMap(IOTDevice::getDateTime,  iotDevice -> iotDevice));

        return deviceMap;
    }

    private IOTDevice getIotDevice(String record) {
        String tokens[] = record.split(",");
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
    private List<String> getCycleTrackerRecords() {
        List<String> records = new ArrayList<>();
        records.add("1582605077000,10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
        records.add("1582605137000,10002,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
        records.add("1582605197000,10003,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");
        records.add("1582605257000,10004,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");

        records.add("1582605437000,10004,WG11155800,11.5185,-0.1736,0.98,OFF,OFF");
        return records;
    }

    private List<String> getGeneralTrackerRecords() {
        List<String> records = new ArrayList<>();
        records.add("1582605257000,10005,6900001001,40.73061,-73.935242,0.11,N/A,OFF");
        records.add("1582605258000,10006,6900001001,40.73071,-73.935242,0.1,N/A,OFF");
        records.add("1582605259000,10007,6900001001,40.73081,-73.935242,0.1,N/A,ON");
        records.add("1582605557000,10012,6900001001,40.73081,-73.935242,0.09,N/A,OFF");
        records.add("1582605615000,10013,6900233111,,,0.1,N/A,ON");
        records.add("1582612875000,10014,6900233111,,,0.1,N/A,OFF");

        return records;
    }
}
