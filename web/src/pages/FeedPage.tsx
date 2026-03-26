import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Plus, Search } from 'lucide-react'
import { useListings, ListingCard } from '@/features/listings'
import type { ListingSearchParams } from '@/features/listings'

export default function FeedPage() {
  const [params, setParams] = useState<ListingSearchParams>({ page: 0 })
  const [query, setQuery] = useState('')
  const { data, isLoading } = useListings(params)

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    setParams((p) => ({ ...p, query: query.trim() || undefined, page: 0 }))
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="sticky top-0 z-10 border-b border-gray-200 bg-white px-4 py-3">
        <div className="mx-auto flex max-w-5xl items-center gap-3">
          <form onSubmit={handleSearch} className="flex flex-1 items-center gap-2">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
              <input
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                className="w-full rounded-lg border border-gray-200 bg-gray-50 py-2 pl-9 pr-3 text-sm focus:border-primary-400 focus:outline-none focus:ring-1 focus:ring-primary-400"
                placeholder="Search listings…"
              />
            </div>
            <button
              type="submit"
              className="rounded-lg bg-primary-500 px-4 py-2 text-sm font-medium text-white hover:bg-primary-600"
            >
              Search
            </button>
          </form>
          <Link
            to="/listings/new"
            className="flex items-center gap-1.5 rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
          >
            <Plus className="h-4 w-4" />
            Sell
          </Link>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-6">
        {isLoading && (
          <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
            {Array.from({ length: 8 }).map((_, i) => (
              <div key={i} className="aspect-square animate-pulse rounded-xl bg-gray-200" />
            ))}
          </div>
        )}

        {!isLoading && data && (
          <>
            <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
              {data.content.map((listing) => (
                <ListingCard key={listing.id} listing={listing} />
              ))}
            </div>

            {data.content.length === 0 && (
              <p className="py-16 text-center text-gray-500">No listings found.</p>
            )}

            {data.totalPages > 1 && (
              <div className="mt-6 flex justify-center gap-2">
                <button
                  disabled={data.first}
                  onClick={() => setParams((p) => ({ ...p, page: (p.page ?? 0) - 1 }))}
                  className="rounded-lg border border-gray-300 px-4 py-2 text-sm disabled:opacity-40"
                >
                  Previous
                </button>
                <span className="flex items-center px-3 text-sm text-gray-600">
                  {data.number + 1} / {data.totalPages}
                </span>
                <button
                  disabled={data.last}
                  onClick={() => setParams((p) => ({ ...p, page: (p.page ?? 0) + 1 }))}
                  className="rounded-lg border border-gray-300 px-4 py-2 text-sm disabled:opacity-40"
                >
                  Next
                </button>
              </div>
            )}
          </>
        )}
      </main>
    </div>
  )
}
