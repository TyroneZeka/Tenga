import { api } from '@/lib/api'
import type { AuthResponse, LoginRequest, RegisterRequest, VerifyOtpRequest } from '../types'

export async function register(data: RegisterRequest): Promise<{ message: string }> {
  const res = await api.post('/auth/register', data)
  return res.data
}

export async function login(data: LoginRequest): Promise<AuthResponse> {
  const res = await api.post('/auth/login', data)
  return res.data
}

export async function verifyOtp(data: VerifyOtpRequest): Promise<AuthResponse> {
  const res = await api.post('/auth/otp/verify', data)
  return res.data
}

export async function logout(): Promise<void> {
  await api.post('/auth/logout')
}
