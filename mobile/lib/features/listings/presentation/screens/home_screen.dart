import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../providers/listings_provider.dart';
import '../../data/models/listing_model.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../shared/utils/currency_formatter.dart';

class HomeScreen extends ConsumerStatefulWidget {
  const HomeScreen({super.key});

  @override
  ConsumerState<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends ConsumerState<HomeScreen> {
  final _searchController = TextEditingController();
  String? _selectedCity;
  String _searchQuery = '';

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  ListingsFilter get _filter => ListingsFilter(
        query: _searchQuery.isEmpty ? null : _searchQuery,
        city: _selectedCity,
      );

  @override
  Widget build(BuildContext context) {
    final listingsAsync = ref.watch(listingsProvider(_filter));

    return Scaffold(
      backgroundColor: AppTheme.background,
      body: RefreshIndicator(
        color: AppTheme.primary,
        onRefresh: () => ref.read(listingsProvider(_filter).notifier).refresh(),
        child: CustomScrollView(
          slivers: [
            _buildAppBar(context),
            _buildSearchBar(),
            _buildCategoryFilters(),
            listingsAsync.when(
              loading: () => const SliverFillRemaining(
                child: Center(child: CircularProgressIndicator(color: AppTheme.primary)),
              ),
              error: (e, _) => SliverFillRemaining(
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.wifi_off, size: 48, color: AppTheme.textSecondary),
                      const SizedBox(height: 12),
                      Text('Failed to load listings',
                          style: const TextStyle(color: AppTheme.textSecondary)),
                      const SizedBox(height: 12),
                      ElevatedButton(
                        onPressed: () =>
                            ref.read(listingsProvider(_filter).notifier).refresh(),
                        style: ElevatedButton.styleFrom(
                            minimumSize: const Size(120, 40)),
                        child: const Text('Retry'),
                      ),
                    ],
                  ),
                ),
              ),
              data: (listings) => listings.isEmpty
                  ? const SliverFillRemaining(
                      child: Center(
                        child: Text('No listings found',
                            style: TextStyle(color: AppTheme.textSecondary)),
                      ),
                    )
                  : _ListingsGrid(
                      listings: listings,
                      onLoadMore: () =>
                          ref.read(listingsProvider(_filter).notifier).loadMore(),
                    ),
            ),
          ],
        ),
      ),
    );
  }

  SliverAppBar _buildAppBar(BuildContext context) {
    return SliverAppBar(
      floating: true,
      snap: true,
      backgroundColor: Colors.white,
      title: Row(
        children: [
          Container(
            width: 30,
            height: 30,
            decoration: BoxDecoration(
              color: AppTheme.primary,
              borderRadius: BorderRadius.circular(8),
            ),
            child: const Icon(Icons.shopping_bag_outlined, color: Colors.white, size: 18),
          ),
          const SizedBox(width: 8),
          const Text('Tenga',
              style: TextStyle(
                  color: AppTheme.textPrimary,
                  fontSize: 20,
                  fontWeight: FontWeight.bold)),
        ],
      ),
      actions: [
        IconButton(
          icon: const Icon(Icons.notifications_outlined, color: AppTheme.textPrimary),
          onPressed: () {},
        ),
      ],
    );
  }

  SliverToBoxAdapter _buildSearchBar() {
    return SliverToBoxAdapter(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(16, 12, 16, 0),
        child: TextField(
          controller: _searchController,
          decoration: InputDecoration(
            hintText: 'Search listings...',
            prefixIcon: const Icon(Icons.search, color: AppTheme.textSecondary),
            suffixIcon: _searchQuery.isNotEmpty
                ? IconButton(
                    icon: const Icon(Icons.clear),
                    onPressed: () {
                      _searchController.clear();
                      setState(() => _searchQuery = '');
                    },
                  )
                : null,
          ),
          onSubmitted: (v) => setState(() => _searchQuery = v.trim()),
          textInputAction: TextInputAction.search,
        ),
      ),
    );
  }

  SliverToBoxAdapter _buildCategoryFilters() {
    final cities = ['All', 'Harare', 'Bulawayo', 'Mutare', 'Gweru', 'Masvingo'];
    return SliverToBoxAdapter(
      child: SizedBox(
        height: 48,
        child: ListView.separated(
          scrollDirection: Axis.horizontal,
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          itemCount: cities.length,
          separatorBuilder: (_, __) => const SizedBox(width: 8),
          itemBuilder: (context, i) {
            final city = cities[i];
            final isSelected =
                city == 'All' ? _selectedCity == null : _selectedCity == city;
            return GestureDetector(
              onTap: () => setState(
                  () => _selectedCity = city == 'All' ? null : city),
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                decoration: BoxDecoration(
                  color: isSelected ? AppTheme.primary : Colors.white,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(
                    color: isSelected ? AppTheme.primary : const Color(0xFFE5E7EB),
                  ),
                ),
                child: Text(
                  city,
                  style: TextStyle(
                    color: isSelected ? Colors.white : AppTheme.textSecondary,
                    fontSize: 13,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

class _ListingsGrid extends StatelessWidget {
  const _ListingsGrid({required this.listings, required this.onLoadMore});

  final List<ListingModel> listings;
  final VoidCallback onLoadMore;

  @override
  Widget build(BuildContext context) {
    return SliverPadding(
      padding: const EdgeInsets.all(12),
      sliver: SliverGrid(
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          mainAxisSpacing: 10,
          crossAxisSpacing: 10,
          childAspectRatio: 0.72,
        ),
        delegate: SliverChildBuilderDelegate(
          (context, i) {
            if (i == listings.length - 4) onLoadMore();
            return _ListingCard(listing: listings[i]);
          },
          childCount: listings.length,
        ),
      ),
    );
  }
}

class _ListingCard extends StatelessWidget {
  const _ListingCard({required this.listing});

  final ListingModel listing;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => context.push('/listings/${listing.id}'),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: ClipRRect(
                borderRadius:
                    const BorderRadius.vertical(top: Radius.circular(12)),
                child: listing.imageUrls.isNotEmpty
                    ? CachedNetworkImage(
                        imageUrl: listing.imageUrls.first,
                        fit: BoxFit.cover,
                        width: double.infinity,
                        placeholder: (_, __) => Container(color: const Color(0xFFF3F4F6)),
                        errorWidget: (_, __, ___) => _placeholder(),
                      )
                    : _placeholder(),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    listing.title,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(
                      fontSize: 13,
                      fontWeight: FontWeight.w500,
                      color: AppTheme.textPrimary,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    formatPrice(listing.price, listing.currency),
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.bold,
                      color: AppTheme.primary,
                    ),
                  ),
                  if (listing.city != null) ...[
                    const SizedBox(height: 4),
                    Row(
                      children: [
                        const Icon(Icons.location_on_outlined,
                            size: 12, color: AppTheme.textSecondary),
                        const SizedBox(width: 2),
                        Expanded(
                          child: Text(
                            listing.city!,
                            style: const TextStyle(
                                fontSize: 11, color: AppTheme.textSecondary),
                            overflow: TextOverflow.ellipsis,
                          ),
                        ),
                      ],
                    ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _placeholder() {
    return Container(
      color: const Color(0xFFF3F4F6),
      child: const Center(
        child: Icon(Icons.image_outlined, size: 40, color: Color(0xFFD1D5DB)),
      ),
    );
  }
}
