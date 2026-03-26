import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import * as userApi from '../api/userApi'
import type { UpdateProfileRequest } from '../types'

const profileKeys = {
  detail: (userId: string) => ['users', userId] as const,
}

export function useProfile(userId: string) {
  return useQuery({
    queryKey: profileKeys.detail(userId),
    queryFn: () => userApi.getProfile(userId),
    enabled: !!userId,
  })
}

export function useUpdateProfile(userId: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: UpdateProfileRequest) => userApi.updateProfile(userId, data),
    onSuccess: (updated) => {
      queryClient.setQueryData(profileKeys.detail(userId), updated)
    },
  })
}

export function useUploadAvatar(userId: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (file: File) => userApi.uploadAvatar(userId, file),
    onSuccess: (updated) => {
      queryClient.setQueryData(profileKeys.detail(userId), updated)
    },
  })
}
