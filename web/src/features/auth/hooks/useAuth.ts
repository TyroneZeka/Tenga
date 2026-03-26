import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'
import * as authApi from '../api/authApi'
import type { LoginRequest, RegisterRequest, VerifyOtpRequest } from '../types'

export function useRegister() {
  const navigate = useNavigate()
  return useMutation({
    mutationFn: (data: RegisterRequest) => authApi.register(data),
    onSuccess: (_data, variables) => {
      navigate('/verify-otp', { state: { phone: variables.phone } })
    },
  })
}

export function useLogin() {
  const navigate = useNavigate()
  return useMutation({
    mutationFn: (data: LoginRequest) => authApi.login(data),
    onSuccess: (_data, variables) => {
      // Backend may require OTP verification on first login
      navigate('/verify-otp', { state: { phone: variables.phone } })
    },
  })
}

export function useVerifyOtp() {
  const navigate = useNavigate()
  const setToken = useAuthStore((s) => s.setToken)
  const setUser = useAuthStore((s) => s.setUser)
  return useMutation({
    mutationFn: (data: VerifyOtpRequest) => authApi.verifyOtp(data),
    onSuccess: (data) => {
      setToken(data.accessToken)
      setUser(data.user)
      navigate('/')
    },
  })
}

export function useLogout() {
  const logout = useAuthStore((s) => s.logout)
  const navigate = useNavigate()
  return useMutation({
    mutationFn: () => authApi.logout(),
    onSettled: () => {
      logout()
      navigate('/login', { replace: true })
    },
  })
}
