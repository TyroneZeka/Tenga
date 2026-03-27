String formatThreadTime(String isoString) {
  try {
    final dt = DateTime.parse(isoString).toLocal();
    final now = DateTime.now();
    final diff = now.difference(dt);

    if (diff.inMinutes < 1) return 'now';
    if (diff.inHours < 1) return '${diff.inMinutes}m';
    if (diff.inDays < 1) {
      return '${dt.hour.toString().padLeft(2, '0')}:${dt.minute.toString().padLeft(2, '0')}';
    }
    if (diff.inDays == 1) return 'Yesterday';
    if (diff.inDays < 7) {
      const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
      return days[dt.weekday - 1];
    }
    return '${dt.day}/${dt.month}/${dt.year}';
  } catch (_) {
    return '';
  }
}

String formatMessageTime(String isoString) {
  try {
    final dt = DateTime.parse(isoString).toLocal();
    return '${dt.hour.toString().padLeft(2, '0')}:${dt.minute.toString().padLeft(2, '0')}';
  } catch (_) {
    return '';
  }
}

bool shouldShowTimestamp(String? prev, String current) {
  if (prev == null) return true;
  try {
    final prevDt = DateTime.parse(prev);
    final curDt = DateTime.parse(current);
    return curDt.difference(prevDt).inMinutes >= 5;
  } catch (_) {
    return false;
  }
}
