package com.esp32.esp32Service.AuthorizedCard;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorized_card")
@Builder @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AuthorizedCard {
    @Id
    @SequenceGenerator(
            name = "feedback_request_sequence",
            sequenceName = "feedback_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedback_request_sequence"
    )
    private Long id;
    private String espId;
    private String code;
}
