import 'package:dio/dio.dart';
import '../models/transaction_model.dart';

class TransactionPage {
  const TransactionPage({required this.content});

  final List<TransactionModel> content;

  factory TransactionPage.fromJson(Map<String, dynamic> json) {
    return TransactionPage(
      content: (json['content'] as List)
          .map((e) => TransactionModel.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }
}

class PaymentRemoteDataSource {
  PaymentRemoteDataSource(this._dio);

  final Dio _dio;

  Future<TransactionModel> initiateEcoCash({
    required String listingId,
    required String sellerId,
    required double amount,
    required String currency,
    required String payerPhone,
  }) async {
    final response = await _dio.post('/payments/ecocash', data: {
      'listingId': listingId,
      'sellerId': sellerId,
      'amount': amount,
      'currency': currency,
      'payerPhone': payerPhone,
    });
    return TransactionModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<TransactionModel> initiateInnBucks({
    required String listingId,
    required String sellerId,
    required double amount,
    required String currency,
    required String payerPhone,
  }) async {
    final response = await _dio.post('/payments/innbucks', data: {
      'listingId': listingId,
      'sellerId': sellerId,
      'amount': amount,
      'currency': currency,
      'payerPhone': payerPhone,
    });
    return TransactionModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<TransactionPage> getTransactions({int page = 0, int size = 20}) async {
    final response = await _dio.get(
      '/payments/transactions',
      queryParameters: {'page': page, 'size': size},
    );
    return TransactionPage.fromJson(response.data as Map<String, dynamic>);
  }
}
