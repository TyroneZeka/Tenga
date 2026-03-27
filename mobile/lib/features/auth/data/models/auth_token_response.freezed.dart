// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'auth_token_response.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
  'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models',
);

AuthTokenResponse _$AuthTokenResponseFromJson(Map<String, dynamic> json) {
  return _AuthTokenResponse.fromJson(json);
}

/// @nodoc
mixin _$AuthTokenResponse {
  String get accessToken => throw _privateConstructorUsedError;
  String get tokenType => throw _privateConstructorUsedError;
  int get expiresIn => throw _privateConstructorUsedError;
  String get role => throw _privateConstructorUsedError;
  String get userId => throw _privateConstructorUsedError;

  /// Serializes this AuthTokenResponse to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of AuthTokenResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $AuthTokenResponseCopyWith<AuthTokenResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuthTokenResponseCopyWith<$Res> {
  factory $AuthTokenResponseCopyWith(
    AuthTokenResponse value,
    $Res Function(AuthTokenResponse) then,
  ) = _$AuthTokenResponseCopyWithImpl<$Res, AuthTokenResponse>;
  @useResult
  $Res call({
    String accessToken,
    String tokenType,
    int expiresIn,
    String role,
    String userId,
  });
}

/// @nodoc
class _$AuthTokenResponseCopyWithImpl<$Res, $Val extends AuthTokenResponse>
    implements $AuthTokenResponseCopyWith<$Res> {
  _$AuthTokenResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of AuthTokenResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = null,
    Object? tokenType = null,
    Object? expiresIn = null,
    Object? role = null,
    Object? userId = null,
  }) {
    return _then(
      _value.copyWith(
            accessToken: null == accessToken
                ? _value.accessToken
                : accessToken // ignore: cast_nullable_to_non_nullable
                      as String,
            tokenType: null == tokenType
                ? _value.tokenType
                : tokenType // ignore: cast_nullable_to_non_nullable
                      as String,
            expiresIn: null == expiresIn
                ? _value.expiresIn
                : expiresIn // ignore: cast_nullable_to_non_nullable
                      as int,
            role: null == role
                ? _value.role
                : role // ignore: cast_nullable_to_non_nullable
                      as String,
            userId: null == userId
                ? _value.userId
                : userId // ignore: cast_nullable_to_non_nullable
                      as String,
          )
          as $Val,
    );
  }
}

/// @nodoc
abstract class _$$AuthTokenResponseImplCopyWith<$Res>
    implements $AuthTokenResponseCopyWith<$Res> {
  factory _$$AuthTokenResponseImplCopyWith(
    _$AuthTokenResponseImpl value,
    $Res Function(_$AuthTokenResponseImpl) then,
  ) = __$$AuthTokenResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({
    String accessToken,
    String tokenType,
    int expiresIn,
    String role,
    String userId,
  });
}

/// @nodoc
class __$$AuthTokenResponseImplCopyWithImpl<$Res>
    extends _$AuthTokenResponseCopyWithImpl<$Res, _$AuthTokenResponseImpl>
    implements _$$AuthTokenResponseImplCopyWith<$Res> {
  __$$AuthTokenResponseImplCopyWithImpl(
    _$AuthTokenResponseImpl _value,
    $Res Function(_$AuthTokenResponseImpl) _then,
  ) : super(_value, _then);

  /// Create a copy of AuthTokenResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? accessToken = null,
    Object? tokenType = null,
    Object? expiresIn = null,
    Object? role = null,
    Object? userId = null,
  }) {
    return _then(
      _$AuthTokenResponseImpl(
        accessToken: null == accessToken
            ? _value.accessToken
            : accessToken // ignore: cast_nullable_to_non_nullable
                  as String,
        tokenType: null == tokenType
            ? _value.tokenType
            : tokenType // ignore: cast_nullable_to_non_nullable
                  as String,
        expiresIn: null == expiresIn
            ? _value.expiresIn
            : expiresIn // ignore: cast_nullable_to_non_nullable
                  as int,
        role: null == role
            ? _value.role
            : role // ignore: cast_nullable_to_non_nullable
                  as String,
        userId: null == userId
            ? _value.userId
            : userId // ignore: cast_nullable_to_non_nullable
                  as String,
      ),
    );
  }
}

/// @nodoc
@JsonSerializable()
class _$AuthTokenResponseImpl implements _AuthTokenResponse {
  const _$AuthTokenResponseImpl({
    required this.accessToken,
    required this.tokenType,
    required this.expiresIn,
    required this.role,
    required this.userId,
  });

  factory _$AuthTokenResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$AuthTokenResponseImplFromJson(json);

  @override
  final String accessToken;
  @override
  final String tokenType;
  @override
  final int expiresIn;
  @override
  final String role;
  @override
  final String userId;

  @override
  String toString() {
    return 'AuthTokenResponse(accessToken: $accessToken, tokenType: $tokenType, expiresIn: $expiresIn, role: $role, userId: $userId)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuthTokenResponseImpl &&
            (identical(other.accessToken, accessToken) ||
                other.accessToken == accessToken) &&
            (identical(other.tokenType, tokenType) ||
                other.tokenType == tokenType) &&
            (identical(other.expiresIn, expiresIn) ||
                other.expiresIn == expiresIn) &&
            (identical(other.role, role) || other.role == role) &&
            (identical(other.userId, userId) || other.userId == userId));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode =>
      Object.hash(runtimeType, accessToken, tokenType, expiresIn, role, userId);

  /// Create a copy of AuthTokenResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$AuthTokenResponseImplCopyWith<_$AuthTokenResponseImpl> get copyWith =>
      __$$AuthTokenResponseImplCopyWithImpl<_$AuthTokenResponseImpl>(
        this,
        _$identity,
      );

  @override
  Map<String, dynamic> toJson() {
    return _$$AuthTokenResponseImplToJson(this);
  }
}

abstract class _AuthTokenResponse implements AuthTokenResponse {
  const factory _AuthTokenResponse({
    required final String accessToken,
    required final String tokenType,
    required final int expiresIn,
    required final String role,
    required final String userId,
  }) = _$AuthTokenResponseImpl;

  factory _AuthTokenResponse.fromJson(Map<String, dynamic> json) =
      _$AuthTokenResponseImpl.fromJson;

  @override
  String get accessToken;
  @override
  String get tokenType;
  @override
  int get expiresIn;
  @override
  String get role;
  @override
  String get userId;

  /// Create a copy of AuthTokenResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$AuthTokenResponseImplCopyWith<_$AuthTokenResponseImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
