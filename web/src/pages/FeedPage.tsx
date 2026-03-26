import { useState, useRef } from 'react'
import { Link } from 'react-router-dom'
import { Search, Plus, SlidersHorizontal, X } from 'lucide-react'
import { useListings, ListingCard } from '@/features/listings'
import { FilterPanel, ActiveFilterChips } from '@/features/listings/components/FilterPanel'
import type { ListingSearchParams } from '@/features/listings'
import { CATEGORIES } from '@/lib/filterOptions'

export default function FeedPage() {
  const [params, setParams] = useState<ListingSearchParams>({ page: 0, size: 20 })
  const [query, setQuery] = useState('')
  const [drawerOpen, setDrawerOpen] = useState(false)
  const { data, isLoading } = useListings(params)
  const categoryRowRef = useRef<HTMLDivElement>(null)

  function updateParams(updates: Partial<ListingSearchParams>) {
    setParams((p) => ({ ...p, ...updates }))
  }

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    updateParams({ query: query.trim() || undefined, page: 0 })
  }

  const hasFilters = !!(
    params.categoryId || params.city || params.condition ||
    params.currency || params.minPrice != null || params.maxPrice != null
  )

  return (
    <div className="min-h-screen bg-gray-100">

      {/* ── Top header ── */}
      <header className="sticky top-0 z-20 bg-gray-900 px-3 py-3">
        <div className="mx-auto flex max-w-6xl items-center gap-2">
          {/* Logo */}
          <Link to="/" className="flex-shrink-0">
            <span className="text-xl font-extrabold tracking-tight text-orange-400">Tenga</span>
          </Link>

          {/* Search bar */}
          <form onSubmit={handleSearch} className="flex flex-1 items-stretch overflow-hidden rounded-full bg-white">
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="min-w-0 flex-1 bg-transparent py-2 pl-4 pr-2 text-sm text-gray-800 placeholder:text-gray-400 focus:outline-none"
              placeholder="Search Tenga…"
            />
            <button
              type="submit"
              className="flex items-center gap-1 rounded-full bg-orange-500 px-4 py-2 text-sm font-semibold text-white hover:bg-orange-600"
            >
              <Search className="h-3.5 w-3.5" />
              <span className="hidden sm:inline">Search</span>
            </button>
          </form>

          {/* Sell button */}
          <Link
            to="/listings/new"
            className="flex flex-shrink-0 items-center gap-1 rounded-full bg-orange-500 px-3 py-2 text-sm font-semibold text-white hover:bg-orange-600"
          >
            <Plus className="h-4 w-4" />
            <span className="hidden sm:inline">Sell</span>
          </Link>
        </div>
      </header>

      {/* ── Category strip ── */}
      <div className="sticky top-[56px] z-10 border-b border-gray-200 bg-white shadow-sm">
        <div
          ref={categoryRowRef}
          className="flex overflow-x-auto"
          style={{ scrollbarWidth: 'none' }}
        >
          {/* All */}
          <button
            onClick={() => updateParams({ categoryId: undefined, page: 0 })}
            className={`flex flex-shrink-0 flex-col items-center gap-0.5 px-3 py-2.5 transition ${
              !params.categoryId
                ? 'border-b-2 border-orange-500 text-orange-500'
                : 'text-gray-600 hover:text-gray-900'
            }`}
          >
            <span className="text-lg leading-none">🛒</span>
            <span className="text-[10px] font-medium">All</span>
          </button>

          {CATEGORIES.map((cat) => (
            <button
              key={cat.id}
              onClick={() =>
                updateParams({ categoryId: params.categoryId === cat.id ? undefined : cat.id, page: 0 })
              }
              className={`flex flex-shrink-0 flex-col items-center gap-0.5 px-3 py-2.5 transition ${
                params.categoryId === cat.id
                  ? 'border-b-2 border-orange-500 text-orange-500'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <span className="text-lg leading-none">{cat.icon}</span>
              <span className="text-[10px] font-medium">{cat.name.split(' ')[0]}</span>
            </button>
          ))}
        </div>
      </div>

      {/* ── Filter bar ── */}
      <div className="border-b border-gray-200 bg-white px-3 py-2">
        <div className="mx-auto flex max-w-6xl items-center justify-between gap-2">
          <div className="flex min-w-0 flex-1 flex-wrap items-center gap-1.5">
            {params.query && (
              <span className="flex items-center gap-1 rounded-full bg-orange-50 px-2.5 py-0.5 text-xs font-medium text-orange-600">
                "{params.query}"
                <button
                  onClick={() => {
                    setQuery('')
                    updateParams({ query: undefined, page: 0 })
                  }}
                >
                  <X className="h-3 w-3" />
                </button>
              </span>
            )}
            <ActiveFilterChips params={params} onChange={updateParams} />
            {!hasFilters && !params.query && (
              <span className="text-xs text-gray-400">
                {data ? `${data.totalElements.toLocaleString()} listings` : ''}
              </span>
            )}
          </div>

          <button
            onClick={() => setDrawerOpen(true)}
            className={`flex flex-shrink-0 items-center gap-1 rounded-full border px-3 py-1 text-xs font-medium transition ${
              hasFilters
                ? 'border-orange-400 text-orange-500'
                : 'border-gray-300 text-gray-600 hover:border-gray-400'
            }`}
          >
            <SlidersHorizontal className="h-3 w-3" />
            Filters
            {hasFilters && (
              <span className="ml-0.5 flex h-3.5 w-3.5 items-center justify-center rounded-full bg-orange-500 text-[9px] font-bold text-white">
                !
              </span>
            )}
          </button>
        </div>
      </div>

      {/* ── Product grid ── */}
      <main className="mx-auto max-w-6xl px-2 py-3">
        {isLoading && (
          <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5">
            {Array.from({ length: 10 }).map((_, i) => (
              <div key={i} className="aspect-[3/4] animate-pulse rounded-lg bg-gray-200" />
            ))}
          </div>
        )}

        {!isLoading && data && (
          <>
            {data.content.length === 0 ? (
              <div className="py-20 text-center">
                <p className="text-gray-500">No listings found.</p>
                {(hasFilters || params.query) && (
                  <button
                    onClick={() => {
                      setQuery('')
                      setParams({ page: 0, size: 20 })
                    }}
                    className="mt-3 text-sm text-orange-500 hover:underline"
                  >
                    Clear all filters
                  </button>
                )}
              </div>
            ) : (
              <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5">
                {data.content.map((listing) => (
                  <ListingCard key={listing.id} listing={listing} />
                ))}
              </div>
            )}

            {data.totalPages > 1 && (
              <div className="mt-6 flex justify-center gap-2">
                <button
                  disabled={data.first}
                  onClick={() => updateParams({ page: (params.page ?? 0) - 1 })}
                  className="rounded-full border border-gray-300 px-5 py-1.5 text-sm disabled:opacity-40"
                >
                  Prev
                </button>
                <span className="flex items-center px-3 text-sm text-gray-500">
                  {data.number + 1} / {data.totalPages}
                </span>
                <button
                  disabled={data.last}
                  onClick={() => updateParams({ page: (params.page ?? 0) + 1 })}
                  className="rounded-full border border-gray-300 px-5 py-1.5 text-sm disabled:opacity-40"
                >
                  Next
                </button>
              </div>
            )}
          </>
        )}
      </main>

      {/* ── Filter drawer (mobile) ── */}
      {drawerOpen && (
        <div className="fixed inset-0 z-30 flex">
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
