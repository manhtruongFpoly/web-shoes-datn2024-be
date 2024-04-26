package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String vnp_ResponseCode;

}
