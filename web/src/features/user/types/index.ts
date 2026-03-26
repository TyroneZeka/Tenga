/** Matches backend UserProfileResponse record */
export interface UserProfile {
  id: string
  userId: string
  displayName: string
  bio: string | null
  avatarUrl: string | null
  city: string | null
  suburb: string | null
  activeListingCount: number
  trustScore: number | null
  memberSince: string | null
}

export interface UpdateProfileRequest {
  displayName?: string
  bio?: string
}
