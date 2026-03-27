import 'package:freezed_annotation/freezed_annotation.dart';

part 'auth_token_response.freezed.dart';
part 'auth_token_response.g.dart';

@freezed
class AuthTokenResponse with _$AuthTokenResponse {
  const factory AuthTokenResponse({
    required String accessToken,
    required String tokenType,
    required int expiresIn,
    required String role,
    required String userId,
  }) = _AuthTokenResponse;

  factory AuthTokenResponse.fromJson(Map<String, dynamic> json) =>
      _$AuthTokenResponseFromJson(json);
}
