// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'transaction_model.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
  'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models',
);

TransactionModel _$TransactionModelFromJson(Map<String, dynamic> json) {
  return _TransactionModel.fromJson(json);
}

/// @nodoc
mixin _$TransactionModel {
  String get id => throw _privateConstructorUsedError;
  String get listingId => throw _privateConstructorUsedError;
  String get buyerId => throw _privateConstructorUsedError;
  String get sellerId => throw _privateConstructorUsedError;
  @JsonKey(fromJson: _toDouble)
  double get amount => throw _privateConstructorUsedError;
  String get currency => throw _privateConstructorUsedError;
  String get paymentMethod => throw _privateConstructorUsedError;
  String get status => throw _privateConstructorUsedError;
  String? get gatewayReference => throw _privateConstructorUsedError;
  String get createdAt => throw _privateConstructorUsedError;
  String? get completedAt => throw _privateConstructorUsedError;

  /// Serializes this TransactionModel to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of TransactionModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $TransactionModelCopyWith<TransactionModel> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $TransactionModelCopyWith<$Res> {
  factory $TransactionModelCopyWith(
    TransactionModel value,
    $Res Function(TransactionModel) then,
  ) = _$TransactionModelCopyWithImpl<$Res, TransactionModel>;
  @useResult
  $Res call({
    String id,
    String listingId,
    String buyerId,
    String sellerId,
    @JsonKey(fromJson: _toDouble) double amount,
    String currency,
    String paymentMethod,
    String status,
    String? gatewayReference,
    String createdAt,
    String? completedAt,
  });
}

/// @nodoc
class _$TransactionModelCopyWithImpl<$Res, $Val extends TransactionModel>
    implements $TransactionModelCopyWith<$Res> {
  _$TransactionModelCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of TransactionModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? listingId = null,
    Object? buyerId = null,
    Object? sellerId = null,
    Object? amount = null,
    Object? currency = null,
    Object? paymentMethod = null,
    Object? status = null,
    Object? gatewayReference = freezed,
    Object? createdAt = null,
    Object? completedAt = freezed,
  }) {
    return _then(
      _value.copyWith(
            id: null == id
                ? _value.id
                : id // ignore: cast_nullable_to_non_nullable
                      as String,
            listingId: null == listingId
                ? _value.listingId
                : listingId // ignore: cast_nullable_to_non_nullable
                      as String,
            buyerId: null == buyerId
                ? _value.buyerId
                : buyerId // ignore: cast_nullable_to_non_nullable
                      as String,
            sellerId: null == sellerId
                ? _value.sellerId
                : sellerId // ignore: cast_nullable_to_non_nullable
                      as String,
            amount: null == amount
                ? _value.amount
                : amount // ignore: cast_nullable_to_non_nullable
                      as double,
            currency: null == currency
                ? _value.currency
                : currency // ignore: cast_nullable_to_non_nullable
                      as String,
            paymentMethod: null == paymentMethod
                ? _value.paymentMethod
                : paymentMethod // ignore: cast_nullable_to_non_nullable
                      as String,
            status: null == status
                ? _value.status
                : status // ignore: cast_nullable_to_non_nullable
                      as String,
            gatewayReference: freezed == gatewayReference
                ? _value.gatewayReference
                : gatewayReference // ignore: cast_nullable_to_non_nullable
                      as String?,
            createdAt: null == createdAt
                ? _value.createdAt
                : createdAt // ignore: cast_nullable_to_non_nullable
                      as String,
            completedAt: freezed == completedAt
                ? _value.completedAt
                : completedAt // ignore: cast_nullable_to_non_nullable
                      as String?,
          )
          as $Val,
    );
  }
}

/// @nodoc
abstract class _$$TransactionModelImplCopyWith<$Res>
    implements $TransactionModelCopyWith<$Res> {
  factory _$$TransactionModelImplCopyWith(
    _$TransactionModelImpl value,
    $Res Function(_$TransactionModelImpl) then,
  ) = __$$TransactionModelImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({
    String id,
    String listingId,
    String buyerId,
    String sellerId,
    @JsonKey(fromJson: _toDouble) double amount,
    String currency,
    String paymentMethod,
    String status,
    String? gatewayReference,
    String createdAt,
    String? completedAt,
  });
}

/// @nodoc
class __$$TransactionModelImplCopyWithImpl<$Res>
    extends _$TransactionModelCopyWithImpl<$Res, _$TransactionModelImpl>
    implements _$$TransactionModelImplCopyWith<$Res> {
  __$$TransactionModelImplCopyWithImpl(
    _$TransactionModelImpl _value,
    $Res Function(_$TransactionModelImpl) _then,
  ) : super(_value, _then);

  /// Create a copy of TransactionModel
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? id = null,
    Object? listingId = null,
    Object? buyerId = null,
    Object? sellerId = null,
    Object? amount = null,
    Object? currency = null,
    Object? paymentMethod = null,
    Object? status = null,
    Object? gatewayReference = freezed,
    Object? createdAt = null,
    Object? completedAt = freezed,
  }) {
    return _then(
      _$TransactionModelImpl(
        id: null == id
            ? _value.id
            : id // ignore: cast_nullable_to_non_nullable
                  as String,
        listingId: null == listingId
            ? _value.listingId
            : listingId // ignore: cast_nullable_to_non_nullable
                  as String,
        buyerId: null == buyerId
            ? _value.buyerId
            : buyerId // ignore: cast_nullable_to_non_nullable
                  as String,
        sellerId: null == sellerId
            ? _value.sellerId
            : sellerId // ignore: cast_nullable_to_non_nullable
                  as String,
        amount: null == amount
            ? _value.amount
            : amount // ignore: cast_nullable_to_non_nullable
                  as double,
        currency: null == currency
            ? _value.currency
            : currency // ignore: cast_nullable_to_non_nullable
                  as String,
        paymentMethod: null == paymentMethod
            ? _value.paymentMethod
            : paymentMethod // ignore: cast_nullable_to_non_nullable
                  as String,
        status: null == status
            ? _value.status
            : status // ignore: cast_nullable_to_non_nullable
                  as String,
        gatewayReference: freezed == gatewayReference
            ? _value.gatewayReference
            : gatewayReference // ignore: cast_nullable_to_non_nullable
                  as String?,
        createdAt: null == createdAt
            ? _value.createdAt
            : createdAt // ignore: cast_nullable_to_non_nullable
                  as String,
        completedAt: freezed == completedAt
            ? _value.completedAt
            : completedAt // ignore: cast_nullable_to_non_nullable
                  as String?,
      ),
    );
  }
}

/// @nodoc
@JsonSerializable()
class _$TransactionModelImpl implements _TransactionModel {
  const _$TransactionModelImpl({
    required this.id,
    required this.listingId,
    required this.buyerId,
    required this.sellerId,
    @JsonKey(fromJson: _toDouble) required this.amount,
    required this.currency,
    required this.paymentMethod,
    required this.status,
    this.gatewayReference,
    required this.createdAt,
    this.completedAt,
  });

  factory _$TransactionModelImpl.fromJson(Map<String, dynamic> json) =>
      _$$TransactionModelImplFromJson(json);

  @override
  final String id;
  @override
  final String listingId;
  @override
  final String buyerId;
  @override
  final String sellerId;
  @override
  @JsonKey(fromJson: _toDouble)
  final double amount;
  @override
  final String currency;
  @override
  final String paymentMethod;
  @override
  final String status;
  @override
  final String? gatewayReference;
  @override
  final String createdAt;
  @override
  final String? completedAt;

  @override
  String toString() {
    return 'TransactionModel(id: $id, listingId: $listingId, buyerId: $buyerId, sellerId: $sellerId, amount: $amount, currency: $currency, paymentMethod: $paymentMethod, status: $status, gatewayReference: $gatewayReference, createdAt: $createdAt, completedAt: $completedAt)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$TransactionModelImpl &&
            (identical(other.id, id) || other.id == id) &&
            (identical(other.listingId, listingId) ||
                other.listingId == listingId) &&
            (identical(other.buyerId, buyerId) || other.buyerId == buyerId) &&
            (identical(other.sellerId, sellerId) ||
                other.sellerId == sellerId) &&
            (identical(other.amount, amount) || other.amount == amount) &&
            (identical(other.currency, currency) ||
                other.currency == currency) &&
            (identical(other.paymentMethod, paymentMethod) ||
                other.paymentMethod == paymentMethod) &&
            (identical(other.status, status) || other.status == status) &&
            (identical(other.gatewayReference, gatewayReference) ||
                other.gatewayReference == gatewayReference) &&
            (identical(other.createdAt, createdAt) ||
                other.createdAt == createdAt) &&
            (identical(other.completedAt, completedAt) ||
                other.completedAt == completedAt));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(
    runtimeType,
    id,
    listingId,
    buyerId,
    sellerId,
    amount,
    currency,
    paymentMethod,
    status,
    gatewayReference,
    createdAt,
    completedAt,
  );

  /// Create a copy of TransactionModel
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$TransactionModelImplCopyWith<_$TransactionModelImpl> get copyWith =>
      __$$TransactionModelImplCopyWithImpl<_$TransactionModelImpl>(
        this,
        _$identity,
      );

  @override
  Map<String, dynamic> toJson() {
    return _$$TransactionModelImplToJson(this);
  }
}

abstract class _TransactionModel implements TransactionModel {
  const factory _TransactionModel({
    required final String id,
    required final String listingId,
    required final String buyerId,
    required final String sellerId,
    @JsonKey(fromJson: _toDouble) required final double amount,
    required final String currency,
    required final String paymentMethod,
    required final String status,
    final String? gatewayReference,
    required final String createdAt,
    final String? completedAt,
  }) = _$TransactionModelImpl;

  factory _TransactionModel.fromJson(Map<String, dynamic> json) =
      _$TransactionModelImpl.fromJson;

  @override
  String get id;
  @override
  String get listingId;
  @override
  String get buyerId;
  @override
  String get sellerId;
  @override
  @JsonKey(fromJson: _toDouble)
  double get amount;
  @override
  String get currency;
  @override
  String get paymentMethod;
  @override
  String get status;
  @override
  String? get gatewayReference;
  @override
  String get createdAt;
  @override
  String? get completedAt;

  /// Create a copy of TransactionModel
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$TransactionModelImplCopyWith<_$TransactionModelImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
