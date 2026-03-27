// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_profile_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$UserProfileModelImpl _$$UserProfileModelImplFromJson(
  Map<String, dynamic> json,
) => _$UserProfileModelImpl(
  id: json['id'] as String,
  userId: json['userId'] as String,
  displayName: json['displayName'] as String,
  bio: json['bio'] as String?,
  avatarUrl: json['avatarUrl'] as String?,
  city: json['city'] as String?,
  suburb: json['suburb'] as String?,
  activeListingCount: _toInt(json['activeListingCount']),
  trustScore: _toDouble(json['trustScore']),
  memberSince: json['memberSince'] as String?,
);

Map<String, dynamic> _$$UserProfileModelImplToJson(
  _$UserProfileModelImpl instance,
) => <String, dynamic>{
  'id': instance.id,
  'userId': instance.userId,
  'displayName': instance.displayName,
  'bio': instance.bio,
  'avatarUrl': instance.avatarUrl,
  'city': instance.city,
  'suburb': instance.suburb,
  'activeListingCount': instance.activeListingCount,
  'trustScore': instance.trustScore,
  'memberSince': instance.memberSince,
};
