import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/datasources/review_remote_datasource.dart';
import '../../data/models/review_model.dart';
import '../../../../core/network/api_client.dart';

final reviewRemoteDataSourceProvider = Provider<ReviewRemoteDataSource>(
  (ref) => ReviewRemoteDataSource(ApiClient.instance),
);

final userReviewsProvider = AsyncNotifierProvider.autoDispose
    .family<UserReviewsNotifier, List<ReviewModel>, String>(
  UserReviewsNotifier.new,
);

class UserReviewsNotifier
    extends AutoDisposeFamilyAsyncNotifier<List<ReviewModel>, String> {
  int _page = 0;
  bool _hasMore = true;

  @override
  Future<List<ReviewModel>> build(String arg) async {
    _page = 0;
    _hasMore = true;
    return _fetchPage();
  }

  Future<List<ReviewModel>> _fetchPage() async {
    final ds = ref.read(reviewRemoteDataSourceProvider);
    final result = await ds.getReviewsForUser(arg, page: _page);
    _hasMore = result.content.length >= 20;
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
