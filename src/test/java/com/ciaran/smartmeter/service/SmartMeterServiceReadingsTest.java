package com.ciaran.smartmeter.service;

import com.ciaran.smartmeter.model.AccountDetails;
import com.ciaran.smartmeter.model.ReadingType;
import com.ciaran.smartmeter.model.SmartMeterAccount;
import com.ciaran.smartmeter.model.SmartMeterReading;
import com.ciaran.smartmeter.repository.SmartMeterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SmartMeterServiceReadingsTest {

    private SmartMeterAccount account;
    SmartMeterReading gasReading1;
    SmartMeterReading elecReading1;
    SmartMeterReading gasReading2;
    SmartMeterReading elecReading2;

    private SmartMeterService smartMeterService;
    @Mock
    private SmartMeterRepository smartMeterRepository;
    @Mock
    private AccountDetails accountDetails;

    @BeforeEach
    private void createTestReadings() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2021-08-12 00:00:00");
        gasReading1 = new SmartMeterReading(1L,10L,12.4, date, ReadingType.GAS_READING);
        elecReading1 = new SmartMeterReading(2L,10L,187.2, date, ReadingType.ELEC_READING);
        gasReading2 = new SmartMeterReading(3L,10L,12.4, date, ReadingType.GAS_READING);
        elecReading2 = new SmartMeterReading(4L,10L,187.2, date, ReadingType.ELEC_READING);
        SmartMeterReading elecReading3 = new SmartMeterReading(5L,10L,12.4, date, ReadingType.ELEC_READING);
        SmartMeterReading elecReading4 = new SmartMeterReading(6L,10L,187.2, date, ReadingType.ELEC_READING);

        List<SmartMeterReading> readings = Stream.of(gasReading1,elecReading1,gasReading2,
                elecReading2,elecReading3,elecReading4).collect(Collectors.toList());

        account = new SmartMeterAccount(10001L, readings);

        smartMeterService = new SmartMeterService(smartMeterRepository, accountDetails);
    }

    @Test
    public void testGetGasReadingsFromAll() throws Exception {

        List<SmartMeterReading> gasReadings = smartMeterService.getGasReadingsFromAll(account.getSmartMeterReadings());
        Assertions.assertEquals(2, gasReadings.size());

        for(SmartMeterReading reading: gasReadings) {
            Assertions.assertEquals(ReadingType.GAS_READING, reading.getReadingType());
        }

    }

    @Test
    public void testGetElecReadingsFromAll() throws Exception {
        List<SmartMeterReading> elecReadings = smartMeterService.getElecReadingsFromAll(account.getSmartMeterReadings());
        Assertions.assertEquals(4, elecReadings.size());

        for(SmartMeterReading reading: elecReadings) {
            Assertions.assertEquals(ReadingType.ELEC_READING, reading.getReadingType());
        }
    }

    @Test
    public void testUpdateReadingType() throws Exception {
        gasReading1.setReadingType(null);
        gasReading2.setReadingType(null);
        elecReading1.setReadingType(null);
        elecReading2.setReadingType(null);

        List<SmartMeterReading> gasReadings = Stream.of(gasReading1, gasReading2).collect(Collectors.toList());

        List<SmartMeterReading> elecReadings = Stream.of(elecReading1, elecReading2).collect(Collectors.toList());

        smartMeterService.updateReadingType(gasReadings, elecReadings);

        for(SmartMeterReading reading: gasReadings) {
            Assertions.assertEquals(ReadingType.GAS_READING, reading.getReadingType());
        }

        for(SmartMeterReading reading: elecReadings) {
            Assertions.assertEquals(ReadingType.ELEC_READING, reading.getReadingType());
        }

    }

    @Test
    public void testIsValidSmartReadingValue() {
        List<SmartMeterReading> readings = account.getSmartMeterReadings();

        Assertions.assertTrue(smartMeterService.isValidSmartReadingValue(readings));

        readings.get(0).setReading(-1.1);

        Assertions.assertFalse(smartMeterService.isValidSmartReadingValue(readings));
    }


    @Test
    public void testIsValidMeterId() {
        List<SmartMeterReading> readings = account.getSmartMeterReadings();

        Assertions.assertTrue(smartMeterService.isValidMeterId(readings));

        readings.get(0).setMeterId(-1L);

        Assertions.assertFalse(smartMeterService.isValidMeterId(readings));
    }
}
