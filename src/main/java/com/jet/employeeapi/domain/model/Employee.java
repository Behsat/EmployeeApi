package com.jet.employeeapi.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "Employee")
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String email;

    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private List<String> hobbies;
}
