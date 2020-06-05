package com.uk.iot.util;

import com.uk.iot.model.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by Senthil on 04/06/2020.
 */
public class ResultBuilder {
    final private static String SPACE = " ";
    final private static String COLON = " : ";
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

    private Body buildMandatoryElements(Body body, IOTDevice iotDevice) {
        body.setId(iotDevice.getProductID());
        //set the tracker name
        body.setName(iotDevice.getProductID().startsWith("WG") ?
                TrackerEnum.TR_WG.getTrackerName() : TrackerEnum.TR_69.getTrackerName());
        //set the date time in UTC
        body.setDatetime(getDateTime(iotDevice.getDateTime().longValue()));
        //set optional element - longitude and latitude
        body.setLongitude(iotDevice.getLongitude());
        body.setLatitude(iotDevice.getLatitude());
        body.setStatus(StatusEnum.ACTIVE.getStatus()); //TODO - check ??
        body.setBattery(getBatteryLife(iotDevice.getBattery()));
        body.setDescription("SUCCESS: Location identified."); //TODO - check ??

        return null;
    }

    public DeviceLocationResult buildReportDeviceErrorResult(HttpStatus httpStatus, String description) {
        StringBuilder status = new StringBuilder().append(httpStatus.value()).append(SPACE).append(httpStatus.getReasonPhrase());
        Body body = new Body();
        body.setDescription(description);
        DeviceLocationResult deviceLocationResult = new DeviceLocationResult(status.toString(), body);

        return deviceLocationResult;
    }

    private String getDateTime(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    //Battery life is also reported as being 'Full' if
    // it is greater than or equal to 98%, 'High' 60% or higher,
    // 'Medium' for 40% or higher, 'Low' for 10% or higher and 'Critical' if below 10%
    private String getBatteryLife(double battery) {
        DecimalFormat decimalFormat = new DecimalFormat();
        int batteryPecentage = Integer.valueOf(decimalFormat.format(battery);

        String batteryStatus = (batteryPecentage >= 98 ? BatteryLife.FULL.getBatteryLife() :
                (batteryPecentage > 60 ? BatteryLife.HIGH.getBatteryLife() :
                        (batteryPecentage > 40 ? BatteryLife.MEDIUM.getBatteryLife() :
                                (batteryPecentage > 10 ? BatteryLife.LOW.getBatteryLife() :
                                        BatteryLife.CRITICAL.getBatteryLife())))) ;
        return batteryStatus;
    }

    public DeviceLocationResult buildReportDeviceSuccessResult(IOTDevice iotDevice) {
        //check airplane mode
        if(iotDevice.getAirPlaneMode().equalsIgnoreCase(""))
    }
}
