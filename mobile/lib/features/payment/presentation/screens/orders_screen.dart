import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../data/datasources/payment_remote_datasource.dart';
import '../../data/models/transaction_model.dart';
import '../../../auth/presentation/providers/auth_provider.dart';
import '../../../../core/network/api_client.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../shared/utils/currency_formatter.dart';
import '../../../../shared/utils/date_formatter.dart';

final transactionsProvider =
    FutureProvider.autoDispose<List<TransactionModel>>((ref) async {
  final ds = PaymentRemoteDataSource(ApiClient.instance);
  final page = await ds.getTransactions();
  return page.content;
});

class OrdersScreen extends ConsumerWidget {
  const OrdersScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final transactionsAsync = ref.watch(transactionsProvider);
    final currentUserId = ref.watch(authProvider).valueOrNull?.userId ?? '';

    return Scaffold(
      appBar: AppBar(title: const Text('My Orders')),
      body: transactionsAsync.when(
        loading: () => const Center(
            child: CircularProgressIndicator(color: AppTheme.primary)),
        error: (e, _) => Center(child: Text('Error: $e')),
        data: (transactions) {
          if (transactions.isEmpty) {
            return const Center(
              child: Text('No orders yet',
                  style: TextStyle(color: AppTheme.textSecondary)),
            );
          }
          return ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: transactions.length,
            separatorBuilder: (_, __) => const SizedBox(height: 10),
            itemBuilder: (context, i) =>
                _OrderCard(tx: transactions[i], currentUserId: currentUserId),
          );
        },
      ),
    );
  }
}

class _OrderCard extends StatelessWidget {
  const _OrderCard({required this.tx, required this.currentUserId});

  final TransactionModel tx;
  final String currentUserId;

  @override
  Widget build(BuildContext context) {
    final isBuyer = tx.buyerId == currentUserId;
    final amountText = formatPrice(tx.amount, tx.currency);
    final statusConfig = _statusConfig(tx.status);

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: statusConfig.color.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  tx.status,
                  style: TextStyle(
                    fontSize: 12,
                    color: statusConfig.color,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
              const Spacer(),
              Text(
                isBuyer ? '- $amountText' : '+ $amountText',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                  color: isBuyer ? AppTheme.errorRed : AppTheme.successGreen,
                ),
              ),
            ],
          ),
          const SizedBox(height: 10),
          Row(
            children: [
              _infoChip(
                icon: Icons.payment,
                label: tx.paymentMethod,
              ),
              const SizedBox(width: 8),
              _infoChip(
                icon: isBuyer ? Icons.arrow_upward : Icons.arrow_downward,
                label: isBuyer ? 'Purchase' : 'Sale',
                color: isBuyer ? AppTheme.errorRed : AppTheme.successGreen,
              ),
            ],
          ),
          const SizedBox(height: 8),
          Text(
            formatThreadTime(tx.createdAt),
            style: const TextStyle(fontSize: 12, color: AppTheme.textSecondary),
          ),
          if (tx.gatewayReference != null) ...[
            const SizedBox(height: 4),
            Text(
              'Ref: ${tx.gatewayReference}',
              style: const TextStyle(fontSize: 11, color: AppTheme.textSecondary),
            ),
          ],
          if (tx.status == 'COMPLETED' && isBuyer) ...[
            const SizedBox(height: 12),
            const Divider(height: 1),
            const SizedBox(height: 10),
            GestureDetector(
              onTap: () => context.push(
                '/review',
                extra: {
                  'transactionId': tx.id,
                  'listingId': tx.listingId,
                  'revieweeId': tx.sellerId,
                },
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: const [
                  Icon(Icons.star_border_rounded,
                      size: 16, color: AppTheme.primary),
                  SizedBox(width: 4),
                  Text(
                    'Leave a Review',
                    style: TextStyle(
                      fontSize: 13,
                      color: AppTheme.primary,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ],
      ),
    );
  }

  Widget _infoChip({required IconData icon, required String label, Color? color}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: const Color(0xFFF3F4F6),
        borderRadius: BorderRadius.circular(6),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 12, color: color ?? AppTheme.textSecondary),
          const SizedBox(width: 4),
          Text(label,
              style: TextStyle(fontSize: 12, color: color ?? AppTheme.textSecondary)),
        ],
      ),
    );
  }

  _StatusConfig _statusConfig(String status) {
    return switch (status) {
      'COMPLETED' => _StatusConfig(AppTheme.successGreen),
      'PENDING' || 'PROCESSING' => _StatusConfig(AppTheme.warningAmber),
      'FAILED' => _StatusConfig(AppTheme.errorRed),
      'REFUNDED' => _StatusConfig(const Color(0xFF7C3AED)),
      _ => _StatusConfig(AppTheme.textSecondary),
    };
  }
}

class _StatusConfig {
  const _StatusConfig(this.color);
  final Color color;
}
