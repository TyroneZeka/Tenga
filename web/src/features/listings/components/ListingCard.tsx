import { Link } from 'react-router-dom'
import type { Listing } from '../types'

interface ListingCardProps {
  listing: Listing
}

export function ListingCard({ listing }: ListingCardProps) {
  const currencyLabel = listing.currency === 'USD' ? 'US$' : 'ZiG'
  const price = Number(listing.price).toLocaleString()

  return (
    <Link to={`/listings/${listing.id}`} className="group block">
      <div className="overflow-hidden rounded-lg bg-white transition hover:shadow-md">
        {/* Image — portrait ratio like product marketplaces */}
        <div className="aspect-[3/4] overflow-hidden bg-gray-100">
          {listing.imageUrls[0] ? (
            <img
              src={listing.imageUrls[0]}
              alt={listing.title}
              className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
            />
          ) : (
            <div className="flex h-full items-center justify-center">
              <span className="text-xs text-gray-300">No photo</span>
            </div>
          )}
        </div>

        {/* Info */}
        <div className="px-2 pb-3 pt-2">
          <p className="line-clamp-2 text-xs leading-snug text-gray-700">{listing.title}</p>
          <p className="mt-1.5 text-sm font-bold text-orange-500">
            {currencyLabel} {price}
          </p>
          {listing.city && (
            <p className="mt-0.5 truncate text-[10px] text-gray-400">{listing.city}</p>
          )}
        </div>
      </div>
    </Link>
  )
}
