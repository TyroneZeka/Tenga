// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'chat_models.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
  'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models',
);

ChatThreadModel _$ChatThreadModelFromJson(Map<String, dynamic> json) {
  return _ChatThreadModel.fromJson(json);
}

/// @nodoc
mixin _$ChatThreadModel {
  String get id => throw _privateConstructorUsedError;
  String get buyerId => throw _privateConstructorUsedError;
  String get sellerId => throw _privateConstructorUsedError;
  String get listingId => throw _privateConstructorUsedError;
  String? get lastMessagePreview => throw _privateConstructorUsedError;
  int get unreadCount => throw _privateConstructorUsedError;
  String get updatedAt => throw _privateConstructorUsedError;
  String? get listingTitle => throw _privateConstructorUsedError;
  String? get listingFirstImageUrl => throw _privateConstructorUsedError;

  /// Serializes this ChatThreadModel to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of ChatThreadModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $ChatThreadModelCopyWith<ChatThreadModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ChatThreadModelCopyWith<$Res> {
  factory $ChatThreadModelCopyWith(
    ChatThreadModel value,
    $Res Function(ChatThreadModel) then,
  ) = _$ChatThreadModelCopyWithImpl<$Res, ChatThreadModel>;
  @useResult
  $Res call({
    String id,
    String buyerId,
    String sellerId,
    String listingId,
    String? lastMessagePreview,
    int unreadCount,
    String updatedAt,
    String? listingTitle,
    String? listingFirstImageUrl,
  });
}

/// @nodoc
class _$ChatThreadModelCopyWithImpl<$Res, $Val extends ChatThreadModel>
    implements $ChatThreadModelCopyWith<$Res> {
  _$ChatThreadModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of ChatThreadModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? buyerId = null,
    Object? sellerId = null,
    Object? listingId = null,
    Object? lastMessagePreview = freezed,
    Object? unreadCount = null,
    Object? updatedAt = null,
    Object? listingTitle = freezed,
    Object? listingFirstImageUrl = freezed,
  }) {
    return _then(
      _value.copyWith(
            id: null == id
                ? _value.id
                : id // ignore: cast_nullable_to_non_nullable
                      as String,
            buyerId: null == buyerId
                ? _value.buyerId
                : buyerId // ignore: cast_nullable_to_non_nullable
                      as String,
            sellerId: null == sellerId
                ? _value.sellerId
                : sellerId // ignore: cast_nullable_to_non_nullable
                      as String,
            listingId: null == listingId
                ? _value.listingId
                : listingId // ignore: cast_nullable_to_non_nullable
                      as String,
            lastMessagePreview: freezed == lastMessagePreview
                ? _value.lastMessagePreview
                : lastMessagePreview // ignore: cast_nullable_to_non_nullable
                      as String?,
            unreadCount: null == unreadCount
                ? _value.unreadCount
                : unreadCount // ignore: cast_nullable_to_non_nullable
                      as int,
            updatedAt: null == updatedAt
                ? _value.updatedAt
                : updatedAt // ignore: cast_nullable_to_non_nullable
                      as String,
            listingTitle: freezed == listingTitle
                ? _value.listingTitle
                : listingTitle // ignore: cast_nullable_to_non_nullable
                      as String?,
            listingFirstImageUrl: freezed == listingFirstImageUrl
                ? _value.listingFirstImageUrl
                : listingFirstImageUrl // ignore: cast_nullable_to_non_nullable
                      as String?,
          )
          as $Val,
    );
  }
}

/// @nodoc
abstract class _$$ChatThreadModelImplCopyWith<$Res>
    implements $ChatThreadModelCopyWith<$Res> {
  factory _$$ChatThreadModelImplCopyWith(
    _$ChatThreadModelImpl value,
    $Res Function(_$ChatThreadModelImpl) then,
  ) = __$$ChatThreadModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({
    String id,
    String buyerId,
    String sellerId,
    String listingId,
    String? lastMessagePreview,
    int unreadCount,
    String updatedAt,
    String? listingTitle,
    String? listingFirstImageUrl,
  });
}

/// @nodoc
class __$$ChatThreadModelImplCopyWithImpl<$Res>
    extends _$ChatThreadModelCopyWithImpl<$Res, _$ChatThreadModelImpl>
    implements _$$ChatThreadModelImplCopyWith<$Res> {
  __$$ChatThreadModelImplCopyWithImpl(
    _$ChatThreadModelImpl _value,
    $Res Function(_$ChatThreadModelImpl) _then,
  ) : super(_value, _then);

  /// Create a copy of ChatThreadModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? buyerId = null,
    Object? sellerId = null,
    Object? listingId = null,
    Object? lastMessagePreview = freezed,
    Object? unreadCount = null,
    Object? updatedAt = null,
    Object? listingTitle = freezed,
    Object? listingFirstImageUrl = freezed,
  }) {
    return _then(
      _$ChatThreadModelImpl(
        id: null == id
            ? _value.id
            : id // ignore: cast_nullable_to_non_nullable
                  as String,
        buyerId: null == buyerId
            ? _value.buyerId
            : buyerId // ignore: cast_nullable_to_non_nullable
                  as String,
        sellerId: null == sellerId
            ? _value.sellerId
            : sellerId // ignore: cast_nullable_to_non_nullable
                  as String,
        listingId: null == listingId
            ? _value.listingId
            : listingId // ignore: cast_nullable_to_non_nullable
                  as String,
        lastMessagePreview: freezed == lastMessagePreview
            ? _value.lastMessagePreview
            : lastMessagePreview // ignore: cast_nullable_to_non_nullable
                  as String?,
        unreadCount: null == unreadCount
            ? _value.unreadCount
            : unreadCount // ignore: cast_nullable_to_non_nullable
                  as int,
        updatedAt: null == updatedAt
            ? _value.updatedAt
            : updatedAt // ignore: cast_nullable_to_non_nullable
                  as String,
        listingTitle: freezed == listingTitle
            ? _value.listingTitle
            : listingTitle // ignore: cast_nullable_to_non_nullable
                  as String?,
        listingFirstImageUrl: freezed == listingFirstImageUrl
            ? _value.listingFirstImageUrl
            : listingFirstImageUrl // ignore: cast_nullable_to_non_nullable
                  as String?,
      ),
    );
  }
}

/// @nodoc
@JsonSerializable()
class _$ChatThreadModelImpl implements _ChatThreadModel {
  const _$ChatThreadModelImpl({
    required this.id,
    required this.buyerId,
    required this.sellerId,
    required this.listingId,
    this.lastMessagePreview,
    required this.unreadCount,
    required this.updatedAt,
    this.listingTitle,
    this.listingFirstImageUrl,
  });

  factory _$ChatThreadModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$ChatThreadModelImplFromJson(json);

  @override
  final String id;
  @override
  final String buyerId;
  @override
  final String sellerId;
  @override
  final String listingId;
  @override
  final String? lastMessagePreview;
  @override
  final int unreadCount;
  @override
  final String updatedAt;
  @override
  final String? listingTitle;
  @override
  final String? listingFirstImageUrl;

  @override
  String toString() {
    return 'ChatThreadModel(id: $id, buyerId: $buyerId, sellerId: $sellerId, listingId: $listingId, lastMessagePreview: $lastMessagePreview, unreadCount: $unreadCount, updatedAt: $updatedAt, listingTitle: $listingTitle, listingFirstImageUrl: $listingFirstImageUrl)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ChatThreadModelImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.buyerId, buyerId) || other.buyerId == buyerId) &&
            (identical(other.sellerId, sellerId) ||
                other.sellerId == sellerId) &&
            (identical(other.listingId, listingId) ||
                other.listingId == listingId) &&
            (identical(other.lastMessagePreview, lastMessagePreview) ||
                other.lastMessagePreview == lastMessagePreview) &&
            (identical(other.unreadCount, unreadCount) ||
                other.unreadCount == unreadCount) &&
            (identical(other.updatedAt, updatedAt) ||
                other.updatedAt == updatedAt) &&
            (identical(other.listingTitle, listingTitle) ||
                other.listingTitle == listingTitle) &&
            (identical(other.listingFirstImageUrl, listingFirstImageUrl) ||
                other.listingFirstImageUrl == listingFirstImageUrl));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
    runtimeType,
    id,
    buyerId,
    sellerId,
    listingId,
    lastMessagePreview,
    unreadCount,
    updatedAt,
    listingTitle,
    listingFirstImageUrl,
  );

  /// Create a copy of ChatThreadModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ChatThreadModelImplCopyWith<_$ChatThreadModelImpl> get copyWith =>
      __$$ChatThreadModelImplCopyWithImpl<_$ChatThreadModelImpl>(
        this,
        _$identity,
      );

  @override
  Map<String, dynamic> toJson() {
    return _$$ChatThreadModelImplToJson(this);
  }
}

abstract class _ChatThreadModel implements ChatThreadModel {
  const factory _ChatThreadModel({
    required final String id,
    required final String buyerId,
    required final String sellerId,
    required final String listingId,
    final String? lastMessagePreview,
    required final int unreadCount,
    required final String updatedAt,
    final String? listingTitle,
    final String? listingFirstImageUrl,
  }) = _$ChatThreadModelImpl;

  factory _ChatThreadModel.fromJson(Map<String, dynamic> json) =
      _$ChatThreadModelImpl.fromJson;

  @override
  String get id;
  @override
  String get buyerId;
  @override
  String get sellerId;
  @override
  String get listingId;
  @override
  String? get lastMessagePreview;
  @override
  int get unreadCount;
  @override
  String get updatedAt;
  @override
  String? get listingTitle;
  @override
  String? get listingFirstImageUrl;

  /// Create a copy of ChatThreadModel
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ChatThreadModelImplCopyWith<_$ChatThreadModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

ChatMessageModel _$ChatMessageModelFromJson(Map<String, dynamic> json) {
  return _ChatMessageModel.fromJson(json);
}

/// @nodoc
mixin _$ChatMessageModel {
  String get id => throw _privateConstructorUsedError;
  String get threadId => throw _privateConstructorUsedError;
  String get senderId => throw _privateConstructorUsedError;
  String get type => throw _privateConstructorUsedError;
  String? get body => throw _privateConstructorUsedError;
  String? get imageUrl => throw _privateConstructorUsedError;
  String get status => throw _privateConstructorUsedError;
  String get sentAt => throw _privateConstructorUsedError;

  /// Serializes this ChatMessageModel to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of ChatMessageModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $ChatMessageModelCopyWith<ChatMessageModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ChatMessageModelCopyWith<$Res> {
  factory $ChatMessageModelCopyWith(
    ChatMessageModel value,
    $Res Function(ChatMessageModel) then,
  ) = _$ChatMessageModelCopyWithImpl<$Res, ChatMessageModel>;
  @useResult
  $Res call({
    String id,
    String threadId,
    String senderId,
    String type,
    String? body,
    String? imageUrl,
    String status,
    String sentAt,
  });
}

/// @nodoc
class _$ChatMessageModelCopyWithImpl<$Res, $Val extends ChatMessageModel>
    implements $ChatMessageModelCopyWith<$Res> {
  _$ChatMessageModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of ChatMessageModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? threadId = null,
    Object? senderId = null,
    Object? type = null,
    Object? body = freezed,
    Object? imageUrl = freezed,
    Object? status = null,
    Object? sentAt = null,
  }) {
    return _then(
      _value.copyWith(
            id: null == id
                ? _value.id
                : id // ignore: cast_nullable_to_non_nullable
                      as String,
            threadId: null == threadId
                ? _value.threadId
                : threadId // ignore: cast_nullable_to_non_nullable
                      as String,
            senderId: null == senderId
                ? _value.senderId
                : senderId // ignore: cast_nullable_to_non_nullable
                      as String,
            type: null == type
                ? _value.type
                : type // ignore: cast_nullable_to_non_nullable
                      as String,
            body: freezed == body
                ? _value.body
                : body // ignore: cast_nullable_to_non_nullable
                      as String?,
            imageUrl: freezed == imageUrl
                ? _value.imageUrl
                : imageUrl // ignore: cast_nullable_to_non_nullable
                      as String?,
            status: null == status
                ? _value.status
                : status // ignore: cast_nullable_to_non_nullable
                      as String,
            sentAt: null == sentAt
                ? _value.sentAt
                : sentAt // ignore: cast_nullable_to_non_nullable
                      as String,
          )
          as $Val,
    );
  }
}

/// @nodoc
abstract class _$$ChatMessageModelImplCopyWith<$Res>
    implements $ChatMessageModelCopyWith<$Res> {
  factory _$$ChatMessageModelImplCopyWith(
    _$ChatMessageModelImpl value,
    $Res Function(_$ChatMessageModelImpl) then,
  ) = __$$ChatMessageModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({
    String id,
    String threadId,
    String senderId,
    String type,
    String? body,
    String? imageUrl,
    String status,
    String sentAt,
  });
}

/// @nodoc
class __$$ChatMessageModelImplCopyWithImpl<$Res>
    extends _$ChatMessageModelCopyWithImpl<$Res, _$ChatMessageModelImpl>
    implements _$$ChatMessageModelImplCopyWith<$Res> {
  __$$ChatMessageModelImplCopyWithImpl(
    _$ChatMessageModelImpl _value,
    $Res Function(_$ChatMessageModelImpl) _then,
  ) : super(_value, _then);

  /// Create a copy of ChatMessageModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? threadId = null,
    Object? senderId = null,
    Object? type = null,
    Object? body = freezed,
    Object? imageUrl = freezed,
    Object? status = null,
    Object? sentAt = null,
  }) {
    return _then(
      _$ChatMessageModelImpl(
        id: null == id
            ? _value.id
            : id // ignore: cast_nullable_to_non_nullable
                  as String,
        threadId: null == threadId
            ? _value.threadId
            : threadId // ignore: cast_nullable_to_non_nullable
                  as String,
        senderId: null == senderId
            ? _value.senderId
            : senderId // ignore: cast_nullable_to_non_nullable
                  as String,
        type: null == type
            ? _value.type
            : type // ignore: cast_nullable_to_non_nullable
                  as String,
        body: freezed == body
            ? _value.body
            : body // ignore: cast_nullable_to_non_nullable
                  as String?,
        imageUrl: freezed == imageUrl
            ? _value.imageUrl
            : imageUrl // ignore: cast_nullable_to_non_nullable
                  as String?,
        status: null == status
            ? _value.status
            : status // ignore: cast_nullable_to_non_nullable
                  as String,
        sentAt: null == sentAt
            ? _value.sentAt
            : sentAt // ignore: cast_nullable_to_non_nullable
                  as String,
      ),
    );
  }
}

/// @nodoc
@JsonSerializable()
class _$ChatMessageModelImpl implements _ChatMessageModel {
  const _$ChatMessageModelImpl({
    required this.id,
    required this.threadId,
    required this.senderId,
    required this.type,
    this.body,
    this.imageUrl,
    required this.status,
    required this.sentAt,
  });

  factory _$ChatMessageModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$ChatMessageModelImplFromJson(json);

  @override
  final String id;
  @override
  final String threadId;
  @override
  final String senderId;
  @override
  final String type;
  @override
  final String? body;
  @override
  final String? imageUrl;
  @override
  final String status;
  @override
  final String sentAt;

  @override
  String toString() {
    return 'ChatMessageModel(id: $id, threadId: $threadId, senderId: $senderId, type: $type, body: $body, imageUrl: $imageUrl, status: $status, sentAt: $sentAt)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ChatMessageModelImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.threadId, threadId) ||
                other.threadId == threadId) &&
            (identical(other.senderId, senderId) ||
                other.senderId == senderId) &&
            (identical(other.type, type) || other.type == type) &&
            (identical(other.body, body) || other.body == body) &&
            (identical(other.imageUrl, imageUrl) ||
                other.imageUrl == imageUrl) &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.sentAt, sentAt) || other.sentAt == sentAt));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
    runtimeType,
    id,
    threadId,
    senderId,
    type,
    body,
    imageUrl,
    status,
    sentAt,
  );

  /// Create a copy of ChatMessageModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ChatMessageModelImplCopyWith<_$ChatMessageModelImpl> get copyWith =>
      __$$ChatMessageModelImplCopyWithImpl<_$ChatMessageModelImpl>(
        this,
        _$identity,
      );

  @override
  Map<String, dynamic> toJson() {
    return _$$ChatMessageModelImplToJson(this);
  }
}

abstract class _ChatMessageModel implements ChatMessageModel {
  const factory _ChatMessageModel({
    required final String id,
    required final String threadId,
    required final String senderId,
    required final String type,
    final String? body,
    final String? imageUrl,
    required final String status,
    required final String sentAt,
  }) = _$ChatMessageModelImpl;

  factory _ChatMessageModel.fromJson(Map<String, dynamic> json) =
      _$ChatMessageModelImpl.fromJson;

  @override
  String get id;
  @override
  String get threadId;
  @override
  String get senderId;
  @override
  String get type;
  @override
  String? get body;
  @override
  String? get imageUrl;
  @override
  String get status;
  @override
  String get sentAt;

  /// Create a copy of ChatMessageModel
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ChatMessageModelImplCopyWith<_$ChatMessageModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

MessageCursorPage _$MessageCursorPageFromJson(Map<String, dynamic> json) {
  return _MessageCursorPage.fromJson(json);
}

/// @nodoc
mixin _$MessageCursorPage {
  List<ChatMessageModel> get messages => throw _privateConstructorUsedError;
  String? get nextCursor => throw _privateConstructorUsedError;
  bool get hasMore => throw _privateConstructorUsedError;

  /// Serializes this MessageCursorPage to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of MessageCursorPage
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $MessageCursorPageCopyWith<MessageCursorPage> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MessageCursorPageCopyWith<$Res> {
  factory $MessageCursorPageCopyWith(
    MessageCursorPage value,
    $Res Function(MessageCursorPage) then,
  ) = _$MessageCursorPageCopyWithImpl<$Res, MessageCursorPage>;
  @useResult
  $Res call({
    List<ChatMessageModel> messages,
    String? nextCursor,
    bool hasMore,
  });
}

/// @nodoc
class _$MessageCursorPageCopyWithImpl<$Res, $Val extends MessageCursorPage>
    implements $MessageCursorPageCopyWith<$Res> {
  _$MessageCursorPageCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of MessageCursorPage
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? messages = null,
    Object? nextCursor = freezed,
    Object? hasMore = null,
  }) {
    return _then(
      _value.copyWith(
            messages: null == messages
                ? _value.messages
                : messages // ignore: cast_nullable_to_non_nullable
                      as List<ChatMessageModel>,
            nextCursor: freezed == nextCursor
                ? _value.nextCursor
                : nextCursor // ignore: cast_nullable_to_non_nullable
                      as String?,
            hasMore: null == hasMore
                ? _value.hasMore
                : hasMore // ignore: cast_nullable_to_non_nullable
                      as bool,
          )
          as $Val,
    );
  }
}

/// @nodoc
abstract class _$$MessageCursorPageImplCopyWith<$Res>
    implements $MessageCursorPageCopyWith<$Res> {
  factory _$$MessageCursorPageImplCopyWith(
    _$MessageCursorPageImpl value,
    $Res Function(_$MessageCursorPageImpl) then,
  ) = __$$MessageCursorPageImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({
    List<ChatMessageModel> messages,
    String? nextCursor,
    bool hasMore,
  });
}

/// @nodoc
class __$$MessageCursorPageImplCopyWithImpl<$Res>
    extends _$MessageCursorPageCopyWithImpl<$Res, _$MessageCursorPageImpl>
    implements _$$MessageCursorPageImplCopyWith<$Res> {
  __$$MessageCursorPageImplCopyWithImpl(
    _$MessageCursorPageImpl _value,
    $Res Function(_$MessageCursorPageImpl) _then,
  ) : super(_value, _then);

  /// Create a copy of MessageCursorPage
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? messages = null,
    Object? nextCursor = freezed,
    Object? hasMore = null,
  }) {
    return _then(
      _$MessageCursorPageImpl(
        messages: null == messages
            ? _value._messages
            : messages // ignore: cast_nullable_to_non_nullable
                  as List<ChatMessageModel>,
        nextCursor: freezed == nextCursor
            ? _value.nextCursor
            : nextCursor // ignore: cast_nullable_to_non_nullable
                  as String?,
        hasMore: null == hasMore
            ? _value.hasMore
            : hasMore // ignore: cast_nullable_to_non_nullable
                  as bool,
      ),
    );
  }
}

/// @nodoc
@JsonSerializable()
class _$MessageCursorPageImpl implements _MessageCursorPage {
  const _$MessageCursorPageImpl({
    required final List<ChatMessageModel> messages,
    this.nextCursor,
    required this.hasMore,
  }) : _messages = messages;

  factory _$MessageCursorPageImpl.fromJson(Map<String, dynamic> json) =>
      _$$MessageCursorPageImplFromJson(json);

  final List<ChatMessageModel> _messages;
  @override
  List<ChatMessageModel> get messages {
    if (_messages is EqualUnmodifiableListView) return _messages;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_messages);
  }

  @override
  final String? nextCursor;
  @override
  final bool hasMore;

  @override
  String toString() {
    return 'MessageCursorPage(messages: $messages, nextCursor: $nextCursor, hasMore: $hasMore)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MessageCursorPageImpl &&
            const DeepCollectionEquality().equals(other._messages, _messages) &&
            (identical(other.nextCursor, nextCursor) ||
                other.nextCursor == nextCursor) &&
            (identical(other.hasMore, hasMore) || other.hasMore == hasMore));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
    runtimeType,
    const DeepCollectionEquality().hash(_messages),
    nextCursor,
    hasMore,
  );

  /// Create a copy of MessageCursorPage
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MessageCursorPageImplCopyWith<_$MessageCursorPageImpl> get copyWith =>
      __$$MessageCursorPageImplCopyWithImpl<_$MessageCursorPageImpl>(
        this,
        _$identity,
      );

  @override
  Map<String, dynamic> toJson() {
    return _$$MessageCursorPageImplToJson(this);
  }
}

abstract class _MessageCursorPage implements MessageCursorPage {
  const factory _MessageCursorPage({
    required final List<ChatMessageModel> messages,
    final String? nextCursor,
    required final bool hasMore,
  }) = _$MessageCursorPageImpl;

  factory _MessageCursorPage.fromJson(Map<String, dynamic> json) =
      _$MessageCursorPageImpl.fromJson;

  @override
  List<ChatMessageModel> get messages;
  @override
  String? get nextCursor;
  @override
  bool get hasMore;

  /// Create a copy of MessageCursorPage
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MessageCursorPageImplCopyWith<_$MessageCursorPageImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
