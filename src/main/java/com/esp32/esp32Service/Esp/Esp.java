package com.esp32.esp32Service.Esp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "esp")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Esp {


    @Id
    private String id;
    private Double temperature;
    private Boolean isFanOn;
    private Boolean isFanAuto;
    private Boolean isEspOn;
}
