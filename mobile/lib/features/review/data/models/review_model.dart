import 'package:freezed_annotation/freezed_annotation.dart';

part 'review_model.freezed.dart';
part 'review_model.g.dart';

@freezed
class ReviewModel with _$ReviewModel {
  const factory ReviewModel({
    required String id,
    required String revieweeId,
    required String reviewerId,
    String? listingId,
    required int rating,
    String? comment,
    required String createdAt,
  }) = _ReviewModel;

  factory ReviewModel.fromJson(Map<String, dynamic> json) =>
      _$ReviewModelFromJson(json);
}

class ReviewPage {
  const ReviewPage({required this.content, required this.totalElements});

  final List<ReviewModel> content;
  final int totalElements;

  factory ReviewPage.fromJson(Map<String, dynamic> json) {
    return ReviewPage(
      content: (json['content'] as List)
          .map((e) => ReviewModel.fromJson(e as Map<String, dynamic>))
          .toList(),
      totalElements: (json['totalElements'] as num).toInt(),
    );
  }
}
