import 'dart:convert';
import 'package:stomp_dart_client/stomp_dart_client.dart';
import '../../../../core/config/app_config.dart';
import '../models/chat_models.dart';

typedef MessageCallback = void Function(ChatMessageModel message);

class ChatWebSocketService {
  StompClient? _client;
  final Map<String, MessageCallback> _subscriptions = {};

  void connect(String accessToken) {
    _client = StompClient(
      config: StompConfig.sockJS(
        url: AppConfig.wsUrl,
        stompConnectHeaders: {'Authorization': 'Bearer $accessToken'},
        onConnect: _onConnected,
        onDisconnect: (_) {},
        onWebSocketError: (_) {},
        reconnectDelay: const Duration(seconds: 5),
      ),
    );
    _client!.activate();
  }

  void _onConnected(StompFrame frame) {
    _client!.subscribe(
      destination: '/user/queue/messages',
      callback: (frame) {
        if (frame.body == null) return;
        try {
          final data = jsonDecode(frame.body!) as Map<String, dynamic>;
          final message = ChatMessageModel.fromJson(data);
          for (final cb in _subscriptions.values) {
            cb(message);
          }
        } catch (_) {}
      },
    );
  }

  String subscribe(String key, MessageCallback callback) {
    _subscriptions[key] = callback;
    return key;
  }

  void unsubscribe(String key) {
    _subscriptions.remove(key);
  }

  void disconnect() {
    _client?.deactivate();
    _client = null;
    _subscriptions.clear();
  }

  bool get isConnected => _client?.connected ?? false;
}
