import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/datasources/payment_remote_datasource.dart';
import '../../data/models/transaction_model.dart';
import '../../../../core/network/api_client.dart';

final paymentProvider = AsyncNotifierProvider.autoDispose<PaymentNotifier, TransactionModel?>(
  PaymentNotifier.new,
);

class PaymentNotifier extends AutoDisposeAsyncNotifier<TransactionModel?> {
  @override
  Future<TransactionModel?> build() async => null;

  Future<void> initiate({
    required String method,
    required String listingId,
    required String sellerId,
    required double amount,
    required String currency,
    required String payerPhone,
  }) async {
    state = const AsyncLoading();
    final ds = PaymentRemoteDataSource(ApiClient.instance);
    state = await AsyncValue.guard(() {
      if (method == 'ECOCASH') {
        return ds.initiateEcoCash(
          listingId: listingId,
          sellerId: sellerId,
          amount: amount,
          currency: currency,
          payerPhone: payerPhone,
        );
      } else {
        return ds.initiateInnBucks(
          listingId: listingId,
          sellerId: sellerId,
          amount: amount,
          currency: currency,
          payerPhone: payerPhone,
        );
      }
    });
  }
}
