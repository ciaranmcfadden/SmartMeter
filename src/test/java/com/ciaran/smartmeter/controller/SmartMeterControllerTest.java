package com.ciaran.smartmeter.controller;

import com.ciaran.smartmeter.exception.InvalidAccountNumberException;
import com.ciaran.smartmeter.exception.InvalidReadingEntryException;
import com.ciaran.smartmeter.model.AccountDetails;
import com.ciaran.smartmeter.model.ReadingType;
import com.ciaran.smartmeter.model.SmartMeterReading;
import com.ciaran.smartmeter.service.SmartMeterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SmartMeterControllerTest {
    private AccountDetails mockAccount;

    @MockBean
    private SmartMeterService smartMeterService;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void startUp() throws Exception {
        Calendar cal = new GregorianCalendar(2021, 10, 12);
        Date d = cal.getTime();
        SmartMeterReading reading1 = new SmartMeterReading(1L,10L,12.4, d, ReadingType.GAS_READING);
        SmartMeterReading reading2 = new SmartMeterReading(2L,10L,187.2, d, ReadingType.ELEC_READING);
        List<SmartMeterReading> gasReadings = new ArrayList<SmartMeterReading>();
        List<SmartMeterReading> elecReadings = new ArrayList<SmartMeterReading>();
        gasReadings.add(reading1);
        elecReadings.add(reading2);
        mockAccount = new AccountDetails(1L, gasReadings, elecReadings);
    }


    @Test
    public void testGetSmartMeterAccountByAccountId() throws Exception {
        doReturn(mockAccount).when(smartMeterService).getAccountDetailsByAccountId(1L);

        mockMvc.perform(get("/api/smart/reads/{accountNumber}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accountId").value(1L));
    }


    @Test
    public void testGetSmartMeterAccountByInvalidAccountId() throws Exception {
        doThrow(new InvalidAccountNumberException("Error! No details for account number 2")).when(smartMeterService).getAccountDetailsByAccountId(2);

        mockMvc.perform(get("/api/smart/reads/{accountNumber}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error! No details for account number 2"));

    }


    @Test
    public void testAddSmartMeterReadsToAccountId() throws Exception {
        AccountDetails postAccount = new AccountDetails();
        doReturn(mockAccount).when(smartMeterService).addNewSmartMeterReadings(any());

        mockMvc.perform(post("/api/smart/reads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postAccount)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accountId").value(1L));

    }

    @Test
    public void testAddSmartMeterReadsByInvalidAccountId() throws Exception {
        doThrow(new InvalidAccountNumberException("Error! No details for account number 2")).when(smartMeterService).addNewSmartMeterReadings(any());

        mockMvc.perform(post("/api/smart/reads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockAccount)))

                .andExpect(status().isNotFound())
                .andExpect(content().string("Error! No details for account number 2"));

    }

    @Test
    public void testAddSmartMeterReadsWithInvalidReading() throws Exception {
        doThrow(new InvalidReadingEntryException("Error! All readings must be greater than 0.")).when(smartMeterService).addNewSmartMeterReadings(any());

        mockMvc.perform(post("/api/smart/reads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockAccount)))

                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error! All readings must be greater than 0."));

    }

    @Test
    public void testAddSmartMeterReadsWithInvalidMeterId() throws Exception {
        doThrow(new InvalidReadingEntryException("Error! All meter IDs must be greater than 0.")).when(smartMeterService).addNewSmartMeterReadings(any());

        mockMvc.perform(post("/api/smart/reads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockAccount)))

                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error! All meter IDs must be greater than 0."));

    }


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
