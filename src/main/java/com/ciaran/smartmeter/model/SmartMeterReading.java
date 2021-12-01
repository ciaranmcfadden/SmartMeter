package com.ciaran.smartmeter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reading")
public class SmartMeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "READING_ID")
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "READING")
    private double reading;

    @Column(name = "READING_DATE")
    private Date date;

    @JsonIgnore
    @Column(name = "READING_TYPE")
    private ReadingType readingType;


}
