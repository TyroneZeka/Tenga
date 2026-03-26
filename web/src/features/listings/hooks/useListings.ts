import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import * as listingsApi from '../api/listingsApi'
import type { CreateListingRequest, ListingSearchParams, UpdateListingRequest } from '../types'

export const listingKeys = {
  all: ['listings'] as const,
  search: (params: ListingSearchParams) => ['listings', 'search', params] as const,
  detail: (id: string) => ['listings', id] as const,
  userListings: (userId: string) => ['listings', 'user', userId] as const,
}

export function useListings(params: ListingSearchParams = {}) {
  return useQuery({
    queryKey: listingKeys.search(params),
    queryFn: () => listingsApi.getListings(params),
  })
}

export function useListing(id: string) {
  return useQuery({
    queryKey: listingKeys.detail(id),
    queryFn: () => listingsApi.getListing(id),
    enabled: !!id,
  })
}

export function useCreateListing() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: CreateListingRequest) => listingsApi.createListing(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: listingKeys.all })
    },
  })
}

export function useUpdateListing(id: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: UpdateListingRequest) => listingsApi.updateListing(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: listingKeys.detail(id) })
      queryClient.invalidateQueries({ queryKey: listingKeys.all })
    },
  })
}

export function useDeleteListing() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id: string) => listingsApi.deleteListing(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: listingKeys.all })
    },
  })
}

export function useUploadListingImages(id: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (files: File[]) => listingsApi.uploadListingImages(id, files),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: listingKeys.detail(id) })
    },
  })
}

export function useUserListings(userId: string) {
  return useQuery({
    queryKey: listingKeys.userListings(userId),
    queryFn: () => listingsApi.getUserListings(userId),
    enabled: !!userId,
  })
}
