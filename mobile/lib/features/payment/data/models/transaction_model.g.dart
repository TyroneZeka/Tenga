// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'transaction_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$TransactionModelImpl _$$TransactionModelImplFromJson(
  Map<String, dynamic> json,
) => _$TransactionModelImpl(
  id: json['id'] as String,
  listingId: json['listingId'] as String,
  buyerId: json['buyerId'] as String,
  sellerId: json['sellerId'] as String,
  amount: (json['amount'] as num).toDouble(),
  currency: json['currency'] as String,
  paymentMethod: json['paymentMethod'] as String,
  status: json['status'] as String,
  gatewayReference: json['gatewayReference'] as String?,
  createdAt: json['createdAt'] as String,
  completedAt: json['completedAt'] as String?,
);

Map<String, dynamic> _$$TransactionModelImplToJson(
  _$TransactionModelImpl instance,
) => <String, dynamic>{
  'id': instance.id,
  'listingId': instance.listingId,
  'buyerId': instance.buyerId,
  'sellerId': instance.sellerId,
  'amount': instance.amount,
  'currency': instance.currency,
  'paymentMethod': instance.paymentMethod,
  'status': instance.status,
  'gatewayReference': instance.gatewayReference,
  'createdAt': instance.createdAt,
  'completedAt': instance.completedAt,
};
