import 'package:dio/dio.dart';
import '../models/review_model.dart';

class ReviewRemoteDataSource {
  ReviewRemoteDataSource(this._dio);

  final Dio _dio;

  Future<void> submitReview({
    required String transactionId,
    required String revieweeId,
    required String listingId,
    required int rating,
    String? comment,
  }) async {
    await _dio.post('/reviews', data: {
      'transactionId': transactionId,
      'revieweeId': revieweeId,
      'listingId': listingId,
      'rating': rating,
      if (comment != null && comment.isNotEmpty) 'comment': comment,
    });
  }

  Future<ReviewPage> getReviewsForUser(String userId,
      {int page = 0, int size = 20}) async {
    final response = await _dio.get(
      '/users/$userId/reviews',
      queryParameters: {'page': page, 'size': size},
    );
    return ReviewPage.fromJson(response.data as Map<String, dynamic>);
  }
}
