export const ZIMBABWE_PHONE_REGEX = /^\+263[0-9]{9}$/

export const CURRENCIES = ['ZIG', 'USD'] as const
export type Currency = (typeof CURRENCIES)[number]

export const CONDITIONS = ['NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR'] as const
export type Condition = (typeof CONDITIONS)[number]

export const MAX_LISTING_IMAGES = 10
export const MAX_IMAGE_SIZE_MB = 5
export const MAX_SEARCH_RADIUS_KM = 100
export const DEFAULT_PAGE_SIZE = 20
