import 'package:freezed_annotation/freezed_annotation.dart';

part 'transaction_model.freezed.dart';
part 'transaction_model.g.dart';

@freezed
class TransactionModel with _$TransactionModel {
  const factory TransactionModel({
    required String id,
    required String listingId,
    required String buyerId,
    required String sellerId,
    @JsonKey(fromJson: _toDouble) required double amount,
    required String currency,
    required String paymentMethod,
    required String status,
    String? gatewayReference,
    required String createdAt,
    String? completedAt,
  }) = _TransactionModel;

  factory TransactionModel.fromJson(Map<String, dynamic> json) =>
      _$TransactionModelFromJson(json);
}

double _toDouble(dynamic value) => (value as num).toDouble();
