import 'package:freezed_annotation/freezed_annotation.dart';

part 'user_profile_model.freezed.dart';
part 'user_profile_model.g.dart';

double _toDouble(dynamic value) => value == null ? 0.0 : (value as num).toDouble();
int _toInt(dynamic value) => value == null ? 0 : (value as num).toInt();

@freezed
class UserProfileModel with _$UserProfileModel {
  const factory UserProfileModel({
    required String id,
    required String userId,
    required String displayName,
    String? bio,
    String? avatarUrl,
    String? city,
    String? suburb,
    @JsonKey(fromJson: _toInt) required int activeListingCount,
    @JsonKey(fromJson: _toDouble) required double trustScore,
    String? memberSince,
  }) = _UserProfileModel;

  factory UserProfileModel.fromJson(Map<String, dynamic> json) =>
      _$UserProfileModelFromJson(json);
}
