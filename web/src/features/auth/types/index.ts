export interface RegisterRequest {
  phone: string
  email?: string
  password: string
  displayName: string
}

export interface LoginRequest {
  phone: string
  password: string
}

export interface VerifyOtpRequest {
  phone: string
  otp: string
}

/** Matches backend AuthTokenResponse */
export interface LoginResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
  role: string
  userId: string
}

/** Used after OTP verify (future enhancement) */
export interface AuthResponse {
  accessToken: string
  user: {
    id: string
    phone: string
    email: string | null
    displayName: string
  }
}
