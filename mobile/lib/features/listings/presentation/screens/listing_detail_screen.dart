import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../providers/listings_provider.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../shared/utils/currency_formatter.dart';

class ListingDetailScreen extends ConsumerWidget {
  const ListingDetailScreen({super.key, required this.id});

  final String id;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final listingAsync = ref.watch(listingDetailProvider(id));

    return listingAsync.when(
      loading: () => const Scaffold(
        body: Center(child: CircularProgressIndicator(color: AppTheme.primary)),
      ),
      error: (e, _) => Scaffold(
        appBar: AppBar(),
        body: Center(child: Text('Failed to load listing: $e')),
      ),
      data: (listing) {
        return Scaffold(
          backgroundColor: Colors.white,
          body: CustomScrollView(
            slivers: [
              SliverAppBar(
                expandedHeight: 320,
                pinned: true,
                backgroundColor: Colors.white,
                foregroundColor: Colors.white,
                flexibleSpace: FlexibleSpaceBar(
                  background: _ImageCarousel(imageUrls: listing.imageUrls),
                ),
              ),
              SliverToBoxAdapter(
                child: Padding(
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Expanded(
                            child: Text(
                              listing.title,
                              style: const TextStyle(
                                  fontSize: 20,
                                  fontWeight: FontWeight.bold,
                                  color: AppTheme.textPrimary),
                            ),
                          ),
                          const SizedBox(width: 12),
                          Text(
                            formatPrice(listing.price, listing.currency),
                            style: const TextStyle(
                                fontSize: 22,
                                fontWeight: FontWeight.bold,
                                color: AppTheme.primary),
                          ),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Row(
                        children: [
                          _Badge(
                            label: listing.condition.replaceAll('_', ' '),
                            color: const Color(0xFF2563EB),
                          ),
                          if (listing.negotiable) ...[
                            const SizedBox(width: 8),
                            const _Badge(label: 'Negotiable', color: AppTheme.successGreen),
                          ],
                          if (listing.city != null) ...[
                            const SizedBox(width: 8),
                            Row(
                              children: [
                                const Icon(Icons.location_on_outlined,
                                    size: 14, color: AppTheme.textSecondary),
                                Text(
                                  listing.city!,
                                  style: const TextStyle(
                                      fontSize: 13, color: AppTheme.textSecondary),
                                ),
                              ],
                            ),
                          ],
                        ],
                      ),
                      const SizedBox(height: 20),
                      const Divider(),
                      const SizedBox(height: 16),
                      const Text('Description',
                          style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.w600,
                              color: AppTheme.textPrimary)),
                      const SizedBox(height: 8),
                      Text(
                        listing.description,
                        style: const TextStyle(
                            fontSize: 15,
                            color: AppTheme.textSecondary,
                            height: 1.6),
                      ),
                      const SizedBox(height: 100),
                    ],
                  ),
                ),
              ),
            ],
          ),
          bottomNavigationBar: _ActionBar(
            listingId: listing.id,
            sellerId: listing.sellerId,
            price: listing.price,
            currency: listing.currency,
          ),
        );
      },
    );
  }
}

class _ImageCarousel extends StatefulWidget {
  const _ImageCarousel({required this.imageUrls});

  final List<String> imageUrls;

  @override
  State<_ImageCarousel> createState() => _ImageCarouselState();
}

class _ImageCarouselState extends State<_ImageCarousel> {
  int _current = 0;

  @override
  Widget build(BuildContext context) {
    if (widget.imageUrls.isEmpty) {
      return Container(
        color: const Color(0xFFF3F4F6),
        child: const Center(
          child: Icon(Icons.image_outlined, size: 64, color: Color(0xFFD1D5DB)),
        ),
      );
    }

    return Stack(
      children: [
        PageView.builder(
          itemCount: widget.imageUrls.length,
          onPageChanged: (i) => setState(() => _current = i),
          itemBuilder: (context, i) => CachedNetworkImage(
            imageUrl: widget.imageUrls[i],
            fit: BoxFit.cover,
            width: double.infinity,
          ),
        ),
        if (widget.imageUrls.length > 1)
          Positioned(
            bottom: 12,
            left: 0,
            right: 0,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(
                widget.imageUrls.length,
                (i) => Container(
                  width: i == _current ? 20 : 8,
                  height: 8,
                  margin: const EdgeInsets.symmetric(horizontal: 3),
                  decoration: BoxDecoration(
                    color: i == _current ? AppTheme.primary : Colors.white60,
                    borderRadius: BorderRadius.circular(4),
                  ),
                ),
              ),
            ),
          ),
      ],
    );
  }
}

class _ActionBar extends StatelessWidget {
  const _ActionBar({
    required this.listingId,
    required this.sellerId,
    required this.price,
    required this.currency,
  });

  final String listingId;
  final String sellerId;
  final double price;
  final String currency;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(16, 12, 16, 28),
      decoration: const BoxDecoration(
        color: Colors.white,
        border: Border(top: BorderSide(color: Color(0xFFE5E7EB))),
      ),
      child: Row(
        children: [
          Expanded(
            child: OutlinedButton.icon(
              onPressed: () => context.push(
                '/chat/new',
                extra: {'listingId': listingId, 'sellerId': sellerId},
              ),
              icon: const Icon(Icons.chat_bubble_outline, size: 18),
              label: const Text('Chat'),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: ElevatedButton.icon(
              onPressed: () => context.push(
                '/payment',
                extra: {
                  'listingId': listingId,
                  'sellerId': sellerId,
                  'amount': price,
                  'currency': currency,
                },
              ),
              icon: const Icon(Icons.payment, size: 18),
              label: const Text('Pay'),
            ),
          ),
        ],
      ),
    );
  }
}

class _Badge extends StatelessWidget {
  const _Badge({required this.label, required this.color});

  final String label;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.3)),
      ),
      child: Text(
        label,
        style: TextStyle(
          fontSize: 12,
          color: color,
          fontWeight: FontWeight.w500,
        ),
      ),
    );
  }
}
