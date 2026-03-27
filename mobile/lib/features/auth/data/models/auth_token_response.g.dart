// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'auth_token_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$AuthTokenResponseImpl _$$AuthTokenResponseImplFromJson(
  Map<String, dynamic> json,
) => _$AuthTokenResponseImpl(
  accessToken: json['accessToken'] as String,
  tokenType: json['tokenType'] as String,
  expiresIn: (json['expiresIn'] as num).toInt(),
  role: json['role'] as String,
  userId: json['userId'] as String,
);

Map<String, dynamic> _$$AuthTokenResponseImplToJson(
  _$AuthTokenResponseImpl instance,
) => <String, dynamic>{
  'accessToken': instance.accessToken,
  'tokenType': instance.tokenType,
  'expiresIn': instance.expiresIn,
  'role': instance.role,
  'userId': instance.userId,
};
