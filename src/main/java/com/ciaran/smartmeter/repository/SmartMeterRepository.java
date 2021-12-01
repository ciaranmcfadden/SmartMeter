package com.ciaran.smartmeter.repository;

import com.ciaran.smartmeter.model.SmartMeterAccount;
import org.springframework.data.repository.CrudRepository;

public interface SmartMeterRepository extends CrudRepository<SmartMeterAccount, Long> {

}
