class AuthState {
  const AuthState({
    this.accessToken,
    this.userId,
    this.role,
    this.isLoggedIn = false,
  });

  const AuthState.initial() : this();

  final String? accessToken;
  final String? userId;
  final String? role;
  final bool isLoggedIn;

  AuthState copyWith({
    String? accessToken,
    String? userId,
    String? role,
    bool? isLoggedIn,
  }) {
    return AuthState(
      accessToken: accessToken ?? this.accessToken,
      userId: userId ?? this.userId,
      role: role ?? this.role,
      isLoggedIn: isLoggedIn ?? this.isLoggedIn,
    );
  }
}
