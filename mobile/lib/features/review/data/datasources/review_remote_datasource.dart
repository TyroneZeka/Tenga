import 'package:dio/dio.dart';
import '../models/review_model.dart';

class ReviewRemoteDataSource {
  ReviewRemoteDataSource(this._dio);

  final Dio _dio;

  Future<ReviewPage> getReviewsForUser(String userId,
      {int page = 0, int size = 20}) async {
    final response = await _dio.get(
      '/users/$userId/reviews',
      queryParameters: {'page': page, 'size': size},
    );
    return ReviewPage.fromJson(response.data as Map<String, dynamic>);
  }
}
