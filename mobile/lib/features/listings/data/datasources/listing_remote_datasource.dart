import 'package:dio/dio.dart';
import '../models/listing_model.dart';

class ListingRemoteDataSource {
  ListingRemoteDataSource(this._dio);

  final Dio _dio;

  Future<ListingPage> getListings({
    String? query,
    String? city,
    String? categoryId,
    String? condition,
    int page = 0,
    int size = 20,
  }) async {
    final response = await _dio.get('/listings', queryParameters: {
      if (query != null && query.isNotEmpty) 'query': query,
      if (city != null && city.isNotEmpty) 'city': city,
      if (categoryId != null) 'categoryId': categoryId,
      if (condition != null) 'condition': condition,
      'page': page,
      'size': size,
    });
    return ListingPage.fromJson(response.data as Map<String, dynamic>);
  }

  Future<ListingModel> getListingById(String id) async {
    final response = await _dio.get('/listings/$id');
    return ListingModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<ListingModel> createListing({
    required String title,
    required String description,
    required double price,
    required String currency,
    required String condition,
    required String categoryId,
    required bool negotiable,
    String? city,
    String? suburb,
  }) async {
    final response = await _dio.post('/listings', data: {
      'title': title,
      'description': description,
      'price': price,
      'currency': currency,
      'condition': condition,
      'categoryId': categoryId,
      'negotiable': negotiable,
      if (city != null) 'city': city,
      if (suburb != null) 'suburb': suburb,
    });
    return ListingModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<ListingModel> publishListing(String id) async {
    final response = await _dio.post('/listings/$id/publish');
    return ListingModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<ListingModel> uploadImage(String listingId, String filePath) async {
    final formData = FormData.fromMap({
      'file': await MultipartFile.fromFile(filePath),
    });
    final response = await _dio.post(
      '/listings/$listingId/images',
      data: formData,
    );
    return ListingModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<void> deleteImage(String listingId, String imageId) async {
    await _dio.delete('/listings/$listingId/images/$imageId');
  }
}
