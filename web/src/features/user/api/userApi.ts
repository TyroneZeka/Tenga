import { api } from '@/lib/api'
import type { UpdateProfileRequest, UserProfile } from '../types'

export async function getProfile(userId: string): Promise<UserProfile> {
  const res = await api.get(`/users/${userId}`)
  return res.data
}

export async function updateProfile(userId: string, data: UpdateProfileRequest): Promise<UserProfile> {
  const res = await api.patch(`/users/${userId}`, data)
  return res.data
}

export async function uploadAvatar(userId: string, file: File): Promise<UserProfile> {
  const form = new FormData()
  form.append('avatar', file)
  const res = await api.post(`/users/${userId}/avatar`, form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return res.data
}
