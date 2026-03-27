import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../../data/datasources/chat_remote_datasource.dart';
import '../../data/datasources/chat_websocket_service.dart';
import '../../data/models/chat_models.dart';
import '../../../../core/config/app_config.dart';
import '../../../../core/network/api_client.dart';

final chatDataSourceProvider = Provider<ChatRemoteDataSource>(
  (ref) => ChatRemoteDataSource(ApiClient.instance),
);

final chatWebSocketProvider = Provider<ChatWebSocketService>((ref) {
  final service = ChatWebSocketService();
  ref.onDispose(service.disconnect);
  return service;
});

final threadsProvider =
    AsyncNotifierProvider<ThreadsNotifier, List<ChatThreadModel>>(
  ThreadsNotifier.new,
);

class ThreadsNotifier extends AsyncNotifier<List<ChatThreadModel>> {
  @override
  Future<List<ChatThreadModel>> build() async {
    final ds = ref.read(chatDataSourceProvider);
    final ws = ref.read(chatWebSocketProvider);

    // Connect WebSocket
    final storage = const FlutterSecureStorage();
    final token = await storage.read(key: AppConfig.accessTokenKey);
    if (token != null && !ws.isConnected) {
      ws.connect(token);
    }

    // Refresh thread list when any new message arrives
    ws.subscribe('threads_refresh', (_) {
      ref.invalidateSelf();
    });

    return ds.getThreads();
  }

  Future<ChatThreadModel> getOrCreateThread(
      String listingId, String sellerId) async {
    final ds = ref.read(chatDataSourceProvider);
    final thread = await ds.getOrCreateThread(listingId, sellerId);
    ref.invalidateSelf();
    return thread;
  }
}

final messagesProvider = AsyncNotifierProvider.autoDispose
    .family<MessagesNotifier, List<ChatMessageModel>, String>(
  MessagesNotifier.new,
);

class MessagesNotifier
    extends AutoDisposeFamilyAsyncNotifier<List<ChatMessageModel>, String> {
  String? _nextCursor;
  bool _hasMore = true;

  @override
  Future<List<ChatMessageModel>> build(String arg) async {
    final ds = ref.read(chatDataSourceProvider);
    final ws = ref.read(chatWebSocketProvider);

    // Connect WebSocket if needed
    final storage = const FlutterSecureStorage();
    final token = await storage.read(key: AppConfig.accessTokenKey);
    if (token != null && !ws.isConnected) {
      ws.connect(token);
    }

    // Subscribe to incoming messages for this thread
    ws.subscribe('thread_$arg', (message) {
      if (message.threadId == arg) {
        final current = state.valueOrNull ?? [];
        state = AsyncData([...current, message]);
      }
    });

    ref.onDispose(() => ws.unsubscribe('thread_$arg'));

    final page = await ds.getMessages(arg);
    _nextCursor = page.nextCursor;
    _hasMore = page.hasMore;

    await ds.markRead(arg);
    return page.messages.reversed.toList();
  }

  Future<void> sendMessage(String body) async {
    final ds = ref.read(chatDataSourceProvider);
    final message = await ds.sendMessage(arg, body);
    final current = state.valueOrNull ?? [];
    state = AsyncData([...current, message]);
  }

  Future<void> loadOlder() async {
    if (!_hasMore || _nextCursor == null) return;
    final ds = ref.read(chatDataSourceProvider);
    final current = state.valueOrNull ?? [];
    final page = await ds.getMessages(arg, cursor: _nextCursor);
    _nextCursor = page.nextCursor;
    _hasMore = page.hasMore;
    state = AsyncData([...page.messages.reversed, ...current]);
  }
}
