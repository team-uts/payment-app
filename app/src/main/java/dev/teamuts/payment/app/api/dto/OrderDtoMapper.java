package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.common.model.Money;
import dev.teamuts.payment.domain.order.dto.PayOrderCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    imports = Money.class)
public interface OrderDtoMapper {
  @Mapping(
      target = "totalAmount",
      expression = "java(Money.of(requestDto.totalAmount(), requestDto.currency()))")
  @Mapping(
      target = "pointAmount",
      expression = "java(Money.of(requestDto.pointAmount(), requestDto.currency()))")
  PayOrderCommand of(PayOrderRequestDto requestDto);
}
