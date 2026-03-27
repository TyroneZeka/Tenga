import 'package:freezed_annotation/freezed_annotation.dart';

part 'chat_models.freezed.dart';
part 'chat_models.g.dart';

@freezed
class ChatThreadModel with _$ChatThreadModel {
  const factory ChatThreadModel({
    required String id,
    required String buyerId,
    required String sellerId,
    required String listingId,
    String? lastMessagePreview,
    required int unreadCount,
    required String updatedAt,
    String? listingTitle,
    String? listingFirstImageUrl,
  }) = _ChatThreadModel;

  factory ChatThreadModel.fromJson(Map<String, dynamic> json) =>
      _$ChatThreadModelFromJson(json);
}

@freezed
class ChatMessageModel with _$ChatMessageModel {
  const factory ChatMessageModel({
    required String id,
    required String threadId,
    required String senderId,
    required String type,
    String? body,
    String? imageUrl,
    required String status,
    required String sentAt,
  }) = _ChatMessageModel;

  factory ChatMessageModel.fromJson(Map<String, dynamic> json) =>
      _$ChatMessageModelFromJson(json);
}

@freezed
class MessageCursorPage with _$MessageCursorPage {
  const factory MessageCursorPage({
    required List<ChatMessageModel> messages,
    String? nextCursor,
    required bool hasMore,
  }) = _MessageCursorPage;

  factory MessageCursorPage.fromJson(Map<String, dynamic> json) =>
      _$MessageCursorPageFromJson(json);
}
