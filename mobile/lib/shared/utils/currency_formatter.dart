String formatPrice(double price, String currency) {
  final formatted = price % 1 == 0
      ? price.toStringAsFixed(0)
      : price.toStringAsFixed(2);
  return switch (currency) {
    'USD' => 'US\$$formatted',
    'ZIG' => 'ZiG $formatted',
    _ => '$currency $formatted',
  };
}
