import 'package:flutter/foundation.dart';

class AppConfig {
  AppConfig._();

  // On Android emulator, localhost is 10.0.2.2.
  // On web (Chrome/Edge) and Windows desktop, it's localhost.
  // Override at build time: --dart-define=BASE_URL=https://api.tenga.co.zw/api/v1
  static String get baseUrl {
    const override = String.fromEnvironment('BASE_URL');
    if (override.isNotEmpty) return override;
    return kIsWeb ? 'http://localhost:8080/api/v1' : 'http://10.0.2.2:8080/api/v1';
  }

  static String get wsUrl {
    const override = String.fromEnvironment('WS_URL');
    if (override.isNotEmpty) return override;
    return kIsWeb ? 'http://localhost:8080/ws/chat' : 'http://10.0.2.2:8080/ws/chat';
  }

  static const int connectTimeoutMs = 15000;
  static const int receiveTimeoutMs = 30000;

  static const String accessTokenKey = 'access_token';
  static const String refreshTokenKey = 'refresh_token';
}
