import 'package:dio/dio.dart';
import '../models/user_profile_model.dart';

class UserRemoteDataSource {
  UserRemoteDataSource(this._dio);

  final Dio _dio;

  Future<UserProfileModel> getProfile(String userId) async {
    final response = await _dio.get('/users/$userId');
    return UserProfileModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<UserProfileModel> updateProfile({
    String? displayName,
    String? bio,
    String? city,
    String? suburb,
  }) async {
    final response = await _dio.patch('/users/me', data: {
      if (displayName != null) 'displayName': displayName,
      if (bio != null) 'bio': bio,
      if (city != null) 'city': city,
      if (suburb != null) 'suburb': suburb,
    });
    return UserProfileModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<UserProfileModel> uploadAvatar(String filePath) async {
    final formData = FormData.fromMap({
      'file': await MultipartFile.fromFile(filePath),
    });
    final response = await _dio.post('/users/me/avatar', data: formData);
    return UserProfileModel.fromJson(response.data as Map<String, dynamic>);
  }
}
