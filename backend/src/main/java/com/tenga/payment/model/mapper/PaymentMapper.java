package com.tenga.payment.model.mapper;

import com.tenga.payment.model.dto.TransactionResponse;
import com.tenga.payment.model.dto.WalletResponse;
import com.tenga.payment.model.entity.Transaction;
import com.tenga.payment.model.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  TransactionResponse toResponse(Transaction transaction);

  WalletResponse toResponse(Wallet wallet);
}
