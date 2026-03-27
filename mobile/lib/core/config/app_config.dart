class AppConfig {
  AppConfig._();

  // Override with --dart-define=BASE_URL=https://... at build time
  static const String baseUrl =
      String.fromEnvironment('BASE_URL', defaultValue: 'http://10.0.2.2:8080/api/v1');

  static const String wsUrl =
      String.fromEnvironment('WS_URL', defaultValue: 'http://10.0.2.2:8080/ws/chat');

  static const int connectTimeoutMs = 15000;
  static const int receiveTimeoutMs = 30000;

  static const String accessTokenKey = 'access_token';
  static const String refreshTokenKey = 'refresh_token';
}
