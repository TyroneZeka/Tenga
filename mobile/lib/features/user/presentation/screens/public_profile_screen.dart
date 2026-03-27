import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/user_provider.dart';
import '../../../listings/presentation/providers/listings_provider.dart';
import '../../../review/presentation/providers/review_provider.dart';
import '../../../review/data/models/review_model.dart';
import '../../../../shared/widgets/listing_card.dart';
import '../../../../core/theme/app_theme.dart';

class PublicProfileScreen extends ConsumerWidget {
  const PublicProfileScreen({super.key, required this.userId});

  final String userId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final profileAsync = ref.watch(userProfileProvider(userId));

    return profileAsync.when(
      loading: () => const Scaffold(
        body: Center(child: CircularProgressIndicator(color: AppTheme.primary)),
      ),
      error: (e, _) => Scaffold(
        appBar: AppBar(),
        body: Center(child: Text('Failed to load profile: $e')),
      ),
      data: (profile) => DefaultTabController(
        length: 2,
        child: Scaffold(
          backgroundColor: AppTheme.background,
          body: NestedScrollView(
            headerSliverBuilder: (context, innerBoxIsScrolled) => [
              SliverAppBar(
                expandedHeight: 220,
                pinned: true,
                forceElevated: innerBoxIsScrolled,
                backgroundColor: AppTheme.darkSurface,
                foregroundColor: Colors.white,
                flexibleSpace: FlexibleSpaceBar(
                  collapseMode: CollapseMode.pin,
                  background: _ProfileHero(profile: profile),
                ),
                bottom: const TabBar(
                  labelColor: Colors.white,
                  unselectedLabelColor: Colors.white54,
                  indicatorColor: AppTheme.primary,
                  indicatorWeight: 3,
                  tabs: [
                    Tab(text: 'Listings'),
                    Tab(text: 'Reviews'),
                  ],
                ),
              ),
            ],
            body: TabBarView(
              children: [
                _ListingsTab(userId: userId),
                _ReviewsTab(userId: userId),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _ProfileHero extends StatelessWidget {
  const _ProfileHero({required this.profile});

  final dynamic profile;

  @override
  Widget build(BuildContext context) {
    return Container(
      color: AppTheme.darkSurface,
      child: SafeArea(
        child: Padding(
          padding: const EdgeInsets.only(bottom: 48),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              _buildAvatar(profile),
              const SizedBox(height: 12),
              Text(
                profile.displayName,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              if (profile.city != null)
                Padding(
                  padding: const EdgeInsets.only(top: 4),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.location_on_outlined,
                          size: 14, color: Colors.white60),
                      Text(
                        profile.city!,
                        style: const TextStyle(
                            color: Colors.white60, fontSize: 13),
                      ),
                    ],
                  ),
                ),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _StatChip(
                    value: profile.activeListingCount.toString(),
                    label: 'listings',
                  ),
                  const SizedBox(width: 16),
                  _StatChip(
                    value: profile.trustScore.toStringAsFixed(1),
                    label: 'trust',
                    icon: Icons.star,
                    iconColor: const Color(0xFFFBBF24),
                  ),
                  const SizedBox(width: 16),
                  _StatChip(
                    value: _memberSince(profile.memberSince),
                    label: 'since',
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildAvatar(profile) {
    if (profile.avatarUrl != null) {
      return CircleAvatar(
        radius: 36,
        backgroundImage: NetworkImage(profile.avatarUrl!),
      );
    }
    return CircleAvatar(
      radius: 36,
      backgroundColor: AppTheme.primary,
      child: Text(
        profile.displayName.isNotEmpty
            ? profile.displayName[0].toUpperCase()
            : '?',
        style: const TextStyle(
            color: Colors.white, fontSize: 28, fontWeight: FontWeight.bold),
      ),
    );
  }

  String _memberSince(String? isoString) {
    if (isoString == null) return '—';
    try {
      return DateTime.parse(isoString).year.toString();
    } catch (_) {
      return '—';
    }
  }
}

class _StatChip extends StatelessWidget {
  const _StatChip({
    required this.value,
    required this.label,
    this.icon,
    this.iconColor,
  });

  final String value;
  final String label;
  final IconData? icon;
  final Color? iconColor;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        if (icon != null) ...[
          Icon(icon, size: 14, color: iconColor ?? Colors.white),
          const SizedBox(width: 3),
        ],
        Text(
          value,
          style: const TextStyle(
              color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
        ),
        const SizedBox(width: 3),
        Text(
          label,
          style: const TextStyle(color: Colors.white60, fontSize: 12),
        ),
      ],
    );
  }
}

// ---------------------------------------------------------------------------
// Listings tab
// ---------------------------------------------------------------------------

class _ListingsTab extends ConsumerWidget {
  const _ListingsTab({required this.userId});

  final String userId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final listingsAsync = ref.watch(userListingsProvider(userId));

    return listingsAsync.when(
      loading: () =>
          const Center(child: CircularProgressIndicator(color: AppTheme.primary)),
      error: (e, _) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.wifi_off, size: 40, color: AppTheme.textSecondary),
            const SizedBox(height: 12),
            Text('Failed to load listings',
                style: const TextStyle(color: AppTheme.textSecondary)),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () =>
                  ref.invalidate(userListingsProvider(userId)),
              style: ElevatedButton.styleFrom(minimumSize: const Size(100, 36)),
              child: const Text('Retry'),
            ),
          ],
        ),
      ),
      data: (listings) {
        if (listings.isEmpty) {
          return const Center(
            child: Text('No listings yet',
                style: TextStyle(color: AppTheme.textSecondary)),
          );
        }
        return GridView.builder(
          padding: const EdgeInsets.all(12),
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            mainAxisSpacing: 10,
            crossAxisSpacing: 10,
            childAspectRatio: 0.72,
          ),
          itemCount: listings.length,
          itemBuilder: (context, i) {
            if (i == listings.length - 4) {
              ref.read(userListingsProvider(userId).notifier).loadMore();
            }
            return ListingCard(listing: listings[i]);
          },
        );
      },
    );
  }
}

// ---------------------------------------------------------------------------
// Reviews tab
// ---------------------------------------------------------------------------

class _ReviewsTab extends ConsumerWidget {
  const _ReviewsTab({required this.userId});

  final String userId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final reviewsAsync = ref.watch(userReviewsProvider(userId));

    return reviewsAsync.when(
      loading: () =>
          const Center(child: CircularProgressIndicator(color: AppTheme.primary)),
      error: (e, _) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.wifi_off, size: 40, color: AppTheme.textSecondary),
            const SizedBox(height: 12),
            Text('Failed to load reviews',
                style: const TextStyle(color: AppTheme.textSecondary)),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () => ref.invalidate(userReviewsProvider(userId)),
              style: ElevatedButton.styleFrom(minimumSize: const Size(100, 36)),
              child: const Text('Retry'),
            ),
          ],
        ),
      ),
      data: (reviews) {
        if (reviews.isEmpty) {
          return const Center(
            child: Text('No reviews yet',
                style: TextStyle(color: AppTheme.textSecondary)),
          );
        }
        return ListView.separated(
          padding: const EdgeInsets.all(16),
          itemCount: reviews.length,
          separatorBuilder: (_, __) => const SizedBox(height: 10),
          itemBuilder: (context, i) {
            if (i == reviews.length - 3) {
              ref.read(userReviewsProvider(userId).notifier).loadMore();
            }
            return _ReviewCard(review: reviews[i]);
          },
        );
      },
    );
  }
}

class _ReviewCard extends StatelessWidget {
  const _ReviewCard({required this.review});

  final ReviewModel review;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFFE5E7EB)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              CircleAvatar(
                radius: 18,
                backgroundColor: AppTheme.primary.withValues(alpha: 0.15),
                child: Text(
                  '?',
                  style: TextStyle(
                      color: AppTheme.primary,
                      fontSize: 14,
                      fontWeight: FontWeight.bold),
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _StarRow(rating: review.rating),
                    const SizedBox(height: 2),
                    Text(
                      _formatDate(review.createdAt),
                      style: const TextStyle(
                          fontSize: 11, color: AppTheme.textSecondary),
                    ),
                  ],
                ),
              ),
            ],
          ),
          if (review.comment != null && review.comment!.isNotEmpty) ...[
            const SizedBox(height: 10),
            Text(
              review.comment!,
              style: const TextStyle(
                  fontSize: 14,
                  color: AppTheme.textPrimary,
                  height: 1.5),
            ),
          ],
        ],
      ),
    );
  }

  String _formatDate(String isoString) {
    try {
      final dt = DateTime.parse(isoString);
      return '${dt.day}/${dt.month}/${dt.year}';
    } catch (_) {
      return '';
    }
  }
}

class _StarRow extends StatelessWidget {
  const _StarRow({required this.rating});

  final int rating;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: List.generate(5, (i) {
        return Icon(
          i < rating ? Icons.star : Icons.star_border,
          size: 14,
          color: const Color(0xFFFBBF24),
        );
      }),
    );
  }
}
