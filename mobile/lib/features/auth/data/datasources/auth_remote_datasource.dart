import 'package:dio/dio.dart';
import '../models/auth_token_response.dart';

class AuthRemoteDataSource {
  AuthRemoteDataSource(this._dio);

  final Dio _dio;

  Future<AuthTokenResponse> login(String username, String password) async {
    final response = await _dio.post('/auth/login', data: {
      'username': username,
      'password': password,
    });
    return AuthTokenResponse.fromJson(response.data as Map<String, dynamic>);
  }

  Future<void> register({
    required String displayName,
    required String phone,
    String? email,
    required String password,
  }) async {
    await _dio.post('/auth/register', data: {
      'displayName': displayName,
      'phone': phone,
      if (email != null && email.isNotEmpty) 'email': email,
      'password': password,
    });
  }

  Future<void> verifyOtp(String recipient, String code) async {
    await _dio.post(
      '/auth/otp/verify',
      data: {'recipient': recipient, 'code': code},
      options: Options(headers: {'X-Otp-Purpose': 'PHONE_VERIFICATION'}),
    );
  }

  Future<AuthTokenResponse> refresh(String refreshToken) async {
    final response = await _dio.post('/auth/refresh', data: {
      'refreshToken': refreshToken,
    });
    return AuthTokenResponse.fromJson(response.data as Map<String, dynamic>);
  }

  Future<void> logout(String? refreshToken) async {
    await _dio.post('/auth/logout', data: {
      if (refreshToken != null) 'refreshToken': refreshToken,
    });
  }
}
