package com.uk.iot.util;

import com.uk.iot.exception.GeneralException;
import com.uk.iot.model.Result;
import com.uk.iot.model.DeviceLocationResult;
import com.uk.iot.model.BatteryLife;
import com.uk.iot.model.IOTDevice;
import com.uk.iot.model.Body;
import com.uk.iot.model.TrackerEnum;
import com.uk.iot.model.AirPlaneMode;
import com.uk.iot.model.StatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.text.DecimalFormat;

/**
 * Created by Senthil on 04/06/2020.
 */
public class ResultBuilder {
    final private static String SPACE = " ";
    static final Logger logger = LogManager.getLogger(ResultBuilder.class);

    /********************* Service 1 (IoT batch data loading service) ******************
    /**
     * Method to build a generic result. Used for service 1 (LoadData).
     * @param httpStatus
     * @param body
     * @return
     */
    public Result buildGenericResult(HttpStatus httpStatus, String body) {
        StringBuilder status = new StringBuilder().append(httpStatus.value()).append(SPACE).append(httpStatus.getReasonPhrase());
        Result result = new Result(status.toString(), buildBody(body));
        return result;
    }

    private Body buildBody(String description) {
        Body body = new Body();
        body.setDescription(description);
        return body;
    }


    /********************* Service 2 (Report device details and location) ******************

    /**
     * Method to build the result for the Service 2 (report the device location).
     * @param iotDevice
     * @return
     */
    public DeviceLocationResult buildReportDeviceResult(IOTDevice iotDevice) {
        Body body = new Body();
        try{
            //Default status and description. Change it later based on GPS information.
            body.setDescription(AirPlaneMode.ON.getDescription());
            body.setStatus(StatusEnum.INACTIVE.getStatus());

            if(iotDevice.getAirPlaneMode().equalsIgnoreCase(AirPlaneMode.OFF.name())) {
                checkGPS(iotDevice, body);
            }
            body.setId(iotDevice.getProductID());

            //device prefixed as 'WG' are for CyclePlusTracker customers
            //device prefixed as '69' are for GeneralTracker customers
            body.setName(iotDevice.getProductID().startsWith("WG") ?
                    TrackerEnum.TR_WG.getTrackerName() : TrackerEnum.TR_69.getTrackerName());
            body.setDatetime(UtilHelper.getDateTime(iotDevice.getDateTime().longValue())); //set the UTC datetime
            body.setBattery(getBatteryLife(iotDevice.getBattery()));
        } catch(GeneralException exception) {
            logger.error(" ****** Device could not be located **** " + exception.getMessage());
            return buildReportDeviceErrorResult(HttpStatus.valueOf(exception.getErrorCode()), exception.getErrorDescription()); //TODO - move to constants
        }

        StringBuilder status = new StringBuilder().append(HttpStatus.OK.value()).append(SPACE).append(HttpStatus.OK.getReasonPhrase());
        return new DeviceLocationResult(status.toString(), body);
    }

    /**
     * If the GPS details (Longitude & Latitude) are present, then set the device status as Active.
     * Otherwise throw back an error.
     * @param iotDevice
     * @param body
     */
    private void checkGPS(IOTDevice iotDevice, Body body) {
        body.setDescription(AirPlaneMode.OFF.getDescription());
        if(StringUtils.isNotBlank(iotDevice.getLongitude()) && StringUtils.isNotBlank(iotDevice.getLatitude())) {
            body.setLongitude(iotDevice.getLongitude());
            body.setLatitude(iotDevice.getLatitude());
            body.setStatus(StatusEnum.ACTIVE.getStatus());
        } else {
            throw new GeneralException(HttpStatus.BAD_REQUEST.value(), "ERROR: Device could not be located");
        }
    }

    /**
     * Method to build the error result. Used for Service 2.
     * @param httpStatus
     * @param description
     * @return
     */
    public DeviceLocationResult buildReportDeviceErrorResult(HttpStatus httpStatus, String description) {
        //StringBuilder status = new StringBuilder().append(httpStatus.value()).append(SPACE).append(httpStatus.getReasonPhrase());
        Body body = new Body();
        body.setDescription(description);
        DeviceLocationResult deviceLocationResult = new DeviceLocationResult(String.valueOf(httpStatus.value()), body);
        return deviceLocationResult;
    }

   /**
     * Battery life is also reported as being 'Full' if it is greater than or equal to 98%,
     * 'High' 60% or higher, 'Medium' for 40% or higher, 'Low' for 10% or higher and 'Critical' if below 10%
     * @param battery
     * @return
     */
    private String getBatteryLife(double battery) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        double batteryPecentage = battery * 100;

        String batteryStatus = (batteryPecentage >= 98 ? BatteryLife.FULL.getBatteryLife() :
                (batteryPecentage >= 60 ? BatteryLife.HIGH.getBatteryLife() :
                        (batteryPecentage >= 40 ? BatteryLife.MEDIUM.getBatteryLife() :
                                (batteryPecentage >= 10 ? BatteryLife.LOW.getBatteryLife() :
                                        BatteryLife.CRITICAL.getBatteryLife())))) ;
        return batteryStatus;
    }

}
