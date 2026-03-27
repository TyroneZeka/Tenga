// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'chat_models.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$ChatThreadModelImpl _$$ChatThreadModelImplFromJson(
  Map<String, dynamic> json,
) => _$ChatThreadModelImpl(
  id: json['id'] as String,
  buyerId: json['buyerId'] as String,
  sellerId: json['sellerId'] as String,
  listingId: json['listingId'] as String,
  lastMessagePreview: json['lastMessagePreview'] as String?,
  unreadCount: (json['unreadCount'] as num).toInt(),
  updatedAt: json['updatedAt'] as String,
  listingTitle: json['listingTitle'] as String?,
  listingFirstImageUrl: json['listingFirstImageUrl'] as String?,
);

Map<String, dynamic> _$$ChatThreadModelImplToJson(
  _$ChatThreadModelImpl instance,
) => <String, dynamic>{
  'id': instance.id,
  'buyerId': instance.buyerId,
  'sellerId': instance.sellerId,
  'listingId': instance.listingId,
  'lastMessagePreview': instance.lastMessagePreview,
  'unreadCount': instance.unreadCount,
  'updatedAt': instance.updatedAt,
  'listingTitle': instance.listingTitle,
  'listingFirstImageUrl': instance.listingFirstImageUrl,
};

_$ChatMessageModelImpl _$$ChatMessageModelImplFromJson(
  Map<String, dynamic> json,
) => _$ChatMessageModelImpl(
  id: json['id'] as String,
  threadId: json['threadId'] as String,
  senderId: json['senderId'] as String,
  type: json['type'] as String,
  body: json['body'] as String?,
  imageUrl: json['imageUrl'] as String?,
  status: json['status'] as String,
  sentAt: json['sentAt'] as String,
);

Map<String, dynamic> _$$ChatMessageModelImplToJson(
  _$ChatMessageModelImpl instance,
) => <String, dynamic>{
  'id': instance.id,
  'threadId': instance.threadId,
  'senderId': instance.senderId,
  'type': instance.type,
  'body': instance.body,
  'imageUrl': instance.imageUrl,
  'status': instance.status,
  'sentAt': instance.sentAt,
};

_$MessageCursorPageImpl _$$MessageCursorPageImplFromJson(
  Map<String, dynamic> json,
) => _$MessageCursorPageImpl(
  messages: (json['messages'] as List<dynamic>)
      .map((e) => ChatMessageModel.fromJson(e as Map<String, dynamic>))
      .toList(),
  nextCursor: json['nextCursor'] as String?,
  hasMore: json['hasMore'] as bool,
);

Map<String, dynamic> _$$MessageCursorPageImplToJson(
  _$MessageCursorPageImpl instance,
) => <String, dynamic>{
  'messages': instance.messages,
  'nextCursor': instance.nextCursor,
  'hasMore': instance.hasMore,
};
