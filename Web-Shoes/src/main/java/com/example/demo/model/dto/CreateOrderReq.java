package com.example.demo.model.dto;

import com.example.demo.contants.RegexContants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

    private String fullname;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String phone;
    private String description;
    private Integer shipping;

    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("coupon_code")
    private String couponCode;

    private Long grandTotal;

}
