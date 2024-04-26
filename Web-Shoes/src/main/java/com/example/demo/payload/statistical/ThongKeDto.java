package com.example.demo.payload.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeDto {
    private Long totalOrder;
    private Long totalMoney;
}
