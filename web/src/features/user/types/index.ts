export interface UserProfile {
  id: string
  userId: string
  displayName: string
  bio: string | null
  avatarUrl: string | null
  trustScore: number | null
  activeListingCount: number
}

export interface UpdateProfileRequest {
  displayName?: string
  bio?: string
}
