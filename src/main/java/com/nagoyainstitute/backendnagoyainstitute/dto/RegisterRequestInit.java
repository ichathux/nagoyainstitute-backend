package com.nagoyainstitute.backendnagoyainstitute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestInit {
    private String nic;
    private String city;
}
