package com.ciaran.smartmeter.model;

import com.ciaran.smartmeter.model.SmartMeterReading;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AccountDetails {
    private long accountId;
    private List<SmartMeterReading> gasReadings;
    private List<SmartMeterReading> elecReadings;
}
