package com.ciaran.smartmeter.service;

import com.ciaran.smartmeter.exception.InvalidMeterIdException;
import com.ciaran.smartmeter.exception.InvalidAccountNumberException;
import com.ciaran.smartmeter.exception.InvalidReadingEntryException;
import com.ciaran.smartmeter.model.AccountDetails;
import com.ciaran.smartmeter.model.ReadingType;
import com.ciaran.smartmeter.model.SmartMeterAccount;
import com.ciaran.smartmeter.model.SmartMeterReading;
import com.ciaran.smartmeter.repository.SmartMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SmartMeterService {

    private SmartMeterRepository smartMeterRepository;
    private AccountDetails accountDetails;

    @Autowired
    public SmartMeterService(SmartMeterRepository smartMeterRepository, AccountDetails accountDetails) {
        this.smartMeterRepository = smartMeterRepository;
        this.accountDetails = accountDetails;
    }


    public AccountDetails getAccountDetailsByAccountId(final long id) {
        Optional<SmartMeterAccount> account = smartMeterRepository.findById(id);
        if(account.isPresent()) {
            return prepareAccountDetails(account);
        }
        else {
            throw new InvalidAccountNumberException("Error! No details for account number " + id);
        }

    }


    public AccountDetails addNewSmartMeterReadings(AccountDetails accountDetails) {
        long accountId = accountDetails.getAccountId();

        updateReadingType(accountDetails.getGasReadings(), accountDetails.getElecReadings());

        List<SmartMeterReading> allNewReadings = Stream.concat(accountDetails.getGasReadings().stream(),
                accountDetails.getElecReadings().stream()).collect(Collectors.toList());

        performInputValidations(accountId, allNewReadings);

        Optional<SmartMeterAccount> account = smartMeterRepository.findById(accountId);
        if(account.isPresent()) {
            account.get().getSmartMeterReadings().addAll(allNewReadings);
            smartMeterRepository.save(account.get());
            return prepareAccountDetails(account);
        }
        throw new InvalidAccountNumberException("Error! No details for account number " + accountId);
    }

    public AccountDetails prepareAccountDetails(Optional<SmartMeterAccount> account) {
        accountDetails.setAccountId(account.get().getAccountId());
        accountDetails.setGasReadings(getGasReadingsFromAll(account.get().getSmartMeterReadings()));
        accountDetails.setElecReadings(getElecReadingsFromAll(account.get().getSmartMeterReadings()));
        return accountDetails;
    }

    public void updateReadingType(List<SmartMeterReading> gas, List<SmartMeterReading> elec) {
        for(SmartMeterReading r: gas) {
            r.setReadingType(ReadingType.GAS_READING);
        }

        for(SmartMeterReading r: elec) {
            r.setReadingType(ReadingType.ELEC_READING);
        }
    }

    public void performInputValidations(Long accountId, List<SmartMeterReading> allNewReadings) {
        if(!smartMeterRepository.existsById(accountId)) {
            throw new InvalidAccountNumberException("Error! No details for account number " + accountId + " to add new readings to.");
        }
        if(!isValidMeterId(allNewReadings)) {
            throw new InvalidMeterIdException("Error! All smart meter IDs must be above 0.");
        }
        if(!isValidSmartReadingValue(allNewReadings)) {
            throw new InvalidReadingEntryException("Error! All input readings from gas and electricity must be above 0.");
        }
    }


    public boolean isValidSmartReadingValue(List<SmartMeterReading> allNewReadings) {
        for(SmartMeterReading r: allNewReadings) {
            if(r.getReading() <= 0.0) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidMeterId(List<SmartMeterReading> allNewReadings) {
        for(SmartMeterReading r: allNewReadings) {
            if(r.getMeterId() <= 0L) {
                return false;
            }
        }
        return true;
    }


    public List<SmartMeterReading> getGasReadingsFromAll(List<SmartMeterReading> allReadings) {
        return allReadings.stream()
                .filter(reading -> Objects.equals(ReadingType.GAS_READING, reading.getReadingType())).collect(Collectors.toList());    }

    public List<SmartMeterReading> getElecReadingsFromAll(List<SmartMeterReading> allReadings) {
        return allReadings.stream()
                .filter(reading -> Objects.equals(ReadingType.ELEC_READING, reading.getReadingType())).collect(Collectors.toList());
    }

}
