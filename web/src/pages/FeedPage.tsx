import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Plus, Search, SlidersHorizontal, X } from 'lucide-react'
import { useListings, ListingCard } from '@/features/listings'
import { FilterPanel, ActiveFilterChips } from '@/features/listings/components/FilterPanel'
import type { ListingSearchParams } from '@/features/listings'

export default function FeedPage() {
  const [params, setParams] = useState<ListingSearchParams>({ page: 0, size: 20 })
  const [query, setQuery] = useState('')
  const [drawerOpen, setDrawerOpen] = useState(false)
  const { data, isLoading } = useListings(params)

  function updateParams(updates: Partial<ListingSearchParams>) {
    setParams((p) => ({ ...p, ...updates }))
  }

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    updateParams({ query: query.trim() || undefined, page: 0 })
  }

  const totalResults = data?.totalElements ?? 0
  const hasFilters = !!(params.categoryId || params.city || params.condition || params.currency || params.minPrice != null || params.maxPrice != null)

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="sticky top-0 z-20 border-b border-gray-200 bg-white px-4 py-3">
        <div className="mx-auto flex max-w-6xl items-center gap-3">
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

          {/* Mobile filter button */}
          <button
            onClick={() => setDrawerOpen(true)}
            className="relative flex items-center gap-1.5 rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-700 hover:bg-gray-50 lg:hidden"
          >
            <SlidersHorizontal className="h-4 w-4" />
            Filters
            {hasFilters && (
              <span className="absolute -right-1 -top-1 flex h-4 w-4 items-center justify-center rounded-full bg-primary-500 text-[10px] font-bold text-white">
                !
              </span>
            )}
          </button>

          <Link
            to="/listings/new"
            className="flex items-center gap-1.5 rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
          >
            <Plus className="h-4 w-4" />
            <span className="hidden sm:inline">Sell</span>
          </Link>
        </div>
      </header>

      <div className="mx-auto max-w-6xl px-4 py-6">
        <div className="flex gap-6">
          {/* Sidebar — desktop */}
          <aside className="hidden w-56 flex-shrink-0 lg:block">
            <div className="sticky top-20 rounded-xl border border-gray-200 bg-white p-4">
              <FilterPanel params={params} onChange={updateParams} />
            </div>
          </aside>

          {/* Main content */}
          <main className="min-w-0 flex-1">
            {/* Results bar */}
            <div className="mb-3 flex items-center justify-between">
              <div className="space-y-1.5">
                {params.query && (
                  <p className="text-sm text-gray-600">
                    Results for <span className="font-medium text-gray-900">"{params.query}"</span>
                  </p>
                )}
                <ActiveFilterChips params={params} onChange={updateParams} />
              </div>
              {!isLoading && data && (
                <p className="flex-shrink-0 text-xs text-gray-400">
                  {totalResults.toLocaleString()} listing{totalResults !== 1 ? 's' : ''}
                </p>
              )}
            </div>

            {/* Skeleton */}
            {isLoading && (
              <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
                {Array.from({ length: 8 }).map((_, i) => (
                  <div key={i} className="aspect-square animate-pulse rounded-xl bg-gray-200" />
                ))}
              </div>
            )}

            {/* Results */}
            {!isLoading && data && (
              <>
                {data.content.length === 0 ? (
                  <div className="py-20 text-center">
                    <p className="text-gray-500">No listings found.</p>
                    {hasFilters && (
                      <button
                        onClick={() =>
                          updateParams({
                            categoryId: undefined,
                            city: undefined,
                            condition: undefined,
                            currency: undefined,
                            minPrice: undefined,
                            maxPrice: undefined,
                            query: undefined,
                            page: 0,
                          })
                        }
                        className="mt-3 text-sm text-primary-600 hover:underline"
                      >
                        Clear all filters
                      </button>
                    )}
                  </div>
                ) : (
                  <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
                    {data.content.map((listing) => (
                      <ListingCard key={listing.id} listing={listing} />
                    ))}
                  </div>
                )}

                {/* Pagination */}
                {data.totalPages > 1 && (
                  <div className="mt-6 flex justify-center gap-2">
                    <button
                      disabled={data.first}
                      onClick={() => updateParams({ page: (params.page ?? 0) - 1 })}
                      className="rounded-lg border border-gray-300 px-4 py-2 text-sm disabled:opacity-40"
                    >
                      Previous
                    </button>
                    <span className="flex items-center px-3 text-sm text-gray-600">
                      {data.number + 1} / {data.totalPages}
                    </span>
                    <button
                      disabled={data.last}
                      onClick={() => updateParams({ page: (params.page ?? 0) + 1 })}
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
      </div>

      {/* Mobile filter drawer */}
      {drawerOpen && (
        <div className="fixed inset-0 z-30 flex lg:hidden">
          <div className="absolute inset-0 bg-black/40" onClick={() => setDrawerOpen(false)} />
          <div className="relative ml-auto flex h-full w-80 flex-col bg-white shadow-xl">
            <div className="flex items-center justify-between border-b border-gray-200 px-4 py-3">
              <h2 className="font-semibold text-gray-900">Filters</h2>
              <button onClick={() => setDrawerOpen(false)} className="text-gray-500 hover:text-gray-800">
                <X className="h-5 w-5" />
              </button>
            </div>
            <div className="flex-1 overflow-y-auto p-4">
              <FilterPanel
                params={params}
                onChange={(updates) => {
                  updateParams(updates)
                  setDrawerOpen(false)
                }}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
