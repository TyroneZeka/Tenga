// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'listing_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$ListingModelImpl _$$ListingModelImplFromJson(Map<String, dynamic> json) =>
    _$ListingModelImpl(
      id: json['id'] as String,
      title: json['title'] as String,
      description: json['description'] as String,
      price: (json['price'] as num).toDouble(),
      currency: json['currency'] as String,
      condition: json['condition'] as String,
      status: json['status'] as String,
      sellerId: json['sellerId'] as String,
      categoryId: json['categoryId'] as String?,
      categoryName: json['categoryName'] as String?,
      city: json['city'] as String?,
      suburb: json['suburb'] as String?,
      negotiable: json['negotiable'] as bool,
      viewCount: (json['viewCount'] as num).toInt(),
      expiresAt: json['expiresAt'] as String?,
      imageUrls:
          (json['imageUrls'] as List<dynamic>?)
              ?.map((e) => e as String)
              .toList() ??
          const [],
      createdAt: json['createdAt'] as String,
      updatedAt: json['updatedAt'] as String,
    );

Map<String, dynamic> _$$ListingModelImplToJson(_$ListingModelImpl instance) =>
    <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'description': instance.description,
      'price': instance.price,
      'currency': instance.currency,
      'condition': instance.condition,
      'status': instance.status,
      'sellerId': instance.sellerId,
      'categoryId': instance.categoryId,
      'categoryName': instance.categoryName,
      'city': instance.city,
      'suburb': instance.suburb,
      'negotiable': instance.negotiable,
      'viewCount': instance.viewCount,
      'expiresAt': instance.expiresAt,
      'imageUrls': instance.imageUrls,
      'createdAt': instance.createdAt,
      'updatedAt': instance.updatedAt,
    };

_$PageMetaImpl _$$PageMetaImplFromJson(Map<String, dynamic> json) =>
    _$PageMetaImpl(
      number: (json['number'] as num).toInt(),
      size: (json['size'] as num).toInt(),
      totalElements: (json['totalElements'] as num).toInt(),
      totalPages: (json['totalPages'] as num).toInt(),
    );

Map<String, dynamic> _$$PageMetaImplToJson(_$PageMetaImpl instance) =>
    <String, dynamic>{
      'number': instance.number,
      'size': instance.size,
      'totalElements': instance.totalElements,
      'totalPages': instance.totalPages,
    };
