package com.ciaran.smartmeter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="account")
public class SmartMeterAccount {

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    private Long accountId;

    @OneToMany(targetEntity = SmartMeterReading.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOUNT_FK", referencedColumnName = "ACCOUNT_NUMBER")
    private List<SmartMeterReading> smartMeterReadings;

}
