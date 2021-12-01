package com.ciaran.smartmeter.service;

import com.ciaran.smartmeter.exception.InvalidAccountNumberException;
import com.ciaran.smartmeter.model.AccountDetails;
import com.ciaran.smartmeter.model.ReadingType;
import com.ciaran.smartmeter.model.SmartMeterAccount;
import com.ciaran.smartmeter.model.SmartMeterReading;
import com.ciaran.smartmeter.repository.SmartMeterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SmartMeterServiceTest {
    private SmartMeterAccount mockAccount;

    @Autowired
    private SmartMeterService smartMeterService;

    @MockBean
    private SmartMeterRepository smartMeterRepository;

    @BeforeEach
    public void startUp() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2021-11-01 00:00:00");
        SmartMeterReading gasReading = new SmartMeterReading(1L,10L,12.4, date, ReadingType.GAS_READING);
        SmartMeterReading elecReading = new SmartMeterReading(2L,10L,187.2, date, ReadingType.ELEC_READING);
        List<SmartMeterReading> readings = new ArrayList<SmartMeterReading>();
        readings.add(gasReading);
        readings.add(elecReading);
        mockAccount  = new SmartMeterAccount(105L, readings);
    }


    @Test
    public void testGetAccountDetailsByAccountId() {
        doReturn(Optional.of(mockAccount)).when(smartMeterRepository).findById(105L);

        AccountDetails returnedAccount = smartMeterService.getAccountDetailsByAccountId(105L);

        Assertions.assertEquals(returnedAccount.getAccountId(), mockAccount.getAccountId());
        Assertions.assertEquals((returnedAccount.getElecReadings().size() + returnedAccount.getGasReadings().size()), mockAccount.getSmartMeterReadings().size());

    }

    @Test
    public void testGetAccountDetailsByInvalidAccountId() {
        doReturn(Optional.empty()).when(smartMeterRepository).findById(110L);
        InvalidAccountNumberException invalidAccountNumber = Assertions.assertThrows(InvalidAccountNumberException.class, ()-> {
            smartMeterService.getAccountDetailsByAccountId(110L);
        });
        Assertions.assertEquals(invalidAccountNumber.getErrorMessage(), "Error! No details for account number 110");
    }


}
