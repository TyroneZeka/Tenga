import { api } from '@/lib/api'
import type {
  CreateListingRequest,
  Listing,
  ListingPage,
  ListingSearchParams,
  UpdateListingRequest,
} from '../types'

export async function getListings(params: ListingSearchParams = {}): Promise<ListingPage> {
  const res = await api.get('/listings', { params })
  return res.data
}

export async function getListing(id: string): Promise<Listing> {
  const res = await api.get(`/listings/${id}`)
  return res.data
}

export async function createListing(data: CreateListingRequest): Promise<Listing> {
  const res = await api.post('/listings', data)
  return res.data
}

export async function updateListing(id: string, data: UpdateListingRequest): Promise<Listing> {
  const res = await api.patch(`/listings/${id}`, data)
  return res.data
}

export async function deleteListing(id: string): Promise<void> {
  await api.delete(`/listings/${id}`)
}

export async function uploadListingImages(id: string, files: File[]): Promise<string[]> {
  const form = new FormData()
  files.forEach((f) => form.append('images', f))
  const res = await api.post(`/listings/${id}/images`, form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return res.data
}

export async function getUserListings(userId: string, page = 0): Promise<ListingPage> {
  const res = await api.get(`/users/${userId}/listings`, { params: { page } })
  return res.data
}
