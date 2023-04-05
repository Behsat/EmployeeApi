package com.jet.employeeapi.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmployeeResponse {

    private UUID uuid;
    private String email;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private List<String> hobbies;
}
