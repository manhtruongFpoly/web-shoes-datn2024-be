package com.example.demo.payload.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyAndYearlyStatisticsDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Long totalOrder;
    private Long totalMoney;
}
