import 'package:dio/dio.dart';
import '../models/chat_models.dart';

class ChatRemoteDataSource {
  ChatRemoteDataSource(this._dio);

  final Dio _dio;

  Future<ChatThreadModel> getOrCreateThread(String listingId, String sellerId) async {
    final response = await _dio.post('/chat/threads', data: {
      'listingId': listingId,
      'sellerId': sellerId,
    });
    return ChatThreadModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<List<ChatThreadModel>> getThreads() async {
    final response = await _dio.get('/chat/threads');
    return (response.data as List)
        .map((e) => ChatThreadModel.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<ChatMessageModel> sendMessage(String threadId, String body) async {
    final response = await _dio.post(
      '/chat/threads/$threadId/messages',
      data: {'type': 'TEXT', 'body': body},
    );
    return ChatMessageModel.fromJson(response.data as Map<String, dynamic>);
  }

  Future<MessageCursorPage> getMessages(
    String threadId, {
    String? cursor,
    int pageSize = 30,
  }) async {
    final response = await _dio.get(
      '/chat/threads/$threadId/messages',
      queryParameters: {
        if (cursor != null) 'cursor': cursor,
        'pageSize': pageSize,
      },
    );
    return MessageCursorPage.fromJson(response.data as Map<String, dynamic>);
  }

  Future<void> markRead(String threadId) async {
    await _dio.patch('/chat/threads/$threadId/read');
  }
}
