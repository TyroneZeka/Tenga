import { useParams, Link, useNavigate } from 'react-router-dom'
import { MapPin, Star, MessageCircle, ArrowLeft, Edit } from 'lucide-react'
import { useListing, useDeleteListing } from '@/features/listings'
import { useAuthStore } from '@/stores/authStore'

export default function ListingDetailPage() {
  const { id } = useParams<{ id: string }>()
  const { data: listing, isLoading } = useListing(id!)
  const { mutate: deleteListing } = useDeleteListing()
  const user = useAuthStore((s) => s.user)
  const navigate = useNavigate()

  if (isLoading) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-8">
        <div className="animate-pulse space-y-4">
          <div className="aspect-video rounded-xl bg-gray-200" />
          <div className="h-6 w-2/3 rounded bg-gray-200" />
          <div className="h-4 w-1/3 rounded bg-gray-200" />
        </div>
      </div>
    )
  }

  if (!listing) return null

  const isOwner = user?.id === listing.seller.id
  const currencyLabel = listing.currency === 'USD' ? 'US$' : 'ZiG'

  function handleDelete() {
    if (!confirm('Delete this listing?')) return
    deleteListing(listing!.id, { onSuccess: () => navigate('/') })
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-3xl px-4 py-6">
        <Link to="/" className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800">
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>

        {listing.imageUrls.length > 0 && (
          <div className="mb-4 overflow-hidden rounded-xl">
            <img
              src={listing.imageUrls[0]}
              alt={listing.title}
              className="w-full object-cover"
              style={{ maxHeight: 400 }}
            />
          </div>
        )}

        <div className="rounded-xl bg-white p-5 shadow-sm">
          <div className="flex items-start justify-between gap-4">
            <div>
              <h1 className="text-xl font-semibold text-gray-900">{listing.title}</h1>
              <p className="mt-1 text-2xl font-bold text-primary-600">
                {currencyLabel} {listing.price.toLocaleString()}
              </p>
            </div>
            {isOwner && (
              <div className="flex gap-2">
                <Link
                  to={`/listings/${listing.id}/edit`}
                  className="flex items-center gap-1 rounded-lg border border-gray-300 px-3 py-1.5 text-sm hover:bg-gray-50"
                >
                  <Edit className="h-3.5 w-3.5" /> Edit
                </Link>
                <button
                  onClick={handleDelete}
                  className="rounded-lg border border-red-200 px-3 py-1.5 text-sm text-red-600 hover:bg-red-50"
                >
                  Delete
                </button>
              </div>
            )}
          </div>

          <div className="mt-3 flex flex-wrap gap-3 text-sm text-gray-600">
            <span className="flex items-center gap-1">
              <MapPin className="h-3.5 w-3.5" /> {listing.cityName}
            </span>
            <span className="rounded-full bg-gray-100 px-2.5 py-0.5 capitalize">
              {listing.condition.toLowerCase().replace('_', ' ')}
            </span>
            <span className="rounded-full bg-gray-100 px-2.5 py-0.5">{listing.categoryName}</span>
          </div>

          <p className="mt-4 text-sm leading-relaxed text-gray-700">{listing.description}</p>

          <div className="mt-5 border-t border-gray-100 pt-4">
            <div className="flex items-center justify-between">
              <Link to={`/users/${listing.seller.id}`} className="flex items-center gap-2 hover:underline">
                <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary-100 text-sm font-medium text-primary-700">
                  {listing.seller.displayName.charAt(0).toUpperCase()}
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-900">{listing.seller.displayName}</p>
                  {listing.seller.trustScore !== null && (
                    <p className="flex items-center gap-0.5 text-xs text-gray-500">
                      <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                      {listing.seller.trustScore.toFixed(1)}
                    </p>
                  )}
                </div>
              </Link>

              {!isOwner && (
                <div className="flex gap-2">
                  <Link
                    to={`/chat?listingId=${listing.id}&sellerId=${listing.seller.id}`}
                    className="flex items-center gap-1.5 rounded-lg border border-gray-300 px-4 py-2 text-sm hover:bg-gray-50"
                  >
                    <MessageCircle className="h-4 w-4" /> Chat
                  </Link>
                  <Link
                    to={`/checkout/${listing.id}`}
                    className="rounded-lg bg-primary-500 px-4 py-2 text-sm font-medium text-white hover:bg-primary-600"
                  >
                    Buy now
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
