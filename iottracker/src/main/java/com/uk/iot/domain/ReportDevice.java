package com.uk.iot.domain;

import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.model.DeviceLocationResult;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.model.Body;
import com.uk.iot.model.TrackerEnum;
import com.uk.iot.model.StatusEnum;
import com.uk.iot.util.ResultBuilder;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

/**
 * Created by Senthil on 04/06/2020.
 */
public class ReportDevice {

    static final Logger logger = LogManager.getLogger(ReportDevice.class);
    /**
     * Method to get the location details of the IOT device for the passing product and/or timestamp.
     * @param productId
     * @param tstmp
     * @return
     */
    public DeviceLocationResult getDeviceLocation(String productId, String tstmp) {
        Map<BigInteger, IOTDevice> deviceMap =  IOTDeviceCache.getDevicesForProduct(productId);

        if(deviceMap == null || deviceMap.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ERROR: Id ").append(productId).append(" not found");
            logger.error(" ****** No device found for the supplier product id **** ");
            return new ResultBuilder().buildReportDeviceErrorResult(HttpStatus.NOT_FOUND, stringBuilder.toString());
        }
        IOTDevice iotDevice = getDeviceLocation(deviceMap, tstmp);
        DeviceLocationResult deviceLocationResult = new ResultBuilder().buildReportDeviceResult(iotDevice);

        //get the dynamic activity status for CyclePlusTracker
        //check only for success response (i.e if device is located)
        Body body = deviceLocationResult.getDeviceLocationDetails();
        if(StringUtils.isNotBlank(body.getId()) && body.getName().equalsIgnoreCase(TrackerEnum.TR_WG.getTrackerName())) {
            getDynamicActivityStatus(deviceLocationResult.getDeviceLocationDetails(), deviceMap, iotDevice.getDateTime());
        }
        return deviceLocationResult;
    }

    /**
     * If the timestamp is passed, this method will get the exact location details.
     * Otherwsie, the matching (latest) location details will be given.
     * @param deviceMap
     * @param tstmp
     * @return
     */
    private IOTDevice getDeviceLocation(Map<BigInteger, IOTDevice> deviceMap, String tstmp) {
        tstmp = StringUtils.isBlank(tstmp) ? String.valueOf(Instant.now().toEpochMilli()) : tstmp;
        final String timeStamp = tstmp;

        IOTDevice iotDevice = deviceMap.entrySet().stream().filter(entry -> String.valueOf(entry.getKey()).
                equalsIgnoreCase(timeStamp)).findFirst().map(Map.Entry::getValue).orElse(null);
        return iotDevice != null ? iotDevice : deviceMap.entrySet().stream().reduce((first, second) -> second).get().getValue();
    }

    /**
     * Method to check the dynamic activity status of CycleTracker customers. If the last 3 consecutive sets of
     * locations are same (i.e the longitude & latitude details are same), then the overall status of the device is 'Inactive'.
     * If there are not much details available, then the status will be 'Not Applicable (N/A)'.
     * @param deviceLocationResult
     * @param deviceMap
     * @param timeStamp
     */
    private void getDynamicActivityStatus(Body deviceLocationResult, Map<BigInteger, IOTDevice> deviceMap, BigInteger timeStamp) {
        deviceMap = deviceMap.entrySet().stream().filter(entry -> entry.getKey().intValue() <= timeStamp.intValue()
                && entry.getValue().getLatitude().equalsIgnoreCase(deviceLocationResult.getLatitude())
                && entry.getValue().getLongitude().equalsIgnoreCase(deviceLocationResult.getLongitude()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if(deviceMap.size() < 3 ) {
            deviceLocationResult.setStatus(StatusEnum.NOT_APPLICABLE.getStatus());
        } else {
            deviceLocationResult.setStatus(StatusEnum.INACTIVE.getStatus());
        }

    }
}
