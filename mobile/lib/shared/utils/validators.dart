final RegExp _zimbabwePhone = RegExp(r'^\+263[0-9]{9}$');
final RegExp _uppercaseLetter = RegExp(r'[A-Z]');
final RegExp _digit = RegExp(r'[0-9]');

String? validateZimbabwePhone(String? value) {
  if (value == null || value.isEmpty) return 'Phone number is required';
  if (!_zimbabwePhone.hasMatch(value)) {
    return 'Enter a valid Zimbabwe number (+263XXXXXXXXX)';
  }
  return null;
}

String? validatePassword(String? value) {
  if (value == null || value.isEmpty) return 'Password is required';
  if (value.length < 8) return 'Password must be at least 8 characters';
  if (!_uppercaseLetter.hasMatch(value)) return 'Password must include an uppercase letter';
  if (!_digit.hasMatch(value)) return 'Password must include a number';
  return null;
}

String? validateRequired(String? value, String fieldName) {
  if (value == null || value.trim().isEmpty) return '$fieldName is required';
  return null;
}
