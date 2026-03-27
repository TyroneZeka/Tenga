import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../../data/datasources/auth_remote_datasource.dart';
import '../../domain/entities/auth_state.dart';
import '../../../../core/config/app_config.dart';
import '../../../../core/network/api_client.dart';

final authProvider = AsyncNotifierProvider<AuthNotifier, AuthState>(AuthNotifier.new);

class AuthNotifier extends AsyncNotifier<AuthState> {
  late final AuthRemoteDataSource _dataSource;
  late final FlutterSecureStorage _storage;

  @override
  Future<AuthState> build() async {
    _dataSource = AuthRemoteDataSource(ApiClient.instance);
    _storage = const FlutterSecureStorage();

    final token = await _storage.read(key: AppConfig.accessTokenKey);
    final userId = await _storage.read(key: 'user_id');
    final role = await _storage.read(key: 'user_role');

    if (token != null && userId != null) {
      return AuthState(
        accessToken: token,
        userId: userId,
        role: role,
        isLoggedIn: true,
      );
    }
    return const AuthState.initial();
  }

  Future<void> login(String username, String password) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      final result = await _dataSource.login(username, password);
      await _persistTokens(result.accessToken, null, result.userId, result.role);
      return AuthState(
        accessToken: result.accessToken,
        userId: result.userId,
        role: result.role,
        isLoggedIn: true,
      );
    });
  }

  Future<void> register({
    required String displayName,
    required String phone,
    String? email,
    required String password,
  }) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      await _dataSource.register(
        displayName: displayName,
        phone: phone,
        email: email,
        password: password,
      );
      // Registration triggers OTP — stay logged out until verified
      return const AuthState.initial();
    });
  }

  Future<void> verifyOtp(String recipient, String code) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      await _dataSource.verifyOtp(recipient, code);
      return const AuthState.initial();
    });
  }

  Future<void> logout() async {
    final refreshToken = await _storage.read(key: AppConfig.refreshTokenKey);
    try {
      await _dataSource.logout(refreshToken);
    } catch (_) {
      // Proceed with local logout even if server call fails
    }
    await _storage.deleteAll();
    state = const AsyncData(AuthState.initial());
  }

  Future<void> _persistTokens(
    String accessToken,
    String? refreshToken,
    String userId,
    String role,
  ) async {
    await _storage.write(key: AppConfig.accessTokenKey, value: accessToken);
    await _storage.write(key: 'user_id', value: userId);
    await _storage.write(key: 'user_role', value: role);
    if (refreshToken != null) {
      await _storage.write(key: AppConfig.refreshTokenKey, value: refreshToken);
    }
  }
}
