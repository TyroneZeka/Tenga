import type { Currency, Condition } from '@/lib/constants'

/** Matches backend ListingResponse record exactly */
export interface Listing {
  id: string
  title: string
  description: string
  price: number
  currency: Currency
  condition: Condition
  status: 'DRAFT' | 'ACTIVE' | 'RESERVED' | 'SOLD' | 'EXPIRED' | 'REMOVED'
  sellerId: string
  categoryId: string
  categoryName: string
  city: string | null
  suburb: string | null
  negotiable: boolean
  viewCount: number
  imageUrls: string[]
  expiresAt: string | null
  createdAt: string
  updatedAt: string
}

/** Matches Spring's Page<T> JSON serialization */
export interface ListingPage {
  content: Listing[]
  totalElements: number
  totalPages: number
  number: number
  size: number
  first: boolean
  last: boolean
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
