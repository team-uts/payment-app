package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.paymentmethod.command.SetupPaymentMethodCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMethodDtoMapper {
  SetupPaymentMethodCommand of(SetupPaymentMethodRequestDto requestDto);
}
