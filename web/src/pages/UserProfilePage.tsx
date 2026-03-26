import { useParams, Link } from 'react-router-dom'
import { Star, ArrowLeft } from 'lucide-react'
import { useProfile } from '@/features/user'
import { useUserListings, ListingCard } from '@/features/listings'

export default function UserProfilePage() {
  const { id } = useParams<{ id: string }>()
  const { data: profile, isLoading } = useProfile(id!)
  const { data: listingsPage } = useUserListings(id!)

  if (isLoading) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-8">
        <div className="animate-pulse space-y-4">
          <div className="h-20 w-20 rounded-full bg-gray-200" />
          <div className="h-5 w-40 rounded bg-gray-200" />
        </div>
      </div>
    )
  }

  if (!profile) return null

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-3xl px-4 py-6">
        <Link to="/" className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800">
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>

        <div className="mb-5 rounded-xl bg-white p-5 shadow-sm">
          <div className="flex items-center gap-4">
            {profile.avatarUrl ? (
              <img
                src={profile.avatarUrl}
                alt={profile.displayName}
                className="h-16 w-16 rounded-full object-cover"
              />
            ) : (
              <div className="flex h-16 w-16 items-center justify-center rounded-full bg-primary-100 text-2xl font-medium text-primary-700">
                {profile.displayName.charAt(0).toUpperCase()}
              </div>
            )}
            <div>
              <h1 className="text-lg font-semibold text-gray-900">{profile.displayName}</h1>
              {profile.trustScore !== null && (
                <p className="flex items-center gap-1 text-sm text-gray-500">
                  <Star className="h-3.5 w-3.5 fill-amber-400 text-amber-400" />
                  {profile.trustScore.toFixed(1)} trust score
                </p>
              )}
              {profile.bio && <p className="mt-1 text-sm text-gray-600">{profile.bio}</p>}
            </div>
          </div>
          <p className="mt-3 text-xs text-gray-400">
            {profile.activeListingCount} active listing{profile.activeListingCount !== 1 ? 's' : ''}
          </p>
        </div>

        {listingsPage && listingsPage.content.length > 0 && (
          <div>
            <h2 className="mb-3 text-sm font-semibold text-gray-700">Listings</h2>
            <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
              {listingsPage.content.map((listing) => (
                <ListingCard key={listing.id} listing={listing} />
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
