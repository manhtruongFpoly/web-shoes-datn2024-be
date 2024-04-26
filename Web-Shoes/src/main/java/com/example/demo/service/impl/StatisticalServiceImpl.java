package com.example.demo.service.impl;

import com.example.demo.contants.OrderStatusEnum;
import com.example.demo.payload.statistical.MonthlyAndYearlyStatisticsDto;
import com.example.demo.payload.statistical.OrderStatusStatisticDto;
import com.example.demo.payload.statistical.StatisticalDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<StatisticalDto> listHoaDonCacNam() {
        return orderRepository.listHoaDonCacNam();
    }

    @Override
    public List<MonthlyAndYearlyStatisticsDto> listHoaDonCacThangVaNam() {
        return orderRepository.listHoaDonThangVaNam();
    }

    @Override
    public OrderStatusStatisticDto thongKeTrangThaiDonHang() {
        OrderStatusStatisticDto thongKeTrangThaiDonHang = new OrderStatusStatisticDto();
        thongKeTrangThaiDonHang.setSoDonChoXacNhan(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.CHOXACNHAN));
        thongKeTrangThaiDonHang.setSoDonDangXuLy(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.DANGXULY));
        thongKeTrangThaiDonHang.setSoDonDangVanChuyen(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.DANGVANCHUYEN));
        thongKeTrangThaiDonHang.setSoDonDaGiao(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.DAGIAO));
        thongKeTrangThaiDonHang.setSoDonDaHuy(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.DAHUY));
        thongKeTrangThaiDonHang.setSoDonDaHoanThanh(orderRepository.thongKeTrangThaiDonHang(OrderStatusEnum.DAHOANTHANH));

        return thongKeTrangThaiDonHang;
    }
}
