import { Link } from 'react-router-dom'
import { MapPin, Star } from 'lucide-react'
import type { Listing } from '../types'

interface ListingCardProps {
  listing: Listing
}

export function ListingCard({ listing }: ListingCardProps) {
  const currencyLabel = listing.currency === 'USD' ? 'US$' : 'ZiG'
  const price = `${currencyLabel} ${listing.price.toLocaleString()}`

  return (
    <Link to={`/listings/${listing.id}`} className="group block">
      <div className="overflow-hidden rounded-xl bg-white shadow-sm ring-1 ring-gray-200 transition hover:shadow-md">
        <div className="aspect-square overflow-hidden bg-gray-100">
          {listing.imageUrls[0] ? (
            <img
              src={listing.imageUrls[0]}
              alt={listing.title}
              className="h-full w-full object-cover transition group-hover:scale-105"
            />
          ) : (
            <div className="flex h-full items-center justify-center text-gray-300 text-sm">
              No photo
            </div>
          )}
        </div>
        <div className="p-3">
          <p className="truncate text-sm font-medium text-gray-900">{listing.title}</p>
          <p className="mt-0.5 text-base font-semibold text-primary-600">{price}</p>
          <div className="mt-1 flex items-center justify-between text-xs text-gray-500">
            <span className="flex items-center gap-0.5">
              <MapPin className="h-3 w-3" />
              {listing.cityName}
            </span>
            {listing.seller.trustScore !== null && (
              <span className="flex items-center gap-0.5">
                <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                {listing.seller.trustScore.toFixed(1)}
              </span>
            )}
          </div>
        </div>
      </div>
    </Link>
  )
}
