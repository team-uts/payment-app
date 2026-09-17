package dev.teamuts.payment.app.api.dto;

import dev.teamuts.payment.domain.paymentmethod.dto.PaymentMethodCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMethodDtoMapper {
  PaymentMethodCommand.SetupPaymentMethod of(SetupPaymentMethodRequestDto requestDto);
}
