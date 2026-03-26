import type { Currency, Condition } from '@/lib/constants'

export interface Listing {
  id: string
  title: string
  description: string
  price: number
  currency: Currency
  condition: Condition
  categoryId: string
  categoryName: string
  cityId: string
  cityName: string
  latitude: number | null
  longitude: number | null
  imageUrls: string[]
  seller: {
    id: string
    displayName: string
    avatarUrl: string | null
    trustScore: number | null
  }
  status: 'ACTIVE' | 'RESERVED' | 'SOLD' | 'INACTIVE'
  createdAt: string
}

export interface ListingPage {
  content: Listing[]
  page: {
    number: number
    size: number
    totalElements: number
    totalPages: number
  }
}

export interface ListingSearchParams {
  query?: string
  categoryId?: string
  cityId?: string
  condition?: Condition
  currency?: Currency
  minPrice?: number
  maxPrice?: number
  latitude?: number
  longitude?: number
  radiusKm?: number
  page?: number
  size?: number
}

export interface CreateListingRequest {
  title: string
  description: string
  price: number
  currency: Currency
  condition: Condition
  categoryId: string
  cityId: string
  latitude?: number
  longitude?: number
}

export type UpdateListingRequest = Partial<CreateListingRequest>
