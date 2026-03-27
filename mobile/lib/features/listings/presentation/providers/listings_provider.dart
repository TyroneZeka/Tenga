import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/datasources/listing_remote_datasource.dart';
import '../../data/models/listing_model.dart';
import '../../../../core/network/api_client.dart';

final listingRemoteDataSourceProvider = Provider<ListingRemoteDataSource>(
  (ref) => ListingRemoteDataSource(ApiClient.instance),
);

final listingsProvider = AsyncNotifierProvider.autoDispose
    .family<ListingsNotifier, List<ListingModel>, ListingsFilter>(
  ListingsNotifier.new,
);

class ListingsFilter {
  const ListingsFilter({this.query, this.city, this.categoryId, this.condition});

  final String? query;
  final String? city;
  final String? categoryId;
  final String? condition;

  @override
  bool operator ==(Object other) =>
      other is ListingsFilter &&
      other.query == query &&
      other.city == city &&
      other.categoryId == categoryId &&
      other.condition == condition;

  @override
  int get hashCode => Object.hash(query, city, categoryId, condition);
}

class ListingsNotifier
    extends AutoDisposeFamilyAsyncNotifier<List<ListingModel>, ListingsFilter> {
  int _page = 0;
  bool _hasMore = true;

  @override
  Future<List<ListingModel>> build(ListingsFilter arg) async {
    _page = 0;
    _hasMore = true;
    return _fetchPage();
  }

  Future<List<ListingModel>> _fetchPage() async {
    final ds = ref.read(listingRemoteDataSourceProvider);
    final result = await ds.getListings(
      query: arg.query,
      city: arg.city,
      categoryId: arg.categoryId,
      condition: arg.condition,
      page: _page,
    );
    _hasMore = result.meta.number + 1 < result.meta.totalPages;
    return result.content;
  }

  Future<void> loadMore() async {
    if (!_hasMore || state is AsyncLoading) return;
    final current = state.valueOrNull ?? [];
    _page++;
    final more = await _fetchPage();
    state = AsyncData([...current, ...more]);
  }

  Future<void> refresh() async {
    _page = 0;
    _hasMore = true;
    state = const AsyncLoading();
    state = await AsyncValue.guard(_fetchPage);
  }
}

final userListingsProvider = AsyncNotifierProvider.autoDispose
    .family<UserListingsNotifier, List<ListingModel>, String>(
  UserListingsNotifier.new,
);

class UserListingsNotifier
    extends AutoDisposeFamilyAsyncNotifier<List<ListingModel>, String> {
  int _page = 0;
  bool _hasMore = true;

  @override
  Future<List<ListingModel>> build(String arg) async {
    _page = 0;
    _hasMore = true;
    return _fetchPage();
  }

  Future<List<ListingModel>> _fetchPage() async {
    final ds = ref.read(listingRemoteDataSourceProvider);
    final result = await ds.getListingsByUser(arg, page: _page);
    _hasMore = result.meta.number + 1 < result.meta.totalPages;
    return result.content;
  }

  Future<void> loadMore() async {
    if (!_hasMore || state is AsyncLoading) return;
    final current = state.valueOrNull ?? [];
    _page++;
    final more = await _fetchPage();
    state = AsyncData([...current, ...more]);
  }
}

final listingDetailProvider = AsyncNotifierProvider.autoDispose
    .family<ListingDetailNotifier, ListingModel, String>(
  ListingDetailNotifier.new,
);

class ListingDetailNotifier
    extends AutoDisposeFamilyAsyncNotifier<ListingModel, String> {
  @override
  Future<ListingModel> build(String arg) async {
    final ds = ref.read(listingRemoteDataSourceProvider);
    return ds.getListingById(arg);
  }
}
