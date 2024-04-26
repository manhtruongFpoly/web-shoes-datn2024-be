package com.example.demo.service;

import com.example.demo.payload.statistical.MonthlyAndYearlyStatisticsDto;
import com.example.demo.payload.statistical.OrderStatusStatisticDto;
import com.example.demo.payload.statistical.StatisticalDto;

import java.util.List;

public interface StatisticalService {
    List<StatisticalDto> listHoaDonCacNam();

    List<MonthlyAndYearlyStatisticsDto> listHoaDonCacThangVaNam();
    OrderStatusStatisticDto thongKeTrangThaiDonHang();
}
