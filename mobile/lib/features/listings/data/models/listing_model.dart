import 'package:freezed_annotation/freezed_annotation.dart';

part 'listing_model.freezed.dart';
part 'listing_model.g.dart';

@freezed
class ListingModel with _$ListingModel {
  const factory ListingModel({
    required String id,
    required String title,
    required String description,
    required double price,
    required String currency,
    required String condition,
    required String status,
    required String sellerId,
    String? categoryId,
    String? categoryName,
    String? city,
    String? suburb,
    required bool negotiable,
    required int viewCount,
    String? expiresAt,
    @Default([]) List<String> imageUrls,
    required String createdAt,
    required String updatedAt,
  }) = _ListingModel;

  factory ListingModel.fromJson(Map<String, dynamic> json) =>
      _$ListingModelFromJson(json);
}

class ListingPage {
  const ListingPage({required this.content, required this.meta});

  final List<ListingModel> content;
  final PageMeta meta;

  factory ListingPage.fromJson(Map<String, dynamic> json) {
    return ListingPage(
      content: (json['content'] as List)
          .map((e) => ListingModel.fromJson(e as Map<String, dynamic>))
          .toList(),
      meta: PageMeta.fromJson(json['page'] as Map<String, dynamic>),
    );
  }
}

@freezed
class PageMeta with _$PageMeta {
  const factory PageMeta({
    required int number,
    required int size,
    required int totalElements,
    required int totalPages,
  }) = _PageMeta;

  factory PageMeta.fromJson(Map<String, dynamic> json) =>
      _$PageMetaFromJson(json);
}
